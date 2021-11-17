package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.util.CalendarUtil;
import com.welb.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luoyaozu
 * @title: ScoreStationController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/5/2817:48
 */
@RestController
@RequestMapping("/scoreStation")
public class ScoreStationController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    IScoreStationService scoreStationService;
    @Resource
    IUserService userService;
    @Resource
    IStationService stationService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IScoreHistoryService historyService;
    @Resource
    IDutyService dutyService;
    @Resource
    IScoreService scoreService;

    /**
     * 通过评分岗位code查找被评分岗位关系数据
     *
     * @param departmentcode
     * @param scoretype
     * @param stationname
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/listScorred", produces = "application/json;charset=utf-8")
    public Object selectListScorred(HttpServletRequest req, String departmentcode, String scoretype,
                                    String stationname, String scorredstationcode, int pageNum, int pageSize, String dbtype) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            PageHelper.startPage(pageNum, pageSize);
            List<ScoreStation> scores;
            try {
                ScoreStation query = new ScoreStation();
                query.setScoretype(scoretype);
                query.setDbtype(dbtype);
                query.setScorredstationcode(scorredstationcode);
                if (!stationname.equals("")) {
                    query.setScorringstationname(stationname);
                    scores = scoreStationService.selectScoreStationScorringLeft(query, departmentcode);
                } else {
                    scores = scoreStationService.selectScoreStationScorringLeft(query, departmentcode);
                }
                getList(map, scores);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }


    /**
     * 获取集合通用方法
     *
     * @param map
     * @param scores
     */
    private void getList(ModelMap map, List<ScoreStation> scores) {
        PageInfo<ScoreStation> pageInfo = new PageInfo<>(scores);
        scores = pageInfo.getList();
        getName(scores);
        map.put("totalPages", pageInfo.getTotal());
        map.put("msg", "查询成功");
        map.put("data", scores);
    }

    /**
     * 通过被评分岗位code查找评分岗位关系数据
     *
     * @param departmentcode
     * @param scoretype
     * @param stationname
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/listScorring", produces = "application/json;charset=utf-8")
    public Object selectListScorring(HttpServletRequest req, String departmentcode, String scoretype,
                                     String stationname, int pageNum, int pageSize, String dbtype) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            PageHelper.startPage(pageNum, pageSize);
            List<ScoreStation> scores;
            try {
                ScoreStation query = new ScoreStation();
                query.setScoretype(scoretype);
                query.setDbtype(dbtype);
                if (!stationname.equals("")) {
                    query.setScorringstationname(stationname);
                    scores = scoreStationService.selectScoreStationScorringLeft(query, departmentcode);
                } else {
                    scores = scoreStationService.selectScoreStationScorringLeft(query, departmentcode);
                }
                getList(map, scores);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    @RequestMapping(value = "/selectScorredTypeDuty", produces = "application/json;charset=utf-8")
    public Object selectScorredTypeDutydb(HttpServletRequest req, String scorredstationcode, String scoretype, String dutycode, int pageNum, int pageSize, String dbtype) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            PageHelper.startPage(pageNum, pageSize);
            try {
                List<ScoreStation> scores = scoreStationService.selectScoreStationByScorredTypeDuty(scorredstationcode, scoretype, dutycode, dbtype);
                getList(map, scores);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    /**
     * 获取用户姓名通用方法
     *
     * @param scoreStationList
     */

    private void getName(List<ScoreStation> scoreStationList) {
        for (ScoreStation scoreStation : scoreStationList) {
            Station station = stationService.selectByStationCode(scoreStation.getScorredstationcode());
            if (station != null) {//查找被评分岗位的岗位和部门
                scoreStation.setScorredstationname(station.getStationname());
                Department department = departmentService.selectByDeptCode(station.getDepartmentcode());
                if (department != null) {
                    scoreStation.setDepartmentcode2(department.getDepartmentcode());
                    scoreStation.setDepartmentname2(department.getDepartmentname());
                } else {
                    scoreStation.setDepartmentcode2("");
                    scoreStation.setDepartmentname2("");
                }
            } else {
                scoreStation.setScorredstationname("");
            }
            Station station1 = stationService.selectByStationCode(scoreStation.getScorringstationcode());
            if (station != null) {//查找被评分岗位的岗位和部门
                scoreStation.setScorringstationname(station1.getStationname());
                Department department1 = departmentService.selectByDeptCode(station1.getDepartmentcode());
                if (department1 != null) {
                    scoreStation.setDepartmentcode1(department1.getDepartmentcode());
                    scoreStation.setDepartmentname1(department1.getDepartmentname());
                } else {
                    scoreStation.setDepartmentcode1("");
                    scoreStation.setDepartmentname1("");
                }
            } else {
                scoreStation.setScorringstationcode("");
            }
        }
    }

    /**
     * 修改评分关系数据
     *
     * @param scoreStation
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateScore(HttpServletRequest req, ScoreStation scoreStation) {
        ModelMap map = new ModelMap();
        String state = (String) req.getSession().getAttribute("state");
        //查找是否有相同的评分岗位
        ScoreStation selectScoreByCode = scoreStationService.selectTypeByCodeDutyIsNull(scoreStation.getScorredstationcode(), scoreStation.getScorredstationcode(), scoreStation.getDbtype());
        //查找要修改之前的数据
        ScoreStation scoreById = scoreStationService.selectByPrimaryKey(scoreStation.getId());
        if (scoreStation.getScorringstationcode().equals(scoreStation.getScorredstationcode())) {
            map.put("msg", "不能添加自己作为评分岗位");
            map.put("code", 1);
        } else if (selectScoreByCode != null) {
            if (selectScoreByCode.getScorringstationcode().equals(scoreStation.getScorringstationcode())) {
                if (selectScoreByCode.getScoretype().equals(scoreStation.getScoretype())) {
                    getScorringInfo(scoreStation, map, selectScoreByCode);
                } else {
                    if (scoreById.getScorringstationcode().equals(scoreStation.getScorringstationcode())) {
                        judgeScoreIsNull(state, scoreStation, map, selectScoreByCode);
                        int count = scoreStationService.updateByPrimaryKeySelective(scoreStation);
                        updateSuccess(map, count, "修改数据成功", "修改数据失败");
                    } else {
                        getScorringInfo(scoreStation, map, selectScoreByCode);
                    }
                }
            } else {
                judgeScoreIsNull(state, scoreStation, map, selectScoreByCode);
                int count = scoreStationService.updateByPrimaryKeySelective(scoreStation);
                updateSuccess(map, count, "修改数据成功", "修改数据失败");
            }
        } else {
            int count = scoreStationService.updateByPrimaryKeySelective(scoreStation);
            updateSuccess(map, count, "修改数据成功", "修改数据失败");
        }
        return map;

    }

    private void getScorringInfo(ScoreStation scoreStation, ModelMap map, ScoreStation selectScoreByCode) {
        Station station = stationService.selectByStationCode(scoreStation.getScorringstationcode());
        String type = selectScoreByCode.getScoretype();
        map.put("msg", station.getStationname() + "(" + type + ")" + "已被选择为其他类评分岗位，请重新选择");
        map.put("code", 1);
    }

    private void judgeScoreIsNull(String state, ScoreStation score, ModelMap map, ScoreStation selectScoreByCode) {
        int count = scoreStationService.updateByPrimaryKeySelective(score);
        String year = CalendarUtil.getYear();
        String month = CalendarUtil.getMonth();
        //   String quarter = CalendarUtil.getQuarter(month);
        int i = Integer.parseInt(month.trim()) - 1;
        //获取当前系统时间
        String sysTime = DateUtil.getTime();

        //手动考核-查看所有季节总结
//        manualUpdateScore(score, map, selectScoreByCode, count, year, month, i, sysTime);


    }

    private void manualUpdateScore(ScoreStation score, ModelMap map, ScoreStation selectScoreByCode, int count, String year, String quarter, int i, String sysTime) {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", score.getDbtype());
        if (setTime != null) {
            try {
                //开始新的月度考核
                month = quarter;
//                getScoreFlowType(score, map, selectScoreByCode, count, setTime.getYear(), setTime.getMonth());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 添加各类评分岗位数据
     *
     * @return
     */
    @RequestMapping(value = "/addScorring", produces = "application/json;charset=utf-8")
    public Object addScorrring(ScoreStation scoreStation, String fullscorringstationcode, String scoretype, String dbtype) {
        ModelMap map = new ModelMap();
        try {
            int flag = 0;
            boolean temp = false;
            StringBuilder sb = new StringBuilder();
            String[] scorringstationcodes = fullscorringstationcode.split(",");
            List<String> scorringcode = new ArrayList<>();
            ScoreStation scoreStation1 = new ScoreStation();
            int ncount = 0;
            for (int i = 0; i < scorringstationcodes.length; i++) {
                // 打分岗位
                Station station = stationService.selectByStationCode(scorringstationcodes[i]);
                String stationCode = station.getStationcode();
                String stationName = station.getStationname();

                scoreStation1.setScorredstationcode(scoreStation.getScorredstationcode());
                scoreStation1.setScoretype(scoretype);
                scoreStation1.setDbtype(dbtype);
                scoreStation1.setScorringstationcode(stationCode);
                List<ScoreStation> selectScoreByCode = scoreStationService.selectTypeByCodeList(scoreStation1.getScorredstationcode(), scoreStation1.getScorringstationcode(), dbtype);
//                可以加自己岗位来评分
//                if (scoreStation1.getScorringstationcode().equals(scoreStation1.getScorredstationcode())) {
//                    flag = 1;
//                    break;
//                }
                if (selectScoreByCode.size() > 0) {
                    temp = true;
                    sb.append(stationName + "(" + scoretype + ")").append(";");
                } else {
                    flag = 3;
                    scorringcode.add(scoreStation1.getScorringstationcode());
                }
            }
//          可以加自己岗位来评分
//            if (flag == 1) {
//                map.put("msg", "不能添加被评分岗位作为评分岗位。");
//                map.put("code", 1);
//            } else {
            if (temp) {
                sb.append("以上岗位已被选择为其他类评分岗位，请重新选择。");
                map.put("msg", sb);
                map.put("code", 1);
            } else {
                if (scorringcode.size() == scorringstationcodes.length) {
                    ScoreStation newScore = new ScoreStation();
                    for (int i = 0; i < scorringcode.size(); i++) {
                        newScore.setScorredstationcode(scoreStation.getScorredstationcode());
                        newScore.setScoretype(scoreStation.getScoretype());
                        newScore.setDbtype(dbtype);
                        newScore.setScorringstationcode(scorringcode.get(i));
                        int count = scoreStationService.insertSelective(newScore);
                        ncount += count;
                    }
                    if (ncount > 0) {
                        map.put("msg", "添加" + scoretype + "类评分岗位成功");
                        map.put("code", 0);
                    } else {
                        map.put("msg", "添加" + scoretype + "类评分岗位失败");
                        map.put("code", 1);
                    }
                }
            }
//            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * 添加各类指标对应的评分岗位数据
     *
     * @return
     */
    @RequestMapping(value = "/addDutyScorring", produces = "application/json;charset=utf-8")
    public Object addDutyScorrring(ScoreStation scoreStation, String fullscorringstationcode, String fulldutycode) {
        ModelMap map = new ModelMap();
        try {
            StringBuilder sb1 = new StringBuilder();
            String[] scorringstationcodes = fullscorringstationcode.split(",");
            List<String> codeList = new ArrayList<>();
            for (String item : scorringstationcodes) {
                codeList.add(item);
            }
            List<Station> stationqueryList = stationService.findStationByInCodeList(codeList);
            long count = 0;
            List<Duty> dutyList = dutyService.selectDutyByStationCode(scoreStation.getScorredstationcode(), scoreStation.getDbtype());
            String[] dutycodes = fulldutycode.split(",");
            List<String> dutyCodeList = new ArrayList<>();
            for (String code : dutycodes) {
                count = dutyList.stream().filter(s -> s.getDutycode().equals(code)).count();
                if (count > 0) {
                    dutyCodeList.add(code);
                }
            }
            boolean temp1 = false;
            int flag = 0;
            String userMsg = "";
            if (dutyCodeList.size() > 0) {
                scorringstationcodes = new String[stationqueryList.size()];
                for (int i = 0; i < stationqueryList.size(); i++) {
                    scorringstationcodes[i] = stationqueryList.get(i).getStationcode();
                }
                String scoreType = scoreStation.getScoretype();
                // 查询去掉 scoreType
                scoreStation.setScoretype(null);
                List<ScoreStation> dutyScoreStationList = scoreStationService.findScoreStationScorringInList(scoreStation, scorringstationcodes);
                List<ScoreStation> createList = new ArrayList<>();
                for (Station st : stationqueryList) {
                    count = dutyScoreStationList.stream().filter(
                            s -> s.getScorringstationcode().equals(st.getStationcode())
                                    && s.getScorredstationcode().equals(scoreStation.getScorredstationcode())
                                    && (s.getDutycode() == null || s.getDutycode().equals(""))
                    ).count();
                    if (count == 0) {
//                        if (flag == 0) {
//                            可以加自己岗位评分
//                            if (scoreStation.getScorredstationcode().equals(st.getStationcode())) {
//                                flag = 1;
//                            } else {
                        for (String code : dutyCodeList) {
                            count = dutyScoreStationList.stream().filter(
                                    s -> s.getScorringstationcode().equals(st.getStationcode())
                                            && s.getScorredstationcode().equals(scoreStation.getScorredstationcode())
                                            && s.getDutycode().equals(code)
                            ).count();

                            if (count == 0) {
                                ScoreStation insert = new ScoreStation();
                                insert.setScoretype(scoreType);
                                insert.setScorredstationcode(scoreStation.getScorredstationcode());
                                insert.setScorringstationcode(st.getStationcode());
                                insert.setDutycode(code);
                                insert.setDbtype(scoreStation.getDbtype());
                                createList.add(insert);
                            } else {
                                temp1 = true;
                                userMsg = st.getStationname() + "(" + st.getDepartmentname() + ")";
                                if (sb1.indexOf(userMsg) == -1) {
                                    sb1.append(userMsg).append(";");
                                }
                            }
                        }
//                            }
//                        }
                    } else {
                        temp1 = true;
                        userMsg = st.getStationname() + "(" + st.getDepartmentname() + ")";
                        if (sb1.indexOf(userMsg) == -1) {
                            sb1.append(userMsg).append(";");
                        }
                    }
                }
//              可以加自己岗位评分
//                if (flag == 1) {
//                    map.put("msg", "不能添加被评分岗位作为评分岗位。");
//                    map.put("code", 1);
//                } else {
                boolean isAdd = false;
                if (createList.size() > 0) {
                    for (ScoreStation insert : createList) {
                        scoreStationService.insertSelective(insert);
                    }
                    isAdd = true;
                }

                if (temp1) {
                    sb1.append("以上岗位已添加或已被其他选择为类评分岗位，请重新选择。");
                    map.put("msg", (isAdd ? "部分评分岗位添加成功。" : "") + sb1);
                    map.put("code", isAdd ? 0 : 1);
                } else {
                    if (isAdd) {
                        map.put("msg", "添加" + scoreType + "类评分岗位成功.");
                        map.put("code", 0);
                    } else {
                        map.put("msg", "添加" + scoreType + "类评分岗位失败.");
                        map.put("code", 1);
                    }
                }
//                }
            } else {
                map.put("msg", "选择的指标无效，请重新选择");
                map.put("code", 1);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * 添加被评分岗位数据
     *
     * @return
     */
    @RequestMapping(value = "/addScorred", produces = "application/json;charset=utf-8")
    public Object addScored(ScoreStation scoreStation, String fullscorredstationcode, String scoretype, String dbtype) {
        ModelMap map = new ModelMap();
        String[] scorredstationcodes = fullscorredstationcode.split(",");
        List<String> scorredstationcode = new ArrayList<>();
        ScoreStation score1 = new ScoreStation();
        int flag = 1;
        boolean temp = false;
        StringBuilder sb = new StringBuilder();
        int counts = 0;
        for (int i = 0; i < scorredstationcodes.length; i++) {
            score1.setScorredstationcode(scorredstationcodes[i]);
            score1.setScoretype(scoretype);
            score1.setDbtype(dbtype);
            score1.setScorringstationcode(score1.getScorringstationcode());
            List<ScoreStation> selectScoreByCode = scoreStationService.selectTypeByCodeList(score1.getScorredstationcode(), score1.getScorringstationcode(), dbtype);

            if (score1.getScorringstationcode().equals(score1.getScorredstationcode())) {
                flag = 1;
                break;
            }
            if (selectScoreByCode.size() > 0) {
                temp = true;
                User user = userService.findUserByUserCode(score1.getScorredstationcode());
                String moneycard = user.getMoneycard();
                String username = user.getUsername();
                String type = selectScoreByCode.get(0).getScoretype();
                sb.append(username + "(" + moneycard + "-" + type + ")").append(";");
            } else {
                scorredstationcode.add(score1.getScorredstationcode());
            }
        }
        if (flag == 1) {
            map.put("msg", "不能添加自己作为被评分岗位");
            map.put("code", 1);
        }
        if (temp) {
            sb.append("以上员工已被选择为其他类被评分岗位，请重新选择");
            map.put("msg", sb);
            map.put("code", 1);
        } else {
            if (scorredstationcode.size() == scorredstationcodes.length) {
                ScoreStation newScore = new ScoreStation();
                for (int i = 0; i < scorredstationcode.size(); i++) {
                    newScore.setScorringstationcode(scoreStation.getScorringstationcode());
                    newScore.setScoretype(scoreStation.getScoretype());
                    newScore.setDbtype(dbtype);
                    newScore.setScorredstationcode(scorredstationcode.get(i));
                    int count = scoreStationService.insertSelective(newScore);
                    counts += count;
                }
                if (counts > 0) {
                    //修改评分状态
                    ScoreHistory history = historyService.selectScoreHistoryByUserCode(scoreStation.getScorringstationcode(), dbtype);
                    if (history != null) {
                        if (history.getScorestatus().equals("3")) {
                            history.setScorestatus("2");
                            historyService.updateByPrimaryKeySelective(history);
                        }
                    }
                    map.put("msg", "添加" + scoretype + "类被评分岗位成功");
                    map.put("code", 0);
                } else {
                    map.put("msg", "添加" + scoretype + "类被评分岗位失败");
                    map.put("code", 1);
                }
            }

        }
        return map;
    }

    @RequestMapping(value = "/batchUpdateScoreStation", produces = "application/json;charset=utf-8")
    public Object batchUpdateScoreStation1(String scorredStationCode, String scoretype, String dutycode, String dbtype, String fullStationCode) {
        ModelMap map = new ModelMap();
        try {
            String[] scorredstationcodes = fullStationCode.split(",");
//                需求需要修改不排除自己岗位
//                List<Station> stationList = stationService.selectStationByNotEF(scorredStationCode);
            List<Station> stationList = stationService.selectStationByNotEF(null);
            List<ScoreStation> scoreStationList = scoreStationService.selectScoreStationByScorredTypeDuty(scorredStationCode, null, dutycode, dbtype);
            List<ScoreStation> createList = new ArrayList<>();
            List<ScoreStation> updateList = new ArrayList<>();
            List<String> codeList = new ArrayList<>();
            long count = 0;
            if (scorredstationcodes.length > 0) {
                for (String code : scorredstationcodes) {
                    if (code != null && !code.equals("")) {
                        codeList.add(code);
                    }
                }
            }
            if (scoreStationList.size() > 0) {
                List<ScoreStation> query = new ArrayList<>();
                for (Station station : stationList) {
                    count = codeList.stream().filter(s -> s.equals(station.getStationcode())).count();
                    if (count > 0) {
                        if (scoretype.equals("E")) {
                            query = scoreStationList.stream().filter(s -> s.getScorringstationcode().equals(station.getStationcode())).collect(Collectors.toList());
                            if (query.size() > 0) {
                                if (!scoretype.equals(query.get(0).getScoretype())) {
                                    ScoreStation update = new ScoreStation();
                                    update.setScoretype(scoretype);
                                    update.setId(query.get(0).getId());
                                    updateList.add(update);
                                }
                            } else {
                                this.insertScoreStation(scorredStationCode, station.getStationcode(), scoretype, dutycode, dbtype, createList);
                            }
                        } else {
                            query = scoreStationList.stream().filter(s -> s.getScorringstationcode().equals(station.getStationcode())).collect(Collectors.toList());
                            if (query.size() > 0) {
                                if (!scoretype.equals(query.get(0).getScoretype())) {
                                    ScoreStation update = new ScoreStation();
                                    update.setScoretype("F");
                                    update.setId(query.get(0).getId());
                                    updateList.add(update);
                                }
                            } else {
                                this.insertScoreStation(scorredStationCode, station.getStationcode(), "F", dutycode, dbtype, createList);
                            }
                        }
                    } else {
                        if (scoretype.equals("E")) {
                            query = scoreStationList.stream().filter(s -> s.getScorringstationcode().equals(station.getStationcode())).collect(Collectors.toList());
                            if (query.size() > 0) {
                                if (scoretype.equals(query.get(0).getScoretype())) {
                                    ScoreStation update = new ScoreStation();
                                    update.setScoretype("F");
                                    update.setId(query.get(0).getId());
                                    updateList.add(update);
                                }
                            } else {
                                this.insertScoreStation(scorredStationCode, station.getStationcode(), "F", dutycode, dbtype, createList);
                            }
                        } else {
                            query = scoreStationList.stream().filter(s -> s.getScorringstationcode().equals(station.getStationcode())).collect(Collectors.toList());
                            if (query.size() > 0) {
                                if (scoretype.equals(query.get(0).getScoretype())) {
                                    ScoreStation update = new ScoreStation();
                                    update.setScoretype("E");
                                    update.setId(query.get(0).getId());
                                    updateList.add(update);
                                }
                            } else {
                                this.insertScoreStation(scorredStationCode, station.getStationcode(), "E", dutycode, dbtype, createList);
                            }
                        }
                    }
                }
            } else {
                for (Station station : stationList) {
                    if (codeList.size() > 0) {
                        count = codeList.stream().filter(s -> s.equals(station.getStationcode())).count();
                        if (count > 0) {
                            this.insertScoreStation(scorredStationCode, station.getStationcode(), scoretype, dutycode, dbtype, createList);
                        } else {
                            this.insertScoreStation(scorredStationCode, station.getStationcode(), scoretype.equals("E") ? "F" : "E", dutycode, dbtype, createList);
                        }

                    } else {
                        this.insertScoreStation(scorredStationCode, station.getStationcode(), scoretype.equals("E") ? "F" : "E", dutycode, dbtype, createList);
                    }
                }
            }
            boolean isEdit = false;
            if (createList.size() > 0) {
                isEdit = true;
                for (ScoreStation insert : createList) {
                    scoreStationService.insertSelective(insert);
                }
            }
            if (updateList.size() > 0) {
                isEdit = true;
                for (ScoreStation update : updateList) {
                    scoreStationService.updateByPrimaryKeySelective(update);
                }
            }
            map.put("msg", isEdit ? "批量更新成功" : "无更新数据");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            map.put("msg", "批量更新失败");
            map.put("code", 1);
        }
        return map;
    }

    private void insertScoreStation(String ScorredStationCode, String ScorringStationCode, String scoreType, String dutyCode, String dbtype, List<ScoreStation> createList) {
        ScoreStation insert = new ScoreStation();
        insert.setScorringstationcode(ScorringStationCode);
        insert.setScorredstationcode(ScorredStationCode);
        insert.setScoretype(scoreType);
        insert.setDutycode(dutyCode);
        insert.setDbtype(dbtype);
        createList.add(insert);
    }

    /**
     * 删除评分关系数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object deleteScore(Integer id) {
        ModelMap map = new ModelMap();
        ScoreStation scoreStation = scoreStationService.selectByPrimaryKey(id);
        int count = 0;
        if (scoreStation != null) {
            count = scoreStationService.deleteByPrimaryKey(id);
        }
        updateSuccess(map, count, "删除数据成功", "删除数据失败");
        return map;

    }

    @RequestMapping(value = "/deleteDutyScore", produces = "application/json;charset=utf-8")
    public Object deleteDutyScore1(ScoreStation scoreStation, String fulldutycode) {
        ModelMap map = new ModelMap();
        try {
            String[] dutycodes = fulldutycode.split(",");
            List<ScoreStation> dutyScoreList = scoreStationService.findScoreStationDutyInList(scoreStation, dutycodes);
            for (ScoreStation item : dutyScoreList) {
                scoreStationService.deleteByPrimaryKey(item.getId());
            }
            map.put("msg", "批量删除成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            map.put("msg", "批量删除失败");
            map.put("code", 1);
        }
        return map;
    }

    @RequestMapping(value = "/deleteScoreDutyScorringUser", produces = "application/json;charset=utf-8")
    public Object deleteScoreDutyScorringUser1(ScoreStation scoreStation) {
        ModelMap map = new ModelMap();
        try {
            ScoreStation ss = scoreStationService.selectTypeByCodeDuty(scoreStation.getScorredstationcode(), scoreStation.getScorringstationcode(),
                    scoreStation.getDutycode(), scoreStation.getDbtype());
            int count = 0;
            if (ss != null) {
                count = scoreStationService.deleteByPrimaryKey(ss.getId());
            }
            if (count > 0) {
                map.put("msg", "批量删除成功");
                map.put("code", 0);
            } else {
                map.put("msg", "批量删除失败");
                map.put("code", 1);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            map.put("msg", "批量删除失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/batchDelete")
    public Object batchDelete(String ids) {
        ModelMap map = new ModelMap();
        try {
            String[] split = ids.split(",");
            Integer id = 0;
            for (int i = 0; i < split.length; i++) {
                id = Integer.parseInt(split[i]);
                ScoreStation scoreStation = scoreStationService.selectByPrimaryKey(id);
                if (scoreStation != null) {
                    scoreStationService.deleteByPrimaryKey(id);
                }
            }
            map.put("msg", "批量删除成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            map.put("msg", "批量删除失败");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 修改评分关系数据
     *
     * @param score
     * @return
     */
    @RequestMapping(value = "/updateScore", produces = "application/json;charset=utf-8")
    public Object updateScore1(HttpServletRequest req, ScoreStation score) {
        ModelMap map = new ModelMap();
        String state = (String) req.getSession().getAttribute("state");
        ScoreStation selectScoreByCode = scoreStationService.selectTypeByCodeDutyIsNull(score.getScorredstationcode(), score.getScorringstationcode(), score.getDbtype());
        ScoreStation scoreById = scoreStationService.selectByPrimaryKey(score.getId());
        if (score.getScorringstationcode().equals(score.getScorredstationcode())) {
            map.put("msg", "不能添加自己作为被评分岗位");
            map.put("code", 1);
        } else {
            if (selectScoreByCode != null) {
                if (selectScoreByCode.getScorredstationcode().equals(score.getScorredstationcode())) {
                    if (selectScoreByCode.getScoretype().equals(score.getScoretype())) {
                        getScorredInfo(score, map, selectScoreByCode);
                    } else {
                        if (scoreById.getScorredstationcode().equals(score.getScorredstationcode())) {
                            judgeScoreIsNull(state, score, map, selectScoreByCode);
                            int count = scoreStationService.updateByPrimaryKeySelective(score);
                            updateSuccess(map, count, "修改数据成功", "修改数据失败");
                        } else {
                            getScorredInfo(score, map, selectScoreByCode);
                        }
                    }
                } else {
                    judgeScoreIsNull(state, score, map, selectScoreByCode);
                    int count = scoreStationService.updateByPrimaryKeySelective(score);
                    updateSuccess(map, count, "修改数据成功", "修改数据失败");
                }
            } else {
                int count = scoreStationService.updateByPrimaryKeySelective(score);
                updateSuccess(map, count, "修改数据成功", "修改数据失败");
            }
        }
        return map;

    }

    private void getScorredInfo(ScoreStation score, ModelMap map, ScoreStation selectScoreByCode) {
        Station station = stationService.selectByStationCode(score.getScorredstationcode());
        String type = selectScoreByCode.getScoretype();
        map.put("msg", station.getStationname() + "(" + type + ")" + "已被选择为其他类评分岗位，请重新选择");
        map.put("code", 1);
    }


    private void updateSuccess(ModelMap map, int count, String 修改数据成功, String 修改数据失败) {
        if (count > 0) {
            map.put("msg", 修改数据成功);
            map.put("code", 0);
        } else {
            map.put("msg", 修改数据失败);
            map.put("code", 1);
        }
    }


    /**
     * 刪除数据库残留的垃圾数据
     *
     * @return
     */
    @RequestMapping("/findAll")
    public Object findAll(String dbtype) {
        ModelMap map = new ModelMap();
        try {
            List<ScoreStation> scoreStationAll = scoreStationService.findScoreStationAll(dbtype);
            if (scoreStationAll.size() > 0) {
                for (ScoreStation scoreStation : scoreStationAll) {
                    Station station1 = stationService.selectByStationCode(scoreStation.getScorredstationcode());
                    Station station2 = stationService.selectByStationCode(scoreStation.getScorringstationcode());
                    if (station1 == null || station2 == null) {
                        scoreStationService.deleteByPrimaryKey(scoreStation.getId());
                    }
                }
                map.put("msg", "成功");
            }
        } catch (Exception E) {
            map.put("msg", "失败");
        }
        return map;
    }

    @RequestMapping("/shengChengScore")
    public Object shengChengScoreAll(String dbtype) {
        ModelMap map = new ModelMap();
        try {
            List<ScoreStation> scoreStationList = scoreStationService.findScoreStationAll(dbtype);
            if (scoreStationList.size() > 0) {
                scoreService.deleteDbtype(dbtype);
                List<User> userListAll = userService.selectUserScoreStationList();
                List<User> user300List = userListAll.stream().filter(s -> s.getRolecode().equals("300")).collect(Collectors.toList());
                List<Score> createList = new ArrayList<>();
                List<User> queryScorringUserList = new ArrayList<>();
                List<User> queryScorredUserList = new ArrayList<>();
                for (ScoreStation item : scoreStationList) {
                    queryScorredUserList = user300List.stream().filter(s -> s.getStationcode().equals(item.getScorredstationcode())).collect(Collectors.toList());
                    for (User ued : queryScorredUserList) {
                        if (ued.getUsercode() != null && !ued.getUsercode().equals("")) {
                            queryScorringUserList = userListAll.stream().filter(s -> s.getStationcode().equals(item.getScorringstationcode())).collect(Collectors.toList());
                            for (User u : queryScorringUserList) {
                                if (u.getUsercode() != null && !u.getUsercode().equals("") && !ued.getUsercode().equals(u.getUsercode())) {
                                    Score score = new Score();
                                    score.setScorredcode(ued.getUsercode());
                                    score.setScorringcode(u.getUsercode());
                                    score.setScoretype(item.getScoretype());
                                    score.setDutycode(item.getDutycode());
                                    score.setDbtype(item.getDbtype());
                                    createList.add(score);
                                }
                            }
                        }
                    }
                }
                if (createList.size() > 0) {
                    for (Score score : createList)
                        scoreService.insertSelective(score);

                    map.put("msg", "生成成功");
                    map.put("code", 0);
                } else {
                    map.put("msg", "无创建数据，生成失败");
                    map.put("code", 0);
                }
            } else {
                map.put("msg", "无岗位关系，生成失败");
                map.put("code", 0);
            }
        } catch (Exception e) {
            map.put("msg", "生成失败");
            map.put("code", 1);
        }
        return map;
    }

}
