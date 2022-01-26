package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.ScoreDutySmTjDto;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.ScoreDutySm;
import com.welb.organization_check.entity.ScoreFlow;
import com.welb.organization_check.info.Info;
import com.welb.organization_check.service.IManualSetTimeService;
import com.welb.organization_check.service.IScoreDutySmService;
import com.welb.organization_check.service.IScoreFlowService;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scoredutysm")
public class ScoreDutySmController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IScoreDutySmService scoreDutySmService;
    @Resource
    IScoreFlowService scoreFlowService;
    @Resource
    IManualSetTimeService setTimeService;

    @RequestMapping(value = "/scoreDutySmTjList", produces = "application/json;charset=utf-8")
    public Object selectScoreDutySmTjList(HttpServletRequest req, String year, String month, String dbtype, String postType) {
        ModelMap map = new ModelMap();
        String userCode = (String) req.getSession().getAttribute("usercode");
        if (userCode != null) {
            try {
                if (year == null || month == null || year.equals("") || month.equals("")) {
                    ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                    year = setTime.getYear();
                    month = setTime.getMonth();
                }
                List<ScoreDutySmTjDto> list = this.getScoreDutySmTjDtoList(year, month, dbtype, postType);
                map.put("totalPages", list.size());
                map.put("msg", "查询自评情况统计表成功");
                map.put("data", list);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "查询自评情况统计表失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }
    private List<ScoreDutySmTjDto> getScoreDutySmTjDtoList(String year, String month, String dbtype, String postType){
        List<ScoreDutySmTjDto> list = new ArrayList<>();

        ScoreDutySm query = new ScoreDutySm();
        query.setYear(year);
        query.setMonth(month);
        query.setDbtype(dbtype);
        List<ScoreDutySm> scoreDutySmList = scoreDutySmService.selectScoreDutySmList(query, postType);
        List<ScoreFlow> flowList = scoreFlowService.selectSummaryFlowByYMTOrPTList(year, month, dbtype, postType,null);
        ScoreDutySmTjDto dto1 = new ScoreDutySmTjDto();
        dto1.setNum(1);
        dto1.setScoreProj("行政后勤科室");
        List<ScoreDutySmTjDto> flowXzList = this.getFlowPostTypeList(flowList,scoreDutySmList,dto1,"3");

        ScoreDutySmTjDto dto2 = new ScoreDutySmTjDto();
        dto2.setNum(2);
        dto2.setScoreProj("临床科主任");
        List<ScoreDutySmTjDto> flowKzrList = this.getFlowPostTypeList(flowList,scoreDutySmList,dto2,"1");

        ScoreDutySmTjDto dto3 = new ScoreDutySmTjDto();
        dto3.setNum(3);
        dto3.setScoreProj("临床护士长");
        List<ScoreDutySmTjDto> flowHszList = this.getFlowPostTypeList(flowList,scoreDutySmList,dto3,"2");

        ScoreDutySmTjDto dto4 = new ScoreDutySmTjDto();
        dto4.setNum(4);
        dto4.setScoreProj("合计");
        dto4.setWwcrs(dto1.getWwcrs() + dto2.getWwcrs() + dto3.getWwcrs());
        dto4.setWcrs(dto1.getWcrs() + dto2.getWcrs() + dto3.getWcrs());
        dto4.setKhrs(dto1.getKhrs() + dto2.getKhrs() + dto3.getKhrs());
        if(dto1.getScorredname() != null) {
            dto4.setScorredname(dto1.getScorredname());
        }
        if(dto2.getScorredname() != null) {
            if(dto4.getScorredname() != null){
                dto4.setScorredname(dto4.getScorredname() + "," + dto2.getScorredname());
            } else {
                dto4.setScorredname(dto2.getScorredname());
            }
        }
        if(dto3.getScorredname() != null) {
            if(dto4.getScorredname() != null){
                dto4.setScorredname(dto4.getScorredname() + "," + dto3.getScorredname());
            } else {
                dto4.setScorredname(dto3.getScorredname());
            }
        }
        list.add(dto1);
        list.add(dto2);
        list.add(dto3);
        list.add(dto4);
        return  list;
    }

    private List<ScoreDutySmTjDto> getFlowPostTypeList(List<ScoreFlow> flowList,List<ScoreDutySm> scoreDutySmList, ScoreDutySmTjDto dto ,String postType) {
        List<ScoreDutySmTjDto> list = new ArrayList<>();
        List<ScoreFlow> flowPostTypeList = flowList.stream().filter(s -> s.getPosttype().equals(postType)).collect(Collectors.toList());
        List<ScoreDutySm> query = new ArrayList<>();
        for (ScoreFlow flow : flowPostTypeList) {
            if (list.stream().filter(s -> s.getScorredcode().equals(flow.getScoredcode())).count() == 0) {
                query = scoreDutySmList.stream().filter(s->s.getScorredcode().equals(flow.getScoredcode())).collect(Collectors.toList());
                ScoreDutySmTjDto item = new ScoreDutySmTjDto();
                item.setScorredcode(flow.getScoredcode());
                item.setScorredname(flow.getScoredname());
                item.setPosttype(postType);
                item.setKhrs(1);
                dto.setKhrs(dto.getKhrs() + 1);
                if(query.size() > 0) {
                    item.setWcrs(1);
                    dto.setWcrs(dto.getWcrs() + 1);
                } else {
                    item.setWwcrs(1);
                    dto.setWwcrs(dto.getWwcrs() + 1);
                    if(dto.getScorredname() == null || dto.equals("")){
                        dto.setScorredname(flow.getScoredname());
                    } else {
                        dto.setScorredname(dto.getScorredname() + "," + flow.getScoredname());
                    }
                }
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
        List<ScoreDutySmTjDto> list = this.getScoreDutySmTjDtoList(info1.getYear(), info1.getMonth(), info1.getDbtype(), null);
        // 创建ExportExcel对象
        try {
            // 獲取工作表
            Workbook workbook = exportBigDataExcel(list);
            // 完成下載
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            downFile(os, response, info1.getYear() + "-" + info1.getMonth() + "自评情况统计表.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook exportBigDataExcel(List<ScoreDutySmTjDto> list) {
        // 1.获取数据
        // 2.创建工作簿
        // 阈值，内存中的对象数量最大值，超过这个值会生成一个临时文件存放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("sheet1");
        String[] titles = new String[]{"序号", "被考核对象", "被考核人数(人)", "完成自评人数(人)", "未自评人数(人)", "未自评人员"};

        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.从集合中取数据并赋值
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(i + 1);
            ScoreDutySmTjDto scoreDutySmTjDto = list.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(scoreDutySmTjDto.getScoreProj());
            row.createCell(2).setCellValue(scoreDutySmTjDto.getKhrs());
            row.createCell(3).setCellValue(scoreDutySmTjDto.getWcrs());
            row.createCell(4).setCellValue(scoreDutySmTjDto.getWwcrs());
            row.createCell(5).setCellValue(scoreDutySmTjDto.getScorredname());
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


}
