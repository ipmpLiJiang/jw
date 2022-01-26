package com.welb.organization_check.controller;

import com.welb.organization_check.dto.ScoreFlowScorringTjDto;
import com.welb.organization_check.dto.UserScoreDto;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.ScoreDetail;
import com.welb.organization_check.entity.ScoreFlow;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.info.Info;
import com.welb.organization_check.service.*;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scoreflow")
public class ScoreFlowController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IScoreFlowService scoreFlowService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IUserScoreDtoService userScoreDtoService;
    @Resource
    IUserService userService;
    @Resource
    IScoreDetailService scoreDetailService;

    @RequestMapping(value = "/scoreFlowTjList", produces = "application/json;charset=utf-8")
    public Object selectScoreFlowTjList(HttpServletRequest req, String year, String month, String dbtype, String postType) {
        ModelMap map = new ModelMap();
        String userCode = (String) req.getSession().getAttribute("usercode");
        if (userCode != null) {
            try {
                if (year == null || month == null || year.equals("") || month.equals("")) {
                    ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                    year = setTime.getYear();
                    month = setTime.getMonth();
                }

                List<ScoreFlow> flowList = scoreFlowService.selectSummaryFlowByYMTOrPTList(year, month, dbtype, postType, null);
                List<ScoreFlowScorringTjDto> list = new ArrayList<>();
                ScoreFlowScorringTjDto dto1 = new ScoreFlowScorringTjDto();
                dto1.setNum(1);
                dto1.setScoreProj("行政后勤科室");
                dto1.setPosttype("3");
                this.getFlowABCDEFPostTypeList(flowList, dto1, dto1.getPosttype());

                ScoreFlowScorringTjDto dto2 = new ScoreFlowScorringTjDto();
                dto2.setNum(2);
                dto2.setScoreProj("临床科主任");
                dto2.setPosttype("1");
                this.getFlowABCDEFPostTypeList(flowList, dto2, dto2.getPosttype());

                ScoreFlowScorringTjDto dto3 = new ScoreFlowScorringTjDto();
                dto3.setNum(3);
                dto3.setScoreProj("临床护士长");
                dto3.setPosttype("2");
                this.getFlowABCDEFPostTypeList(flowList, dto3, dto3.getPosttype());

                ScoreFlowScorringTjDto dto4 = new ScoreFlowScorringTjDto();
                dto4.setNum(4);
                dto4.setScoreProj("合计");
                dto4.setKhrs(dto1.getKhrs() + dto2.getKhrs() + dto3.getKhrs());
                dto4.setYcyABCrs(dto1.getYcyABCrs() + dto2.getYcyABCrs() + dto3.getYcyABCrs());
                dto4.setYcyDrs(dto1.getYcyDrs() + dto2.getYcyDrs() + dto3.getYcyDrs());
                dto4.setYcyEFrs(dto1.getYcyEFrs() + dto2.getYcyEFrs() + dto3.getYcyEFrs());
                dto4.setSjcyABCrs(dto1.getSjcyABCrs() + dto2.getSjcyABCrs() + dto3.getSjcyABCrs());
                dto4.setSjcyDrs(dto1.getSjcyDrs() + dto2.getSjcyDrs() + dto3.getSjcyDrs());
                dto4.setSjcyEFrs(dto1.getSjcyEFrs() + dto2.getSjcyEFrs() + dto3.getSjcyEFrs());
                list.add(dto1);
                list.add(dto2);
                list.add(dto3);
                list.add(dto4);
                map.put("totalPages", list.size());
                map.put("msg", "测评打分情况统计表成功");
                map.put("data", list);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "测评打分情况统计表失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void getFlowABCDEFPostTypeList(List<ScoreFlow> flowList, ScoreFlowScorringTjDto dto, String postType) {
//        List<ScoreFlowScorringTjDto> list = new ArrayList<>();
//        List<ScoreFlowScorringTjDto> query = new ArrayList<>();
        List<ScoreFlow> flowPostTypeList = new ArrayList<>();
        List<ScoreFlow> flowABCList = new ArrayList<>();
        List<ScoreFlow> flowDList = new ArrayList<>();
        List<ScoreFlow> flowEFList1 = new ArrayList<>();
        List<ScoreFlow> flowEFList = new ArrayList<>();
        List<ScoreFlow> list = new ArrayList<>();
        List<ScoreFlow> flowABCDEFList = new ArrayList<>();
        flowPostTypeList = flowList.stream().filter(s -> s.getPosttype().equals(postType)).collect(Collectors.toList());
        if (flowPostTypeList.size() > 0) {
            flowABCList = flowPostTypeList.stream().filter(s -> s.getScoretype().equals("A") || s.getScoretype().equals("B") ||
                    s.getScoretype().equals("C")).collect(Collectors.toList());

            flowDList = flowPostTypeList.stream().filter(s -> s.getScoretype().equals("D")).collect(Collectors.toList());

            flowEFList1 = flowPostTypeList.stream().filter(s -> s.getScoretype().equals("E") || s.getScoretype().equals("F")).collect(Collectors.toList());

            for (ScoreFlow flow : flowEFList1) {
                if (flowEFList.stream().filter(s -> s.getScoredcode().equals(flow.getScoredcode()) &&
                        s.getScorringcode().equals(flow.getScorringcode()) && s.getScoreState().equals(flow.getScoreState())).count() == 0) {
                    ScoreFlow flowEF = new ScoreFlow();
                    flowEF.setScoredcode(flow.getScoredcode());
                    flowEF.setScorringcode(flow.getScorringcode());
                    flowEF.setScoreState(flow.getScoreState());
                    flowEFList.add(flowEF);
                }
            }
            int count = 0;
            count = flowPostTypeList.stream().collect(Collectors.groupingBy(ScoreFlow::getScoredcode)).size();
            dto.setKhrs(count);

//            count = flowABCList.stream().collect(Collectors.groupingBy(ScoreFlow::getScorringcode)).size();
//            dto.setYcyABCrs(count);
//            count = flowDList.stream().collect(Collectors.groupingBy(ScoreFlow::getScorringcode)).size();
//            dto.setYcyDrs(count);
//            count = flowEFList.stream().collect(Collectors.groupingBy(ScoreFlow::getScorringcode)).size();
//            dto.setYcyEFrs(count);
            for (ScoreFlow flow : flowABCList) {
                if (list.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode())).count() == 0) {
                    dto.setYcyABCrs(dto.getYcyABCrs() + 1);
                    flowABCDEFList = flowABCList.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode()) &&
                            s.getScoreState().equals("2")).collect(Collectors.toList());
                    if (flowABCDEFList.size() > 0) {
                        dto.setSjcyABCrs(dto.getSjcyABCrs() + 1);
                    }
                    ScoreFlow item = new ScoreFlow();
                    item.setScorringcode(flow.getScorringcode());
                    list.add(item);
                }
            }
            flowABCDEFList.clear();
            list.clear();
            for (ScoreFlow flow : flowDList) {
                if (list.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode())).count() == 0) {
                    dto.setYcyDrs(dto.getYcyDrs() + 1);
                    flowABCDEFList = flowDList.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode()) &&
                            s.getScoreState().equals("2")).collect(Collectors.toList());
                    if (flowABCDEFList.size() > 0) {
                        dto.setSjcyDrs(dto.getSjcyDrs() + 1);
                    }
                    ScoreFlow item = new ScoreFlow();
                    item.setScorringcode(flow.getScorringcode());
                    list.add(item);
                }
            }
            flowABCDEFList.clear();
            list.clear();
            for (ScoreFlow flow : flowEFList) {
                if (list.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode())).count() == 0) {
                    dto.setYcyEFrs(dto.getYcyEFrs() + 1);
                    flowABCDEFList = flowEFList.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode()) &&
                            s.getScoreState().equals("2")).collect(Collectors.toList());
                    if (flowABCDEFList.size() > 0) {
                        dto.setSjcyEFrs(dto.getSjcyEFrs() + 1);
                    }
                    ScoreFlow item = new ScoreFlow();
                    item.setScorringcode(flow.getScorringcode());
                    list.add(item);
                }
            }

