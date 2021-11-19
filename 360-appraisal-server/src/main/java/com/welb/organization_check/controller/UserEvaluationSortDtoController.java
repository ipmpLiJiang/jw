package com.welb.organization_check.controller;

import com.welb.organization_check.info.Info;
import net.sf.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.SortTemp;
import com.welb.organization_check.dto.UserEvaluationSortDto;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.ResultReport;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IManualSetTimeService;
import com.welb.organization_check.service.IResultReportService;
import com.welb.organization_check.service.IUserEvaluationSortDtoService;
import com.welb.organization_check.service.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luoyaozu
 * @title: UserDtoController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/416:48
 */
@RestController
@RequestMapping("/scoreSort")
public class UserEvaluationSortDtoController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IUserService userService;
    @Resource
    IResultReportService resultReportService;

    @Resource
    IUserEvaluationSortDtoService userEvaluationSortDtoService;

    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object findAll(HttpServletRequest req, int pageNum, int pageSize, UserEvaluationSortDto dto) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            try {
                if (dto.getYear().equals("") && dto.getMonth().equals("") ||
                        dto.getYear() == null && dto.getMonth() == null) {
                    ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dto.getDbtype());
                    dto.setYear(setTime.getYear());
                    dto.setMonth(setTime.getMonth());
                }
                UserEvaluationSortDto dtoAll = new UserEvaluationSortDto();
                dtoAll.setYear(dto.getYear());
                dtoAll.setMonth(dto.getMonth());
                dtoAll.setDbtype(dto.getDbtype());
                List<UserEvaluationSortDto> dtoAllList = userEvaluationSortDtoService.selectUserEvaluationReportSortList(dtoAll);
                this.setEvaluationSortNum(dtoAllList);
                //pageNum:当前所在的页面  pageSize:每页有多少条数据
                PageHelper.startPage(pageNum, pageSize);
                List<UserEvaluationSortDto> userEvaluationSortDtos = userEvaluationSortDtoService.selectUserEvaluationReportSortList(dto);
                PageInfo<UserEvaluationSortDto> pageInfo = new PageInfo<>(userEvaluationSortDtos);
