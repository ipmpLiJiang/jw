package com.welb.organization_check.controller;

import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserSummaryDto;
import com.welb.organization_check.dto.UserScoreDto;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author luoyaozu
 * @title: HomePageController
 * @projectName xh-360appraisal-interface
 * @description: 首页控制器
 * @date 2019/6/1122:02
 */
@RestController
@RequestMapping("/homepage")
@Transactional
public class HomePageController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IUserSummaryDtoService dtoService;
    @Resource
    IStationService stationService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IDutyService dutyService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IScoreDetailService detailService;
    @Resource
    IScoreService scoreService;
    @Resource
    IUserService userService;
    @Resource
    IEvaluationReportService evaluationReportService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IScoreHistoryService historyService;
    @Resource
    IUserDtoService userDtoService;
    @Autowired
    IUserScoreDtoService userScoreDtoService;
    @Autowired
    IMonthSummaryService monthSummaryService;
    @Resource
    IResultReportService resultReportService;
    @Autowired
    IScoreYdyfService ydyfService;

    /**
     * 个人考核详情页面
     *
     * @return
     */
    @RequestMapping(value = "/getDetail", produces = "application/json;charset=utf-8")
    public Object getDetail(HttpServletRequest req, UserSummaryDto dtos, String scorringUserCode) {
        //获取当前登录用户的编号
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            Map<String, Object> data = new LinkedHashMap<>();
            UserSummaryDto dto;
            if (usercode == null) {
                map.put("msg", "用户登录过期，请重新登录");
                map.put("code", 810);
            } else {
                try {
                    if (dtos.getMonth() != null && dtos.getYear() != null) {
                        dto = dtoService.selectUserSummaryByLike(dtos);
                        if (dto != null) {
                            //查找岗位
                            dto.setMonth(dtos.getMonth());
                            getMapList(map, usercode, data, dto, scorringUserCode, false);
                        } else {
                            map.put("msg", "数据为空");
                            map.put("code", 0);
                        }
                    } else {
//                    当前年份
                        String year = CalendarUtil.getYear();
//                    当前月份h
                        String month = CalendarUtil.getMonth();
//                    当前月度
                        String quarter = month;//CalendarUtil.getQuarter(month);
                        //当前上一月度
                        int count = Integer.parseInt(month.trim()) - 1;
                        //获取当前系统时间
                        String sysTime = DateUtil.getTime();

                        //手动考核  --个人考核详情页面
                        manualGetDetail(dtos, map, usercode, data, year, quarter, count, sysTime, false);

                    }
                } catch (Exception e) {
                    log.error(LogUtil.getTrace(e));
                    map.put("msg", "查询个人考核详情失败");
                    map.put("code", 1);
                }
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }


    /**
     * 个人考核详情页面
     *
     * @return
     */
    @RequestMapping(value = "/getDutyDetail", produces = "application/json;charset=utf-8")
    public Object getDutyDetail(HttpServletRequest req, UserSummaryDto dtos, String scorringUserCode) {
        //获取当前登录用户的编号
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            Map<String, Object> data = new LinkedHashMap<>();
            UserSummaryDto dto;
            if (usercode == null) {
                map.put("msg", "用户登录过期，请重新登录");
                map.put("code", 810);
            } else {
                try {
                    if (dtos.getMonth() != null && dtos.getYear() != null) {
                        dto = dtoService.selectUserSummaryByLike(dtos);
                        if (dto != null) {
                            //查找岗位
                            dto.setMonth(dtos.getMonth());
                            getMapList(map, usercode, data, dto, scorringUserCode, true);
                        } else {
                            map.put("msg", "数据为空");
                            map.put("code", 0);
                        }
                    } else {
//                    当前年份
                        String year = CalendarUtil.getYear();
//                    当前月份h
                        String month = CalendarUtil.getMonth();
//                    当前月度
                        String quarter = month;//CalendarUtil.getQuarter(month);
                        //当前上一月度
                        int count = Integer.parseInt(month.trim()) - 1;
                        //获取当前系统时间
                        String sysTime = DateUtil.getTime();

                        //手动考核  --个人考核详情页面
                        manualGetDetail(dtos, map, usercode, data, year, quarter, count, sysTime, true);

                    }
                } catch (Exception e) {
                    log.error(LogUtil.getTrace(e));
                    map.put("msg", "查询个人考核详情失败");
                    map.put("code", 1);
                }
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void getMapList(ModelMap map, String usercode, Map<String, Object> data, UserSummaryDto dto, String scorringUserCode, boolean isDuty) {
        //获取评分人给被评分人打分的情况
        Station station = new Station();
        List<ScoreFlow> flow = new ArrayList<>();
        if (scorringUserCode != null && !scorringUserCode.equals("")) {
            flow = flowService.selectByScoreFlow(dto.getSerialno(), scorringUserCode, dto.getDbtype());
        } else {
            station = stationService.selectByStationCode(dto.getStationcode());
            flow = flowService.selectByScoreFlow(dto.getSerialno(), usercode, dto.getDbtype());
        }
        if (isDuty) {
            if (scorringUserCode != null && !scorringUserCode.equals("")) {
                usercode = scorringUserCode;
            }
            getDutyFlow(map, data, dto, station, flow, usercode);
        } else {
            getFlow(map, data, dto, station, flow);
        }
    }

    private void manualGetDetail(UserSummaryDto dtos, ModelMap map, String usercode, Map<String, Object> data, String year, String quarter, int count, String sysTime, boolean isDuty) throws ParseException {
        String month;
        UserSummaryDto dto;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dtos.getDbtype());
        if (setTime != null) {
            //新一月度考核-手动设置的考核时间未超过系统自动考核时间

            //开始新的月度考核
            month = quarter;
            dtos.setYear(setTime.getYear());
            dtos.setMonth(setTime.getMonth());

            dto = dtoService.selectUserSummaryByLike(dtos);
            dto.setMonth(setTime.getMonth());
            //查找岗位
            getMapList(map, usercode, data, dto, null, isDuty);


        }
    }

    private void automaticGetDetail(UserSummaryDto dtos, ModelMap map, String usercode, Map<String, Object> data, String year, int count) {
        String month;
        UserSummaryDto dto;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
        } else {
            month = String.valueOf(count);
        }
        dtos.setYear(year);
        dtos.setMonth(month);
        dto = dtoService.selectUserSummaryByLike(dtos);
        dto.setMonth(month);
        //查找岗位
        getMapList(map, usercode, data, dto, null, false);
    }

    private void getDutyFlow(ModelMap map, Map<String, Object> data, UserSummaryDto dto, Station station, List<ScoreFlow> scoreFlow, String userCode) {
        // 1、被评分人 2、评分人
        List<Score> scoreList = scoreService.selectTypeByCodeList(dto.getUsercode(), userCode, dto.getDbtype());
        long count = scoreList.stream().filter(s -> s.getScoretype().equals("D")).count();
        List<Duty> dutyList = new ArrayList<>();
        String scoreTypeAbc = "";
        boolean isDuty = false;
        // 判断是否有 D
        if (count > 0) {
            isDuty = true;
            // 判断是否有 A,B,C
            List<Score> scoreAbcList = scoreList.stream().filter(s -> !s.getScoretype().equals("D")).collect(Collectors.toList());
            if (scoreAbcList.size() == 0) {
                dutyList = dutyService.queryDutyByScorringCode(userCode, dto.getUsercode(), "D", dto.getDbtype());
            } else {
                dutyList = dutyService.queryDutyByStationCode(dto.getStationcode(), dto.getDbtype());
                scoreTypeAbc = scoreAbcList.get(0).getScoretype();
            }
        } else {
            dutyList = dutyService.queryDutyByStationCode(dto.getStationcode(), dto.getDbtype());
            scoreTypeAbc = scoreList.get(0).getScoretype();
        }
        if (scoreFlow.size() > 0) {
            Double totalScore = scoreFlow.stream().mapToDouble(ScoreFlow::getScore).sum();

            data.put("total", totalScore + "分");
            //获取基础量化指标相关信息
//                List<Duty> dutyJichu = dutyService.queryDutyByType("0", dto.getStationcode());
            List<Duty> dutyJichu = dutyList.stream().filter(s -> s.getDutytype().equals("0")).collect(Collectors.toList());
            getDutyInfoNew(scoreFlow, dutyJichu);
            //获取关键量化指标的相关信息
//                List<Duty> dutyYiban = dutyService.queryDutyByType("1", dto.getStationcode());
            List<Duty> dutyYiban = dutyList.stream().filter(s -> s.getDutytype().equals("1")).collect(Collectors.toList());
            getDutyInfoNew(scoreFlow, dutyYiban);
            //获取关键量化指标的相关信息
//                List<Duty> dutyZhongdian = dutyService.queryDutyByType("2", dto.getStationcode());
            List<Duty> dutyZhongdian = dutyList.stream().filter(s -> s.getDutytype().equals("2")).collect(Collectors.toList());
            getDutyInfoNew(scoreFlow, dutyZhongdian);
            //获取关键量化指标的相关信息
//                List<Duty> dutyMubiao = dutyService.queryDutyByType("3", dto.getStationcode());
            List<Duty> dutyMubiao = dutyList.stream().filter(s -> s.getDutytype().equals("3")).collect(Collectors.toList());
            getDutyInfoNew(scoreFlow, dutyMubiao);
            //判断是否可编辑
            if (dto.getState().equals("7")) {
                dto.setIsedit("1");
            } else {
                dto.setIsedit("0");
            }
            this.setDuty(dutyJichu, scoreList, scoreTypeAbc, isDuty);
            this.setDuty(dutyYiban, scoreList, scoreTypeAbc, isDuty);
            this.setDuty(dutyZhongdian, scoreList, scoreTypeAbc, isDuty);
            this.setDuty(dutyMubiao, scoreList, scoreTypeAbc, isDuty);
            data.put("detail", dto);
            data.put("stations", station);
            data.put("dutyJichu", dutyJichu);
            data.put("dutyYiban", dutyYiban);
            data.put("dutyZhongdian", dutyZhongdian);
            data.put("dutyMubiao", dutyMubiao);
            map.put("msg", "查询个人考核详情成功");
            map.put("data", data);
            map.put("code", 0);

        } else {
            data.put("total", "");
//            List<Duty> dutyJichu = dutyService.queryDutyByType("0", dto.getStationcode());
            List<Duty> dutyJichu = dutyList.stream().filter(s -> s.getDutytype().equals("0")).collect(Collectors.toList());
            for (Duty duty : dutyJichu) {
                duty.setScore("");
            }
            //获取关键量化指标的相关信息
//            List<Duty> dutyYiban = dutyService.queryDutyByType("1", dto.getStationcode());
            List<Duty> dutyYiban = dutyList.stream().filter(s -> s.getDutytype().equals("1")).collect(Collectors.toList());
            for (Duty duty : dutyYiban) {
                duty.setScore("");
            }
            //获取关键量化指标的相关信息
//            List<Duty> dutyZhongdian = dutyService.queryDutyByType("2", dto.getStationcode());
            List<Duty> dutyZhongdian = dutyList.stream().filter(s -> s.getDutytype().equals("2")).collect(Collectors.toList());
            for (Duty duty : dutyZhongdian) {
                duty.setScore("");
            }

            //获取关键量化指标的相关信息
//            List<Duty> dutyMubiao = dutyService.queryDutyByType("3", dto.getStationcode());
            List<Duty> dutyMubiao = dutyList.stream().filter(s -> s.getDutytype().equals("3")).collect(Collectors.toList());
            for (Duty duty : dutyMubiao) {
                duty.setScore("");
            }
            if (dto.getState().equals("7")) {
                dto.setIsedit("1");
            } else {
                dto.setIsedit("0");
            }
            this.setDuty(dutyJichu, scoreList, scoreTypeAbc, isDuty);
            this.setDuty(dutyYiban, scoreList, scoreTypeAbc, isDuty);
            this.setDuty(dutyZhongdian, scoreList, scoreTypeAbc, isDuty);
            this.setDuty(dutyMubiao, scoreList, scoreTypeAbc, isDuty);
            //获取基础量化指标相关信息
            data.put("detail", dto);
            data.put("stations", station);
            data.put("dutyJichu", dutyJichu);
            data.put("dutyYiban", dutyYiban);
            data.put("dutyZhongdian", dutyZhongdian);
            data.put("dutyMubiao", dutyMubiao);

            map.put("msg", "查询个人考核详情成功");
            map.put("data", data);
            map.put("code", 0);
        }
    }

    private void getFlow(ModelMap map, Map<String, Object> data, UserSummaryDto dto, Station station, List<ScoreFlow> scoreFlow) {
        if (scoreFlow.size() > 0) {

            for (ScoreFlow flow : scoreFlow) {
                data.put("total", flow.getScore() + "分");

                Duty du = new Duty();
                du.setDbtype("1");
                List<Duty> dutyList = dutyService.selectDutyAll(du);

                //获取 政治建设
                List<Duty> dutyJichu = dutyList.stream().filter(p -> p.getDutytype().equals("4")).collect(Collectors.toList());
                getDutyInfo(flow, dutyJichu);
                //获取 思想建设
                List<Duty> dutyYiban = dutyList.stream().filter(p -> p.getDutytype().equals("5")).collect(Collectors.toList());
                getDutyInfo(flow, dutyYiban);
                //获取 组织建设
                List<Duty> dutyZhongdian = dutyList.stream().filter(p -> p.getDutytype().equals("6")).collect(Collectors.toList());
                getDutyInfo(flow, dutyZhongdian);
                //获取 党建创新
                List<Duty> dutyMubiao = dutyList.stream().filter(p -> p.getDutytype().equals("7")).collect(Collectors.toList());
                getDutyInfo(flow, dutyMubiao);
                //获取 作风建设
                List<Duty> dutyZuofeng = dutyList.stream().filter(p -> p.getDutytype().equals("8")).collect(Collectors.toList());
                getDutyInfo(flow, dutyZuofeng);
                //判断是否可编辑
                if (dto.getState().equals("7")) {
                    dto.setIsedit("1");
                } else {
                    dto.setIsedit("0");
                }
                data.put("detail", dto);
                data.put("stations", station);
                data.put("dutyJichu", dutyJichu);
                data.put("dutyYiban", dutyYiban);
                data.put("dutyZhongdian", dutyZhongdian);
                data.put("dutyMubiao", dutyMubiao);
                data.put("dutyZuofeng", dutyZuofeng);
                map.put("msg", "查询个人考核详情成功");
                map.put("data", data);
                map.put("code", 0);
            }
        } else {
            data.put("total", "");

            Duty du = new Duty();
            du.setDbtype("1");
            List<Duty> dutyList = dutyService.selectDutyAll(du);

            List<Duty> dutyJichu = dutyList.stream().filter(p -> p.getDutytype().equals("4")).collect(Collectors.toList());
            for (Duty duty : dutyJichu) {
                duty.setScore("");
            }
            //获取关键量化指标的相关信息
            List<Duty> dutyYiban = dutyList.stream().filter(p -> p.getDutytype().equals("5")).collect(Collectors.toList());
            for (Duty duty : dutyYiban) {
                duty.setScore("");
            }
            //获取关键量化指标的相关信息
            List<Duty> dutyZhongdian = dutyList.stream().filter(p -> p.getDutytype().equals("6")).collect(Collectors.toList());
            for (Duty duty : dutyZhongdian) {
                duty.setScore("");
            }

            //获取关键量化指标的相关信息
            List<Duty> dutyMubiao = dutyList.stream().filter(p -> p.getDutytype().equals("7")).collect(Collectors.toList());
            for (Duty duty : dutyMubiao) {
                duty.setScore("");
            }

            List<Duty> dutyZuofeng = dutyList.stream().filter(p -> p.getDutytype().equals("8")).collect(Collectors.toList());
            for (Duty duty : dutyZuofeng) {
                duty.setScore("");
            }

            if (dto.getState().equals("7")) {
                dto.setIsedit("1");
            } else {
                dto.setIsedit("0");
            }
            //获取基础量化指标相关信息
            data.put("detail", dto);
            data.put("stations", station);
            data.put("dutyJichu", dutyJichu);
            data.put("dutyYiban", dutyYiban);
            data.put("dutyZhongdian", dutyZhongdian);
            data.put("dutyMubiao", dutyMubiao);
            data.put("dutyZuofeng", dutyZuofeng);


            map.put("msg", "查询个人考核详情成功");
            map.put("data", data);
            map.put("code", 0);
        }
    }

    private void getDutyInfo(ScoreFlow flow, List<Duty> dutyJichu) {
        for (Duty duty : dutyJichu) {
            ScoreDetail detail = detailService.selectDetailBySerialNo(duty.getDutycode(), flow.getSerialno());
            if (detail != null) {
                duty.setScore(detail.getScore());
            } else {
                duty.setScore("");
            }

        }
    }

    private void getDutyInfoNew(List<ScoreFlow> flowList, List<Duty> dutyJichu) {
        for (Duty duty : dutyJichu) {
            ScoreDetail detail = null;
            for (ScoreFlow flow : flowList) {
                detail = detailService.selectDetailBySerialNo(duty.getDutycode(), flow.getSerialno());
                if (detail != null) {
                    duty.setScore(detail.getScore());
                    break;
                } else {
                    duty.setScore("");
                }
            }

        }
    }

    private void setDuty(List<Duty> dutyList, List<Score> scoreList, String scoreTypeAbc, boolean isDuty) {
        if (dutyList.size() > 0) {
            if (isDuty) {
                List<Score> dsList = scoreList.stream().filter(s -> s.getDutycode() != null && !s.getDutycode().equals("")).collect(Collectors.toList());
                List<Score> queryScoreList = new ArrayList<>();
                for (Duty duty : dutyList) {
                    queryScoreList = dsList.stream().filter(s -> s.getDutycode().equals(duty.getDutycode())).collect(Collectors.toList());
                    if (queryScoreList.size() > 0) {
                        duty.setScoretype(queryScoreList.get(0).getScoretype());
                    } else {
                        duty.setScoretype(scoreTypeAbc);
                    }
                }
            } else {
                for (Duty duty : dutyList) {
                    duty.setScoretype(scoreTypeAbc);
                }
            }
        }
    }


    /**
     * 计算总分
     *
     * @param dto
     * @param dutyJiChu
     * @param dutyYiban
     * @return
     */
    @RequestMapping(value = "/getTotalScore", produces = "application/json;charset=utf-8")
    public Object getTotalScore(HttpServletRequest req, String scorringcode, UserSummaryDto dto, String dutyJiChu,
                                String dutyYiban, String dutyZhongdian, String dutyMubiao, String dutyZuofeng, EvaluationReport report, String year, String month) {
        ModelMap map = new ModelMap();
        //保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            ScoreFlow flow = new ScoreFlow();
            flow.setScorringcode(scorringcode);
            flow.setScoredcode(dto.getEmployeecode());
            flow.setMserialno(dto.getSerialno());
            flow.setScore(dto.getTotal());
            flow.setDbtype(dto.getDbtype());
            report.setTotalscore(dto.getTotal());
            new SimpleDateFormat("yyyy-MM-dd");
            flow.setScoredate(new Date());
            //通过评分人和被评分人code查找评分关系数据
            Score score = scoreService.selectTypeByCode(dto.getEmployeecode(), scorringcode, dto.getDbtype());
            flow.setScoretype(score.getScoretype());
            User user = userService.findUserByUserCode(dto.getEmployeecode());
            if ("A".equals(flow.getScoretype())) {
                flow.setRatio(user.getAratio());
            } else if ("B".equals(flow.getScoretype())) {
                flow.setRatio(user.getBratio());
            } else if ("C".equals(flow.getScoretype())) {
                flow.setRatio(user.getCratio());
            } else if ("D".equals(flow.getScoretype())) {
                flow.setRatio(user.getDratio());
            }
            List<ScoreFlow> flow1 = flowService.selectByScoreFlow(dto.getSerialno(), scorringcode, dto.getDbtype());
            if (flow1.size() > 0) {//不为空  则修改评分信息
                List<ScoreFlow> list = new ArrayList<>();
                for (ScoreFlow scoreFlow : flow1) {
                    scoreFlow.setScore(dto.getTotal());
                    list.add(scoreFlow);
                }
                if (list.size() > 0) {
                    flowService.batchUpdate(list);
                }
                for (ScoreFlow scoreFlow : list) {
                    getScore(dutyJiChu, dutyYiban, dutyZhongdian, dutyMubiao, dutyZuofeng, scoreFlow);
                }
            } else {
                flowService.insertSelective(flow);
                getScore(dutyJiChu, dutyYiban, dutyZhongdian, dutyMubiao, dutyZuofeng, flow);
            }
            /*========================计算完总分之后  个人测评报告也相应的产生一条数据==================================================*/
            getEvaluationReportInfo(req, dto, report);

            /*========================被打分用户分数添加或更新  往历史评分数据表插数据==================================================*/
            addOrUpdateScoreHistory(dto, year, month, df, user);
            /*=========================打分用户的评分状态更新==========================================================================*/
            updateScoreStatus(scorringcode, year, month, dto.getDbtype());
            map.put("msg", "操作成功");
            map.put("code", 0);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "操作失败");
            map.put("error", e.getMessage());
            map.put("eor", e.getStackTrace());
            map.put("code", 1);
        }

        return map;
    }

    /**
     * 计算总分
     *
     * @param dto
     * @param dutyJiChu
     * @param dutyYiban
     * @return
     */
    @RequestMapping(value = "/getDutyTotalScore", produces = "application/json;charset=utf-8")
    public Object getDutyTotalScore(HttpServletRequest req, String scorringcode, UserSummaryDto dto, String dutyJiChu,
                                    String dutyYiban, String dutyZhongdian, String dutyMubiao, EvaluationReport report, String year, String month) {
        ModelMap map = new ModelMap();
        //保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            List<Score> scoreList = scoreService.selectTypeByCodeList(dto.getEmployeecode(), scorringcode, dto.getDbtype());
            List<Score> scoreABCList = scoreList.stream().filter(s -> !s.getScoretype().equals("D")).collect(Collectors.toList());
            List<Score> scoreDList = scoreList.stream().filter(s -> s.getScoretype().equals("D")).collect(Collectors.toList());
            List<String> typeList = new ArrayList<>();
            if (scoreABCList.size() > 0) {
                typeList.add(scoreABCList.get(0).getScoretype());
            }
            if (scoreDList.size() > 0) {
                typeList.add(scoreDList.get(0).getScoretype());
            }
            List<ScoreFlow> flowList = new ArrayList<>();
            User user = userService.findUserByUserCode(dto.getEmployeecode());
            for (String type : typeList) {
                ScoreFlow flow = new ScoreFlow();
                flow.setScorringcode(scorringcode);
                flow.setScoredcode(dto.getEmployeecode());
                flow.setMserialno(dto.getSerialno());
//                下面代码重新赋值
//                flow.setScore(dto.getTotal());
                report.setTotalscore(dto.getTotal());
                new SimpleDateFormat("yyyy-MM-dd");
                flow.setScoredate(new Date());
                //通过评分人和被评分人code查找评分关系数据

                flow.setScoretype(type);
                flow.setDbtype(dto.getDbtype());

                if ("A".equals(flow.getScoretype())) {
                    flow.setRatio(user.getAratio());
                } else if ("B".equals(flow.getScoretype())) {
                    flow.setRatio(user.getBratio());
                } else if ("C".equals(flow.getScoretype())) {
                    flow.setRatio(user.getCratio());
                } else if ("D".equals(flow.getScoretype())) {
                    flow.setRatio(user.getDratio());
                }
                flowList.add(flow);
            }
            // 方法把单个查询，改成改了全部
            List<ScoreFlow> flow1 = flowService.selectByScoreFlow(dto.getSerialno(), scorringcode, dto.getDbtype());
            if (flow1.size() > 0) {//不为空  则修改评分信息
                getDutyScore(dutyJiChu, dutyYiban, dutyZhongdian, dutyMubiao, flow1, scoreList);
            } else {
                for (ScoreFlow scoreFlow : flowList) {
                    flowService.insertSelective(scoreFlow);
                }
                getDutyScore(dutyJiChu, dutyYiban, dutyZhongdian, dutyMubiao, flowList, scoreList);
            }
            /*========================计算完总分之后  个人测评报告也相应的产生一条数据==================================================*/
            getEvaluationReportInfo(req, dto, report);

            /*========================被打分用户分数添加或更新  往历史评分数据表插数据==================================================*/
            addOrUpdateScoreHistory(dto, year, month, df, user);
            /*=========================打分用户的评分状态更新==========================================================================*/
            updateScoreStatus(scorringcode, year, month, dto.getDbtype());
            map.put("msg", "操作成功");
            map.put("code", 0);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "操作失败");
            map.put("error", e.getMessage());
            map.put("eor", e.getStackTrace());
            map.put("code", 1);
        }

        return map;
    }


    private void updateScoreStatus(String scorringcode, String year, String month, String dbtype) {
        ScoreHistory historyState = new ScoreHistory();
        historyState.setYear(year);
        historyState.setMonth(month);
        historyState.setDbtype(dbtype);
        historyState.setUsercode(scorringcode);
        UserDto userDto = new UserDto();
        userDto.setYear(year);
        userDto.setMonth(month);
        userDto.setDbtype(dbtype);
        userDto.setEmployeecode(scorringcode);
        int dtoTotalCount = userDtoService.getTotalCount(userDto);
        String mserialno = year + "-" + month + "-" + dbtype;
        int flowTotalCount = flowService.getTotalCount(mserialno, scorringcode, dbtype);
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

    private ScoreHistory addOrUpdateScoreHistory(UserSummaryDto dto, String year, String month, DecimalFormat df, User user) {
        ScoreHistory history = new ScoreHistory();
        history.setYear(year);
        history.setMonth(month);
        history.setDbtype(dto.getDbtype());
        history.setUsercode(dto.getEmployeecode());

        ScoreHistory scoreHistory = historyService.selectOneByHistory(history);
        if (dto.getDbtype().equals("1")) {
            getTypeUserScore(dto, df, user, history);
        } else {
            getDutyTypeUserScore(dto, history);
        }
        if (scoreHistory == null) {//新增操作
            history.setScorestatus("1");
            historyService.insertSelective(history);
        } else {//更新操作
            history.setId(scoreHistory.getId());
            historyService.updateByPrimaryKeySelective(history);
        }
        return history;
    }

    private void getTypeUserScore(UserSummaryDto dto, DecimalFormat df, User user, ScoreHistory history) {
        Double totalRatio = 0.0;
        //A类评分
        Double AScore = flowService.getTypeAvgScore(dto.getSerialno(), "A", dto.getDbtype());
        if (AScore == null) {
            AScore = 0.0;
            history.setAscore(AScore);
        } else {
            history.setAscore(Double.parseDouble(df.format(AScore)));
            totalRatio += user.getAratio();
        }
        //B类评分
        Double BScore = flowService.getTypeAvgScore(dto.getSerialno(), "B", dto.getDbtype());
        if (BScore == null) {
            BScore = 0.0;
            history.setBscore(BScore);
        } else {
            history.setBscore(Double.parseDouble(df.format(BScore)));
            totalRatio += user.getBratio();
        }
        //C类评分
        Double CScore = flowService.getTypeAvgScore(dto.getSerialno(), "C", dto.getDbtype());
        if (CScore == null) {
            CScore = 0.0;
            history.setCscore(CScore);
        } else {
            history.setCscore(Double.parseDouble(df.format(CScore)));
            totalRatio += user.getCratio();
        }
        //D类评分
        Double DScore = flowService.getTypeAvgScore(dto.getSerialno(), "D", dto.getDbtype());
        if (DScore == null) {
            DScore = 0.0;
            history.setDscore(DScore);
        } else {
            history.setDscore(Double.parseDouble(df.format(DScore)));
            totalRatio += user.getDratio();
        }

        //获取总分  总分= A类总分 X A类评分人系数 +  B类总分 X B类评分人系数 + C类总分 X C类评分人系数 + D类总分 X D类评分人系数

        Double totalscore = history.getAscore() * user.getAratio() + history.getBscore() * user.getBratio() +
                history.getCscore() * user.getCratio() + history.getDscore() * user.getDratio();
        if (totalscore == 0.0 || totalscore == null) {
            history.setTotalscore(0.0);
        } else {
            history.setTotalscore(Double.parseDouble(df.format(totalscore / totalRatio)));
        }
    }

    private void getDutyTypeUserScore(UserSummaryDto dto, ScoreHistory history) {
        List<UserScoreDto> list = userScoreDtoService.findUserDutyScore(dto.getYear(), dto.getMonth(), dto.getEmployeecode(), null, dto.getDbtype());
        List<UserScoreDto> dutyAndRatioList = userScoreDtoService.getTypeUserDutyScore(list,false);
        Double sumScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getScore).sum();
        Double aScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getAScore).sum();
        Double bScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getBScore).sum();
        Double cScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getCScore).sum();
        Double dScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getDScore).sum();

        history.setTotalscore(sumScore);
        history.setAscore(aScore);
        history.setBscore(bScore);
        history.setCscore(cScore);
        history.setDscore(dScore);
    }

    private void getEvaluationReportInfo(HttpServletRequest req, UserSummaryDto dto, EvaluationReport report) throws ParseException {
        String state = (String) req.getSession().getAttribute("state");
        report.setUsercode(dto.getEmployeecode());
//            年份
        String year = CalendarUtil.getYear();
//            月份
        String month = CalendarUtil.getMonth();
//            月度
        String quarter = month;// CalendarUtil.getQuarter(month);
        int count = Integer.parseInt(quarter) - 1;
        //获取当前系统时间
        String sysTime = DateUtil.getTime();
        //手动考核-计算总分
        if (state.equals("1")) {
            ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dto.getDbtype());
            if (setTime != null) {

                //新一月度考核-手动设置的考核时间未超过系统自动考核时间

                //开始新的月度考核
                month = quarter;
                insertOrUpdateEvaluationReport(report, setTime.getYear(), setTime.getMonth(), dto.getDbtype());

            }
        }
    }

    private void insertOrUpdateEvaluationReport(EvaluationReport report, String year, String month, String dbtype) {
        EvaluationReport evaluationReport;
        report.setYear(year);
        report.setMonth(month);
        report.setDbtype(dbtype);
        evaluationReport = evaluationReportService.selectReportByUserCode(report);
        if (evaluationReport == null) {
            evaluationReportService.insertSelective(report);
        } else {
            report.setId(evaluationReport.getId());
            evaluationReportService.updateByPrimaryKeySelective(report);
        }
    }

    private void automaticGetEvaluationInfo(EvaluationReport report, String year, int count) {
        String month;
        EvaluationReport evaluationReport;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
        } else {
            month = String.valueOf(count);
        }
        //  insertOrUpdateEvaluationReport(report, year, month);
    }

    /**
     * 向scoreflow数据库表添加或修改数据
     *
     * @param dutyJiChu
     * @param dutyYiBan
     * @param flow1
     */

    private void getScore(String dutyJiChu, String dutyYiBan, String dutyZhongdian, String dutyMubiao, String dutyZuofeng, ScoreFlow flow1) {
        ScoreDetail detail = new ScoreDetail();
        JSONArray array = JSONArray.fromObject(dutyJiChu);
        List jichu = JSONArray.toList(array, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, jichu);

        JSONArray array1 = JSONArray.fromObject(dutyYiBan);
        List yiban = JSONArray.toList(array1, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, yiban);

        JSONArray array2 = JSONArray.fromObject(dutyZhongdian);
        List zhongdian = JSONArray.toList(array2, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, zhongdian);

        JSONArray array3 = JSONArray.fromObject(dutyMubiao);
        List mubiao = JSONArray.toList(array3, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, mubiao);

        JSONArray array4 = JSONArray.fromObject(dutyZuofeng);
        List zuofeng = JSONArray.toList(array4, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, zuofeng);
    }

    private void getDutyScore(String dutyJiChu, String dutyYiBan, String dutyZhongdian, String dutyMubiao, List<ScoreFlow> flowList, List<Score> scoreList) {
        ScoreDetail detail = new ScoreDetail();
        JSONArray array = JSONArray.fromObject(dutyJiChu);
        Double[] abcd = new Double[2];
        abcd[0] = 0.0;
        abcd[1] = 0.0;
        List jichu = JSONArray.toList(array, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdateDuty(flowList, scoreList, detail, jichu, abcd);

        JSONArray array1 = JSONArray.fromObject(dutyYiBan);
        List yiban = JSONArray.toList(array1, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdateDuty(flowList, scoreList, detail, yiban, abcd);

        JSONArray array2 = JSONArray.fromObject(dutyZhongdian);
        List zhongdian = JSONArray.toList(array2, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdateDuty(flowList, scoreList, detail, zhongdian, abcd);

        JSONArray array3 = JSONArray.fromObject(dutyMubiao);
        List mubiao = JSONArray.toList(array3, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdateDuty(flowList, scoreList, detail, mubiao, abcd);

        for (ScoreFlow flow : flowList) {
            if (flow.getScoretype().equals("D")) {
                ScoreFlow update = new ScoreFlow();
                update.setSerialno(flow.getSerialno());
                update.setScore(abcd[0]);
                flowService.updateByPrimaryKeySelective(update);
            } else {
                ScoreFlow update = new ScoreFlow();
                update.setSerialno(flow.getSerialno());
                update.setScore(abcd[1]);
                flowService.updateByPrimaryKeySelective(update);
            }
        }

    }

    private void judgeAddOrUpdate(ScoreFlow flow1, ScoreDetail detail, List<DutyCodeAndScore> jichu) {
        List<ScoreDetail> listInsert = new ArrayList<>();
        List<ScoreDetail> listUpdate = new ArrayList<>();
        for (int i = 0; i < jichu.size(); i++) {
            ScoreDetail scoreDetail = new ScoreDetail();
            scoreDetail.setFSerialNo(flow1.getSerialno());
            scoreDetail.setDSerialNo(jichu.get(i).getTopicId());
            scoreDetail.setScore(jichu.get(i).getScore());
            scoreDetail.setDbtype(flow1.getDbtype());
            ScoreDetail detail1 = detailService.selectDetailBySerialNo(scoreDetail.getDSerialNo(), scoreDetail.getFSerialNo());
            if (detail1 != null) {
                scoreDetail.setSerialNo(detail1.getSerialNo());
                listUpdate.add(scoreDetail);
            } else {
                scoreDetail.setSerialNo(StringUtil.generatorId());
                listInsert.add(scoreDetail);
            }
        }
        if (listInsert.size() > 0) {
            detailService.batchInset(listInsert);
        }
        if (listUpdate.size() > 0) {
            detailService.batchUpdate(listUpdate);
        }
    }

    private void judgeAddOrUpdateDuty(List<ScoreFlow> flowList, List<Score> scoreList, ScoreDetail detail, List<DutyCodeAndScore> jichu, Double[] abcd) {
        List<ScoreDetail> listInsert = new ArrayList<>();
        List<ScoreDetail> listUpdate = new ArrayList<>();
        List<Score> scoreQueryList = new ArrayList<>();
        List<ScoreFlow> scoreFlowQueryList = new ArrayList<>();
        for (int i = 0; i < jichu.size(); i++) {
            DutyCodeAndScore dcas = jichu.get(i);
            if (dcas.getScoretype().equals("D")) {
                scoreQueryList = scoreList.stream().filter(s -> s.getDutycode() != null && s.getDutycode().equals(dcas.getTopicId()) &&
                        s.getScoretype().equals(dcas.getScoretype())).collect(Collectors.toList());
            } else {
                scoreQueryList = scoreList.stream().filter(s -> s.getScoretype().equals(dcas.getScoretype())).collect(Collectors.toList());
            }
            scoreFlowQueryList = flowList.stream().filter(s -> s.getScoretype().equals(dcas.getScoretype())).collect(Collectors.toList());
            if (scoreQueryList.size() > 0 && scoreFlowQueryList.size() > 0) {
                ScoreDetail scoreDetail = new ScoreDetail();
                scoreDetail.setFSerialNo(scoreFlowQueryList.get(0).getSerialno());
                scoreDetail.setDSerialNo(dcas.getTopicId());
                if (NumberUtil.isNumber(dcas.getScore())) {
                    scoreDetail.setScore(dcas.getScore());
                    if (dcas.getScoretype().equals("D")) {
                        abcd[0] += Double.parseDouble(dcas.getScore());
                    } else {
                        abcd[1] += Double.parseDouble(dcas.getScore());
                    }
                }
                scoreDetail.setRatio(scoreFlowQueryList.get(0).getRatio());
                scoreDetail.setScoretype(dcas.getScoretype());
                scoreDetail.setDbtype(scoreFlowQueryList.get(0).getDbtype());
                ScoreDetail detail1 = detailService.selectDetailBySerialNo(scoreDetail.getDSerialNo(), scoreDetail.getFSerialNo());
                if (detail1 != null) {
                    scoreDetail.setSerialNo(detail1.getSerialNo());
                    listUpdate.add(scoreDetail);
                } else {
                    scoreDetail.setSerialNo(StringUtil.generatorId());
                    listInsert.add(scoreDetail);
                }
            }
        }
        if (listInsert.size() > 0) {
            detailService.batchInset(listInsert);
        }
        if (listUpdate.size() > 0) {
            detailService.batchUpdate(listUpdate);
        }
    }

    @RequestMapping(value = "/jisuanScore", produces = "application/json;charset=utf-8")
    public Object jisuanScore(HttpServletRequest req, String dbtype) {
        if (dbtype.equals("1")) {
            return this.jisuan(req, dbtype);
        } else {
            return this.jisuanDuty(req, dbtype);
        }
    }

    public Object jisuan(HttpServletRequest req, String dbtype) {
        //获取当前登录用户的编号
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            Map<String, Object> data = new LinkedHashMap<>();
            try {
                String msg = "";
                ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                if (setTime != null) {
                    String year = setTime.getYear();
                    String month = setTime.getMonth();
                    List<MonthSummary> monthSummaryList = monthSummaryService.selectListByYearAndMonth(year, month, dbtype);
                    if (monthSummaryList != null) {
                        long count = monthSummaryList.stream().filter(s -> !s.getState().equals("7")).count();
                        if (count == 0) {
                            String dzbsj = "党支部书记";
                            String zdzb = "总党支部书记";
                            List<ScoreHistory> shList = historyService.findUserScoreHistory(year, month, dbtype, null);
                            List<ScoreHistory> scoreHistoryList = shList.stream().filter(s -> s.getDbbk() != null && s.getDbbk() != "" && s.getDbbk().equals(dzbsj)).collect(Collectors.toList());
                            List<ScoreHistory> zongList = shList.stream().filter(s -> s.getDbbk() != null && s.getDbbk() != "" && s.getDbbk().equals(zdzb)).collect(Collectors.toList());
                            if (scoreHistoryList.size() > 0) {
                                List<String> typeList = new ArrayList<>();
                                typeList.add("4");
                                typeList.add("5");
                                typeList.add("6");
                                typeList.add("7");
                                typeList.add("8");
                                List<UserScoreDto> userScoreDtoList = userScoreDtoService.findUserScore(year, month, typeList, dbtype);
                                if (userScoreDtoList.size() > 0) {
//                                    Map<String, List<UserScoreDto>> byAuthor = userScoreDtoList.stream().map()
//                                            .collect(Collectors.groupingBy(UserScoreDto::getScoreType));
                                    typeList = new ArrayList<>();
                                    typeList.add("A");
                                    typeList.add("B");
                                    typeList.add("C");
                                    typeList.add("D");
                                    List<UserScoreDto> userQueryList = new ArrayList<>();
                                    Double totalRatio = 0.0;
                                    Double totalSumScore = 0.0;
                                    Double sum0Score = 0.0;
                                    Double sum1Score = 0.0;
                                    Double sum2Score = 0.0;
                                    Double sum3Score = 0.0;
                                    Double sum4Score = 0.0;
                                    Double ratio = 0.0;
                                    Double score0 = 0.0;
                                    Double score1 = 0.0;
                                    Double score2 = 0.0;
                                    Double score3 = 0.0;
                                    Double score4 = 0.0;
                                    Double ratioA = 0.0;
                                    Double score0A = 0.0;
                                    Double score1A = 0.0;
                                    Double score2A = 0.0;
                                    Double score3A = 0.0;
                                    Double score4A = 0.0;
                                    Double ratioB = 0.0;
                                    Double score0B = 0.0;
                                    Double score1B = 0.0;
                                    Double score2B = 0.0;
                                    Double score3B = 0.0;
                                    Double score4B = 0.0;
                                    Double ratioC = 0.0;
                                    Double score0C = 0.0;
                                    Double score1C = 0.0;
                                    Double score2C = 0.0;
                                    Double score3C = 0.0;
                                    Double score4C = 0.0;
                                    Double ratioD = 0.0;
                                    Double score0D = 0.0;
                                    Double score1D = 0.0;
                                    Double score2D = 0.0;
                                    Double score3D = 0.0;
                                    Double score4D = 0.0;
                                    List<ResultReport> rrList = new ArrayList<>();
                                    for (ScoreHistory item : scoreHistoryList) {
                                        totalRatio = 0.0;
                                        userQueryList = userScoreDtoList.stream().filter(
                                                s -> s.getScoredCode().equals(item.getUsercode())
                                        ).collect(Collectors.toList());

                                        for (String type : typeList) {
                                            score0 = this.getScore(userQueryList, type, "4");
                                            score1 = this.getScore(userQueryList, type, "5");
                                            score2 = this.getScore(userQueryList, type, "6");
                                            score3 = this.getScore(userQueryList, type, "7");
                                            score4 = this.getScore(userQueryList, type, "8");
                                            ratio = userQueryList.stream().filter(s -> s.getScoreType().equals(type)).map(
                                                    UserScoreDto::getRatio).findFirst().orElse(0.0);
                                            totalRatio += ratio;

                                            if (type.equals("A")) {
                                                ratioA = ratio;
                                                score0A = score0;
                                                score1A = score1;
                                                score2A = score2;
                                                score3A = score3;
                                                score4A = score4;
                                            } else if (type.equals("B")) {
                                                ratioB = ratio;
                                                score0B = score0;
                                                score1B = score1;
                                                score2B = score2;
                                                score3B = score3;
                                                score4B = score4;
                                            } else if (type.equals("C")) {
                                                ratioC = ratio;
                                                score0C = score0;
                                                score1C = score1;
                                                score2C = score2;
                                                score3C = score3;
                                                score4C = score4;
                                            } else {
                                                ratioD = ratio;
                                                score0D = score0;
                                                score1D = score1;
                                                score2D = score2;
                                                score3D = score3;
                                                score4D = score4;
                                            }
                                        }

                                        sum0Score = score0A * ratioA + score0B * ratioB +
                                                score0C * ratioC + score0D * ratioD;

                                        sum1Score = score1A * ratioA + score1B * ratioB +
                                                score1C * ratioC + score1D * ratioD;

                                        sum2Score = score2A * ratioA + score2B * ratioB +
                                                score2C * ratioC + score2D * ratioD;

                                        sum3Score = score3A * ratioA + score3B * ratioB +
                                                score3C * ratioC + score3D * ratioD;

                                        sum4Score = score4A * ratioA + score4B * ratioB +
                                                score4C * ratioC + score4D * ratioD;

                                        sum0Score = sum0Score == 0 ? 0 : sum0Score / totalRatio;
                                        sum1Score = sum1Score == 0 ? 0 : sum1Score / totalRatio;
                                        sum2Score = sum2Score == 0 ? 0 : sum2Score / totalRatio;
                                        sum3Score = sum3Score == 0 ? 0 : sum3Score / totalRatio;
                                        sum4Score = sum4Score == 0 ? 0 : sum4Score / totalRatio;

                                        // ResultReport表 得出计算后结果
                                        score0A = score0A * ratioA / totalRatio;
                                        score0B = score0B * ratioB / totalRatio;
                                        score0C = score0C * ratioC / totalRatio;
                                        score0D = score0D * ratioD / totalRatio;

                                        score1A = score1A * ratioA / totalRatio;
                                        score1B = score1B * ratioB / totalRatio;
                                        score1C = score1C * ratioC / totalRatio;
                                        score1D = score1D * ratioD / totalRatio;

                                        score2A = score2A * ratioA / totalRatio;
                                        score2B = score2B * ratioB / totalRatio;
                                        score2C = score2C * ratioC / totalRatio;
                                        score2D = score2D * ratioD / totalRatio;

                                        score3A = score3A * ratioA / totalRatio;
                                        score3B = score3B * ratioB / totalRatio;
                                        score3C = score3C * ratioC / totalRatio;
                                        score3D = score3D * ratioD / totalRatio;

                                        score4A = score4A * ratioA / totalRatio;
                                        score4B = score4B * ratioB / totalRatio;
                                        score4C = score4C * ratioC / totalRatio;
                                        score4D = score4D * ratioD / totalRatio;

                                        this.getResultReport("4", "政治建设", item, score0A, score0B, score0C, score0D, sum0Score, 0, rrList);
                                        this.getResultReport("5", "思想建设", item, score1A, score1B, score1C, score1D, sum1Score, 0, rrList);
                                        this.getResultReport("6", "组织建设", item, score2A, score2B, score2C, score2D, sum2Score, 0, rrList);
                                        this.getResultReport("7", "党建创新", item, score3A, score3B, score3C, score3D, sum3Score, 0, rrList);
                                        this.getResultReport("8", "作风建设", item, score4A, score4B, score4C, score4D, sum4Score, 0, rrList);

                                        item.setSumJcScore(sum0Score);
                                        item.setSumGwScore(sum1Score);
                                        item.setSumZdScore(sum2Score);
                                        item.setSumMbScore(sum3Score);
                                        item.setSumZfScore(sum4Score);
                                        item.setTotalscore(sum0Score + sum1Score + sum2Score + sum3Score + sum4Score);

                                    }

                                    EvaluationReport query = new EvaluationReport();
                                    query.setYear(year);
                                    query.setMonth(month);
                                    query.setDbtype(dbtype);
                                    List<EvaluationReport> evaluationReportList = evaluationReportService.selectEvaluationReportList(query);
                                    List<EvaluationReport> queryErList = new ArrayList<>();
                                    this.getZongScoreHistoryData(year, month, dbtype, zdzb, zongList, scoreHistoryList);
                                    for (ScoreHistory item : scoreHistoryList) {
                                        totalSumScore += item.getTotalscore();
                                    }
                                    Double avgScore = totalSumScore / scoreHistoryList.size();
                                    for (ScoreHistory item : scoreHistoryList) {
                                        if (item.getId() != null) {
                                            ScoreHistory scoreHistory = new ScoreHistory();
                                            scoreHistory.setId(item.getId());
                                            scoreHistory.setDfScore(item.getDfScore());
                                            scoreHistory.setSumTotalScore(item.getTotalscore());
                                            scoreHistory.setTotalscore(item.getTotalscore());
                                            scoreHistory.setSumMbScore(item.getSumMbScore());
                                            scoreHistory.setSumZfScore(item.getSumZfScore());
                                            historyService.updateByPrimaryKeySelective(scoreHistory);
                                        } else {
                                            item.setSumTotalScore(item.getTotalscore());
                                            historyService.insertSelective(item);
                                        }
                                        queryErList = evaluationReportList.stream().filter(s -> s.getUsercode().equals(item.getUsercode())).collect(Collectors.toList());
                                        if (queryErList.size() > 0) {
                                            EvaluationReport updateEr = new EvaluationReport();
                                            updateEr.setId(queryErList.get(0).getId());
                                            updateEr.setBasicscore(item.getSumJcScore());
                                            updateEr.setKeyscore(item.getSumGwScore());
                                            updateEr.setZdScore(item.getSumZdScore());
                                            updateEr.setMbScore(item.getSumMbScore());
                                            updateEr.setZfScore(item.getSumZfScore());
                                            updateEr.setTotalscore(item.getTotalscore());
                                            updateEr.setDfScore(item.getDfScore());
                                            updateEr.setAvgscore(avgScore);
                                            evaluationReportService.updateByPrimaryKeySelective(updateEr);
                                        } else {
                                            EvaluationReport insertEr = new EvaluationReport();
                                            insertEr.setYear(year);
                                            insertEr.setMonth(month);
                                            insertEr.setUsercode(item.getUsercode());
                                            insertEr.setBasicscore(item.getSumJcScore());
                                            insertEr.setKeyscore(item.getSumGwScore());
                                            insertEr.setZdScore(item.getSumZdScore());
                                            insertEr.setMbScore(item.getSumMbScore());
                                            insertEr.setZfScore(item.getSumZfScore());
                                            insertEr.setTotalscore(item.getTotalscore());
                                            insertEr.setAvgscore(avgScore);
                                            insertEr.setDfScore(item.getDfScore());
                                            insertEr.setDbtype(dbtype);
                                            evaluationReportService.insertSelective(insertEr);
                                        }
                                    }
                                    if (rrList.size() > 0) {
                                        this.updateOrInsertResultReport(rrList, year, month, dbtype);
                                    }
                                    msg = "OK";
                                } else {
                                    msg = "考核季度明细数据为空";
                                }
                            } else {
                                msg = "考核季度历史数据为空";
                            }
                        } else {
                            msg = "考核季度数据还有评分未完成";
                        }
                    } else {
                        msg = "考核季度数据为空";
                    }
                } else {
                    msg = "考核季度为空";
                }
                map.put("msg", msg.equals("OK") ? "计算完成." : msg);
                map.put("code", msg.equals("OK") ? 0 : 1);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询个人考核详情失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void getZongScoreHistoryData(String year, String month, String dbtype, String zdzb, List<ScoreHistory> zongShList, List<ScoreHistory> shList) {
        List<User> userZdzbList = userService.findUserBranchByDbbk(zdzb);
        List<String> codeList = new ArrayList<>();
        long count = 0;
        for (User user : userZdzbList) {
            codeList.add(user.getUsercode());
        }
        List<MonthSummary> monthSummaryList = monthSummaryService.selectSummaryByInEmployeeCode(year, month, dbtype, codeList);
//        List<ScoreHistory> scoreHistoryList = historyService.selectHistoryByInUserCode(year, month, dbtype, codeList);
//        List<EvaluationReport> evaluationReportList = evaluationReportService.selectEvaluationReportByInUserCode(year, month, dbtype, codeList);
        List<ScoreHistory> queryHistory = new ArrayList<>();
        List<ScoreHistory> queryZongHistory = new ArrayList<>();
        for (User user : userZdzbList) {
            String serialno = year + "-" + month + "-" + dbtype + "-" + user.getUsercode();
            count = monthSummaryList.stream().filter(s -> s.getSerialno().equals(serialno)).count();
            if (count == 0) {
                MonthSummary summary = new MonthSummary();
                summary.setSerialno(serialno);
                summary.setMonth(month);
                summary.setIssend("0");
                summary.setYear(year);
                summary.setDbtype(dbtype);
                summary.setEmployeecode(user.getUsercode());
                summary.setState("7");
                monthSummaryService.insertSelective(summary);
            }
        }
        Double ascore = 0.0;
        Double bscore = 0.0;
        Double cscore = 0.0;
        Double dscore = 0.0;
        Double tscore = 0.0;
        Double stscore = 0.0;
        Double dfScore = 0.0;
        Double sumJcScore = 0.0;
        Double sumGwScore = 0.0;
        Double sumMbScore = 0.0;
        Double sumZdScore = 0.0;
        Double sumZfScore = 0.0;

        for (User user : userZdzbList) {
            queryZongHistory = zongShList.stream().filter(s -> s.getUsercode().equals(user.getUsercode())).collect(Collectors.toList());
            queryHistory = shList.stream().filter(s -> s.getBranchcode() != null && s.getBranchcode().equals(user.getBranchcode())).collect(Collectors.toList());
            ScoreHistory item = new ScoreHistory();
            if (queryZongHistory.size() > 0) {
                item = queryZongHistory.get(0);
            } else {
                item.setUsercode(user.getUsercode());
                item.setYear(year);
                item.setMonth(month);
                item.setDbtype(dbtype);
                item.setScorestatus("1");
            }
//            ascore = 0.0;
//            bscore = 0.0;
//            cscore = 0.0;
//            dscore = 0.0;
            tscore = 0.0;
            stscore = 0.0;
            dfScore = 0.0;
            sumJcScore = 0.0;
            sumGwScore = 0.0;
            sumMbScore = 0.0;
            sumZdScore = 0.0;
            sumZfScore = 0.0;
            for (ScoreHistory history : queryHistory) {
//                ascore += history.getAscore();
//                bscore += history.getBscore();
//                cscore += history.getCscore();
//                dscore += history.getDscore();
                tscore += history.getTotalscore();
                stscore += history.getSumTotalScore();
                dfScore += history.getDfScore();
                sumJcScore += history.getSumJcScore();
                sumGwScore += history.getSumGwScore();
                sumZdScore += history.getSumZdScore();
                sumMbScore += history.getSumMbScore();
                sumZfScore += history.getSumZfScore();
            }
            if (queryHistory.size() > 0) {
//                ascore = ascore == 0 ? 0.0 : ascore / queryHistory.size();
//                bscore = bscore == 0 ? 0.0 : bscore / queryHistory.size();
//                cscore = cscore == 0 ? 0.0 : cscore / queryHistory.size();
//                dscore = dscore == 0 ? 0.0 : dscore / queryHistory.size();
                tscore = tscore == 0 ? 0.0 : tscore / queryHistory.size();
                stscore = stscore == 0 ? 0.0 : stscore / queryHistory.size();
                dfScore = dfScore == 0 ? 0.0 : dfScore / queryHistory.size();
                sumJcScore = sumJcScore == 0 ? 0.0 : sumJcScore / queryHistory.size();
                sumGwScore = sumGwScore == 0 ? 0.0 : sumGwScore / queryHistory.size();
                sumMbScore = sumMbScore == 0 ? 0.0 : sumMbScore / queryHistory.size();
                sumZdScore = sumZdScore == 0 ? 0.0 : sumZdScore / queryHistory.size();
                sumZfScore = sumZfScore == 0 ? 0.0 : sumZfScore / queryHistory.size();
            }
//            item.setAscore(ascore);
//            item.setBscore(bscore);
//            item.setCscore(cscore);
//            item.setDscore(dscore);
            item.setTotalscore(tscore);
            item.setSumTotalScore(stscore);
            item.setDfScore(dfScore);
            item.setSumJcScore(sumJcScore);
            item.setSumGwScore(sumGwScore);
            item.setSumMbScore(sumMbScore);
            item.setSumZdScore(sumZdScore);
            item.setSumZfScore(sumZfScore);

            shList.add(item);
        }

    }

    public Object jisuanDuty(HttpServletRequest req, String dbtype) {
        //获取当前登录用户的编号
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            Map<String, Object> data = new LinkedHashMap<>();
            try {
                String msg = "";
                ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                if (setTime != null) {
                    String year = setTime.getYear();
                    String month = setTime.getMonth();
                    List<MonthSummary> monthSummaryList = monthSummaryService.selectListByYearAndMonth(year, month, dbtype);
                    if (monthSummaryList != null) {
                        long count = monthSummaryList.stream().filter(s -> !s.getState().equals("7")).count();
                        if (count == 0) {
                            List<ScoreHistory> scoreHistoryList = historyService.findUserScoreHistory(year, month, dbtype, null);
                            if (scoreHistoryList.size() > 0) {
                                List<String> typeList = new ArrayList<>();
                                typeList.add("0");
                                typeList.add("1");
                                typeList.add("2");
                                typeList.add("3");
                                List<UserScoreDto> userScoreDtoList = userScoreDtoService.findUserDutyScore(year, month, null, typeList, dbtype);
                                if (userScoreDtoList.size() > 0) {
//                                    Map<String, List<UserScoreDto>> byAuthor = userScoreDtoList.stream().map()
//                                            .collect(Collectors.groupingBy(UserScoreDto::getScoreType));
                                    typeList = new ArrayList<>();
                                    typeList.add("A");
                                    typeList.add("B");
                                    typeList.add("C");
                                    typeList.add("D");
                                    List<UserScoreDto> userQueryList = new ArrayList<>();
//                                    Double totalRatio = 0.0;
                                    Double totalMbScore = 0.0;
                                    Double sumJcScore = 0.0;
                                    Double sumGwScore = 0.0;
                                    Double sumZdScore = 0.0;
                                    Double sumMbScore = 0.0;
//                                    Double ratio = 0.0;
//                                    Double score0 = 0.0;
//                                    Double score1 = 0.0;
//                                    Double score2 = 0.0;
//                                    Double score3 = 0.0;
//                                    Double ratioA = 0.0;
                                    Double score0A = 0.0;
                                    Double score1A = 0.0;
                                    Double score2A = 0.0;
                                    Double score3A = 0.0;
//                                    Double ratioB = 0.0;
                                    Double score0B = 0.0;
                                    Double score1B = 0.0;
                                    Double score2B = 0.0;
                                    Double score3B = 0.0;
//                                    Double ratioC = 0.0;
                                    Double score0C = 0.0;
                                    Double score1C = 0.0;
                                    Double score2C = 0.0;
                                    Double score3C = 0.0;
//                                    Double ratioD = 0.0;
                                    Double score0D = 0.0;
                                    Double score1D = 0.0;
                                    Double score2D = 0.0;
                                    Double score3D = 0.0;
                                    int mbMaxCount = 5;
                                    int mbSumCount = 0;
                                    Double mbAvgScore = 0.0;
                                    int mbCount = 0;
                                    List<ResultReport> rrList = new ArrayList<>();
                                    for (ScoreHistory item : scoreHistoryList) {
                                        userQueryList = userScoreDtoList.stream().filter(
                                                s -> s.getScoredCode().equals(item.getUsercode())
                                        ).collect(Collectors.toList());

                                        List<UserScoreDto> dutyAndRatioList = userScoreDtoService.getTypeUserDutyScore(userQueryList,true);

                                        score0A = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("0")).mapToDouble(UserScoreDto::getAScore).sum();
                                        score1A = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("1")).mapToDouble(UserScoreDto::getAScore).sum();
                                        score2A = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getAScore).sum();
                                        score3A = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getAScore).sum();
                                        score0B = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("0")).mapToDouble(UserScoreDto::getBScore).sum();
                                        score1B = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("1")).mapToDouble(UserScoreDto::getBScore).sum();
                                        score2B = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getBScore).sum();
                                        score3B = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getBScore).sum();
                                        score0C = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("0")).mapToDouble(UserScoreDto::getCScore).sum();
                                        score1C = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("1")).mapToDouble(UserScoreDto::getCScore).sum();
                                        score2C = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getCScore).sum();
                                        score3C = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getCScore).sum();
                                        score0D = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("0")).mapToDouble(UserScoreDto::getDScore).sum();
                                        score1D = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("1")).mapToDouble(UserScoreDto::getDScore).sum();
                                        score2D = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getDScore).sum();
                                        score3D = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getDScore).sum();

                                        sumJcScore = score0A + score0B + score0C + score0D;

                                        sumGwScore = score1A + score1B + score1C + score1D;

                                        sumZdScore = score2A + score2B + score2C + score2D;

                                        sumMbScore = score3A + score3B + score3C + score3D;

                                        if(userQueryList.size()>0) {
                                            mbCount = userQueryList.stream().filter(
                                                    s -> s.getDutyType().equals("3")).collect(Collectors.groupingBy(UserScoreDto::getDserialNo)).size();
                                        } else {
                                            mbCount = mbMaxCount;
                                        }
                                        this.getResultReport("0", "基础评分", item, score0A, score0B, score0C, score0D, sumJcScore, 0, rrList);
                                        this.getResultReport("1", "岗位评分", item, score1A, score1B, score1C, score1D, sumGwScore, 0, rrList);
                                        this.getResultReport("2", "重点评分", item, score2A, score2B, score2C, score2D, sumZdScore, 0, rrList);
                                        this.getResultReport("3", "目标评分", item, score3A, score3B, score3C, score3D, sumMbScore, mbCount, rrList);

                                        item.setSumJcScore(sumJcScore);
                                        item.setSumGwScore(sumGwScore);
                                        item.setSumZdScore(sumZdScore);
                                        item.setSumMbScore(sumMbScore);
                                        item.setTotalscore(sumJcScore + sumGwScore + sumZdScore + sumMbScore);

                                        item.setMbCount(mbCount);
                                        mbSumCount += mbCount;
                                        //累计目标总分数
                                        totalMbScore += sumMbScore;
                                    }
                                    mbAvgScore = totalMbScore == 0 ? 0.0 : totalMbScore / mbSumCount;
                                    Double jsMbScore = 0.0;
                                    boolean isUpdate = false;
                                    Double totalSumScore = 0.0;
                                    Double totalScore = 0.0;
                                    ScoreYdyf queryYdyf = new ScoreYdyf();
                                    queryYdyf.setYear(setTime.getYear());
                                    queryYdyf.setMonth(setTime.getMonth());
                                    List<ScoreYdyf> ydyfQueryList = new ArrayList<>();
                                    List<ScoreYdyf> ydyfList = ydyfService.findYdyfList(queryYdyf);
                                    for (ScoreHistory item : scoreHistoryList) {
                                        totalScore = item.getTotalscore();
                                        jsMbScore = 0.0;
                                        isUpdate = false;
                                        ydyfQueryList = ydyfList.stream().filter(s -> s.getMoneyCard().equals(item.getMoneycard())).collect(Collectors.toList());
                                        if (ydyfQueryList.size() > 0) {
                                            item.setDfScore(ydyfQueryList.get(0).getScore());
                                            totalScore += item.getDfScore();
                                        } else {
                                            item.setDfScore(0.0);
                                        }
                                        if (item.getMbCount() != mbMaxCount) {
                                            isUpdate = true;
                                        }
                                        if (isUpdate) {
                                            jsMbScore = (mbMaxCount - item.getMbCount()) * mbAvgScore;
                                            totalScore += jsMbScore;
                                        }
                                        item.setAvgMbScore(jsMbScore);
                                        item.setSumTotalScore(totalScore);
                                        totalSumScore += totalScore;
                                    }
                                    EvaluationReport query = new EvaluationReport();
                                    query.setYear(year);
                                    query.setMonth(month);
                                    query.setDbtype(dbtype);
                                    List<EvaluationReport> evaluationReportList = evaluationReportService.selectEvaluationReportList(query);
                                    List<EvaluationReport> queryErList = new ArrayList<>();
                                    Double avgScore = totalSumScore / scoreHistoryList.size();

                                    for (ScoreHistory item : scoreHistoryList) {
                                        ScoreHistory scoreHistory = new ScoreHistory();
                                        scoreHistory.setId(item.getId());
                                        scoreHistory.setDfScore(item.getDfScore());
                                        scoreHistory.setAvgMbScore(item.getAvgMbScore());
                                        scoreHistory.setSumTotalScore(item.getSumTotalScore());
                                        scoreHistory.setTotalscore(item.getTotalscore());
                                        scoreHistory.setSumMbScore(item.getSumMbScore());
                                        queryErList = evaluationReportList.stream().filter(s -> s.getUsercode().equals(item.getUsercode())).collect(Collectors.toList());
                                        if (queryErList.size() > 0) {
                                            EvaluationReport updateEr = new EvaluationReport();
                                            updateEr.setId(queryErList.get(0).getId());
                                            updateEr.setBasicscore(item.getSumJcScore());
                                            updateEr.setKeyscore(item.getSumGwScore());
                                            updateEr.setZdScore(item.getSumZdScore());
                                            updateEr.setSumMbAvgScore(scoreHistory.getAvgMbScore());
                                            updateEr.setMbScore(item.getSumMbScore());
                                            updateEr.setTotalscore(item.getSumTotalScore());
                                            updateEr.setDfScore(item.getDfScore());
                                            updateEr.setAvgscore(avgScore);
                                            evaluationReportService.updateByPrimaryKeySelective(updateEr);
                                        } else {
                                            EvaluationReport insertEr = new EvaluationReport();
                                            insertEr.setYear(year);
                                            insertEr.setMonth(month);
                                            insertEr.setUsercode(item.getUsercode());
                                            insertEr.setBasicscore(item.getSumJcScore());
                                            insertEr.setKeyscore(item.getSumGwScore());
                                            insertEr.setZdScore(item.getSumZdScore());
                                            insertEr.setSumMbAvgScore(scoreHistory.getAvgMbScore());
                                            insertEr.setMbScore(item.getSumMbScore());
                                            insertEr.setTotalscore(item.getSumTotalScore());
                                            insertEr.setAvgscore(avgScore);
                                            insertEr.setDfScore(item.getDfScore());
                                            insertEr.setDbtype(dbtype);
                                            evaluationReportService.insertSelective(insertEr);
                                        }
                                        historyService.updateByPrimaryKeySelective(scoreHistory);
                                    }
                                    if (rrList.size() > 0) {
                                        this.updateOrInsertResultReport(rrList, year, month,mbAvgScore,mbMaxCount,dbtype);
                                    }
                                    msg = "OK";
                                } else {
                                    msg = "考核月明细数据为空";
                                }
                            } else {
                                msg = "考核月历史数据为空";
                            }
                        } else {
                            msg = "考核月结数据还有评分未完成";
                        }
                    } else {
                        msg = "考核月结数据为空";
                    }
                } else {
                    msg = "考核月为空";
                }
                map.put("msg", msg.equals("OK") ? "计算完成." : msg);
                map.put("code", msg.equals("OK") ? 0 : 1);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询个人考核详情失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }


    private double getScore(List<UserScoreDto> userQueryList, String stype, String dtype) {
        long pingfenCount = userQueryList.stream().filter(
                s -> s.getScoreType().equals(stype) && s.getDutyType().equals(dtype))
                .map(UserScoreDto::getScorringCode).distinct().count();

        Double score = userQueryList.stream().filter(
                s -> s.getScoreType().equals(stype) && s.getDutyType().equals(dtype)).mapToDouble(UserScoreDto::getScore).sum();

        score = score.isNaN() ? 0 : score;

        score = score == 0 ? 0 : score / pingfenCount;
        return score;
    }

    private void updateOrInsertResultReport(List<ResultReport> rrList, String year, String month, String dbtype) {
        EvaluationReport query = new EvaluationReport();
        query.setYear(year);
        query.setMonth(month);
        query.setDbtype(dbtype);
        List<EvaluationReport> evaluationReportList = evaluationReportList = evaluationReportService.selectEvaluationReportList(query);
        if (evaluationReportList.size() > 0) {
            List<ResultReport> resultReportList = resultReportService.selectResultReportList(year, month, dbtype);
            List<ResultReport> rrQueryList = new ArrayList<>();
            if (resultReportList.size() > 0) {
                List<ResultReport> rList = new ArrayList<>();
                for (EvaluationReport er : evaluationReportList) {
                    rrQueryList = rrList.stream().filter(s -> s.getUserCode().equals(er.getUsercode())).collect(Collectors.toList());
                    for (ResultReport rr : rrQueryList) {
                        rList = resultReportList.stream().filter(s -> s.getResultreportcode().equals(rr.getResultreportcode()) && s.getEvaluationreportcode().equals(er.getId())).collect(Collectors.toList());
                        if (rList.size() == 0) {
                            rr.setEvaluationreportcode(er.getId());
                            resultReportService.insertSelective(rr);
                        } else {
                            rr.setId(rList.get(0).getId());
                            resultReportService.updateByPrimaryKeySelective(rr);
                        }
                    }
                }
            } else {
                for (EvaluationReport er : evaluationReportList) {
                    rrQueryList = rrList.stream().filter(s -> s.getUserCode().equals(er.getUsercode())).collect(Collectors.toList());
                    for (ResultReport rr : rrQueryList) {
                        rr.setEvaluationreportcode(er.getId());
                        resultReportService.insertSelective(rr);
                    }
                }
            }
        }
    }

    private void updateOrInsertResultReport(List<ResultReport> rrList, String year, String month, Double mbAvgScore, Integer mbMaxCount, String dbtype) {
        EvaluationReport query = new EvaluationReport();
        query.setYear(year);
        query.setMonth(month);
        query.setDbtype(dbtype);
        List<EvaluationReport> evaluationReportList = evaluationReportList = evaluationReportService.selectEvaluationReportList(query);
        if (evaluationReportList.size() > 0) {
            List<ResultReport> resultReportList = resultReportService.selectResultReportList(year, month, dbtype);
            List<ResultReport> rrQueryList = new ArrayList<>();
            Double jsMbScore = 0.0;
            if (resultReportList.size() > 0) {
                List<ResultReport> rList = new ArrayList<>();
                for (EvaluationReport er : evaluationReportList) {
                    rrQueryList = rrList.stream().filter(s -> s.getUserCode().equals(er.getUsercode())).collect(Collectors.toList());
                    for (ResultReport rr : rrQueryList) {
                        jsMbScore = 0.0;
                        if (rr.getResultreportcode().equals("3")) {
                            jsMbScore = (mbMaxCount - rr.getMbCount()) * mbAvgScore;
                            rr.setSumMbAvgScore(jsMbScore);
                        }
                        rr.setScore(rr.getScore() + jsMbScore);
                        rList = resultReportList.stream().filter(s -> s.getResultreportcode().equals(rr.getResultreportcode()) && s.getEvaluationreportcode().equals(er.getId())).collect(Collectors.toList());
                        if (rList.size() == 0) {
                            rr.setEvaluationreportcode(er.getId());
                            resultReportService.insertSelective(rr);
                        } else {
                            rr.setId(rList.get(0).getId());
                            resultReportService.updateByPrimaryKeySelective(rr);
                        }
                    }
                }
            } else {
                for (EvaluationReport er : evaluationReportList) {
                    rrQueryList = rrList.stream().filter(s -> s.getUserCode().equals(er.getUsercode())).collect(Collectors.toList());
                    for (ResultReport rr : rrQueryList) {
                        jsMbScore = 0.0;
                        if (rr.getResultreportcode().equals("3")) {
                            jsMbScore = (mbMaxCount - rr.getMbCount()) * mbAvgScore;
                            rr.setSumMbAvgScore(jsMbScore);
                        }
                        rr.setScore(rr.getScore() + jsMbScore);
                        rr.setEvaluationreportcode(er.getId());
                        resultReportService.insertSelective(rr);
                    }
                }
            }
        }
    }


    private void getResultReport(String code, String name, ScoreHistory item, double A, double B, double C, double D, double sum, Integer mbCount, List<ResultReport> list) {
        ResultReport rr = new ResultReport();
        rr.setResultreportcode(code);
        rr.setResultreportname(name);
        rr.setUserCode(item.getUsercode());
        rr.setAscore(A);
        rr.setBscore(B);
        rr.setCscore(C);
        rr.setDscore(D);
        rr.setScore(sum);
        rr.setMbCount(mbCount);
        list.add(rr);
    }

}
