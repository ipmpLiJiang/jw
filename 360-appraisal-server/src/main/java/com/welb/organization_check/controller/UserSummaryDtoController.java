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
    @Resource
    IBranchService branchService;

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
            ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dto.getDbtype());
            if (setTime != null) {
                try {
                    getSummaryList(dto, map, usercode, summarys, setTime.getYear(), setTime.getMonth());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                map.put("msg", "当前年月没有季度评分数据");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

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
                List<ScoreFlow> flowScorringScorredcode = flowService.selectByScorringScoredFlow(dto1.getSerialno(), dto1.getScorringcode(), dto1.getScorredcode(), dto.getDbtype());
                if (flowScorringScorredcode.size() > 0) {
                    dto1.setScoreState(flowScorringScorredcode.get(0).getScoreState());
                } else {
                    dto1.setScoreState("1");
                }
                List<ScoreFlow> flow = flowService.selectByScoreFlow(dto1.getSerialno(), dto1.getScorringcode(), dto.getDbtype());
                if (flow.size() == 0 || (flow.size() > 0 && flow.get(0).getScoreState().equals("1"))) {
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
            log.error(e.getMessage(), e);
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
//            UserDto userDto = new UserDto();
//            userDto.setYear(year);
//            userDto.setMonth(month);
//            userDto.setEmployeecode(list.getUsercode());
            List<ScoreFlow> scorringList = flowService.selectScoreFlowScorringCode(year, month, list.getDbtype(), list.getUsercode());
//            int dtoTotalCount = userDtoService.getTotalCount(userDto);
//            String mserialno = year + "-" + month;
//            int flowTotalCount = flowService.getTotalCount(mserialno, list.getUsercode(), null);
            int dtoTotalCount = scorringList.size();
            //ScoreState 1 未评分 2 已评分
            int flowTotalCount = scorringList.stream().filter(s -> s.getScoreState().equals("2")).collect(Collectors.toList()).size();
            if (flowTotalCount == 0) {
                historyState.setScorestatus("1");
            } else if (flowTotalCount < dtoTotalCount) {
                historyState.setScorestatus("2");
            } else {
                // 在计算中计算已完成
//            historyState.setScorestatus("3");
                historyState.setScorestatus("2");
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
            if (summaryDto.getYear() == null && summaryDto.getMonth() == null ||
                    summaryDto.getYear().equals("") && summaryDto.getMonth().equals("")) {
                ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", summaryDto.getDbtype());
                if (setTime != null) {
                    try {
                        getSummary(summaryDto, map, usercode, setTime.getYear(), setTime.getMonth(), pageNum, pageSize);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    map.put("msg", "当前年月没有季度评分数据");
                    map.put("code", 1);
                }
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
                    log.error(e.getMessage(), e);
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

    @RequestMapping(value = "/selectUserSummaryScorredCode", produces = "application/json;charset=utf-8")
    public Object selectUserSummaryScorredCode(UserSummaryDto summaryDto, int pageNum, int pageSize, HttpServletRequest req) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            ManualSetTime setTime = null;
            //如果年份和月度同时为空  默认查当前月度的个人评分
            if (summaryDto.getYear() == null && summaryDto.getMonth() == null ||
                    summaryDto.getYear().equals("") && summaryDto.getMonth().equals("")) {
                setTime = setTimeService.selectManualByYearAndMonth("", "", summaryDto.getDbtype());
                if (setTime != null) {
                    summaryDto.setYear(setTime.getYear());
                    summaryDto.setMonth(setTime.getMonth());
                } else {
                    map.put("msg", "当前年月没有季度评分数据");
                    map.put("code", 1);
                }
            }
            if (setTime != null) {
                PageHelper.startPage(pageNum, pageSize);
                try {//当年份或者月度不为空的时候   查询的是历史的个人评分数据
                    summaryDto.setScorredcode(usercode);
                    List<UserSummaryDto> dtos = summaryDtoService.selectUserSummaryScorredCode(summaryDto);
                    getSummaryInfo(dtos, summaryDto.getDbtype());
                    PageInfo<UserSummaryDto> pageInfo = new PageInfo<>(dtos);
                    dtos = pageInfo.getList();
                    map.put("totalPages", pageInfo.getTotal());
                    map.put("msg", "查询自评列表数据成功");
                    map.put("data", dtos);
                    map.put("code", 0);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    map.put("msg", "查询自评列表数据失败");
                    map.put("code", 1);
                }
            } else {
                map.put("msg", "当前年月没有季度评分数据");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void getSummary(UserSummaryDto summaryDto, ModelMap map, String usercode, String year, String month, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        try {
            summaryDto.setYear(year);
            summaryDto.setMonth(month);
            summaryDto.setDbtype(summaryDto.getDbtype());
            summaryDto.setScorringcode(usercode);
            List<UserSummaryDto> dtos = summaryDtoService.selectUserSummary(summaryDto);
            getSummaryInfo(dtos, summaryDto.getDbtype());
            PageInfo<UserSummaryDto> pageInfo = new PageInfo<>(dtos);
            dtos = pageInfo.getList();
            map.put("totalPages", pageInfo.getTotal());
            map.put("msg", "查询待评分列表数据成功");
            map.put("data", dtos);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
                Double sumScore = flow.stream().mapToDouble(ScoreFlow::getScore).sum();
//                dto.setStatus(sumScore.toString());
                dto.setStatus(sumScore > 0 ? "1" : "2");
//                for (ScoreFlow flow1 : flow) {
//                    String totalscore = String.valueOf(flow1.getScore());
//                    dto.setStatus(totalscore);
//                }
            } else {
                dto.setStatus("0");
            }
            if (scorringcode != null) {
                List<ScoreFlow> flowScorringScorredcode = flowService.selectByScorringScoredFlow(mesrialno, scorringcode, user.getUsercode(), dbtype);
                if (flowScorringScorredcode.size() > 0) {
                    dto.setScoreState(flowScorringScorredcode.get(0).getScoreState());
                } else {
                    dto.setScoreState("1");
                }
            }
            if(user.getBranchcode()!=null && !user.getBranchcode().equals("")) {
                Branch branch = branchService.selectByPrimaryKey(user.getBranchcode());
                if (branch != null) {
                    dto.setBranchname(branch.getBranchname());
                } else {
                    dto.setBranchname("");
                }
            }
        }
    }

}