//            for (ScoreFlow flow : flowPostTypeList) {
//                if (list.stream().filter(s -> s.getScorredcode().equals(flow.getScoredcode())).count() == 0) {
//                    dto.setKhrs(dto.getKhrs() + 1);
//                    flowABCDEFList = flowABCList.stream().filter(s -> s.getScoredcode().equals(flow.getScoredcode())).collect(Collectors.toList());
//                    for (ScoreFlow sbcFlow : flowABCDEFList) {
//                        if (sbcFlow.getScoreState().equals("2")) {
//                            dto.setSjcyABCrs(dto.getSjcyABCrs() + 1);
//                        }
//                    }
//                    flowABCDEFList = flowDList.stream().filter(s -> s.getScoredcode().equals(flow.getScoredcode())).collect(Collectors.toList());
//                    for (ScoreFlow dflow : flowABCDEFList) {
//                        if (dflow.getScoreState().equals("2")) {
//                            dto.setSjcyDrs(dto.getSjcyDrs() + 1);
//                        }
//                    }
//                    flowABCDEFList = flowEFList.stream().filter(s -> s.getScoredcode().equals(flow.getScoredcode())).collect(Collectors.toList());
//                    for (ScoreFlow efFlow : flowABCDEFList) {
//                        if (efFlow.getScoreState().equals("2")) {
//                            dto.setSjcyEFrs(dto.getSjcyEFrs() + 1);
//                        }
//                    }
//                    ScoreFlowScorringTjDto item = new ScoreFlowScorringTjDto();
//                    item.setScorredcode(flow.getScoredcode());
//                    list.add(item);
//                }
//            }
        }
    }


    private List<String> getTypeList(String scoreType) {
        List<String> typeList = new ArrayList<>();
        if (scoreType.equals("ABC")) {
            typeList.add("A");
            typeList.add("B");
            typeList.add("C");
        } else if (scoreType.equals("D")) {
            typeList.add("D");
        } else {
            typeList.add("E");
            typeList.add("F");
        }
        return typeList;
    }

    @RequestMapping(value = "/scoreFlowDetailTjList", produces = "application/json;charset=utf-8")
    public Object scoreFlowDetailTjList(HttpServletRequest req, String year, String month, String dbtype, String postType, String scoreType) {
        ModelMap map = new ModelMap();
        String userCode = (String) req.getSession().getAttribute("usercode");
        if (userCode != null) {
            try {
                if (year == null || month == null || year.equals("") || month.equals("")) {
                    ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                    year = setTime.getYear();
                    month = setTime.getMonth();
                }
                List<String> typeList = this.getTypeList(scoreType);
                List<ScoreFlow> flowList = scoreFlowService.selectSummaryFlowByYMTOrPTList(year, month, dbtype, postType, typeList);
                List<ScoreFlowScorringTjDto> list = new ArrayList<>();
                if (flowList.size() > 0) {
                    List<ScoreDetail> detailList = scoreDetailService.selectDetailByInFSerialNoList(year, month, dbtype, postType, typeList);
                    list = this.getScorringTjDto(flowList, detailList);
                    for (ScoreFlowScorringTjDto item : list) {
                        User userQuery = new User();
                        userQuery.setUsercode(item.getScorringcode());
                        User user = userService.selectUserBuGwByMoneyCard(userQuery);
                        if (user != null)
                            item.setScorringname(user.getUsername());
                    }
                    if (list.size() > 0) {
                        list = list.stream().sorted(Comparator.comparing(ScoreFlowScorringTjDto::getYcyrs).reversed()).collect(Collectors.toList());
                    }
                }
                map.put("totalPages", list.size());
                map.put("msg", "打分详情统计表成功");
                map.put("data", list);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "打分详情统计表失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }

    private List<ScoreFlowScorringTjDto> getScorringTjDto(List<ScoreFlow> flowList, List<ScoreDetail> detailList) {
        List<ScoreFlowScorringTjDto> list = new ArrayList<>();
        List<ScoreFlowScorringTjDto> ssfList = new ArrayList<>();
        List<ScoreDetail> query = new ArrayList<>();
        List<ScoreFlowScorringTjDto> tjQuery = new ArrayList<>();
        for (ScoreFlow flow : flowList) {
            if (ssfList.stream().filter(s -> s.getFserialno().equals(flow.getSerialno())).count() == 0) {
                query = detailList.stream().filter(s -> s.getFSerialNo().equals(flow.getSerialno())).collect(Collectors.toList());
                ScoreFlowScorringTjDto item = new ScoreFlowScorringTjDto();
                item.setFserialno(flow.getSerialno());
                item.setScorringcode(flow.getScorringcode());
                item.setScorredcode(flow.getScoredcode());
                item.setScorestate(flow.getScoreState());
                item.setScoretype(flow.getScoretype());
                item.setYcyzb(query.size());
                if (flow.getScoreState().equals("2")) {
                    item.setSjcyzb(query.size());
                } else {
                    item.setSjcyzb(0);
                }
                item.setScorestate(flow.getScoreState());
                ssfList.add(item);
            }
        }

        int count = 0;
        double sum = 0.0;
        for (ScoreFlowScorringTjDto tjDto : ssfList) {
            if (list.stream().filter(s -> s.getScorringcode().equals(tjDto.getScorringcode())).count() == 0) {
                tjQuery = ssfList.stream().filter(s -> s.getScorringcode().equals(tjDto.getScorringcode())).collect(Collectors.toList());
                ScoreFlowScorringTjDto item = new ScoreFlowScorringTjDto();
                item.setScorringcode(tjDto.getScorringcode());
                count = tjQuery.stream().collect(Collectors.groupingBy(ScoreFlowScorringTjDto::getScorredcode)).size();
                item.setYcyrs(count);
                count = tjQuery.stream().filter(s -> s.getScorestate().equals("2")).collect(Collectors.groupingBy(ScoreFlowScorringTjDto::getScorredcode)).size();
                item.setSjcyrs(count);
                sum = tjQuery.stream().mapToDouble(ScoreFlowScorringTjDto::getYcyzb).sum();
                item.setYcyzb((int) sum);
                sum = tjQuery.stream().mapToDouble(ScoreFlowScorringTjDto::getSjcyzb).sum();
                item.setSjcyzb((int) sum);
                list.add(item);
            }
        }

        return list;
    }

    @RequestMapping(value = "/export", produces = "application/json;charset=utf-8")
    public void exportExcelData1(HttpServletResponse response, String info) {
        //json字符串转对象
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        if (info1.getYear() == null || info1.getMonth() == null || info1.getYear().equals("") || info1.getMonth().equals("")) {
            ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", info1.getDbtype());
            info1.setYear(setTime.getYear());
            info1.setMonth(setTime.getMonth());
        }
        List<ScoreFlow> flowList = scoreFlowService.selectSummaryFlowByYMTOrPTList(info1.getYear(), info1.getMonth(),
                info1.getDbtype(), info1.getPostType(), null);

        List<ScoreFlowScorringTjDto> list = new ArrayList<>();
        List<ScoreFlowScorringTjDto> abcDetail = new ArrayList<>();
        List<ScoreFlowScorringTjDto> efDetail = new ArrayList<>();
        List<ScoreFlowScorringTjDto> dDetail = new ArrayList<>();
        if (flowList.size() > 0) {
            String pt = info1.getPostType();
            ScoreFlowScorringTjDto dto1 = new ScoreFlowScorringTjDto();
            dto1.setNum(1);
            dto1.setScoreProj(pt.equals("3") ? "行政后勤科室" : pt.equals("2") ? "临床科主任" : "临床护士长");
            dto1.setPosttype(pt);
            this.getFlowABCDEFPostTypeList(flowList, dto1, dto1.getPosttype());
            list.add(dto1);

            List<ScoreDetail> detailList = scoreDetailService.selectDetailByInFSerialNoList(info1.getYear(), info1.getMonth(),
                    info1.getDbtype(), info1.getPostType(), null);

            List<ScoreDetail> abcDetailList = detailList.stream().filter(s -> s.getScoretype().equals("A") ||
                    s.getScoretype().equals("B") || s.getScoretype().equals("C")).collect(Collectors.toList());

            List<ScoreFlow> queryFlowList = flowList.stream().filter(s -> s.getScoretype().equals("A") ||
                    s.getScoretype().equals("B") || s.getScoretype().equals("C")).collect(Collectors.toList());

            abcDetail = this.getScorringTjDto(queryFlowList, abcDetailList);
            for (ScoreFlowScorringTjDto item : abcDetail) {
                User userQuery = new User();
                userQuery.setUsercode(item.getScorringcode());
                User user = userService.selectUserBuGwByMoneyCard(userQuery);
                if (user != null)
                    item.setScorringname(user.getUsername());
            }
            if (abcDetail.size() > 0) {
                abcDetail = abcDetail.stream().sorted(Comparator.comparing(ScoreFlowScorringTjDto::getYcyrs).reversed()).collect(Collectors.toList());
            }

            List<ScoreDetail> dDetailList = detailList.stream().filter(s -> s.getScoretype().equals("D")).collect(Collectors.toList());
            queryFlowList = flowList.stream().filter(s -> s.getScoretype().equals("D")).collect(Collectors.toList());

            dDetail = this.getScorringTjDto(queryFlowList, dDetailList);
            for (ScoreFlowScorringTjDto item : dDetail) {
                User userQuery = new User();
                userQuery.setUsercode(item.getScorringcode());
                User user = userService.selectUserBuGwByMoneyCard(userQuery);
                if (user != null)
                    item.setScorringname(user.getUsername());
            }
            if (dDetail.size() > 0) {
                dDetail = dDetail.stream().sorted(Comparator.comparing(ScoreFlowScorringTjDto::getYcyrs).reversed()).collect(Collectors.toList());
            }

            List<ScoreDetail> efDetailList = detailList.stream().filter(s -> s.getScoretype().equals("E") ||
                    s.getScoretype().equals("F")).collect(Collectors.toList());

            queryFlowList = flowList.stream().filter(s -> s.getScoretype().equals("E") ||
                    s.getScoretype().equals("F")).collect(Collectors.toList());

            efDetail = this.getScorringTjDto(queryFlowList, efDetailList);
            for (ScoreFlowScorringTjDto item : efDetail) {
                User userQuery = new User();
                userQuery.setUsercode(item.getScorringcode());
                User user = userService.selectUserBuGwByMoneyCard(userQuery);
                if (user != null)
                    item.setScorringname(user.getUsername());
            }
            if (efDetail.size() > 0) {
                efDetail = efDetail.stream().sorted(Comparator.comparing(ScoreFlowScorringTjDto::getYcyrs).reversed()).collect(Collectors.toList());
            }
        }
        // 创建ExportExcel对象
        try {
            // 獲取工作表
            Workbook workbook = exportBigDataExcel(list, abcDetail, dDetail, efDetail);
            // 完成下載
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            String title = "";
            if (info1.getPostType() != null && !info1.getPostType().equals("")) {
                title = info1.getPostType().equals("3") ? "行政" : info1.getPostType().equals("2") ? "护士长" : "科主任";
            }
            title = !title.equals("") ? "-" + title : "";
            downFile(os, response, info1.getYear() + "-" + info1.getMonth() + "测评打分情况统计表" + title + ".xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook exportBigDataExcel(List<ScoreFlowScorringTjDto> list, List<ScoreFlowScorringTjDto> abcDetail,
                                             List<ScoreFlowScorringTjDto> dDetail, List<ScoreFlowScorringTjDto> efDetail) {
        // 1.获取数据
        // 2.创建工作簿
        // 阈值，内存中的对象数量最大值，超过这个值会生成一个临时文件存放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("sheet1");

        Row titleRow1 = sheet.createRow(0);
        Cell celXh = titleRow1.createCell(0);
        celXh.setCellValue("序号");
        Cell celDx = titleRow1.createCell(1);
        celDx.setCellValue("被考核对象");
        Cell celRs = titleRow1.createCell(2);
        celRs.setCellValue("被考核人数(人)");
        Cell celYcySj = titleRow1.createCell(3);
        celYcySj.setCellValue("应参与 / 实际参与测评打分人数(人)");

        Row titleRow2 = sheet.createRow(1);
        Cell celYld = titleRow2.createCell(3);
        celYld.setCellValue("院领导");
        Cell celZchb = titleRow2.createCell(4);
        celZchb.setCellValue("中层干部");
        Cell celYg = titleRow2.createCell(5);
        celYg.setCellValue("员工");

        CellRangeAddress region = new CellRangeAddress(0, 0, 3, 5);
        sheet.addMergedRegion(region);
        CellRangeAddress region1 = new CellRangeAddress(0, 1, 0, 0);
        sheet.addMergedRegion(region1);
        CellRangeAddress region2 = new CellRangeAddress(0, 1, 1, 1);
        sheet.addMergedRegion(region2);
        CellRangeAddress region3 = new CellRangeAddress(0, 1, 2, 2);
        sheet.addMergedRegion(region3);

        String abcRs = "";
        String efRs = "";
        String dRs = "";

        // 3.从集合中取数据并赋值
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(i + 2);
            ScoreFlowScorringTjDto item = list.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(item.getScoreProj());
            row.createCell(2).setCellValue(item.getKhrs());
            abcRs = item.getYcyABCrs() + " / " + item.getSjcyABCrs();
            efRs = item.getYcyEFrs() + " / " + item.getSjcyEFrs();
            dRs = item.getYcyDrs() + " / " + item.getSjcyDrs();
            row.createCell(3).setCellValue(abcRs);
            row.createCell(4).setCellValue(efRs);
            row.createCell(5).setCellValue(dRs);
        }

        String[] titles = new String[]{"序号", "评分员工", "应考核人数(人)", "实际考核人数(人)", "应打分指标(条)", "实际打分指标(条)"};
        int nRow = 4;

        Row row0 = sheet.createRow(nRow);
        row0.createCell(0).setCellValue("");

        nRow = nRow + 1;
        Row row01 = sheet.createRow(nRow);
        row01.createCell(0).setCellValue("院领导(" + abcRs +")");

        CellRangeAddress regionAbc = new CellRangeAddress(nRow, nRow, 0, 5);
        sheet.addMergedRegion(regionAbc);

        nRow = nRow + 1;
        Row titleRowAbc = sheet.createRow(nRow);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRowAbc.createCell(i);
            cell.setCellValue(titles[i]);
        }

        // 3.从集合中取数据并赋值
        for (int i = 0; i < abcDetail.size(); i++) {
            Row row = sheet.createRow(i + nRow + 1);
            ScoreFlowScorringTjDto item = abcDetail.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(item.getScorringname());
            row.createCell(2).setCellValue(item.getYcyrs());
            row.createCell(3).setCellValue(item.getSjcyrs());
            row.createCell(4).setCellValue(item.getYcyzb());
            row.createCell(5).setCellValue(item.getSjcyzb());
        }
        nRow = nRow+ abcDetail.size() + 1;
        Row row10 = sheet.createRow(nRow);
        row10.createCell(0).setCellValue("");
        nRow = nRow + 1;

        Row row11 = sheet.createRow(nRow);
        row11.createCell(0).setCellValue("中层干部(" + efRs +")");
        CellRangeAddress regionEf = new CellRangeAddress(nRow, nRow, 0, 5);
        sheet.addMergedRegion(regionEf);

        nRow = nRow + 1;
        Row titleRowEf = sheet.createRow(nRow);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRowEf.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.从集合中取数据并赋值
        for (int i = 0; i < efDetail.size(); i++) {
            Row row = sheet.createRow(i + nRow + 1);
            ScoreFlowScorringTjDto item = efDetail.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(item.getScorringname());
            row.createCell(2).setCellValue(item.getYcyrs());
            row.createCell(3).setCellValue(item.getSjcyrs());
            row.createCell(4).setCellValue(item.getYcyzb());
            row.createCell(5).setCellValue(item.getSjcyzb());
        }

        nRow = nRow +efDetail.size() + 1;

        Row row20 = sheet.createRow(nRow );
        row20.createCell(0).setCellValue("");
        nRow = nRow + 1;

        Row row21 = sheet.createRow(nRow );
        row21.createCell(0).setCellValue("员工(" + dRs +")");
        CellRangeAddress regionD = new CellRangeAddress(nRow, nRow, 0, 5);
        sheet.addMergedRegion(regionD);

        nRow = nRow + 1;
        Row titleRowD = sheet.createRow(nRow);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRowD.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.从集合中取数据并赋值
        for (int i = 0; i < dDetail.size(); i++) {
            Row row = sheet.createRow(i + nRow + 1);
            ScoreFlowScorringTjDto item = dDetail.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(item.getScorringname());
            row.createCell(2).setCellValue(item.getYcyrs());
            row.createCell(3).setCellValue(item.getSjcyrs());
            row.createCell(4).setCellValue(item.getYcyzb());
            row.createCell(5).setCellValue(item.getSjcyzb());
        }

        return wb;

    }

    private void downFile(ByteArrayOutputStream os, HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }


    /*
    @RequestMapping(value = "/scoreFlowDetailTjList1", produces = "application/json;charset=utf-8")
    public Object scoreFlowDetailTjList1(HttpServletRequest req, String year, String month, String dbtype, String postType, String scoreType) {
        ModelMap map = new ModelMap();
        String userCode = (String) req.getSession().getAttribute("usercode");
        if (userCode != null) {
            try {
                if (year == null || month == null || year.equals("") || month.equals("")) {
                    ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                    year = setTime.getYear();
                    month = setTime.getMonth();
                }
                List<String> typeList = this.getTypeList(scoreType);
                List<UserScoreDto> usdto = userScoreDtoService.selectUserDetailByYMDTAndPTAndSTList(year, month, dbtype, postType, typeList);
                List<ScoreFlowScorringTjDto> list = this.getScorringTjDto(usdto);
                for (ScoreFlowScorringTjDto item : list) {
                    User userQuery = new User();
                    userQuery.setUsercode(item.getScorringcode());
                    User user = userService.selectUserBuGwByMoneyCard(userQuery);
                    if (user != null)
                        item.setScorringname(user.getUsername());
                }
                if (list.size() > 0) {
                    list = list.stream().sorted(Comparator.comparing(ScoreFlowScorringTjDto::getYcyrs).reversed()).collect(Collectors.toList());
                }
                map.put("totalPages", list.size());
                map.put("msg", "打分详情统计表成功");
                map.put("data", list);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "打分详情统计表失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }

    private List<ScoreFlowScorringTjDto> getScorringTjDto(List<UserScoreDto> usdtos) {
        List<ScoreFlowScorringTjDto> list = new ArrayList<>();
        List<ScoreFlowScorringTjDto> ssfList = new ArrayList<>();
        List<UserScoreDto> query = new ArrayList<>();
        List<ScoreFlowScorringTjDto> tjQuery = new ArrayList<>();
        for (UserScoreDto dto : usdtos) {
            if (ssfList.stream().filter(s -> s.getFserialno().equals(dto.getFserialNo())).count() == 0) {
                query = usdtos.stream().filter(s -> s.getFserialNo().equals(dto.getFserialNo())).collect(Collectors.toList());
                ScoreFlowScorringTjDto item = new ScoreFlowScorringTjDto();
                item.setFserialno(dto.getFserialNo());
                item.setScorringcode(dto.getScorringCode());
                item.setScorredcode(dto.getScoredCode());
                item.setScorestate(dto.getScoreState());
                item.setScoretype(dto.getScoreType());
                item.setYcyzb(query.size());
                query = query.stream().filter(s -> s.getScoreState().equals("2")).collect(Collectors.toList());
                item.setSjcyzb(query.size());
                ssfList.add(item);
            }
        }

        int count = 0;
        double sum = 0.0;
        for (ScoreFlowScorringTjDto tjDto : ssfList) {
            if (list.stream().filter(s -> s.getScorringcode().equals(tjDto.getScorringcode())).count() == 0) {
                tjQuery = ssfList.stream().filter(s -> s.getScorringcode().equals(tjDto.getScorringcode())).collect(Collectors.toList());
                ScoreFlowScorringTjDto item = new ScoreFlowScorringTjDto();
                item.setScorringcode(tjDto.getScorringcode());
                count = tjQuery.stream().collect(Collectors.groupingBy(ScoreFlowScorringTjDto::getScorredcode)).size();
                item.setYcyrs(count);
                count = tjQuery.stream().filter(s -> s.getScorestate().equals("2")).collect(Collectors.groupingBy(ScoreFlowScorringTjDto::getScorredcode)).size();
                item.setSjcyrs(count);
                sum = tjQuery.stream().mapToDouble(ScoreFlowScorringTjDto::getYcyzb).sum();
                item.setYcyzb((int) sum);
                sum = tjQuery.stream().mapToDouble(ScoreFlowScorringTjDto::getSjcyzb).sum();
                item.setSjcyzb((int) sum);
                list.add(item);
            }
        }
        return list;
    }
*/
}
