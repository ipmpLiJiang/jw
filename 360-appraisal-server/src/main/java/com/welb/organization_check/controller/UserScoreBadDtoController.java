package com.welb.organization_check.controller;


import net.sf.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.UserScoreBadDto;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.Station;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.info.Info;
import com.welb.organization_check.service.IManualSetTimeService;
import com.welb.organization_check.service.IUserScoreBadDtoService;
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
@RequestMapping("/scoreBad")
public class UserScoreBadDtoController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IUserService userService;

    @Resource
    IUserScoreBadDtoService userScoreBadDtoService;

    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object findStationAll(HttpServletRequest req, int pageNum, int pageSize, UserScoreBadDto dto) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            try {
                if (dto.getYear() == null || dto.getMonth() == null || dto.getYear().equals("") || dto.getMonth().equals("")) {
                    ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "",dto.getDbtype());
                    dto.setYear(setTime.getYear());
                    dto.setMonth(setTime.getMonth());
                }
                //pageNum:当前所在的页面  pageSize:每页有多少条数据
                PageHelper.startPage(pageNum, pageSize);
                List<UserScoreBadDto> userScoreBadDtos  = userScoreBadDtoService.selectUserScoreBadDto(dto);

                PageInfo<UserScoreBadDto> pageInfo = new PageInfo<>(userScoreBadDtos);
                List<UserScoreBadDto> userScoreBadList = pageInfo.getList();
                //分页后再赋值
                List<User> userList = new ArrayList<>();
                List<User> userQueryList = new ArrayList<>();
                for (UserScoreBadDto item : userScoreBadList) {
                    this.getUserName(item,userList,userQueryList);
                }

                //数据总量userScoreBadList
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", userScoreBadList);
                map.put("msg", "查询差评评分汇总成功 ");
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage() , e);
                map.put("msg", "查询差评评分汇总失败 ");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void getUserName(UserScoreBadDto item,List<User> userList,List<User> userQueryList){
        userQueryList = userList.stream().filter(s->s.getUsercode().equals(item.getUsercode())).collect(Collectors.toList());
        if(userQueryList.size() == 0) {
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

        userQueryList = userList.stream().filter(s->s.getUsercode().equals(item.getScorringCode())).collect(Collectors.toList());
        if(userQueryList.size() == 0) {
            User userQuery = new User();
            userQuery.setUsercode(item.getScorringCode());
            User user = userService.selectUserBuGwByMoneyCard(userQuery);
            if (user != null) {
                userList.add(user);
            }
            item.setScorringName(user.getUsername());
            item.setScorringStationcode(user.getStationcode());
            item.setScorringStationname(user.getStationname());
            item.setScorringDepartmentcode(user.getDepartmentcode());
            item.setScorringDepartmentname(user.getDepartmentname());
        } else {
            item.setScorringName(userQueryList.get(0).getUsername());
            item.setScorringStationcode(userQueryList.get(0).getStationcode());
            item.setScorringStationname(userQueryList.get(0).getStationname());
            item.setScorringDepartmentcode(userQueryList.get(0).getDepartmentcode());
            item.setScorringDepartmentname(userQueryList.get(0).getDepartmentname());
        }

    }

    @RequestMapping(value = "/export", produces = "application/json;charset=utf-8")
    public void exportExcelData1(HttpServletResponse response, String info) {
        //json字符串转对象
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        UserScoreBadDto dto = new UserScoreBadDto();
//        dto.setUsername(info1.getUsername());
//        dto.setStationcode(info1.getStationcode());
        if (info1.getYear().equals("") || info1.getMonth().equals("")) {
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth("", "", info1.getDbtype());
            dto.setYear(manualSetTime.getYear());
            dto.setMonth(manualSetTime.getMonth());
        } else {
            dto.setYear(info1.getYear());
            dto.setMonth(info1.getMonth());
        }
        dto.setDbtype(info1.getDbtype());
        List<UserScoreBadDto> userScoreBadList  = userScoreBadDtoService.selectUserScoreBadDto(dto);
        List<User> userList = new ArrayList<>();
        List<User> userQueryList = new ArrayList<>();
        for (UserScoreBadDto item : userScoreBadList) {
            this.getUserName(item,userList,userQueryList);
        }
        // 创建ExportExcel对象
        try {
            // 獲取工作表
            Workbook workbook = exportBigDataExcel(userScoreBadList);
            // 完成下載
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            downFile(os, response, dto.getYear() + "-" + dto.getMonth() + "差评评分汇总.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook exportBigDataExcel(List<UserScoreBadDto> userScoreBadDtoList) {
        // 1.获取数据
        // 2.创建工作簿
        // 阈值，内存中的对象数量最大值，超过这个值会生成一个临时文件存放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("sheet1");
        String[] titles = new String[]{"序号", "用户姓名", "指标类型", "具体指标", "评分类型", "差异原因", "打分人姓名"};

        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.从集合中取数据并赋值
        for (int i = 0; i < userScoreBadDtoList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            UserScoreBadDto userScoreBadDto = userScoreBadDtoList.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(userScoreBadDto.getUsername());
            row.createCell(2).setCellValue(userScoreBadDto.getDutyTypeName());
            row.createCell(3).setCellValue(userScoreBadDto.getDutyName());
            row.createCell(4).setCellValue(userScoreBadDto.getScoreType());
            row.createCell(5).setCellValue(userScoreBadDto.getCpsm());
            row.createCell(6).setCellValue(userScoreBadDto.getScorringName());
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
