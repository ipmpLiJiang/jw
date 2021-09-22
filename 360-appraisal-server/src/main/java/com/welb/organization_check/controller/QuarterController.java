package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.MonthSummary;
import com.welb.organization_check.entity.Score;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.*;
import com.welb.util.CalendarUtil;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luoyaozu
 * @title: QuarterController
 * @projectName xh-360appraisal-interface
 * @description: 月度总结控制层
 * @date 2019/5/3117:02
 */
@RestController
@RequestMapping("/quarter")
public class QuarterController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IMonthSummaryService summaryService;
    @Resource
    IUserService userService;
    @Resource
    IUserDtoService dtoService;
    @Resource
    IStationService stationService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IScoreService scoreService;

    /**
     * 查看所有月节总结  包含模糊条件查询
     *
     * @param pageNum
     * @param pageSize
     * @param dto
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectAll(HttpServletRequest req, int pageNum, int pageSize, UserDto dto) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        //   String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<User> users = userService.findUserAll();
            if (dto.getDbtype().equals("1")) {
                users = users.stream().filter(p -> p.getDbtype().equals(dto.getDbtype())).collect(Collectors.toList());
            }
            ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dto.getDbtype());
            manualSelectAll(pageNum, pageSize, dto, map, users, setTime.getYear(), setTime.getMonth(), 0, "", dto.getDbtype());
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void manualSelectAll(int pageNum, int pageSize, UserDto dto, ModelMap map, List<User> users, String year, String quarter, int count, String sysTime, String dbtype) {
        String month;
        try {
            //开始新的月度考核
            month = quarter;
            List<MonthSummary> summaryList = summaryService.selectSummaryListByYearAndMonth(year, month, dbtype);
            summaryList.stream().filter(p -> p.getDbtype().equals(dbtype)).collect(Collectors.toList());
            if (summaryList.size() != users.size()) {
                addMonthSummary(year, month, users, dbtype);
            }
            PageHelper.startPage(pageNum, pageSize);
            findUserAndUserList(dto, map, year, month, dbtype);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getSummaryList(int pageNum, int pageSize, UserDto dto, ModelMap map, List<User> users, String year, String month, String dbtype) {
        List<MonthSummary> summaryList = summaryService.selectSummaryListByYearAndMonth(year, month, dbtype);
        if (summaryList.size() != users.size()) {
            addMonthSummary(year, month, users, dbtype);
        }
        PageHelper.startPage(pageNum, pageSize);
        findUserAndUserList(dto, map, year, month, dbtype);
    }

    private void automaticSelectAll(int pageNum, int pageSize, UserDto dto, ModelMap map, List<User> users, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
        } else {
            month = String.valueOf(count);
        }
        // getSummaryList(pageNum, pageSize, dto, map, users, year, month);
    }

    private void findUserAndUserList(UserDto dto, ModelMap map, String year, String month, String dbtype) {
        List<UserDto> userDtoList;
        try {
            dto.setYear(year);
            dto.setDbtype(dbtype);
            dto.setMonth(month);
            userDtoList = dtoService.selectUserDtoLikeToQuarter(dto);
            PageInfo<UserDto> pageInfo = new PageInfo<>(userDtoList);
            userDtoList = pageInfo.getList();
            map.put("totalPages", pageInfo.getTotal());
            map.put("msg", "查询季度总结成功");
            map.put("data", userDtoList);
            map.put("code", 0);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询季度总结失败");
            map.put("code", 1);
        }
    }

    private void addMonthSummary(String year, String month, List<User> users, String dbtype) {
        List<MonthSummary> list = new ArrayList<>();
        for (User user : users) {
            String serialno = year + "-" + month + "-" + user.getUsercode();
            MonthSummary monthSummary = summaryService.selectByPrimaryKey(serialno);
            if (monthSummary == null) {
                MonthSummary summary = new MonthSummary();
                summary.setSerialno(serialno);
                summary.setMonth(month);
                summary.setIssend("0");
                summary.setYear(year);
                summary.setDbtype(dbtype);
                summary.setEmployeecode(user.getUsercode());
                summary.setState("0");
                list.add(summary);
            }
        }
        if (list.size() > 0) {
            summaryService.batchInsert(list);
        }
    }

    /**
     * 批量月节提交状态修改
     *
     * @param serialnos
     * @return
     */
    @RequestMapping(value = "/updateSummarySubmitState", produces = "application/json;charset=utf-8")
    public Object updateSummarySubmitState(HttpServletRequest req, String serialnos) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            String[] serialno = serialnos.split(",");
            int counts = 0;
            for (int i = 0; i < serialno.length; i++) {
                int count = summaryService.updateSubmitStateBySerialNo(serialno[i]);
                counts += count;
            }
            if (counts > 0) {
                map.put("msg", "批量季结提交成功");
                map.put("code", 0);
            } else {
                map.put("msg", "批量季结提交失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    /**
     * 批量月节评分状态修改
     *
     * @param serialnos
     * @return
     */
    @RequestMapping(value = "/updateSummaryGradeState", produces = "application/json;charset=utf-8")
    public Object updateSummaryGradeState(String serialnos) {
        ModelMap map = new ModelMap();
        String[] serialno = serialnos.split(",");
        int counts = 0;
        for (int i = 0; i < serialno.length; i++) {
            int count = summaryService.updateGradeStateBySerialNo(serialno[i]);
            counts += count;
        }
        if (counts > 0) {
            map.put("msg", "批量季结评分状态修改成功");
            map.put("code", 0);
        } else {
            map.put("msg", "批量季结评分状态修改失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 将月节评分状态全部修改为月节评分
     *
     * @return
     */
    @RequestMapping(value = "/updateSummaryGradeStateAll", produces = "application/json;charset=utf-8")
    public Object updateSummaryGradeStateAll(String dbtype) {
        ModelMap map = new ModelMap();
        int count = summaryService.updateStateAll(dbtype);
        if (count > 0) {
            map.put("msg", "全部季结评分修改成功");
            map.put("code", 0);
        } else {
            map.put("msg", "全部季结评分修改失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 将月节评分状态全部修改为月节评分
     *
     * @return
     */
    @RequestMapping(value = "/updateStateBySerialNo", produces = "application/json;charset=utf-8")
    public Object updateStateBySerialNo(MonthSummary summary) {
        ModelMap map = new ModelMap();
        String state = summary.getState();
        String username = summary.getEmployeecode() + " : " + summary.getEmployeename();
        summary = summaryService.selectByPrimaryKey(summary.getSerialno());
        Score score = new Score();
        score.setDbtype(summary.getDbtype());
        score.setScorredcode(summary.getEmployeecode());
        List<Score> scoreList = scoreService.selectScoreByCodeAndType(score);
        if (scoreList.size() > 0) {
            MonthSummary update = new MonthSummary();
            update.setSerialno(summary.getSerialno());
            update.setState(state);
            int count = summaryService.updateStateBySerialNo(update);
            if (count > 0) {
                map.put("msg", "修改季结状态成功");
                map.put("code", 0);
            } else {
                map.put("msg", "修改季结状态失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "当前用户(" + username + ") 未分配评分关系,修改季结状态失败");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 查看月节内容
     *
     * @return
     */
    @RequestMapping(value = "/selectSummaryBySerialno", produces = "application/json;charset=utf-8")
    public Object selectSummaryBySerialno(String serialno) {
        ModelMap map = new ModelMap();
        try {
            MonthSummary summary = summaryService.selectByPrimaryKey(serialno);
            map.put("msg", "查看季结内容成功");
            map.put("data", summary);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查看季结内容失败");
            map.put("code", 1);
        }

        return map;
    }
}
