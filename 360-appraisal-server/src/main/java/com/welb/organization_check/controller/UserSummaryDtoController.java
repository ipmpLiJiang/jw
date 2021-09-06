package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserSummaryDto;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.util.CalendarUtil;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luoyaozu
 * @title: UserDtoController
 * @projectName xh-360appraisal-interface
 * @description: 人员评分管理控制类
 * @date 2019/6/416:48
 */
@RestController
@RequestMapping("/usersummary")
public class UserSummaryDtoController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IUserSummaryDtoService summaryDtoService;
    @Resource
    IUserService userService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IStationService stationService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IScoreHistoryService historyService;
    @Resource
    IUserDtoService userDtoService;

    /**
     * 查询首页待评分人数据列表
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectUserDtoLike(HttpServletRequest req, UserSummaryDto dto) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<UserSummaryDto> summarys = new ArrayList<>();
            //获取当前年份
            String year = CalendarUtil.getYear();
            //获取当前月份
            String month = CalendarUtil.getMonth();
            //获取当前月度
            String quarter = month;//CalendarUtil.getQuarter(month);
            //当前上一个月度
            int count = Integer.parseInt(quarter.trim()) - 1;
            //获取当前系统时间
            String sysTime = DateUtil.getTime();

            //手动考核-查看所有季节总结
            manualSelectUserDtoLke(dto, map, usercode, summarys, year, quarter, count, sysTime);

        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }

    private void manualSelectUserDtoLke(UserSummaryDto dto, ModelMap map, String usercode, List<UserSummaryDto> summarys, String year, String quarter, int count, String sysTime) {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dto.getDbtype());
        if (setTime != null) {
            //新一月度考核-手动设置的考核时间未超过系统自动考核时间
            try {

                //开始新的月度考核
                month = quarter;
                getSummaryList(dto, map, usercode, summarys, setTime.getYear(), setTime.getMonth());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void automaticSelectUserDtoLike(UserSummaryDto dto, ModelMap map, String usercode, List<UserSummaryDto> summarys, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
        } else {
            month = String.valueOf(count);
        }
        // getSummaryList(dto, map, usercode, summarys, year, month);
    }

    private void getSummaryList(UserSummaryDto dto, ModelMap map, String usercode, List<UserSummaryDto> summarys, String year, String month) {
        List<UserSummaryDto> summaryList;
        try {
            dto.setMonth(month);
            dto.setYear(year);
            dto.setDbtype(dto.getDbtype());
            dto.setScorringcode(usercode);
            if (dto.getDbtype().equals("1")) {
                summaryList = summaryDtoService.selectUserSummaryBySixState(dto);
            } else {
                summaryList = summaryDtoService.selectUserSummaryBySixStateNew(dto);
            }
            for (UserSummaryDto dto1 : summaryList) {
                List<ScoreFlow> flow = flowService.selectByScoreFlow(dto1.getSerialno(), dto1.getScorringcode(), dto.getDbtype());
                if (flow.size() == 0) {
                    summarys.add(dto1);
                }
            }

            //修改每一位用户的打分状态
//            updateScoreHistoryScoreStatus(dto, year, month);
            map.put("totalPages", summarys.size());
            map.put("msg", "查询待评分列表数据成功");
            map.put("data", summarys);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询待评分列表数据失败");
            map.put("code", 1);
        }
    }

    private void updateScoreHistoryScoreStatus(UserSummaryDto dto, String year, String month) {
        List<ScoreHistory> scoreHistory = historyService.findScoreHistoryList(dto.getYear(), dto.getMonth());
        for (ScoreHistory list : scoreHistory) {
            ScoreHistory historyState = new ScoreHistory();
            historyState.setYear(year);
            historyState.setMonth(month);
            historyState.setUsercode(list.getUsercode());
            UserDto userDto = new UserDto();
            userDto.setYear(year);
            userDto.setMonth(month);
            userDto.setEmployeecode(list.getUsercode());
            int dtoTotalCount = userDtoService.getTotalCount(userDto);
            String mserialno = year + "-" + month;
            int flowTotalCount = flowService.getTotalCount(mserialno, list.getUsercode(), null);
            if (flowTotalCount == 0) {
                historyState.setScorestatus("1");
            } else if (flowTotalCount < dtoTotalCount) {
                historyState.setScorestatus("2");
            } else {
                historyState.setScorestatus("3");
            }
            ScoreHistory history = historyService.selectOneByHistory(historyState);
            if (history == null) {//新增操作
                historyService.insertSelective(historyState);
            } else {//更新操作
                historyState.setId(history.getId());
                historyService.updateByPrimaryKeySelective(historyState);
            }
        }
    }


    /**
     * 人员评分管理查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/selectUserSummaryLike", produces = "application/json;charset=utf-8")
    public Object selectUserSummaryLike(UserSummaryDto summaryDto, int pageNum, int pageSize, HttpServletRequest req) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<UserSummaryDto> dtos;
            //如果年份和月度同时为空  默认查当前月度的个人评分
            if (summaryDto.getYear().equals("") && summaryDto.getMonth().equals("") ||
                    summaryDto.getYear() == null && summaryDto.getMonth() == null) {
                //获取当前年份
                String year = CalendarUtil.getYear();
                //获取当前月份
                String month = CalendarUtil.getMonth();
                //获取当前月度
                String quarter = month;// CalendarUtil.getQuarter(month);
                //当前上一个月度
                int count = Integer.parseInt(quarter.trim()) - 1;
                //获取当前系统时间
                String sysTime = DateUtil.getTime();

                //手动考核-查看所有季节总结
                manualSelectUserSummaryLike(summaryDto, map, usercode, year, quarter, count, sysTime, pageNum, pageSize, summaryDto.getDbtype());

            } else {
                try {//当年份或者月度不为空的时候   查询的是历史的个人评分数据
                    summaryDto.setScorringcode(usercode);
                    dtos = summaryDtoService.selectUserSummary(summaryDto);
                    getSummaryInfo(dtos, summaryDto.getDbtype());
                    PageInfo<UserSummaryDto> pageInfo = new PageInfo<>(dtos);
                    dtos = pageInfo.getList();
                    map.put("totalPages", pageInfo.getTotal());
                    map.put("msg", "查询待评分列表数据成功");
                    map.put("data", dtos);
                    map.put("code", 0);
                } catch (Exception e) {
                    log.error(LogUtil.getTrace(e));
                    map.put("msg", "查询待评分列表数据失败");
                    map.put("code", 1);
                }
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void manualSelectUserSummaryLike(UserSummaryDto summaryDto, ModelMap map, String usercode, String year, String quarter, int count, String sysTime, int pageNum, int pageSize, String dbtype) {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
        if (setTime != null) {
            //新一月度考核-手动设置的考核时间未超过系统自动考核时间
            try {
                //开始新的月度考核

                month = quarter;
                getSummary(summaryDto, map, usercode, setTime.getYear(), setTime.getMonth(), pageNum, pageSize, dbtype);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void automaticSelectUserSummaryLike(UserSummaryDto summaryDto, ModelMap map, String usercode, String year, int count, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
            // getSummary(summaryDto, map, usercode, year, month,pageNum,pageSize);

        } else {
            month = String.valueOf(count);
            //getSummary(summaryDto, map, usercode, year, month,pageNum,pageSize);

        }
    }

    private void getSummary(UserSummaryDto summaryDto, ModelMap map, String usercode, String year, String month, int pageNum, int pageSize, String dbtype) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserSummaryDto> dtos;
        try {
            summaryDto.setYear(year);
            summaryDto.setMonth(month);
            summaryDto.setDbtype(dbtype);
            summaryDto.setScorringcode(usercode);
            if(summaryDto.getDbtype().equals("1")) {
                dtos = summaryDtoService.selectUserSummary(summaryDto);
            }else {
                dtos = summaryDtoService.selectUserSummaryNew(summaryDto);
            }
            getSummaryInfo(dtos, dbtype);
            PageInfo<UserSummaryDto> pageInfo = new PageInfo<>(dtos);
            dtos = pageInfo.getList();
            map.put("totalPages", pageInfo.getTotal());
            map.put("msg", "查询待评分列表数据成功");
            map.put("data", dtos);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询待评分列表数据失败");
            map.put("code", 1);
        }
    }

    private void getSummaryInfo(List<UserSummaryDto> dtos, String dbtype) {
        for (UserSummaryDto dto : dtos) {
            User user = userService.findUserByUserCode(dto.getScorredcode());
            if (user != null) {
                //获取被评分人的编号和姓名
                dto.setScorredname(user.getUsername());
                Station station = stationService.selectByStationCode(dto.getStationcode());
                dto.setStationcode(user.getStationcode());
                if (station != null) {//获取岗位的编号和名称
                    dto.setStationname(station.getStationname());
                    Department department = departmentService.selectByDeptCode(station.getDepartmentcode());
                    dto.setDepartmentcode(station.getDepartmentcode());
                    if (department != null) {//获取部门的编号和名字
                        dto.setDepartmentname(department.getDepartmentname());
                    } else {
                        dto.setDepartmentname("");
                    }
                } else {
                    dto.setStationname("");
                }
            } else {
                dto.setUsername("");
            }
            String mesrialno = dto.getSerialno();
            String scorringcode = dto.getScorringcode();
            List<ScoreFlow> flow = flowService.selectByScoreFlow(mesrialno, scorringcode, dbtype);
            if (flow.size() > 0) {
                Double sumScore = flow.stream().mapToDouble(ScoreFlow:: getScore).sum();
                dto.setStatus(sumScore.toString());
//                for (ScoreFlow flow1 : flow) {
//                    String totalscore = String.valueOf(flow1.getScore());
//                    dto.setStatus(totalscore);
//                }
            } else {
                dto.setStatus("评分");
            }
        }
    }

}