//                long current = pageNum == 1 ? 0 : (pageNum - 1) * pageSize;
//                List<UserEvaluationSortDto> userEvaluationSortList = userEvaluationSortDtos.stream().sorted(Comparator.comparing(UserEvaluationSortDto::getSortNum)).skip(current).limit(pageSize).collect(Collectors.toList());
                List<UserEvaluationSortDto> userEvaluationSortList = pageInfo.getList();
                this.getResultReport(dto.getYear(), dto.getMonth(), dto.getDbtype(), userEvaluationSortList, dtoAllList);

                //数据总量userScoreBadList
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", userEvaluationSortList);
                map.put("msg", "查询评分排序汇总成功 ");
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "查询评分排序汇总失败 ");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void setEvaluationSortNum(List<UserEvaluationSortDto> dtoList) {
        List<SortTemp> sortList = new ArrayList<>();
        List<Double> scoreList = new ArrayList<>();

        Double val = 0.0;
        for (UserEvaluationSortDto dto : dtoList) {
            val = dto.getTotalscore();
            if (!scoreList.contains(val)) {
                scoreList.add(val);
            }
        }
        scoreList = scoreList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        for (int i = 1; i <= scoreList.size(); i++) {
            SortTemp item = new SortTemp();
            item.setNum(i);
            item.setScore(scoreList.get(i - 1));
            sortList.add(item);
        }

        List<SortTemp> sortQuertList = new ArrayList<>();
        for (UserEvaluationSortDto dto : dtoList) {
            sortQuertList = sortList.stream().filter(s -> s.getScore().equals(dto.getTotalscore())).collect(Collectors.toList());
            if (sortQuertList.size() > 0) {
                dto.setSortNum(sortQuertList.get(0).getNum());
            }
        }
    }

    private void getUserName(UserEvaluationSortDto item, List<User> userList, List<User> userQueryList) {
        userQueryList = userList.stream().filter(s -> s.getUsercode().equals(item.getUsercode())).collect(Collectors.toList());
        if (userQueryList.size() == 0) {
            User userQuery = new User();
            userQuery.setUsercode(item.getUsercode());
            User user = userService.selectUserBuGwByMoneyCard(userQuery);
            if (user != null) {
                userList.add(user);
            }
            item.setStationcode(user.getStationcode());
            item.setStationname(user.getStationname());
            item.setDepartmentcode(user.getDepartmentcode());
            item.setDepartmentname(user.getDepartmentname());
        } else {
            item.setStationcode(userQueryList.get(0).getStationcode());
            item.setStationname(userQueryList.get(0).getStationname());
            item.setDepartmentcode(userQueryList.get(0).getDepartmentcode());
            item.setDepartmentname(userQueryList.get(0).getDepartmentname());
        }

    }

    private List<ResultReport> getResultReport(String year, String month, String dbtype,
                                               List<UserEvaluationSortDto> dtoList, List<UserEvaluationSortDto> dtoAllList) {
        List<ResultReport> reportList = resultReportService.selectResultReportByYearMonth(year, month, dbtype);
        List<ResultReport> rrList = new ArrayList<>();
        boolean isMb = false;
        if (reportList.size() > 0) {
            List<ResultReport> jcList = reportList.stream().filter(s -> s.getResultreportcode().equals(
                    dbtype.equals("2") ? "0" : "4"
            )).collect(Collectors.toList());
            List<ResultReport> gwList = reportList.stream().filter(s -> s.getResultreportcode().equals(
                    dbtype.equals("2") ? "1" : "5"
            )).collect(Collectors.toList());
            List<ResultReport> zdList = reportList.stream().filter(s -> s.getResultreportcode().equals(
                    dbtype.equals("2") ? "2" : "6"
            )).collect(Collectors.toList());
            List<ResultReport> mbList = reportList.stream().filter(s -> s.getResultreportcode().equals(
                    dbtype.equals("2") ? "3" : "7"
            )).collect(Collectors.toList());

            if (dbtype.equals("2")) {
                isMb = true;
            }
            List<ResultReport> zfList = new ArrayList<>();
            if (dbtype.equals("1")) {
                // 作风
                zfList = reportList.stream().filter(s -> s.getResultreportcode().equals("8")).collect(Collectors.toList());
                this.setResultReportNum(zfList, false);
            } else {
                // 党风廉政
                zfList = reportList.stream().filter(s -> s.getResultreportcode().equals("10")).collect(Collectors.toList());
                this.setResultReportNum(zfList, false);
            }
            this.setResultReportNum(jcList, false);
            this.setResultReportNum(gwList, false);
            this.setResultReportNum(zdList, false);
            this.setResultReportNum(mbList, isMb);
            List<ResultReport> jcQuery = new ArrayList<>();
            List<ResultReport> gwQuery = new ArrayList<>();
            List<ResultReport> zdQuery = new ArrayList<>();
            List<ResultReport> mbQuery = new ArrayList<>();
            List<ResultReport> zfQuery = new ArrayList<>();
            List<User> userList = new ArrayList<>();
            List<User> userQueryList = new ArrayList<>();

            List<UserEvaluationSortDto> queryAll = new ArrayList<>();
            for (UserEvaluationSortDto item : dtoList) {
                queryAll = dtoAllList.stream().filter(s -> s.getId().equals(item.getId())).collect(Collectors.toList());
                if (queryAll.size() > 0) {
                    item.setSortNum(queryAll.get(0).getSortNum());
                }
                this.getUserName(item, userList, userQueryList);
                if (isMb) {
                    item.setSumMbAvgScore(item.getSumMbAvgScore() == null ? 0 : item.getSumMbAvgScore());
                    item.setMbScore(item.getMbScore() == null ? 0 : item.getMbScore());
                    item.setMbScore(item.getSumMbAvgScore() + item.getMbScore());
                }

                jcQuery = jcList.stream().filter(s -> s.getEvaluationreportcode().equals(item.getId())).collect(Collectors.toList());
                gwQuery = gwList.stream().filter(s -> s.getEvaluationreportcode().equals(item.getId())).collect(Collectors.toList());
                zdQuery = zdList.stream().filter(s -> s.getEvaluationreportcode().equals(item.getId())).collect(Collectors.toList());
                mbQuery = mbList.stream().filter(s -> s.getEvaluationreportcode().equals(item.getId())).collect(Collectors.toList());
                zfQuery = zfList.stream().filter(s -> s.getEvaluationreportcode().equals(item.getId())).collect(Collectors.toList());

                if (jcQuery.size() > 0) {
                    item.setBasicSortNum(jcQuery.get(0).getNum());
                    item.setBasicAvgScore(jcQuery.get(0).getAvgscore());
                }
                if (gwQuery.size() > 0) {
                    item.setKeySortNum(gwQuery.get(0).getNum());
                    item.setKeyAvgScore(gwQuery.get(0).getAvgscore());
                }
                if (zdQuery.size() > 0) {
                    item.setZdSortNum(zdQuery.get(0).getNum());
                    item.setZdAvgScore(zdQuery.get(0).getAvgscore());
                }
                if (mbQuery.size() > 0) {
                    item.setMbSortNum(mbQuery.get(0).getNum());
                    item.setMbAvgScore(mbQuery.get(0).getAvgscore());
                }
                if (zfQuery.size() > 0) {
                    if (dbtype.equals("1")) {
                        item.setZfSortNum(zfQuery.get(0).getNum());
                        item.setZfAvgScore(zfQuery.get(0).getAvgscore());
                    } else {
                        item.setDfSortNum(zfQuery.get(0).getNum());
                        item.setDfAvgScore(zfQuery.get(0).getAvgscore());
                    }
                }
            }
        }
        return rrList;
    }

    private void setResultReportNum(List<ResultReport> reportList, boolean isMb) {
        if (reportList.size() > 0) {
            List<SortTemp> sortList = new ArrayList<>();
            List<Double> scoreList = new ArrayList<>();

            Double val = 0.0;
            for (ResultReport rr : reportList) {
                val = rr.getScore();
                if (!scoreList.contains(val)) {
                    scoreList.add(val);
                }
            }
            scoreList = scoreList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

            for (int i = 1; i <= scoreList.size(); i++) {
                SortTemp item = new SortTemp();
                item.setNum(i);
                item.setScore(scoreList.get(i - 1));
                sortList.add(item);
            }
            List<SortTemp> sortQuertList = new ArrayList<>();
            for (ResultReport item : reportList) {
                sortQuertList = sortList.stream().filter(s -> s.getScore().equals(item.getScore())).collect(Collectors.toList());
                if (sortQuertList.size() > 0) {
                    item.setNum(sortQuertList.get(0).getNum());
                }
            }
        }
    }


    @RequestMapping(value = "/export", produces = "application/json;charset=utf-8")
    public void exportExcelData1(HttpServletResponse response, String info) {
        //json字符串转对象
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        UserEvaluationSortDto dto = new UserEvaluationSortDto();
        if (info1.getYear().equals("") || info1.getMonth().equals("")) {
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth("", "", info1.getDbtype());
            dto.setYear(manualSetTime.getYear());
            dto.setMonth(manualSetTime.getMonth());
        } else {
            dto.setYear(info1.getYear());
            dto.setMonth(info1.getMonth());
        }
        dto.setDbtype(info1.getDbtype());
        List<UserEvaluationSortDto> dtoList = userEvaluationSortDtoService.selectUserEvaluationReportSortList(dto);
        this.setEvaluationSortNum(dtoList);
        this.getResultReport(dto.getYear(), dto.getMonth(), dto.getDbtype(), dtoList, dtoList);
        // 创建ExportExcel对象
        try {
            // 獲取工作表
            Workbook workbook = exportBigDataExcel(dtoList);
            // 完成下載
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            downFile(os, response, dto.getYear() + "-" + dto.getMonth() + "评分排序汇总.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SXSSFWorkbook exportBigDataExcel(List<UserEvaluationSortDto> dtoList) {
        // 1.获取数据
        // 2.创建工作簿
        // 阈值，内存中的对象数量最大值，超过这个值会生成一个临时文件存放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("sheet1");
        String[] titles = new String[]{"总排名", "用户姓名", "岗位名称", "总得分", "总平均分", "基础指标得分", "基础指标平均分",
        "基础指标排名","岗位职责得分","岗位职责平均分","岗位职责排名","重点任务得分","重点任务平均分","重点任务排名",
                "目标任务得分","目标任务平均分","目标任务排名"};

        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.从集合中取数据并赋值
        for (int i = 0; i < dtoList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            UserEvaluationSortDto item = dtoList.get(i);
            row.createCell(0).setCellValue(setNull(item.getSortNum()));
            row.createCell(1).setCellValue(item.getUsername());
            row.createCell(2).setCellValue(item.getStationname());
            row.createCell(3).setCellValue(setNull(item.getTotalscore()));
            row.createCell(4).setCellValue(setNull(item.getAvgscore()));
            row.createCell(5).setCellValue(setNull(item.getBasicscore()));
            row.createCell(6).setCellValue(setNull(item.getBasicAvgScore()));
            row.createCell(7).setCellValue(setNull(item.getBasicSortNum()));
            row.createCell(8).setCellValue(setNull(item.getKeyscore()));
            row.createCell(9).setCellValue(setNull(item.getKeyAvgScore()));
            row.createCell(10).setCellValue(setNull(item.getKeySortNum()));
            row.createCell(11).setCellValue(setNull(item.getZdScore()));
            row.createCell(12).setCellValue(setNull(item.getZdAvgScore()));
            row.createCell(13).setCellValue(setNull(item.getZdSortNum()));
            row.createCell(14).setCellValue(setNull(item.getMbScore()));
            row.createCell(15).setCellValue(setNull(item.getMbAvgScore()));
            row.createCell(16).setCellValue(setNull(item.getMbSortNum()));
        }
        return wb;
    }
    private String setNull(Object o){
        if(o == null) {
            return "";
        }
        return  o.toString();
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


}
