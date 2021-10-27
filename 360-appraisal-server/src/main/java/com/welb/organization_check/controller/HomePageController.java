package com.welb.organization_check.controller;

import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserSummaryDto;
import com.welb.organization_check.dto.UserScoreDto;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.util.*;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    IScoreDutySmService scoreDutySmService;

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
                            getMapList(map, usercode, data, dto, scorringUserCode, false, 1);
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
                        manualGetDetail(dtos, map, usercode, data, year, quarter, count, sysTime, false, 1);

                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
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
                            getMapList(map, usercode, data, dto, scorringUserCode, true, 1);
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
                        manualGetDetail(dtos, map, usercode, data, year, quarter, count, sysTime, true, 1);

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


    @RequestMapping(value = "/getDetail2", produces = "application/json;charset=utf-8")
    public Object getDetail2(HttpServletRequest req, UserSummaryDto dtos) {
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
                boolean isDuty = dtos.getDbtype().equals("1") ? false : true;
                try {
                    if (dtos.getMonth() != null && dtos.getYear() != null) {
                        dto = dtoService.selectUserSummaryByLike(dtos);
                        if (dto != null) {
                            //查找岗位
                            dto.setMonth(dtos.getMonth());
                            getMapList(map, usercode, data, dto, null, isDuty, 2);
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
                        manualGetDetail(dtos, map, usercode, data, year, quarter, count, sysTime, isDuty, 2);

                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
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

    private void getMapList(ModelMap map, String usercode, Map<String, Object> data, UserSummaryDto dto, String scorringUserCode, boolean isDuty, int type) {
        //获取评分人给被评分人打分的情况
        Station station = new Station();
        List<ScoreFlow> flow = new ArrayList<>();
        if (scorringUserCode != null && !scorringUserCode.equals("")) {
            flow = flowService.selectByScoreFlow(dto.getSerialno(), scorringUserCode, dto.getDbtype());
        } else {
            station = stationService.selectByStationCode(dto.getStationcode());
            if (type == 1) {
                flow = flowService.selectByScoreFlow(dto.getSerialno(), usercode, dto.getDbtype());
            } else {
                flow = flowService.selectByScoredCodeFlow(dto.getSerialno(), usercode, dto.getDbtype());
                if (flow.size() > 0) {
                    flow = flow.stream().filter(s -> s.getScoretype().equals("A") ||
                            s.getScoretype().equals("B") ||
                            s.getScoretype().equals("C") ||
                            s.getScoretype().equals("D")).collect(Collectors.toList());
                    if (flow.size() > 0) {
                        List<ScoreFlow> flow1 = new ArrayList<>();
                        flow1.add(flow.get(0));
                        flow = flow1;
                        scorringUserCode = flow.get(0).getScorringcode();
                    } else {
                        flow = new ArrayList<>();
                    }
                }
            }
        }

        List<ScoreDutySm> dutySmList = this.getDutySmList(dto.getYear(),dto.getMonth(),dto.getUsercode(),dto.getDbtype());
        if (isDuty) {
            if (scorringUserCode != null && !scorringUserCode.equals("")) {
                usercode = scorringUserCode;
            }
            getDutyFlow(map, data, dto, station, flow, usercode, dutySmList, type);
        } else {
            getFlow(map, data, dto, station, flow, dutySmList, type);
        }
    }

    private List<ScoreDutySm> getDutySmList (String year,String month,String userCode,String dbtype) {
        ScoreDutySm query = new ScoreDutySm();
        query.setYear(year);
        query.setMonth(month);
        query.setScorredcode(userCode);
        query.setDbtype(dbtype);
        return scoreDutySmService.selectScoreDutySmList(query);
    }

    private void manualGetDetail(UserSummaryDto dtos, ModelMap map, String usercode, Map<String, Object> data, String year, String quarter, int count, String sysTime, boolean isDuty, int type) throws ParseException {
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
            getMapList(map, usercode, data, dto, null, isDuty, type);


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
        getMapList(map, usercode, data, dto, null, false, 1);
    }

    private void getDutyFlow(ModelMap map, Map<String, Object> data, UserSummaryDto dto, Station station, List<ScoreFlow> scoreFlow, String userCode, List<ScoreDutySm> dutySmList, int type) {
        // 1、被评分人 2、评分人
        List<Score> scoreList = scoreService.selectTypeByCodeList(dto.getUsercode(), userCode, dto.getDbtype());
        List<String> scoreTypeList = new ArrayList<>();
        String scoreType = "";
        boolean isEdit = false;
        long count = 0;
        if (scoreList.size() > 0) {
            count = scoreList.stream().filter(s -> (s.getScoretype().equals("E"))).count();
            if (count > 0) {
                scoreTypeList.add("E");
            }
            count = scoreList.stream().filter(s -> (s.getScoretype().equals("F"))).count();
            if (count > 0) {
                scoreTypeList.add("F");
            }
        }
        List<Duty> dutyList = new ArrayList<>();
        boolean isDuty = false;
        // 判断是否有 E、F
        if (count > 0) {
            isDuty = true;
            // 判断是否有 A,B,C
            List<Score> scoreAbcList = scoreList.stream().filter(s -> (!s.getScoretype().equals("E") && !s.getScoretype().equals("F"))).collect(Collectors.toList());
            if (scoreAbcList.size() == 0) {
                dutyList = dutyService.queryDutyByScorringCode(userCode, dto.getUsercode(), scoreTypeList, dto.getDbtype());
            } else {
                dutyList = dutyService.queryDutyByStationCode(dto.getStationcode(), dto.getDbtype());
                scoreType = scoreAbcList.size() == 0 ? "" :scoreAbcList.get(0).getScoretype();
            }
        } else {
            dutyList = dutyService.queryDutyByStationCode(dto.getStationcode(), dto.getDbtype());
            scoreType = scoreList.size() == 0 ? "" : scoreList.get(0).getScoretype();
        }
        if (scoreFlow.size() > 0) {
            Double totalScore = scoreFlow.stream().mapToDouble(ScoreFlow::getScore).sum();

            data.put("total", type == 2 ? "0" : totalScore + "分");
            //获取基础量化指标相关信息
//                List<Duty> dutyJichu = dutyService.queryDutyByType("0", dto.getStationcode());
            List<Duty> dutyJichu = dutyList.stream().filter(s -> s.getDutytype().equals("0")).collect(Collectors.toList());
            getDutyInfoNew(scoreFlow, dutyJichu, dutySmList, type);
            //获取关键量化指标的相关信息
//                List<Duty> dutyYiban = dutyService.queryDutyByType("1", dto.getStationcode());
            List<Duty> dutyYiban = dutyList.stream().filter(s -> s.getDutytype().equals("1")).collect(Collectors.toList());
            getDutyInfoNew(scoreFlow, dutyYiban, dutySmList, type);
            //获取关键量化指标的相关信息
//                List<Duty> dutyZhongdian = dutyService.queryDutyByType("2", dto.getStationcode());
            List<Duty> dutyZhongdian = dutyList.stream().filter(s -> s.getDutytype().equals("2")).collect(Collectors.toList());
            getDutyInfoNew(scoreFlow, dutyZhongdian, dutySmList, type);
            //获取关键量化指标的相关信息
//                List<Duty> dutyMubiao = dutyService.queryDutyByType("3", dto.getStationcode());
            List<Duty> dutyMubiao = dutyList.stream().filter(s -> s.getDutytype().equals("3")).collect(Collectors.toList());
            getDutyInfoNew(scoreFlow, dutyMubiao, dutySmList, type);
            //判断是否可编辑
            if (type == 1) {
                isEdit = dto.getState().equals("7") ? true : false;
            } else {
                isEdit = dto.getState().equals("6") || dto.getState().equals("7") ? true : false;
            }
            if (isEdit) {
                dto.setIsedit("1");
            } else {
                dto.setIsedit("0");
            }
            if (type == 1) {
                this.setDuty(dutyJichu, scoreList, scoreType, isDuty);
                this.setDuty(dutyYiban, scoreList, scoreType, isDuty);
                this.setDuty(dutyZhongdian, scoreList, scoreType, isDuty);
                this.setDuty(dutyMubiao, scoreList, scoreType, isDuty);
            }
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
                this.setScoreAndZp(duty,dutySmList,type);
            }

            //获取关键量化指标的相关信息
//            List<Duty> dutyYiban = dutyService.queryDutyByType("1", dto.getStationcode());
            List<Duty> dutyYiban = dutyList.stream().filter(s -> s.getDutytype().equals("1")).collect(Collectors.toList());
            for (Duty duty : dutyYiban) {
                this.setScoreAndZp(duty,dutySmList,type);
            }

            //获取关键量化指标的相关信息
//            List<Duty> dutyZhongdian = dutyService.queryDutyByType("2", dto.getStationcode());
            List<Duty> dutyZhongdian = dutyList.stream().filter(s -> s.getDutytype().equals("2")).collect(Collectors.toList());
            for (Duty duty : dutyZhongdian) {
                this.setScoreAndZp(duty,dutySmList,type);
            }

            //获取关键量化指标的相关信息
//            List<Duty> dutyMubiao = dutyService.queryDutyByType("3", dto.getStationcode());
            List<Duty> dutyMubiao = dutyList.stream().filter(s -> s.getDutytype().equals("3")).collect(Collectors.toList());
            for (Duty duty : dutyMubiao) {
                this.setScoreAndZp(duty,dutySmList,type);
            }

            if (type == 1) {
                isEdit = dto.getState().equals("7") ? true : false;
            } else {
                isEdit = dto.getState().equals("6") || dto.getState().equals("7") ? true : false;
            }
            if (isEdit) {
                dto.setIsedit("1");
            } else {
                dto.setIsedit("0");
            }
            if (type == 1) {
                this.setDuty(dutyJichu, scoreList, scoreType, isDuty);
                this.setDuty(dutyYiban, scoreList, scoreType, isDuty);
                this.setDuty(dutyZhongdian, scoreList, scoreType, isDuty);
                this.setDuty(dutyMubiao, scoreList, scoreType, isDuty);
            }
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

    private void getFlow2(ModelMap map, Map<String, Object> data, UserSummaryDto dto, Station station, List<ScoreFlow> scoreFlow, List<ScoreDutySm> dutySmList, int type) {
        boolean isEdit = false;
        if (scoreFlow.size() > 0) {
            for (ScoreFlow flow : scoreFlow) {
                data.put("total", type == 2 ? "0" : flow.getScore() + "分");

                Duty du = new Duty();
                du.setDbtype(dto.getDbtype());
                du.setStationcode(dto.getStationcode());
                List<Duty> dutyList = dutyService.selectDutyAll(du);

                List<Duty> dutyJichu = dutyList.stream().filter(p -> p.getDutytype().equals("0")).collect(Collectors.toList());
                getDutyInfo(flow, dutyJichu, dutySmList, type);

                List<Duty> dutyYiban = dutyList.stream().filter(p -> p.getDutytype().equals("1")).collect(Collectors.toList());
                getDutyInfo(flow, dutyYiban, dutySmList, type);

                List<Duty> dutyZhongdian = dutyList.stream().filter(p -> p.getDutytype().equals("2")).collect(Collectors.toList());
                getDutyInfo(flow, dutyZhongdian, dutySmList, type);

                List<Duty> dutyMubiao = dutyList.stream().filter(p -> p.getDutytype().equals("3")).collect(Collectors.toList());
                getDutyInfo(flow, dutyMubiao, dutySmList, type);

                //判断是否可编辑
                if (type == 1) {
                    isEdit = dto.getState().equals("7") ? true : false;
                } else {
                    isEdit = dto.getState().equals("6") || dto.getState().equals("7") ? true : false;
                }
                if (isEdit) {
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
                map.put("msg", "查询个人考核详情成功");
                map.put("data", data);
                map.put("code", 0);
            }
        } else {
            data.put("total", "");

            Duty du = new Duty();
            du.setDbtype(dto.getDbtype());
            du.setStationcode(dto.getStationcode());
            List<Duty> dutyList = dutyService.selectDutyAll(du);

            List<Duty> dutyJichu = dutyList.stream().filter(p -> p.getDutytype().equals("0")).collect(Collectors.toList());
            for (Duty duty : dutyJichu) {
                duty.setScore(duty.getDefScore() == null ? "" : duty.getDefScore().toString());
            }

            List<Duty> dutyYiban = dutyList.stream().filter(p -> p.getDutytype().equals("1")).collect(Collectors.toList());
            for (Duty duty : dutyYiban) {
                duty.setScore(duty.getDefScore() == null ? "" : duty.getDefScore().toString());
            }

            List<Duty> dutyZhongdian = dutyList.stream().filter(p -> p.getDutytype().equals("2")).collect(Collectors.toList());
            for (Duty duty : dutyZhongdian) {
                duty.setScore(duty.getDefScore() == null ? "" : duty.getDefScore().toString());
            }

            List<Duty> dutyMubiao = dutyList.stream().filter(p -> p.getDutytype().equals("3")).collect(Collectors.toList());
            for (Duty duty : dutyMubiao) {
                duty.setScore(duty.getDefScore() == null ? "" : duty.getDefScore().toString());
            }

            if (type == 1) {
                isEdit = dto.getState().equals("7") ? true : false;
            } else {
                isEdit = dto.getState().equals("6") || dto.getState().equals("7") ? true : false;
            }
            if (isEdit) {
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

            map.put("msg", "查询个人考核详情成功");
            map.put("data", data);
            map.put("code", 0);
        }
    }

    private void getFlow(ModelMap map, Map<String, Object> data, UserSummaryDto dto, Station station, List<ScoreFlow> scoreFlow, List<ScoreDutySm> dutySmList, int type) {
        boolean isEdit = false;
        if (scoreFlow.size() > 0) {
            for (ScoreFlow flow : scoreFlow) {
                data.put("total", type == 2 ? "0" : flow.getScore() + "分");

                Duty du = new Duty();
                du.setDbtype(dto.getDbtype());
                du.setDbbk(dto.getDbbk());
                List<Duty> dutyList = dutyService.selectDutyAll(du);

                //获取 政治建设
                List<Duty> dutyJichu = dutyList.stream().filter(p -> p.getDutytype().equals("4")).collect(Collectors.toList());
                getDutyInfo(flow, dutyJichu, dutySmList, type);
                //获取 思想建设
                List<Duty> dutyYiban = dutyList.stream().filter(p -> p.getDutytype().equals("5")).collect(Collectors.toList());
                getDutyInfo(flow, dutyYiban, dutySmList, type);
                //获取 组织建设
                List<Duty> dutyZhongdian = dutyList.stream().filter(p -> p.getDutytype().equals("6")).collect(Collectors.toList());
                getDutyInfo(flow, dutyZhongdian, dutySmList, type);
                //获取 党建创新
                List<Duty> dutyMubiao = dutyList.stream().filter(p -> p.getDutytype().equals("7")).collect(Collectors.toList());
                getDutyInfo(flow, dutyMubiao, dutySmList, type);
                //获取 作风建设
                List<Duty> dutyZuofeng = dutyList.stream().filter(p -> p.getDutytype().equals("8")).collect(Collectors.toList());
                getDutyInfo(flow, dutyZuofeng, dutySmList, type);
                //判断是否可编辑
                if (type == 1) {
                    isEdit = dto.getState().equals("7") ? true : false;
                } else {
                    isEdit = dto.getState().equals("6") || dto.getState().equals("7") ? true : false;
                }
                if (isEdit) {
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
            du.setDbtype(dto.getDbtype());
            du.setDbbk(dto.getDbbk());
            List<Duty> dutyList = dutyService.selectDutyAll(du);

            List<Duty> dutyJichu = dutyList.stream().filter(p -> p.getDutytype().equals("4")).collect(Collectors.toList());
            for (Duty duty : dutyJichu) {
                this.setScoreAndZp(duty,dutySmList,type);
            }
            //获取关键量化指标的相关信息
            List<Duty> dutyYiban = dutyList.stream().filter(p -> p.getDutytype().equals("5")).collect(Collectors.toList());
            for (Duty duty : dutyYiban) {
                this.setScoreAndZp(duty,dutySmList,type);
            }
            //获取关键量化指标的相关信息
            List<Duty> dutyZhongdian = dutyList.stream().filter(p -> p.getDutytype().equals("6")).collect(Collectors.toList());
            for (Duty duty : dutyZhongdian) {
                this.setScoreAndZp(duty,dutySmList,type);
            }

            //获取关键量化指标的相关信息
            List<Duty> dutyMubiao = dutyList.stream().filter(p -> p.getDutytype().equals("7")).collect(Collectors.toList());
            for (Duty duty : dutyMubiao) {
                this.setScoreAndZp(duty,dutySmList,type);
            }

            List<Duty> dutyZuofeng = dutyList.stream().filter(p -> p.getDutytype().equals("8")).collect(Collectors.toList());
            for (Duty duty : dutyZuofeng) {
                this.setScoreAndZp(duty,dutySmList,type);
            }

            if (type == 1) {
                isEdit = dto.getState().equals("7") ? true : false;
            } else {
                isEdit = dto.getState().equals("6") || dto.getState().equals("7") ? true : false;
            }
            if (isEdit) {
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

    private void setScoreAndZp(Duty duty,List<ScoreDutySm> dutySmList,int type){
        List<ScoreDutySm> queryDutySmList = dutySmList.stream().filter(s -> s.getDutycode().equals(duty.getDutycode())).collect(Collectors.toList());
        if (type == 1) {
            duty.setScore(duty.getDefScore() == null ? "" : duty.getDefScore().toString());
        }
        if(queryDutySmList.size() > 0){
            duty.setZpsm(queryDutySmList.get(0).getZpsm());
        }
    }

    private void getDutyInfo(ScoreFlow flow, List<Duty> dutyJichu, List<ScoreDutySm> dutySmList, int type) {
        List<ScoreDutySm> queryDutySmList = new ArrayList<>();
        for (Duty duty : dutyJichu) {
            queryDutySmList = dutySmList.stream().filter(s -> s.getDutycode().equals(duty.getDutycode())).collect(Collectors.toList());
            if (type == 1) {
                ScoreDetail detail = detailService.selectDetailBySerialNo(duty.getDutycode(), flow.getSerialno());
                if (detail != null) {
                    duty.setScore(this.getDutyInfoScore(duty, detail));
                    duty.setCpsm(detail.getCpsm());
                } else {
                    duty.setScore(duty.getDefScore() == null ? "" : duty.getDefScore().toString());
                }
            }
            if (queryDutySmList.size() > 0) {
                duty.setZpsm(queryDutySmList.get(0).getZpsm());
            }
        }
    }

    private String getDutyInfoScore(Duty duty, ScoreDetail detail) {
        if (duty.getAscore() != null && detail.getScore() != null && Double.parseDouble(duty.getAscore()) == Double.parseDouble(detail.getScore())) {
            return duty.getAscore();
        } else if (duty.getBscore() != null && detail.getScore() != null && Double.parseDouble(duty.getBscore()) == Double.parseDouble(detail.getScore())) {
            return duty.getBscore();
        } else if (duty.getCscore() != null && detail.getScore() != null && Double.parseDouble(duty.getCscore()) == Double.parseDouble(detail.getScore())) {
            return duty.getCscore();
        } else if (duty.getDscore() != null && detail.getScore() != null && Double.parseDouble(duty.getDscore()) == Double.parseDouble(detail.getScore())) {
            return duty.getDscore();
        } else {
            return detail.getScore();
        }
    }

    private void getDutyInfoNew(List<ScoreFlow> flowList, List<Duty> dutyJichu, List<ScoreDutySm> dutySmList, int type) {
        List<ScoreDutySm> queryDutySmList = new ArrayList<>();
        for (Duty duty : dutyJichu) {
            queryDutySmList = dutySmList.stream().filter(s -> s.getDutycode().equals(duty.getDutycode())).collect(Collectors.toList());
            if (type == 1) {
                ScoreDetail detail = null;
                for (ScoreFlow flow : flowList) {
                    detail = detailService.selectDetailBySerialNo(duty.getDutycode(), flow.getSerialno());
                    if (detail != null) {
                        duty.setScore(detail.getScore());
                        duty.setCpsm(detail.getCpsm());
                        break;
                    } else {
                        duty.setScore(duty.getDefScore() == null ? "" : duty.getDefScore().toString());
                    }
                }
            }
            if (queryDutySmList.size() > 0) {
                duty.setZpsm(queryDutySmList.get(0).getZpsm());
            }

        }
    }

    private void setDuty(List<Duty> dutyList, List<Score> scoreList, String scoreType, boolean isDuty) {
        if (dutyList.size() > 0) {
            if (isDuty) {
                List<Score> dsList = scoreList.stream().filter(s -> s.getDutycode() != null && !s.getDutycode().equals("")).collect(Collectors.toList());
                List<Score> queryScoreList = new ArrayList<>();
                for (Duty duty : dutyList) {
                    queryScoreList = dsList.stream().filter(s -> s.getDutycode().equals(duty.getDutycode())).collect(Collectors.toList());
                    if (queryScoreList.size() > 0) {
                        duty.setScoretype(queryScoreList.get(0).getScoretype());
                    } else {
                        duty.setScoretype(scoreType);
                    }
                }
            } else {
                for (Duty duty : dutyList) {
                    duty.setScoretype(scoreType);
                }
            }
        }
    }

    private boolean isEF(String scoretype) {
        if (scoretype.equals("E") || scoretype.equals("F")) {
            return true;
        }
        return false;
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
            //新字段 2 已打分
            flow.setScoreState("2");
            User user = userService.findUserByUserCode(dto.getEmployeecode());
            if ("A".equals(flow.getScoretype())) {
                flow.setRatio(dto.getDbtype().equals("1") ? user.getAratio() : user.getAratio2());
            } else if ("B".equals(flow.getScoretype())) {
                flow.setRatio(dto.getDbtype().equals("1") ? user.getBratio() : user.getBratio2());
            } else if ("C".equals(flow.getScoretype())) {
                flow.setRatio(dto.getDbtype().equals("1") ? user.getCratio() : user.getCratio2());
            } else if ("D".equals(flow.getScoretype())) {
                flow.setRatio(dto.getDbtype().equals("1") ? user.getDratio() : user.getDratio2());
            } else if ("E".equals(flow.getScoretype())) {
                flow.setRatio(dto.getDbtype().equals("1") ? user.getEratio() : user.getEratio2());
            } else if ("F".equals(flow.getScoretype())) {
                flow.setRatio(dto.getDbtype().equals("1") ? user.getFratio() : user.getFratio2());
            }
            List<ScoreFlow> flow1 = flowService.selectByScoreFlow(dto.getSerialno(), scorringcode, dto.getDbtype());
            if (flow1.size() > 0) {//不为空  则修改评分信息
                List<ScoreFlow> list = new ArrayList<>();
                for (ScoreFlow scoreFlow : flow1) {
                    scoreFlow.setScoreState("2");
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
            log.error(e.getMessage(), e);
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
//            String scoreTypeEF = "";
//            if(scoreList.size()>0) {
//                if (this.isEF(scoreList.get(0).getScoretype())){
//                    scoreTypeEF = scoreList.get(0).getScoretype();
//                }
//            }
            List<Score> scoreABCList = scoreList.stream().filter(s -> !s.getScoretype().equals("E") && !s.getScoretype().equals("F")).collect(Collectors.toList());
            List<Score> scoreEFList = scoreList.stream().filter(s -> (s.getScoretype().equals("E") || s.getScoretype().equals("F"))).collect(Collectors.toList());
            List<String> typeList = new ArrayList<>();
            if (scoreABCList.size() > 0) {
                typeList.add(scoreABCList.get(0).getScoretype());
            }

            for(Score score : scoreEFList) {
                typeList.add(score.getScoretype());
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
                //新字段 2 已打分
                flow.setScoreState("2");
                flow.setDbtype(dto.getDbtype());

                if ("A".equals(flow.getScoretype())) {
                    flow.setRatio(dto.getDbtype().equals("1") ? user.getAratio() : user.getAratio2());
                } else if ("B".equals(flow.getScoretype())) {
                    flow.setRatio(dto.getDbtype().equals("1") ? user.getBratio() : user.getBratio2());
                } else if ("C".equals(flow.getScoretype())) {
                    flow.setRatio(dto.getDbtype().equals("1") ? user.getCratio() : user.getCratio2());
                } else if ("D".equals(flow.getScoretype())) {
                    flow.setRatio(dto.getDbtype().equals("1") ? user.getDratio() : user.getDratio2());
                } else if ("E".equals(flow.getScoretype())) {
                    flow.setRatio(dto.getDbtype().equals("1") ? user.getEratio() : user.getEratio2());
                } else if ("F".equals(flow.getScoretype())) {
                    flow.setRatio(dto.getDbtype().equals("1") ? user.getFratio() : user.getFratio2());
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
            log.error(e.getMessage(), e);
            map.put("msg", "操作失败");
            map.put("error", e.getMessage());
            map.put("eor", e.getStackTrace());
            map.put("code", 1);
        }

        return map;
    }

    @RequestMapping(value = "/saveDutySm", produces = "application/json;charset=utf-8")
    public Object saveDutySm1(HttpServletRequest req, UserSummaryDto dto, String dutySm) {
        ModelMap map = new ModelMap();
        try {
            if (!dutySm.equals("")) {
                List<ScoreDutySm> dutySmList = this.getDutySmList(dto.getYear(),dto.getMonth(),dto.getUsercode(),dto.getDbtype());
                List<ScoreDutySm> queryList = new ArrayList<>();
                List<ScoreDutySm> insertList = new ArrayList<>();
                List<ScoreDutySm> updateList = new ArrayList<>();
                JSONArray array = JSONArray.fromObject(dutySm);
                List<DutyCodeAndScore> dutyscList = JSONArray.toList(array, new DutyCodeAndScore(), new JsonConfig());
                for (int i = 0; i < dutyscList.size(); i++) {
                    String dutyCode = dutyscList.get(i).getTopicId();
                    String zpsm = dutyscList.get(i).getZpsm();
                    zpsm = zpsm == null || zpsm.equals("") ? "" : zpsm;
                    queryList = dutySmList.stream().filter(s -> s.getDutycode().equals(dutyCode)).collect(Collectors.toList());
                    if (queryList.size() == 0) {
                        ScoreDutySm entity = new ScoreDutySm();
                        entity.setYear(dto.getYear());
                        entity.setMonth(dto.getMonth());
                        entity.setDutycode(dutyCode);
                        entity.setScorredcode(dto.getEmployeecode());
                        entity.setZpsm(zpsm);
                        entity.setDbtype(dto.getDbtype());
                        insertList.add(entity);
                    } else {
                        ScoreDutySm entity = new ScoreDutySm();
                        entity.setId(queryList.get(0).getId());
                        entity.setZpsm(zpsm);
                        updateList.add(entity);
                    }
                }
                if (insertList.size() > 0) {
                    scoreDutySmService.batchInset(insertList);
                }
                if (updateList.size() > 0) {
                    scoreDutySmService.batchUpdate(updateList);
                }
                map.put("msg", "操作成功");
                map.put("code", 0);
            } else {
                map.put("msg", "操作失败,没有获取到数据.");
                map.put("code", 1);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
        getTypeUserScore(dto, df, user, history);
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

        //E类评分
        Double EScore = flowService.getTypeAvgScore(dto.getSerialno(), "E", dto.getDbtype());
        if (EScore == null) {
            EScore = 0.0;
            history.setEscore(EScore);
        } else {
            history.setEscore(Double.parseDouble(df.format(EScore)));
            totalRatio += user.getEratio();
        }

        //F类评分
        Double FScore = flowService.getTypeAvgScore(dto.getSerialno(), "F", dto.getDbtype());
        if (FScore == null) {
            FScore = 0.0;
            history.setFscore(FScore);
        } else {
            history.setFscore(Double.parseDouble(df.format(FScore)));
            totalRatio += user.getFratio();
        }

        //获取总分  总分= A类总分 X A类评分人系数 +  B类总分 X B类评分人系数 + C类总分 X C类评分人系数 + D类总分 X D类评分人系数

        Double totalscore = history.getAscore() * user.getAratio() + history.getBscore() * user.getBratio() +
                history.getCscore() * user.getCratio() + history.getDscore() * user.getDratio() +
                history.getEscore() * user.getEratio() + history.getFscore() * user.getFratio();
        if (totalscore == 0.0 || totalscore == null) {
            history.setTotalscore(0.0);
        } else {
            history.setTotalscore(Double.parseDouble(df.format(totalscore / totalRatio)));
        }
    }

    private void getDutyTypeUserScore(UserSummaryDto dto, ScoreHistory history) {
        List<UserScoreDto> list = userScoreDtoService.findUserDutyScore(dto.getYear(), dto.getMonth(), dto.getEmployeecode(), null, dto.getDbtype());
        List<UserScoreDto> dutyAndRatioList = userScoreDtoService.getTypeUserDutyScore(list, false);
        Double sumScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getScore).sum();
        Double aScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getAscore).sum();
        Double bScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getBscore).sum();
        Double cScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getCscore).sum();
        Double dScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getDscore).sum();

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
            report.setState(0);
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

        if (flow1.getDbtype().equals("1")) {
            JSONArray array4 = JSONArray.fromObject(dutyZuofeng);
            List zuofeng = JSONArray.toList(array4, new DutyCodeAndScore(), new JsonConfig());
            judgeAddOrUpdate(flow1, detail, zuofeng);
        }
    }

    private void getDutyScore(String dutyJiChu, String dutyYiBan, String dutyZhongdian, String dutyMubiao, List<ScoreFlow> flowList, List<Score> scoreList) {
        ScoreDetail detail = new ScoreDetail();
        JSONArray array = JSONArray.fromObject(dutyJiChu);
        Double[] abcd = new Double[3];
        abcd[0] = 0.0;
        abcd[1] = 0.0;
        abcd[2] = 0.0;
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
            if (flow.getScoretype().equals("E") || flow.getScoretype().equals("F")) {
                ScoreFlow update = new ScoreFlow();
                update.setScoreState("2");
                update.setSerialno(flow.getSerialno());
                update.setScore(flow.getScoretype().equals("E") ? abcd[0] : abcd[1]);
                flowService.updateByPrimaryKeySelective(update);
            } else {
                ScoreFlow update = new ScoreFlow();
                update.setScoreState("2");
                update.setSerialno(flow.getSerialno());
                update.setScore(abcd[2]);
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
            scoreDetail.setCpsm(jichu.get(i).getCpsm());
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
            if (dcas.getScoretype().equals("E") || dcas.getScoretype().equals("F")) {
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
                scoreDetail.setCpsm(jichu.get(i).getCpsm());
                if (NumberUtil.isNumber(dcas.getScore())) {
                    scoreDetail.setScore(dcas.getScore());
                    if (dcas.getScoretype().equals("E")) {
                        abcd[0] += Double.parseDouble(dcas.getScore());
                    } else if(dcas.getScoretype().equals("F")) {
                        abcd[1] += Double.parseDouble(dcas.getScore());
                    } else {
                        abcd[2] += Double.parseDouble(dcas.getScore());
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
                            List<ScoreHistory> shList = historyService.findUserScoreHistory(year, month, dbtype, null);
                            List<ScoreHistory> scoreHistoryList = shList.stream().filter(s -> s.getDbbk() != null && s.getDbbk() != "" && (
                                    s.getDbbk().equals("3") || s.getDbbk().equals("4"))).collect(Collectors.toList());
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
                                    typeList.add("E");
                                    typeList.add("F");
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
                                    Double ratioE = 0.0;
                                    Double score0E = 0.0;
                                    Double score1E = 0.0;
                                    Double score2E = 0.0;
                                    Double score3E = 0.0;
                                    Double score4E = 0.0;
                                    Double ratioF = 0.0;
                                    Double score0F = 0.0;
                                    Double score1F = 0.0;
                                    Double score2F = 0.0;
                                    Double score3F = 0.0;
                                    Double score4F = 0.0;
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
                                            } else if (type.equals("D")) {
                                                ratioD = ratio;
                                                score0D = score0;
                                                score1D = score1;
                                                score2D = score2;
                                                score3D = score3;
                                                score4D = score4;
                                            } else if (type.equals("E")) {
                                                ratioE = ratio;
                                                score0E = score0;
                                                score1E = score1;
                                                score2E = score2;
                                                score3E = score3;
                                                score4E = score4;
                                            } else if (type.equals("F")) {
                                                ratioF = ratio;
                                                score0F = score0;
                                                score1F = score1;
                                                score2F = score2;
                                                score3F = score3;
                                                score4F = score4;
                                            }
                                        }

                                        sum0Score = score0A * ratioA + score0B * ratioB +
                                                score0C * ratioC + score0D * ratioD +
                                                score0E * ratioE + score0F * ratioF;

                                        sum1Score = score1A * ratioA + score1B * ratioB +
                                                score1C * ratioC + score1D * ratioD +
                                                score1E * ratioE + score1F * ratioF;

                                        sum2Score = score2A * ratioA + score2B * ratioB +
                                                score2C * ratioC + score2D * ratioD +
                                                score2E * ratioE + score2F * ratioF;

                                        sum3Score = score3A * ratioA + score3B * ratioB +
                                                score3C * ratioC + score3D * ratioD +
                                                score3E * ratioE + score3F * ratioF;

                                        sum4Score = score4A * ratioA + score4B * ratioB +
                                                score4C * ratioC + score4D * ratioD +
                                                score4E * ratioE + score4F * ratioF;

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
                                        score0E = score0E * ratioE / totalRatio;
                                        score0F = score0F * ratioF / totalRatio;

                                        score1A = score1A * ratioA / totalRatio;
                                        score1B = score1B * ratioB / totalRatio;
                                        score1C = score1C * ratioC / totalRatio;
                                        score1D = score1D * ratioD / totalRatio;
                                        score1E = score1E * ratioE / totalRatio;
                                        score1F = score1F * ratioF / totalRatio;

                                        score2A = score2A * ratioA / totalRatio;
                                        score2B = score2B * ratioB / totalRatio;
                                        score2C = score2C * ratioC / totalRatio;
                                        score2D = score2D * ratioD / totalRatio;
                                        score2E = score2E * ratioE / totalRatio;
                                        score2F = score2F * ratioF / totalRatio;

                                        score3A = score3A * ratioA / totalRatio;
                                        score3B = score3B * ratioB / totalRatio;
                                        score3C = score3C * ratioC / totalRatio;
                                        score3D = score3D * ratioD / totalRatio;
                                        score3E = score3E * ratioE / totalRatio;
                                        score3F = score3F * ratioF / totalRatio;

                                        score4A = score4A * ratioA / totalRatio;
                                        score4B = score4B * ratioB / totalRatio;
                                        score4C = score4C * ratioC / totalRatio;
                                        score4D = score4D * ratioD / totalRatio;
                                        score4E = score4E * ratioE / totalRatio;
                                        score4F = score4F * ratioF / totalRatio;

                                        this.getResultReport("4", "政治建设", item, score0A, score0B, score0C, score0D, score0E, score0F, sum0Score, 0, rrList);
                                        this.getResultReport("5", "思想建设", item, score1A, score1B, score1C, score1D, score1E, score1F, sum1Score, 0, rrList);
                                        this.getResultReport("6", "组织建设", item, score2A, score2B, score2C, score2D, score2E, score2F, sum2Score, 0, rrList);
                                        this.getResultReport("7", "党建创新", item, score3A, score3B, score3C, score3D, score3E, score3F, sum3Score, 0, rrList);
                                        this.getResultReport("8", "作风建设", item, score4A, score4B, score4C, score4D, score4E, score4F, sum4Score, 0, rrList);

                                        item.setSumJcScore(sum0Score);
                                        item.setSumGwScore(sum1Score);
                                        item.setSumZdScore(sum2Score);
                                        item.setSumMbScore(sum3Score);
                                        item.setSumZfScore(sum4Score);
                                        item.setTotalscore(sum0Score + sum1Score + sum2Score + sum3Score + sum4Score);
                                        totalSumScore += item.getTotalscore();
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
                                        scoreHistory.setSumTotalScore(item.getTotalscore());
                                        scoreHistory.setTotalscore(item.getTotalscore());
                                        scoreHistory.setSumMbScore(item.getSumMbScore());
                                        scoreHistory.setSumZfScore(item.getSumZfScore());
                                        historyService.updateByPrimaryKeySelective(scoreHistory);
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
                                            insertEr.setState(0);
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
                log.error(e.getMessage(), e);
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

    public Object jisuan2(HttpServletRequest req, String dbtype) {
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
                                List<UserScoreDto> userScoreDtoList = userScoreDtoService.findUserScore(year, month, typeList, dbtype);
                                if (userScoreDtoList.size() > 0) {
//                                    Map<String, List<UserScoreDto>> byAuthor = userScoreDtoList.stream().map()
//                                            .collect(Collectors.groupingBy(UserScoreDto::getScoreType));
                                    typeList = new ArrayList<>();
                                    typeList.add("A");
                                    typeList.add("B");
                                    typeList.add("C");
                                    typeList.add("D");
                                    typeList.add("E");
                                    typeList.add("F");
                                    List<UserScoreDto> userQueryList = new ArrayList<>();
                                    Double totalRatio = 0.0;
                                    Double sum0Score = 0.0;
                                    Double sum1Score = 0.0;
                                    Double sum2Score = 0.0;
                                    Double sum3Score = 0.0;
                                    Double ratio = 0.0;
                                    Double score0 = 0.0;
                                    Double score1 = 0.0;
                                    Double score2 = 0.0;
                                    Double score3 = 0.0;
                                    Double ratioA = 0.0;
                                    Double score0A = 0.0;
                                    Double score1A = 0.0;
                                    Double score2A = 0.0;
                                    Double score3A = 0.0;
                                    Double ratioB = 0.0;
                                    Double score0B = 0.0;
                                    Double score1B = 0.0;
                                    Double score2B = 0.0;
                                    Double score3B = 0.0;
                                    Double ratioC = 0.0;
                                    Double score0C = 0.0;
                                    Double score1C = 0.0;
                                    Double score2C = 0.0;
                                    Double score3C = 0.0;
                                    Double ratioD = 0.0;
                                    Double score0D = 0.0;
                                    Double score1D = 0.0;
                                    Double score2D = 0.0;
                                    Double score3D = 0.0;
                                    Double ratioE = 0.0;
                                    Double score0E = 0.0;
                                    Double score1E = 0.0;
                                    Double score2E = 0.0;
                                    Double score3E = 0.0;
                                    Double ratioF = 0.0;
                                    Double score0F = 0.0;
                                    Double score1F = 0.0;
                                    Double score2F = 0.0;
                                    Double score3F = 0.0;
                                    int mbMaxCount = 5;

                                    Double totalKzrSumScore = 0.0;
                                    Double totalHszSumScore = 0.0;
                                    Double totalXzSumScore = 0.0;
                                    Double kzrCount = 0.0;
                                    Double hszCount = 0.0;
                                    Double xzCount = 0.0;

                                    int mbCount = 0;
                                    List<ResultReport> rrList = new ArrayList<>();
                                    ScoreYdyf queryYdyf = new ScoreYdyf();
                                    queryYdyf.setYear(setTime.getYear());
                                    queryYdyf.setMonth(setTime.getMonth());
                                    List<ScoreYdyf> ydyfQueryList = new ArrayList<>();
                                    List<ScoreYdyf> ydyfList = ydyfService.findYdyfList(queryYdyf);
                                    for (ScoreHistory item : scoreHistoryList) {
                                        totalRatio = 0.0;
                                        userQueryList = userScoreDtoList.stream().filter(
                                                s -> s.getScoredCode().equals(item.getUsercode())
                                        ).collect(Collectors.toList());

                                        for (String type : typeList) {
                                            score0 = this.getScore(userQueryList, type, "0");
                                            score1 = this.getScore(userQueryList, type, "1");
                                            score2 = this.getScore(userQueryList, type, "2");
                                            score3 = this.getScore(userQueryList, type, "3");
                                            ratio = userQueryList.stream().filter(s -> s.getScoreType().equals(type)).map(
                                                    UserScoreDto::getRatio).findFirst().orElse(0.0);
                                            totalRatio += ratio;

                                            if (type.equals("A")) {
                                                ratioA = ratio;
                                                score0A = score0;
                                                score1A = score1;
                                                score2A = score2;
                                                score3A = score3;
                                            } else if (type.equals("B")) {
                                                ratioB = ratio;
                                                score0B = score0;
                                                score1B = score1;
                                                score2B = score2;
                                                score3B = score3;
                                            } else if (type.equals("C")) {
                                                ratioC = ratio;
                                                score0C = score0;
                                                score1C = score1;
                                                score2C = score2;
                                                score3C = score3;
                                            } else if (type.equals("D")) {
                                                ratioD = ratio;
                                                score0D = score0;
                                                score1D = score1;
                                                score2D = score2;
                                                score3D = score3;
                                            } else if (type.equals("E")) {
                                                ratioE = ratio;
                                                score0E = score0;
                                                score1E = score1;
                                                score2E = score2;
                                                score3E = score3;
                                            } else if (type.equals("F")) {
                                                ratioF = ratio;
                                                score0F = score0;
                                                score1F = score1;
                                                score2F = score2;
                                                score3F = score3;
                                            }
                                        }

                                        sum0Score = score0A * ratioA + score0B * ratioB +
                                                score0C * ratioC + score0D * ratioD +
                                                score0E * ratioE + score0F * ratioF;

                                        sum1Score = score1A * ratioA + score1B * ratioB +
                                                score1C * ratioC + score1D * ratioD +
                                                score1E * ratioE + score1F * ratioF;

                                        sum2Score = score2A * ratioA + score2B * ratioB +
                                                score2C * ratioC + score2D * ratioD +
                                                score2E * ratioE + score2F * ratioF;

                                        sum3Score = score3A * ratioA + score3B * ratioB +
                                                score3C * ratioC + score3D * ratioD +
                                                score3E * ratioE + score3F * ratioF;

                                        sum0Score = sum0Score == 0 ? 0 : sum0Score / totalRatio;
                                        sum1Score = sum1Score == 0 ? 0 : sum1Score / totalRatio;
                                        sum2Score = sum2Score == 0 ? 0 : sum2Score / totalRatio;
                                        sum3Score = sum3Score == 0 ? 0 : sum3Score / totalRatio;

                                        // ResultReport表 得出计算后结果
                                        score0A = score0A * ratioA / totalRatio;
                                        score0B = score0B * ratioB / totalRatio;
                                        score0C = score0C * ratioC / totalRatio;
                                        score0D = score0D * ratioD / totalRatio;
                                        score0E = score0E * ratioE / totalRatio;
                                        score0F = score0F * ratioF / totalRatio;

                                        score1A = score1A * ratioA / totalRatio;
                                        score1B = score1B * ratioB / totalRatio;
                                        score1C = score1C * ratioC / totalRatio;
                                        score1D = score1D * ratioD / totalRatio;
                                        score1E = score1E * ratioE / totalRatio;
                                        score1F = score1F * ratioF / totalRatio;

                                        score2A = score2A * ratioA / totalRatio;
                                        score2B = score2B * ratioB / totalRatio;
                                        score2C = score2C * ratioC / totalRatio;
                                        score2D = score2D * ratioD / totalRatio;
                                        score2E = score2E * ratioE / totalRatio;
                                        score2F = score2F * ratioF / totalRatio;

                                        score3A = score3A * ratioA / totalRatio;
                                        score3B = score3B * ratioB / totalRatio;
                                        score3C = score3C * ratioC / totalRatio;
                                        score3D = score3D * ratioD / totalRatio;
                                        score3E = score3E * ratioE / totalRatio;
                                        score3F = score3F * ratioF / totalRatio;

                                        if (userQueryList.size() > 0) {
                                            mbCount = userQueryList.stream().filter(
                                                    s -> s.getDutyType().equals("3")).collect(Collectors.groupingBy(UserScoreDto::getDserialNo)).size();
                                        } else {
                                            mbCount = mbMaxCount;
                                        }

                                        ydyfQueryList = ydyfList.stream().filter(s -> s.getMoneyCard().equals(item.getMoneycard())).collect(Collectors.toList());
                                        if (ydyfQueryList.size() > 0) {
                                            item.setDfScore(ydyfQueryList.get(0).getScore());
                                        } else {
                                            item.setDfScore(0.0);
                                        }

                                        this.getResultReport("0", "基础评分", item, score0A, score0B, score0C, score0D, score0E, score0F, sum0Score, 0, rrList);
                                        this.getResultReport("1", "岗位评分", item, score1A, score1B, score1C, score1D, score1E, score1F, sum1Score, 0, rrList);
                                        this.getResultReport("2", "重点评分", item, score2A, score2B, score2C, score2D, score2E, score2F, sum2Score, 0, rrList);
                                        this.getResultReport("3", "目标评分", item, score3A, score3B, score3C, score3D, score3E, score3F, sum3Score, mbCount, rrList);
                                        this.getResultReport("10", "党风廉政", item, 0, 0, 0, 0, 0, 0, item.getDfScore(), 0, rrList);

                                        item.setSumJcScore(sum0Score);
                                        item.setSumGwScore(sum1Score);
                                        item.setSumZdScore(sum2Score);
                                        item.setSumMbScore(sum3Score);
                                        item.setTotalscore(sum0Score + sum1Score + sum2Score + sum3Score);

                                        item.setMbCount(mbCount);

                                        if (item.getPostType() != null && item.getPostType().equals("1")) {
                                            totalKzrSumScore += sum3Score;
                                            kzrCount += mbCount;
                                        }
                                        if (item.getPostType() != null && item.getPostType().equals("2")) {
                                            totalHszSumScore += sum3Score;
                                            hszCount += mbCount;
                                        }
                                        if (item.getPostType() != null && item.getPostType().equals("3")) {
                                            totalXzSumScore += sum3Score;
                                            xzCount += mbCount;
                                        }
                                    }

                                    Double avgMbKzrScore = kzrCount == 0 ? 0 : totalKzrSumScore / kzrCount;
                                    Double avgMbHszScore = hszCount == 0 ? 0 : totalHszSumScore / hszCount;
                                    Double avgMbXzScore = xzCount == 0 ? 0 : totalXzSumScore / xzCount;

                                    Double jsMbScore = 0.0;
                                    boolean isUpdate = false;
                                    totalKzrSumScore = 0.0;
                                    totalHszSumScore = 0.0;
                                    totalXzSumScore = 0.0;
                                    kzrCount = 0.0;
                                    hszCount = 0.0;
                                    xzCount = 0.0;
                                    Double totalScore = 0.0;

                                    for (ScoreHistory item : scoreHistoryList) {
                                        totalScore = item.getTotalscore();
                                        jsMbScore = 0.0;
                                        isUpdate = false;

                                        totalScore += item.getDfScore();

                                        if (item.getMbCount() != mbMaxCount) {
                                            isUpdate = true;
                                        }
                                        if (item.getPostType() != null && item.getPostType().equals("1")) {
                                            if (isUpdate) {
                                                jsMbScore = (mbMaxCount - item.getMbCount()) * avgMbKzrScore;
                                                totalScore += jsMbScore;
                                            }
                                            totalKzrSumScore += totalScore;
                                            kzrCount += 1;
                                        }
                                        if (item.getPostType() != null && item.getPostType().equals("2")) {
                                            if (isUpdate) {
                                                jsMbScore = (mbMaxCount - item.getMbCount()) * avgMbHszScore;
                                                totalScore += jsMbScore;
                                            }
                                            totalHszSumScore += totalScore;
                                            hszCount += 1;
                                        }
                                        if (item.getPostType() != null && item.getPostType().equals("3")) {
                                            if (isUpdate) {
                                                jsMbScore = (mbMaxCount - item.getMbCount()) * avgMbXzScore;
                                                totalScore += jsMbScore;
                                            }
                                            totalXzSumScore += totalScore;
                                            xzCount += 1;
                                        }
                                        item.setAvgMbScore(jsMbScore);
                                        item.setSumTotalScore(totalScore);
                                    }

                                    EvaluationReport query = new EvaluationReport();
                                    query.setYear(year);
                                    query.setMonth(month);
                                    query.setDbtype(dbtype);
                                    List<EvaluationReport> evaluationReportList = evaluationReportService.selectEvaluationReportList(query);
                                    List<EvaluationReport> queryErList = new ArrayList<>();

                                    Double avgKzrScore = kzrCount == 0 ? 0 : totalKzrSumScore / kzrCount;
                                    Double avgHszScore = hszCount == 0 ? 0 : totalHszSumScore / hszCount;
                                    Double avgXzScore = xzCount == 0 ? 0 : totalXzSumScore / xzCount;

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
                                            if (item.getPostType() != null && item.getPostType().equals("1")) {
                                                updateEr.setAvgscore(avgKzrScore);
                                            } else if (item.getPostType() != null && item.getPostType().equals("2")) {
                                                updateEr.setAvgscore(avgHszScore);
                                            } else if (item.getPostType() != null && item.getPostType().equals("3")) {
                                                updateEr.setAvgscore(avgXzScore);
                                            } else {
                                                updateEr.setAvgscore(0.0);
                                            }

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
                                            if (item.getPostType() != null && item.getPostType().equals("1")) {
                                                insertEr.setAvgscore(avgKzrScore);
                                            } else if (item.getPostType() != null && item.getPostType().equals("2")) {
                                                insertEr.setAvgscore(avgHszScore);
                                            } else if (item.getPostType() != null && item.getPostType().equals("3")) {
                                                insertEr.setAvgscore(avgXzScore);
                                            } else {
                                                insertEr.setAvgscore(0.0);
                                            }
                                            insertEr.setDfScore(item.getDfScore());
                                            insertEr.setState(0);
                                            insertEr.setDbtype(dbtype);
                                            evaluationReportService.insertSelective(insertEr);
                                        }
                                        historyService.updateByPrimaryKeySelective(scoreHistory);
                                    }
                                    if (rrList.size() > 0) {
                                        this.updateOrInsertResultReportMb(rrList, year, month, avgMbKzrScore, avgMbHszScore, avgMbXzScore, mbMaxCount, dbtype);
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
                log.error(e.getMessage(), e);
                map.put("msg", "查询个人考核详情失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
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
                                    typeList.add("E");
                                    typeList.add("F");
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
//                                    Double ratioE = 0.0;
                                    Double score0E = 0.0;
                                    Double score1E = 0.0;
                                    Double score2E = 0.0;
                                    Double score3E = 0.0;
//                                    Double ratioF = 0.0;
                                    Double score0F = 0.0;
                                    Double score1F = 0.0;
                                    Double score2F = 0.0;
                                    Double score3F = 0.0;
                                    int mbMaxCount = 5;
                                    Double totalKzrSumScore = 0.0;
                                    Double totalHszSumScore = 0.0;
                                    Double totalXzSumScore = 0.0;
                                    Double kzrCount = 0.0;
                                    Double hszCount = 0.0;
                                    Double xzCount = 0.0;
                                    int mbCount = 0;
                                    List<ResultReport> rrList = new ArrayList<>();
                                    ScoreYdyf queryYdyf = new ScoreYdyf();
                                    queryYdyf.setYear(setTime.getYear());
                                    queryYdyf.setMonth(setTime.getMonth());
                                    List<ScoreYdyf> ydyfQueryList = new ArrayList<>();
                                    List<ScoreYdyf> ydyfList = ydyfService.findYdyfList(queryYdyf);
                                    for (ScoreHistory item : scoreHistoryList) {
                                        userQueryList = userScoreDtoList.stream().filter(
                                                s -> s.getScoredCode().equals(item.getUsercode())
                                        ).collect(Collectors.toList());

                                        List<UserScoreDto> dutyAndRatioList = userScoreDtoService.getTypeUserDutyScore(userQueryList, true);

                                        score0A = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("0")).mapToDouble(UserScoreDto::getAscore).sum();
                                        score1A = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("1")).mapToDouble(UserScoreDto::getAscore).sum();
                                        score2A = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getAscore).sum();
                                        score3A = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getAscore).sum();
                                        score0B = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("0")).mapToDouble(UserScoreDto::getBscore).sum();
                                        score1B = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("1")).mapToDouble(UserScoreDto::getBscore).sum();
                                        score2B = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getBscore).sum();
                                        score3B = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getBscore).sum();
                                        score0C = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("0")).mapToDouble(UserScoreDto::getCscore).sum();
                                        score1C = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("1")).mapToDouble(UserScoreDto::getCscore).sum();
                                        score2C = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getCscore).sum();
                                        score3C = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getCscore).sum();
                                        score0D = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("0")).mapToDouble(UserScoreDto::getDscore).sum();
                                        score1D = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("1")).mapToDouble(UserScoreDto::getDscore).sum();
                                        score2D = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getDscore).sum();
                                        score3D = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getDscore).sum();
                                        score0E = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("0")).mapToDouble(UserScoreDto::getEscore).sum();
                                        score1E = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("1")).mapToDouble(UserScoreDto::getEscore).sum();
                                        score2E = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getEscore).sum();
                                        score3E = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getEscore).sum();
                                        score0F = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("0")).mapToDouble(UserScoreDto::getFscore).sum();
                                        score1F = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("1")).mapToDouble(UserScoreDto::getFscore).sum();
                                        score2F = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getFscore).sum();
                                        score3F = dutyAndRatioList.stream().filter(s -> s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getFscore).sum();

                                        sumJcScore = score0A + score0B + score0C + score0D + score0E + score0F;

                                        sumGwScore = score1A + score1B + score1C + score1D + score1E + score1F;

                                        sumZdScore = score2A + score2B + score2C + score2D + score2E + score2F;

                                        sumMbScore = score3A + score3B + score3C + score3D + score3E + score3F;

                                        if (userQueryList.size() > 0) {
                                            mbCount = userQueryList.stream().filter(
                                                    s -> s.getDutyType().equals("3")).collect(Collectors.groupingBy(UserScoreDto::getDserialNo)).size();
                                        } else {
                                            mbCount = mbMaxCount;
                                        }

                                        ydyfQueryList = ydyfList.stream().filter(s -> s.getMoneyCard().equals(item.getMoneycard())).collect(Collectors.toList());
                                        if (ydyfQueryList.size() > 0) {
                                            item.setDfScore(ydyfQueryList.get(0).getScore());
                                        } else {
                                            item.setDfScore(0.0);
                                        }
                                        this.getResultReport("0", "基础评分", item, score0A, score0B, score0C, score0D, score0E, score0F, sumJcScore, 0, rrList);
                                        this.getResultReport("1", "岗位评分", item, score1A, score1B, score1C, score1D, score1E, score1F, sumGwScore, 0, rrList);
                                        this.getResultReport("2", "重点评分", item, score2A, score2B, score2C, score2D, score2E, score2F, sumZdScore, 0, rrList);
                                        this.getResultReport("3", "目标评分", item, score3A, score3B, score3C, score3D, score3E, score3F, sumMbScore, mbCount, rrList);
                                        this.getResultReport("10", "党风廉政", item, 0, 0, 0, 0, 0, 0, item.getDfScore(), 0, rrList);

                                        item.setSumJcScore(sumJcScore);
                                        item.setSumGwScore(sumGwScore);
                                        item.setSumZdScore(sumZdScore);
                                        item.setSumMbScore(sumMbScore);
                                        item.setTotalscore(sumJcScore + sumGwScore + sumZdScore + sumMbScore);

                                        item.setMbCount(mbCount);

                                        if (item.getPostType() != null && item.getPostType().equals("1")) {
                                            totalKzrSumScore += sumMbScore;
                                            kzrCount += mbCount;
                                        }
                                        if (item.getPostType() != null && item.getPostType().equals("2")) {
                                            totalHszSumScore += sumMbScore;
                                            hszCount += mbCount;
                                        }
                                        if (item.getPostType() != null && item.getPostType().equals("3")) {
                                            totalXzSumScore += sumMbScore;
                                            xzCount += mbCount;
                                        }
                                    }
                                    Double avgMbKzrScore = kzrCount == 0 ? 0 : totalKzrSumScore / kzrCount;
                                    Double avgMbHszScore = hszCount == 0 ? 0 : totalHszSumScore / hszCount;
                                    Double avgMbXzScore = xzCount == 0 ? 0 : totalXzSumScore / xzCount;

                                    Double jsMbScore = 0.0;
                                    boolean isUpdate = false;
                                    totalKzrSumScore = 0.0;
                                    totalHszSumScore = 0.0;
                                    totalXzSumScore = 0.0;
                                    kzrCount = 0.0;
                                    hszCount = 0.0;
                                    xzCount = 0.0;
                                    Double totalScore = 0.0;

                                    for (ScoreHistory item : scoreHistoryList) {
                                        totalScore = item.getTotalscore();
                                        jsMbScore = 0.0;
                                        isUpdate = false;

                                        totalScore += item.getDfScore();

                                        if (item.getMbCount() != mbMaxCount) {
                                            isUpdate = true;
                                        }
                                        if (item.getPostType() != null && item.getPostType().equals("1")) {
                                            if (isUpdate) {
                                                jsMbScore = (mbMaxCount - item.getMbCount()) * avgMbKzrScore;
                                                totalScore += jsMbScore;
                                            }
                                            totalKzrSumScore += totalScore;
                                            kzrCount += 1;
                                        }
                                        if (item.getPostType() != null && item.getPostType().equals("2")) {
                                            if (isUpdate) {
                                                jsMbScore = (mbMaxCount - item.getMbCount()) * avgMbHszScore;
                                                totalScore += jsMbScore;
                                            }
                                            totalHszSumScore += totalScore;
                                            hszCount += 1;
                                        }
                                        if (item.getPostType() != null && item.getPostType().equals("3")) {
                                            if (isUpdate) {
                                                jsMbScore = (mbMaxCount - item.getMbCount()) * avgMbXzScore;
                                                totalScore += jsMbScore;
                                            }
                                            totalXzSumScore += totalScore;
                                            xzCount += 1;
                                        }
                                        item.setAvgMbScore(jsMbScore);
                                        item.setSumTotalScore(totalScore);
                                    }
                                    EvaluationReport query = new EvaluationReport();
                                    query.setYear(year);
                                    query.setMonth(month);
                                    query.setDbtype(dbtype);
                                    List<EvaluationReport> evaluationReportList = evaluationReportService.selectEvaluationReportList(query);
                                    List<EvaluationReport> queryErList = new ArrayList<>();

                                    Double avgKzrScore = kzrCount == 0 ? 0 : totalKzrSumScore / kzrCount;
                                    Double avgHszScore = hszCount == 0 ? 0 : totalHszSumScore / hszCount;
                                    Double avgXzScore = xzCount == 0 ? 0 : totalXzSumScore / xzCount;

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
                                            if (item.getPostType() != null && item.getPostType().equals("1")) {
                                                updateEr.setAvgscore(avgKzrScore);
                                            } else if (item.getPostType() != null && item.getPostType().equals("2")) {
                                                updateEr.setAvgscore(avgHszScore);
                                            } else if (item.getPostType() != null && item.getPostType().equals("3")) {
                                                updateEr.setAvgscore(avgXzScore);
                                            } else {
                                                updateEr.setAvgscore(0.0);
                                            }
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
                                            if (item.getPostType() != null && item.getPostType().equals("1")) {
                                                insertEr.setAvgscore(avgKzrScore);
                                            } else if (item.getPostType() != null && item.getPostType().equals("2")) {
                                                insertEr.setAvgscore(avgHszScore);
                                            } else if (item.getPostType() != null && item.getPostType().equals("3")) {
                                                insertEr.setAvgscore(avgXzScore);
                                            } else {
                                                insertEr.setAvgscore(0.0);
                                            }
                                            insertEr.setDfScore(item.getDfScore());
                                            insertEr.setDbtype(dbtype);
                                            evaluationReportService.insertSelective(insertEr);
                                        }
                                        historyService.updateByPrimaryKeySelective(scoreHistory);
                                    }
                                    if (rrList.size() > 0) {
                                        this.updateOrInsertResultReportMb(rrList, year, month, avgMbKzrScore, avgMbHszScore, avgMbXzScore, mbMaxCount, dbtype);
                                    }
                                    msg = "OK";
                                } else {
                                    msg = "考核季结明细数据为空";
                                }
                            } else {
                                msg = "考核季结历史数据为空";
                            }
                        } else {
                            msg = "考核季结数据还有评分未完成";
                        }
                    } else {
                        msg = "考核季结数据为空";
                    }
                } else {
                    msg = "考核季结为空";
                }
                map.put("msg", msg.equals("OK") ? "计算完成." : msg);
                map.put("code", msg.equals("OK") ? 0 : 1);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
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
        long count1 = rrList.stream().filter(s -> s.getResultreportcode().equals("4")).count();
        Double jcScore1 = this.getAvgScore(rrList, "4", count1);
        Double gwScore1 = this.getAvgScore(rrList, "5", count1);
        Double zdScore1 = this.getAvgScore(rrList, "6", count1);
        Double mbScore1 = this.getAvgScore(rrList, "7", count1);
        Double dfScore1 = this.getAvgScore(rrList, "8", count1);
        if (evaluationReportList.size() > 0) {
            List<ResultReport> resultReportList = resultReportService.selectResultReportList(year, month, dbtype);
            List<ResultReport> rrQueryList = new ArrayList<>();
            if (resultReportList.size() > 0) {
                List<ResultReport> rList = new ArrayList<>();
                for (EvaluationReport er : evaluationReportList) {
                    rrQueryList = rrList.stream().filter(s -> s.getUserCode().equals(er.getUsercode())).collect(Collectors.toList());
                    for (ResultReport rr : rrQueryList) {
                        rList = resultReportList.stream().filter(s -> s.getResultreportcode().equals(rr.getResultreportcode()) && s.getEvaluationreportcode().equals(er.getId())).collect(Collectors.toList());
                        if (rr.getResultreportcode().equals("4")) {
                            rr.setAvgscore(jcScore1);
                        } else if (rr.getResultreportcode().equals("5")) {
                            rr.setAvgscore(gwScore1);
                        } else if (rr.getResultreportcode().equals("6")) {
                            rr.setAvgscore(zdScore1);
                        } else if (rr.getResultreportcode().equals("7")) {
                            rr.setAvgscore(mbScore1);
                        } else if (rr.getResultreportcode().equals("8")) {
                            rr.setAvgscore(dfScore1);
                        }
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
                        if (rr.getResultreportcode().equals("4")) {
                            rr.setAvgscore(jcScore1);
                        } else if (rr.getResultreportcode().equals("5")) {
                            rr.setAvgscore(gwScore1);
                        } else if (rr.getResultreportcode().equals("6")) {
                            rr.setAvgscore(zdScore1);
                        } else if (rr.getResultreportcode().equals("7")) {
                            rr.setAvgscore(mbScore1);
                        } else if (rr.getResultreportcode().equals("8")) {
                            rr.setAvgscore(dfScore1);
                        }
                        rr.setEvaluationreportcode(er.getId());
                        resultReportService.insertSelective(rr);
                    }
                }
            }
        }
    }

    private Double getPostScore(List<ResultReport> rrList, String postType, String code, long count) {
        Double score = rrList.stream().filter(s -> s.getPostType().equals(postType) && s.getResultreportcode().equals(code)).mapToDouble(ResultReport::getScore).sum();
        return count == 0 ? 0 : score / count;
    }

    private Double getAvgScore(List<ResultReport> rrList, String code, long count) {
        Double score = rrList.stream().filter(s -> s.getResultreportcode().equals(code)).mapToDouble(ResultReport::getScore).sum();
        return count == 0 ? 0 : score / count;
    }

    private void updateOrInsertResultReportMb(List<ResultReport> rrList, String year, String month, Double avgMbKzrScore, Double avgMbHszScore, Double avgMbXzScore, Integer mbMaxCount, String dbtype) {
        EvaluationReport query = new EvaluationReport();
        query.setYear(year);
        query.setMonth(month);
        query.setDbtype(dbtype);
        List<EvaluationReport> evaluationReportList = evaluationReportService.selectEvaluationReportList(query);

        // PostType 1、科主任 2、护士长 3、行政
        // Resultreportcode 重点目标
        Double jsMbScore = 0.0;
        for (ResultReport rr : rrList) {
            jsMbScore = 0.0;
            if (rr.getPostType().equals("1") && rr.getResultreportcode().equals("3")) {
                jsMbScore = (mbMaxCount - rr.getMbCount()) * avgMbKzrScore;
                rr.setSumMbAvgScore(jsMbScore);
            }
            if (rr.getPostType().equals("2") && rr.getResultreportcode().equals("3")) {
                jsMbScore = (mbMaxCount - rr.getMbCount()) * avgMbHszScore;
                rr.setSumMbAvgScore(jsMbScore);
            }
            if (rr.getPostType().equals("3") && rr.getResultreportcode().equals("3")) {
                jsMbScore = (mbMaxCount - rr.getMbCount()) * avgMbXzScore;
                rr.setSumMbAvgScore(jsMbScore);
            }
            rr.setScore(rr.getScore() + jsMbScore);
        }
        long count1 = rrList.stream().filter(s -> s.getPostType().equals("1") && s.getResultreportcode().equals("0")).count();
        Double jcScore1 = this.getPostScore(rrList, "1", "0", count1);
        Double gwScore1 = this.getPostScore(rrList, "1", "1", count1);
        Double zdScore1 = this.getPostScore(rrList, "1", "2", count1);
        Double mbScore1 = this.getPostScore(rrList, "1", "3", count1);
        Double dfScore1 = this.getPostScore(rrList, "1", "10", count1);
        long count2 = rrList.stream().filter(s -> s.getPostType().equals("2") && s.getResultreportcode().equals("0")).count();
        Double jcScore2 = this.getPostScore(rrList, "2", "0", count2);
        Double gwScore2 = this.getPostScore(rrList, "2", "1", count2);
        Double zdScore2 = this.getPostScore(rrList, "2", "2", count2);
        Double mbScore2 = this.getPostScore(rrList, "2", "3", count2);
        Double dfScore2 = this.getPostScore(rrList, "2", "10", count2);
        long count3 = rrList.stream().filter(s -> s.getPostType().equals("3") && s.getResultreportcode().equals("0")).count();
        Double jcScore3 = this.getPostScore(rrList, "3", "0", count3);
        Double gwScore3 = this.getPostScore(rrList, "3", "1", count3);
        Double zdScore3 = this.getPostScore(rrList, "3", "2", count3);
        Double mbScore3 = this.getPostScore(rrList, "3", "3", count3);
        Double dfScore3 = this.getPostScore(rrList, "3", "10", count3);

        if (evaluationReportList.size() > 0) {
            List<ResultReport> resultReportList = resultReportService.selectResultReportList(year, month, dbtype);
            List<ResultReport> rrQueryList = new ArrayList<>();
            if (resultReportList.size() > 0) {
                List<ResultReport> rList = new ArrayList<>();
                for (EvaluationReport er : evaluationReportList) {
                    rrQueryList = rrList.stream().filter(s -> s.getUserCode().equals(er.getUsercode())).collect(Collectors.toList());
                    for (ResultReport rr : rrQueryList) {
                        if (rr.getPostType().equals("1")) {
                            if (rr.getResultreportcode().equals("0")) {
                                rr.setAvgscore(jcScore1);
                            } else if (rr.getResultreportcode().equals("1")) {
                                rr.setAvgscore(gwScore1);
                            } else if (rr.getResultreportcode().equals("2")) {
                                rr.setAvgscore(zdScore1);
                            } else if (rr.getResultreportcode().equals("3")) {
                                rr.setAvgscore(mbScore1);
                            } else if (rr.getResultreportcode().equals("10")) {
                                rr.setAvgscore(dfScore1);
                            }
                        } else if (rr.getPostType().equals("2")) {
                            if (rr.getResultreportcode().equals("0")) {
                                rr.setAvgscore(jcScore2);
                            } else if (rr.getResultreportcode().equals("1")) {
                                rr.setAvgscore(gwScore2);
                            } else if (rr.getResultreportcode().equals("2")) {
                                rr.setAvgscore(zdScore2);
                            } else if (rr.getResultreportcode().equals("3")) {
                                rr.setAvgscore(mbScore2);
                            } else if (rr.getResultreportcode().equals("10")) {
                                rr.setAvgscore(dfScore2);
                            }
                        } else if (rr.getPostType().equals("3")) {
                            if (rr.getResultreportcode().equals("0")) {
                                rr.setAvgscore(jcScore3);
                            } else if (rr.getResultreportcode().equals("1")) {
                                rr.setAvgscore(gwScore3);
                            } else if (rr.getResultreportcode().equals("2")) {
                                rr.setAvgscore(zdScore3);
                            } else if (rr.getResultreportcode().equals("3")) {
                                rr.setAvgscore(mbScore3);
                            } else if (rr.getResultreportcode().equals("10")) {
                                rr.setAvgscore(dfScore3);
                            }
                        }

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
                        if (rr.getPostType().equals("1")) {
                            if (rr.getResultreportcode().equals("0")) {
                                rr.setAvgscore(jcScore1);
                            } else if (rr.getResultreportcode().equals("1")) {
                                rr.setAvgscore(gwScore1);
                            } else if (rr.getResultreportcode().equals("2")) {
                                rr.setAvgscore(zdScore1);
                            } else if (rr.getResultreportcode().equals("3")) {
                                rr.setAvgscore(mbScore1);
                            } else if (rr.getResultreportcode().equals("10")) {
                                rr.setAvgscore(dfScore1);
                            }
                        } else if (rr.getPostType().equals("2")) {
                            if (rr.getResultreportcode().equals("0")) {
                                rr.setAvgscore(jcScore2);
                            } else if (rr.getResultreportcode().equals("1")) {
                                rr.setAvgscore(gwScore2);
                            } else if (rr.getResultreportcode().equals("2")) {
                                rr.setAvgscore(zdScore2);
                            } else if (rr.getResultreportcode().equals("3")) {
                                rr.setAvgscore(mbScore2);
                            } else if (rr.getResultreportcode().equals("10")) {
                                rr.setAvgscore(dfScore2);
                            }
                        } else if (rr.getPostType().equals("3")) {
                            if (rr.getResultreportcode().equals("0")) {
                                rr.setAvgscore(jcScore3);
                            } else if (rr.getResultreportcode().equals("1")) {
                                rr.setAvgscore(gwScore3);
                            } else if (rr.getResultreportcode().equals("2")) {
                                rr.setAvgscore(zdScore3);
                            } else if (rr.getResultreportcode().equals("3")) {
                                rr.setAvgscore(mbScore3);
                            } else if (rr.getResultreportcode().equals("10")) {
                                rr.setAvgscore(dfScore3);
                            }
                        }

                        resultReportService.insertSelective(rr);
                    }
                }
            }
        }
    }


    private void getResultReport(String code, String name, ScoreHistory item, double A, double B, double C, double D,
                                 double E, double F, double sum, Integer mbCount, List<ResultReport> list) {
        ResultReport rr = new ResultReport();
        rr.setResultreportcode(code);
        rr.setResultreportname(name);
        rr.setUserCode(item.getUsercode());
        rr.setAscore(A);
        rr.setBscore(B);
        rr.setCscore(C);
        rr.setDscore(D);
        rr.setEscore(E);
        rr.setFscore(F);
        rr.setScore(sum);
        rr.setMbCount(mbCount);
        rr.setPostType(item.getPostType());
        list.add(rr);
    }

}
