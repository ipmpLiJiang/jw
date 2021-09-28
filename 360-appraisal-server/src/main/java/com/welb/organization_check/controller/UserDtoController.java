package com.welb.organization_check.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserScoreDto;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.info.Info;
import com.welb.organization_check.service.*;
import com.welb.util.*;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
@RequestMapping("/history")
public class UserDtoController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    IUserDtoService dtoService;
    @Resource
    IMonthSummaryService summaryService;
    @Resource
    IStationService stationService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IScoreHistoryService historyService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IUserService userService;
    @Resource
    IScoreDetailService scoreDetailService;
    @Resource
    IDutyService dutyService;
    @Resource
    IScoreService scoreService;
    @Autowired
    IUserScoreDtoService userScoreDtoService;
    @Autowired
    IScoreDutySmService scoreDutySmService;
    /**
     * 历史评分汇总查询列表 查询所有年份的所有年度
     *
     * @param dto
     * @param pageNum
     * @param pageSize
     * @return
     */

  /*  @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectUserDtoLike(HttpServletRequest req, UserDto dto, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<UserDto> userDtoList;
            try {
                getCurrentgetGradeState(state);
                PageHelper.startPage(pageNum, pageSize);
                userDtoList = dtoService.selectUserDtoLike(dto);
                getStationName(userDtoList);
                PageInfo<UserDto> pageInfo = new PageInfo<>(userDtoList);
                userDtoList = pageInfo.getList();
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "查询历史评分成功");
                map.put("data", userDtoList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "查询历史评分失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }
*/

    /**
     * 评分汇总查询列表数据  ；当前年份的上一月度
     *
     * @param dto
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/scorelist", produces = "application/json;charset=utf-8")
    public Object selectUserDtoLikeByUserAndState(HttpServletRequest req, UserDto dto, int pageNum, int pageSize, String dbtype) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            try {
//                //当前年份
//                String year = CalendarUtil.getYear();
//                //当前月份
//                String month = CalendarUtil.getMonth();
//                //获取当前月度
//                String quarter = month;// CalendarUtil.getQuarter(month);
//                int counts = Integer.parseInt(quarter.trim()) - 1;
//                //获取当前系统时间
//                String sysTime = DateUtil.getTime();

                //手动考核-查看所有月节总结
                manualSelectUserDtoLikeByUserAndState(dto, map, dbtype, pageNum, pageSize);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询评分汇总列表失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void manualSelectUserDtoLikeByUserAndState(UserDto dto, ModelMap map, String dbtype, int pageNum, int pageSize) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
        if (setTime != null) {
            //开始新的月度考核

            List<User> users = new ArrayList<>();
            long count = 0;
            List<User> scorringUserList = userService.selectUserPfr(dbtype);
            if(scorringUserList.size()>0) {
                if (dbtype.equals("2")) {
                    List<String> roleList = new ArrayList<>();
                    roleList.add("300"); //普通用户
                    roleList.add("150"); //打分用户
                    users = userService.findUserByRoleCode(roleList, dbtype);
                } else { //党部考核
                    List<String> roleList = new ArrayList<>();
                    roleList.add("100");
                    roleList.add("150");
                    roleList.add("200");
                    roleList.add("300");
                    roleList.add("50");
                    User user = new User();
                    user.setDbtype(dbtype);
                    users = userService.selectUserAll(user, roleList);
                }
                for (User u : scorringUserList) {
                    count = users.stream().filter(s -> s.getUsercode().equals(u.getUsercode())).count();
                    if (count == 0) {
                        users.add(u);
                    }
                }
                if (users.size() > 0) {
                    List<MonthSummary> summaryList = summaryService.selectSummaryListByYearAndMonth(setTime.getYear(), setTime.getMonth(), dbtype);
                    // summaryList =summaryList.stream().filter(p->p.getDbtype()!=null && p.getDbtype().equals(dbtype)).collect(Collectors.toList());
                    if (summaryList.size() != users.size()) {
                        addMonthSummary(setTime.getYear(), setTime.getMonth(), users, dbtype);

                    }
                    getUserDtos(dto, map, setTime.getYear(), setTime.getMonth(), pageNum, pageSize, dbtype);

                }
            }else{
                map.put("msg", "未创建评分人关系.");
                map.put("code", 1);
            }
        }
    }

    private void addMonthSummary(String year, String month, List<User> users, String dbtype) {
        List<MonthSummary> list = new ArrayList<>();
        for (User user : users) {
            String serialno = year + "-" + month + "-" + dbtype + "-" + user.getUsercode();
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

    private void getUserDtos(UserDto dto, ModelMap map, String year, String month, int pageNum, int pageSize, String dbtype) {
        List<UserDto> userDtos;
        dto.setYear(year);
        dto.setMonth(month);
        PageHelper.startPage(pageNum, pageSize);
        List<String> roleList = new ArrayList<>();

        if (dbtype.equals("2")) {
            roleList.add("300");
            userDtos = dtoService.selectUserDtoLike(dto, roleList);
        } else { //党部考核
            dto.setDbtype(dbtype);
            userDtos = dtoService.selectUserDtoLike(dto, roleList);
        }

        this.getShengCheng(year, month, dbtype);
        //getStationName(userDtos);
        PageInfo<UserDto> pageInfo = new PageInfo<>(userDtos);
        userDtos = pageInfo.getList();
        map.put("totalPages", pageInfo.getTotal());
        map.put("msg", "查询评分汇总列表成功");
        map.put("data", userDtos);
        map.put("code", 0);
    }

    private void getShengCheng(String year, String month, String dbtype) {
        UserDto query = new UserDto();
        query.setYear(year);
        query.setMonth(month);
        query.setDbtype(dbtype);
        List<UserDto> userDtoList = dtoService.selectUserByMonthSummaryList(query);
        List<UserDto> bdfrUserDboList = new ArrayList<>();
        //筛选出被打分人
        if (dbtype.equals("1")) {
            bdfrUserDboList = userDtoList.stream().filter(s -> s.getDbbk() != null && (
                    s.getDbbk().equals("3") || s.getDbbk().equals("4"))).collect(Collectors.toList());
        } else {
            bdfrUserDboList = userDtoList.stream().filter(s -> s.getRolecode().equals("300")).collect(Collectors.toList());
        }
        if (bdfrUserDboList.size() > 0) {
            List<ScoreHistory> bdfrHistoryList = historyService.selectHistoryByMonthSummaryList(year, month, dbtype);
            List<ScoreFlow> bdfrFlowList = flowService.selectFlowByMonthSummaryList(year, month, dbtype);
            List<ScoreDetail> bdfrDetailList = scoreDetailService.selectDetailByMonthSummaryList(year, month, dbtype);
            Duty dutyQuery = new Duty();
            dutyQuery.setDbtype(dbtype);
            List<Duty> dutyList = dutyService.selectDutyAll(dutyQuery);
            List<Score> scoreList = scoreService.findScoreAll(dbtype);
            ScoreDutySm queryDutySm  = new ScoreDutySm();
            queryDutySm.setYear(year);
            queryDutySm.setMonth(month);
            queryDutySm.setDbtype(dbtype);
            List<ScoreDutySm> dutySmList =  scoreDutySmService.selectScoreDutySmList(queryDutySm);

            // History
            List<ScoreHistory> insertHistoryList = new ArrayList<>();
            List<ScoreHistory> updateHistoryList = new ArrayList<>();
            List<ScoreHistory> historyList = new ArrayList<>();
            // Flow
            List<ScoreFlow> insertFlowList = new ArrayList<>();
            List<ScoreFlow> updateFlowList = new ArrayList<>();
            // Detail
            List<ScoreDetail> insertDetailList = new ArrayList<>();
            //DutySm
            List<ScoreDutySm> insertDutySmList = new ArrayList<>();
            List<String> scoreTypeList = new ArrayList<>();
            scoreTypeList.add("A");
            scoreTypeList.add("B");
            scoreTypeList.add("C");
            scoreTypeList.add("D");
            scoreTypeList.add("E");
            scoreTypeList.add("F");

            this.getShengChengResult(year, month, scoreTypeList, bdfrUserDboList, dutyList, scoreList, historyList,
                    bdfrHistoryList, bdfrFlowList, bdfrDetailList, insertFlowList, updateFlowList, insertHistoryList, updateHistoryList, insertDetailList,dutySmList,insertDutySmList, dbtype);

            if (insertHistoryList.size() > 0) {
                historyService.batchInsert(insertHistoryList);
            }
            if (updateHistoryList.size() > 0) {
                for (ScoreHistory history : updateHistoryList)
                    historyService.updateByPrimaryKeySelective(history);
            }
            if (insertFlowList.size() > 0) {
                flowService.batchInsert(insertFlowList);
            }
            if (updateFlowList.size() > 0) {
                for (ScoreFlow flow : updateFlowList)
                    flowService.updateByPrimaryKeySelective(flow);
            }
            if (insertDetailList.size() > 0) {
                scoreDetailService.batchInset(insertDetailList);
            }
            if(insertDutySmList.size() > 0) {
                scoreDutySmService.batchInset(insertDutySmList);
            }
        }
    }

    //需求更改此方面 不用
    private void getDutyShengChengResult(String year, String month, List<String> scoreTypeList, List<UserDto> bdfrUserDboList,
                                         List<Duty> dutyList, List<Duty> queryDutyList,
                                         List<Score> scoreList, List<Score> queryScoreList,
                                         List<ScoreHistory> historyList, List<ScoreHistory> bdfrHistoryList, List<ScoreHistory> queryHistoryList,
                                         List<ScoreFlow> bdfrFlowList, List<ScoreFlow> queryFlowList, List<ScoreFlow> queryHbFlowList,
                                         List<ScoreDetail> bdfrDetailList, List<ScoreDetail> queryDetailList,
                                         List<ScoreFlow> insertFlowList, List<ScoreFlow> updateFlowList,
                                         List<ScoreHistory> insertHistoryList, List<ScoreHistory> updateHistoryList,
                                         List<ScoreDetail> insertDetailList, String dbtype) {
        List<Score> queryScoreListD = new ArrayList<>();

        Double sumScore = 0.0;
        Double ratio = 0.0;
        long count = 0;
        for (UserDto dto : bdfrUserDboList) {
            queryDutyList = dutyList.stream().filter(s -> s.getStationcode().equals(dto.getStationcode())).collect(Collectors.toList());

            queryHistoryList = bdfrHistoryList.stream().filter(s -> s.getUsercode().equals(dto.getUsercode())).collect(Collectors.toList());
            if (queryHistoryList.size() == 0) {
                ScoreHistory history = new ScoreHistory();
                history.setUsercode(dto.getUsercode());
                history.setYear(year);
                history.setMonth(month);
                history.setScorestatus("1");
                history.setAscore(0.0);
                history.setBscore(0.0);
                history.setCscore(0.0);
                history.setDscore(0.0);
                history.setTotalscore(0.0);
                history.setDbtype(dbtype);
                historyList.add(history);
            } else {
                historyList.add(queryHistoryList.get(0));
            }
            if (queryDutyList.size() > 0) {
                for (String scoreType : scoreTypeList) {
                    queryScoreList = scoreList.stream().filter(s -> s.getScoretype().equals(scoreType) &&
                            s.getScorredcode().equals(dto.getUsercode())).collect(Collectors.toList());
                    if (queryScoreList.size() > 0) {
                        for (Score score : queryScoreList) {
                            count = 0;
                            queryFlowList = bdfrFlowList.stream().filter(s -> s.getMserialno().equals(dto.getSerialno()) &&
                                    s.getScoredcode().equals(score.getScorredcode()) &&
                                    s.getScorringcode().equals(score.getScorringcode()) &&
                                    s.getScoretype().equals(score.getScoretype())).collect(Collectors.toList());

                            count = insertFlowList.stream().filter(s -> s.getMserialno().equals(dto.getSerialno()) &&
                                    s.getScoredcode().equals(score.getScorredcode()) &&
                                    s.getScorringcode().equals(score.getScorringcode()) &&
                                    s.getScoretype().equals(score.getScoretype())).count();

                            queryScoreListD = scoreList.stream().filter(s -> s.getScorredcode().equals(score.getScorredcode()) &&
                                    s.getScorringcode().equals(score.getScorringcode()) &&
                                    s.getScoretype().equals("D") && s.getDutycode() != null).collect(Collectors.toList());

                            if (queryFlowList.size() == 0 && count == 0) {
                                ratio = 0.0;
                                ScoreFlow scoreFlow = new ScoreFlow();
                                scoreFlow.setSerialno(StringUtil.getGuid());
                                scoreFlow.setMserialno(dto.getSerialno());
                                scoreFlow.setScoredcode(score.getScorredcode());
                                scoreFlow.setScorringcode(score.getScorringcode());
                                scoreFlow.setScoretype(score.getScoretype());
                                if (scoreType.equals("A")) {
                                    ratio = dto.getAratio();
                                }
                                if (scoreType.equals("B")) {
                                    ratio = dto.getBratio();
                                }
                                if (scoreType.equals("C")) {
                                    ratio = dto.getCratio();
                                }
                                if (scoreType.equals("D")) {
                                    ratio = dto.getDratio();
                                }
                                scoreFlow.setRatio(ratio);
                                scoreFlow.setDbtype(dbtype);
                                sumScore = 0.0;

                                for (Duty duty : queryDutyList) {
                                    count = queryScoreListD.stream().filter(s -> s.getDutycode().equals(duty.getDutycode())).count();

                                    if ((!score.getScoretype().equals("D") && count == 0) ||
                                            (score.getScoretype().equals("D") && count > 0)) {
                                        ScoreDetail detail = new ScoreDetail();
                                        detail.setSerialNo(StringUtil.generatorId());
                                        detail.setFSerialNo(scoreFlow.getSerialno());
                                        detail.setDSerialNo(duty.getDutycode());
                                        detail.setScore(duty.getDefScore() == null ? "0" : duty.getDefScore().toString());
                                        detail.setScoretype(scoreType);
                                        detail.setRatio(scoreFlow.getRatio());
                                        detail.setDbtype(dbtype);
                                        insertDetailList.add(detail);
                                        sumScore += duty.getDefScore() == null ? 0 : duty.getDefScore();
                                    }
                                }
                                scoreFlow.setScore(sumScore);
                                insertFlowList.add(scoreFlow);

                            } else {
                                if (count == 0) {
                                    ScoreFlow updateFlow = queryFlowList.get(0);
                                    sumScore = 0.0;

                                    for (Duty duty : queryDutyList) {
                                        count = queryScoreListD.stream().filter(s -> s.getDutycode().equals(duty.getDutycode())).count();

                                        if ((!score.getScoretype().equals("D") && count == 0) || (score.getScoretype().equals("D") && count > 0)) {
                                            queryDetailList = bdfrDetailList.stream().filter(s -> s.getFSerialNo().equals(updateFlow.getSerialno()) &&
                                                    s.getDSerialNo().equals(duty.getDutycode())).collect(Collectors.toList());
                                            if (queryDetailList.size() == 0) {
                                                ScoreDetail detail = new ScoreDetail();
                                                detail.setSerialNo(StringUtil.generatorId());
                                                detail.setFSerialNo(updateFlow.getSerialno());
                                                detail.setDSerialNo(duty.getDutycode());
                                                detail.setScore(duty.getDefScore() == null ? "0" : duty.getDefScore().toString());
                                                detail.setScoretype(scoreType);
                                                detail.setRatio(updateFlow.getRatio());
                                                detail.setDbtype(dbtype);
                                                insertDetailList.add(detail);
                                                sumScore += duty.getDefScore() == null ? 0 : duty.getDefScore();
                                            }
                                        }
                                    }
                                    if (sumScore != 0.0) {
                                        updateFlow.setScore(updateFlow.getScore() + sumScore);
                                        updateFlowList.add(updateFlow);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        List<ScoreFlow> flowList = bdfrFlowList;
        if (insertFlowList.size() > 0) {
            flowList.addAll(insertFlowList);
        }
        List<ScoreDetail> detailList = bdfrDetailList;
        if (insertDetailList.size() > 0) {
            detailList.addAll(insertDetailList);
        }
        sumScore = 0.0;
        Double aScore = 0.0;
        Double bScore = 0.0;
        Double cScore = 0.0;
        Double dScore = 0.0;
        List<UserScoreDto> dutyAndRatioList = new ArrayList<>();
        for (ScoreHistory history : historyList) {
            dutyAndRatioList = new ArrayList<>();
            queryHbFlowList = flowList.stream().filter(s -> s.getScoredcode().equals(history.getUsercode())).collect(Collectors.toList());
            for (ScoreFlow flow : queryHbFlowList) {
                queryDetailList = detailList.stream().filter(s -> s.getFSerialNo().equals(flow.getSerialno())).collect(Collectors.toList());
                for (ScoreDetail detail : queryDetailList) {
                    UserScoreDto usd = new UserScoreDto();
                    usd.setDserialNo(detail.getDSerialNo());
                    usd.setScore(detail.getScore() == null ? 0.0 : Double.parseDouble(detail.getScore()));
                    usd.setRatio(detail.getRatio());
                    usd.setScoreType(detail.getScoretype());
                    dutyAndRatioList.add(usd);
                }
            }
            dutyAndRatioList = userScoreDtoService.getTypeUserDutyScore(dutyAndRatioList, false);
            sumScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getScore).sum();
            aScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getAscore).sum();
            bScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getBscore).sum();
            cScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getCscore).sum();
            dScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getDscore).sum();

            history.setTotalscore(sumScore);
            history.setAscore(aScore);
            history.setBscore(bScore);
            history.setCscore(cScore);
            history.setDscore(dScore);

            if (history.getId() == null) {
                insertHistoryList.add(history);
            } else {
                updateHistoryList.add(history);
            }
            sumScore = 0.0;
            aScore = 0.0;
            bScore = 0.0;
            cScore = 0.0;
            dScore = 0.0;
        }
    }


    private void getShengChengResult(String year, String month, List<String> scoreTypeList, List<UserDto> bdfrUserDboList,
                                     List<Duty> dutyList,  List<Score> scoreList,
                                     List<ScoreHistory> historyList, List<ScoreHistory> bdfrHistoryList,
                                     List<ScoreFlow> bdfrFlowList, List<ScoreDetail> bdfrDetailList,
                                     List<ScoreFlow> insertFlowList, List<ScoreFlow> updateFlowList,
                                     List<ScoreHistory> insertHistoryList, List<ScoreHistory> updateHistoryList,
                                     List<ScoreDetail> insertDetailList,List<ScoreDutySm> dutySmList,List<ScoreDutySm> insertDutySmList, String dbtype) {
        // 查询
        List<Duty> queryDutyList = new ArrayList<>();
        List<Score> queryScoreList = new ArrayList<>();
        List<ScoreHistory> queryHistoryList = new ArrayList<>();
        List<ScoreFlow> queryFlowList = new ArrayList<>();
        List<ScoreFlow> queryHbFlowList = new ArrayList<>();
        List<ScoreDetail> queryDetailList = new ArrayList<>();
        List<ScoreDutySm> queryDutySmList = new ArrayList<>();

        Double sumScore = 0.0;
        Double ratio = 0.0;
        boolean isDutySm = false;
        for (UserDto dto : bdfrUserDboList) {
            if(dbtype.equals("1")) {
                queryDutyList = dutyList.stream().filter(s -> s.getDbbk().equals(dto.getDbbk())).collect(Collectors.toList());
            } else {
                queryDutyList = dutyList.stream().filter(s -> s.getStationcode().equals(dto.getStationcode())).collect(Collectors.toList());
            }
            isDutySm = false;
            queryHistoryList = bdfrHistoryList.stream().filter(s -> s.getUsercode().equals(dto.getUsercode())).collect(Collectors.toList());
            if (queryHistoryList.size() == 0) {
                ScoreHistory history = new ScoreHistory();
                history.setUsercode(dto.getUsercode());
                history.setYear(year);
                history.setMonth(month);
                history.setScorestatus("1");
                history.setAscore(0.0);
                history.setBscore(0.0);
                history.setCscore(0.0);
                history.setDscore(0.0);
                history.setEscore(0.0);
                history.setFscore(0.0);
                history.setTotalscore(0.0);
                history.setDbtype(dbtype);
                historyList.add(history);
            } else {
                historyList.add(queryHistoryList.get(0));
            }

            if (queryDutyList.size() > 0) {
                queryDutySmList = dutySmList.stream().filter(s->s.getScorredcode().equals(dto.getUsercode())).collect(Collectors.toList());
                for (String scoreType : scoreTypeList) {
                    queryScoreList = scoreList.stream().filter(s -> s.getScoretype().equals(scoreType) &&
                            s.getScorredcode().equals(dto.getUsercode())).collect(Collectors.toList());
                    if (queryScoreList.size() > 0) {
                        for (Score score : queryScoreList) {
                            queryFlowList = bdfrFlowList.stream().filter(s -> s.getMserialno().equals(dto.getSerialno()) &&
                                    s.getScoredcode().equals(score.getScorredcode()) &&
                                    s.getScorringcode().equals(score.getScorringcode()) &&
                                    s.getScoretype().equals(score.getScoretype())).collect(Collectors.toList());
                            if (queryFlowList.size() == 0) {
                                ratio = 0.0;
                                ScoreFlow scoreFlow = new ScoreFlow();
                                scoreFlow.setSerialno(StringUtil.getGuid());
                                scoreFlow.setMserialno(dto.getSerialno());
                                scoreFlow.setScoredcode(score.getScorredcode());
                                scoreFlow.setScorringcode(score.getScorringcode());
                                scoreFlow.setScoretype(score.getScoretype());
                                if (scoreType.equals("A")) {
                                    ratio = dto.getAratio();
                                }
                                if (scoreType.equals("B")) {
                                    ratio = dto.getBratio();
                                }
                                if (scoreType.equals("C")) {
                                    ratio = dto.getCratio();
                                }
                                if (scoreType.equals("D")) {
                                    ratio = dto.getDratio();
                                }
                                if (scoreType.equals("E")) {
                                    ratio = dto.getEratio();
                                }
                                if (scoreType.equals("F")) {
                                    ratio = dto.getFratio();
                                }
                                scoreFlow.setRatio(ratio);
                                scoreFlow.setDbtype(dbtype);
                                sumScore = 0.0;
                                for (Duty duty : queryDutyList) {
                                    ScoreDetail detail = new ScoreDetail();
                                    detail.setSerialNo(StringUtil.generatorId());
                                    detail.setFSerialNo(scoreFlow.getSerialno());
                                    detail.setDSerialNo(duty.getDutycode());
                                    detail.setScore(duty.getDefScore() == null ? "0" : duty.getDefScore().toString());
                                    detail.setDbtype(dbtype);
                                    insertDetailList.add(detail);
                                    sumScore += duty.getDefScore() == null ? 0 : duty.getDefScore();

                                    if(queryDutySmList.size()==0 && !isDutySm) {
                                        ScoreDutySm dutySm = new ScoreDutySm();
                                        dutySm.setYear(year);
                                        dutySm.setMonth(month);
                                        dutySm.setDutycode(duty.getDutycode());
                                        dutySm.setScorredcode(dto.getUsercode());
                                        dutySm.setZpsm("无");
                                        dutySm.setDbtype(dbtype);
                                        insertDutySmList.add(dutySm);
                                    }
                                }
                                scoreFlow.setScore(sumScore);
                                insertFlowList.add(scoreFlow);
                            } else {
                                ScoreFlow updateFlow = queryFlowList.get(0);
                                sumScore = 0.0;
                                for (Duty duty : queryDutyList) {
                                    queryDetailList = bdfrDetailList.stream().filter(s -> s.getFSerialNo().equals(updateFlow.getSerialno()) &&
                                            s.getDSerialNo().equals(duty.getDutycode())).collect(Collectors.toList());
                                    if (queryDetailList.size() == 0) {
                                        ScoreDetail detail = new ScoreDetail();
                                        detail.setSerialNo(StringUtil.generatorId());
                                        detail.setFSerialNo(updateFlow.getSerialno());
                                        detail.setDSerialNo(duty.getDutycode());
                                        detail.setScore(duty.getDefScore() == null ? "0" : duty.getDefScore().toString());
                                        detail.setDbtype(dbtype);
                                        insertDetailList.add(detail);
                                        sumScore += duty.getDefScore() == null ? 0 : duty.getDefScore();

                                        if(queryDutySmList.size()==0 && !isDutySm) {
                                            ScoreDutySm dutySm = new ScoreDutySm();
                                            dutySm.setYear(year);
                                            dutySm.setMonth(month);
                                            dutySm.setDutycode(duty.getDutycode());
                                            dutySm.setScorredcode(dto.getUsercode());
                                            dutySm.setZpsm("无");
                                            dutySm.setDbtype(dbtype);
                                            insertDutySmList.add(dutySm);
                                        }
                                    }
                                }
                                if (sumScore != 0.0) {
                                    updateFlow.setScore(updateFlow.getScore() + sumScore);
                                    updateFlowList.add(updateFlow);
                                }
                            }
                            isDutySm = true;
                        }
                    }
                }
            }
        }

        List<ScoreFlow> flowList = new ArrayList<>();
        if (insertFlowList.size() > 0) {
            flowList.addAll(insertFlowList);
        }
        if (updateFlowList.size() > 0) {
            flowList.addAll(updateFlowList);
        }
        long count = 0;
        for (ScoreFlow flow : bdfrFlowList) {
            count = flowList.stream().filter(s -> s.getMserialno().equals(flow.getMserialno()) &&
                    s.getScorringcode().equals(flow.getScorringcode())).count();
            if (count == 0)
                flowList.add(flow);
        }

        Double ARatio = 0.0;
        Double BRatio = 0.0;
        Double CRatio = 0.0;
        Double DRatio = 0.0;
        Double ERatio = 0.0;
        Double FRatio = 0.0;
        Double AScore = 0.0;
        Double BScore = 0.0;
        Double CScore = 0.0;
        Double DScore = 0.0;
        Double EScore = 0.0;
        Double FScore = 0.0;

        Double totalScore = 0.0;
        Double totalRatio = 0.0;
        for (ScoreHistory h : historyList) {
            count = flowList.stream().filter(s -> s.getScoredcode().equals(h.getUsercode())).count();
            if (count > 0) {
                for (String scoreType : scoreTypeList) {
                    queryHbFlowList = flowList.stream().filter(s -> s.getScoredcode().equals(h.getUsercode()) &&
                            s.getScoretype().equals(scoreType)).collect(Collectors.toList());
                    if (queryHbFlowList.size() > 0) {
                        ratio = queryHbFlowList.get(0).getRatio();
                        sumScore = queryHbFlowList.stream().mapToDouble(ScoreFlow::getScore).sum();
                        if (scoreType.equals("A")) {
                            ARatio = ratio;
                            AScore = sumScore == 0 ? 0.0 : sumScore / queryHbFlowList.size();
                        }
                        if (scoreType.equals("B")) {
                            BRatio = ratio;
                            BScore = sumScore == 0 ? 0.0 : sumScore / queryHbFlowList.size();
                        }
                        if (scoreType.equals("C")) {
                            CRatio = ratio;
                            CScore = sumScore == 0 ? 0.0 : sumScore / queryHbFlowList.size();
                        }
                        if (scoreType.equals("D")) {
                            DRatio = ratio;
                            DScore = sumScore == 0 ? 0.0 : sumScore / queryHbFlowList.size();
                        }
                        if (scoreType.equals("E")) {
                            ERatio = ratio;
                            EScore = sumScore == 0 ? 0.0 : sumScore / queryHbFlowList.size();
                        }
                        if (scoreType.equals("F")) {
                            FRatio = ratio;
                            FScore = sumScore == 0 ? 0.0 : sumScore / queryHbFlowList.size();
                        }
                        totalRatio += ratio;
                    }
                }
                h.setAscore(AScore);
                h.setBscore(BScore);
                h.setCscore(CScore);
                h.setDscore(DScore);
                h.setEscore(EScore);
                h.setFscore(FScore);
                totalScore = (AScore == 0 ? 0 : AScore * ARatio / totalRatio) + (BScore == 0 ? 0 : BScore * BRatio / totalRatio) +
                        (CScore == 0 ? 0 : CScore * CRatio / totalRatio) + (DScore == 0 ? 0 : DScore * DRatio / totalRatio) +
                        (EScore == 0 ? 0 : EScore * ERatio / totalRatio) + (FScore == 0 ? 0 : FScore * FRatio / totalRatio);
                h.setTotalscore(totalScore);
                if (h.getId() == null) {
                    insertHistoryList.add(h);
                } else {
                    updateHistoryList.add(h);
                }
                totalRatio = 0.0;
                ARatio = 0.0;
                BRatio = 0.0;
                CRatio = 0.0;
                DRatio = 0.0;
                ERatio = 0.0;
                FRatio = 0.0;
                AScore = 0.0;
                BScore = 0.0;
                CScore = 0.0;
                DScore = 0.0;
                EScore = 0.0;
                FScore = 0.0;
            }
        }
    }

    private void automaticSelectUserDtoLikeByUserAndState(UserDto dto, ModelMap map, String year, int counts, int pageNum, int pageSize) {
        String month;
        if (counts == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";

        } else {
            month = String.valueOf(counts);
        }
        // getUserDtos(dto, map, year, month, pageNum, pageSize);
    }

    /**
     * 获取人员相关信息 如岗位 部门 分数
     *
     * @param userDtoList
     */
    private void getStationName(List<UserDto> userDtoList, String dbtype) {
        //保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            for (UserDto userDto : userDtoList) {
                if (!userDto.getRolecode().equals("150")) {
                    Double totalRatio = 0.0;
                    Double AScore = flowService.getTypeAvgScore(userDto.getSerialno(), "A", dbtype);
                    if (AScore == null) {
                        AScore = 0.0;
                        userDto.setAScore(AScore);
                    } else {
                        userDto.setAScore(Double.parseDouble(df.format(AScore)));
                        totalRatio += userDto.getAratio();
                    }
                    Double BScore = flowService.getTypeAvgScore(userDto.getSerialno(), "B", dbtype);
                    if (BScore == null) {
                        BScore = 0.0;
                        userDto.setBScore(BScore);
                    } else {
                        userDto.setBScore(Double.parseDouble(df.format(BScore)));
                        totalRatio += userDto.getBratio();
                    }
                    Double CScore = flowService.getTypeAvgScore(userDto.getSerialno(), "C", dbtype);
                    if (CScore == null) {
                        CScore = 0.0;
                        userDto.setCScore(CScore);
                    } else {
                        userDto.setCScore(Double.parseDouble(df.format(CScore)));
                        totalRatio += userDto.getCratio();
                    }
                    Double DScore = flowService.getTypeAvgScore(userDto.getSerialno(), "D", dbtype);
                    if (DScore == null) {
                        DScore = 0.0;
                        userDto.setDScore(DScore);
                    } else {
                        userDto.setDScore(Double.parseDouble(df.format(DScore)));
                        totalRatio += userDto.getDratio();
                    }
                    //获取总分  总分= A类总分 X A类评分人系数 +  B类总分 X B类评分人系数 + C类总分 X C类评分人系数 + D类总分 X D类评分人系数

                    Double totalscore = userDto.getAScore() * userDto.getAratio() + userDto.getBScore() * userDto.getBratio() +
                            userDto.getCScore() * userDto.getCratio() + userDto.getDScore() * userDto.getDratio();
                    if (totalscore == 0.0 || totalscore == null) {
                        userDto.setTotalScore(0.0);
                    } else {
                        userDto.setTotalScore(Double.parseDouble(df.format(totalscore / totalRatio)));
                    }
                } else {
                    userDto.setAScore(0.0);
                    userDto.setBScore(0.0);
                    userDto.setCScore(0.0);
                    userDto.setDScore(0.0);
                    userDto.setTotalScore(0.0);
                }
            }

        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    /**
     * 批量月节评分状态修改
     *
     * @param serialnos
     * @return
     */
    @RequestMapping(value = "/updateFinishGradeBySerialNo", produces = "application/json;charset=utf-8")
    public Object updateFinishGradeBySerialNo(String serialnos) {
        ModelMap map = new ModelMap();
        String[] serialno = serialnos.split(",");

        int counts = 0;
        for (int i = 0; i < serialno.length; i++) {

            int count = summaryService.updateFinishGradeBySerialNo(serialno[i]);
            counts += count;
        }
        if (counts > 0) {
            map.put("msg", "批量修改季结评分完成状态成功");
            map.put("code", 0);
        } else {
            map.put("msg", "批量修改季结评分完成状态失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 将月节评分状态全部修改为月节评分
     *
     * @return
     */
    @RequestMapping(value = "/updateFinishGradeAll", produces = "application/json;charset=utf-8")
    public Object updateFinishGradeAll(String dbtype) {
        ModelMap map = new ModelMap();
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
        if(setTime!=null) {
            int count = summaryService.updateFinishGradeAll(setTime.getYear(), setTime.getMonth(), dbtype);
            if (count > 0) {
                map.put("msg", "全部修改季结评分完成成功");
                map.put("code", 0);
            } else {
                map.put("msg", "全部修改季结评分完成失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "全部修改季结评分完成失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 评分汇总导出excel表
     *
     * @param response
     */
    @RequestMapping(value = "exportScore", produces = "application/json;charset=utf-8")
    public void exportExcelData(HttpServletRequest req, HttpServletResponse response, UserDto dto, String info) throws ParseException {
        String state = (String) req.getSession().getAttribute("state");
        // 定义表的标题
        String title = "评分汇总-员工列表";
        //定义表的列名
        String[] rowsName = new String[]{"序号", "用户姓名", "发薪号", "所属部门", "所属岗位", "季结状态", "A", "B", "C", "D", "总分"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs = null;
        //json字符串转对象
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        dto.setDbtype(info1.getDbtype());
        //当前年份
        String year = CalendarUtil.getYear();
        //当前月份
        String month = CalendarUtil.getMonth();
        //获取当前月度
        String quarter = month;//CalendarUtil.getQuarter(month);
        int counts = Integer.parseInt(quarter.trim()) - 1;
        //获取当前系统时间
        String sysTime = DateUtil.getTime();

        //手动考核-查看所有月节总结
        manualExportExcelData(dto, rowsName, dataList, year, quarter, counts, sysTime);

        try {
            // 创建ExportExcel对象
            ExcelUtil excelUtil = new ExcelUtil();
            String fileName = new String("评分汇总文档.xls".getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
            excelUtil.exportExcel(title, rowsName, dataList, fileName, response);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
        }
    }

    @RequestMapping(value = "exportHistoryGrade", produces = "application/json;charset=utf-8")
    public void exportHistoryGrade(HttpServletRequest req, HttpServletResponse response) throws ParseException {
        String state = (String) req.getSession().getAttribute("state");
        // 定义表的标题
        String title = "评分汇总-员工列表";
        //定义表的列名
        String[] rowsName = new String[]{"序号", "用户姓名", "发薪号", "所属部门", "所属岗位", "季结状态", "A", "B", "C", "D", "总分"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs;
        for (int i = 0; i < 2; i++) {
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = "1" + i;
            ;
            objs[2] = "1" + i;
            ;
            objs[3] = "1" + i;
            ;
            objs[4] = "1" + i;
            ;
            objs[5] = "1" + i;
            ;
            objs[6] = "1" + i;
            ;
            objs[7] = "1" + i;
            ;
            objs[8] = "1" + i;
            ;
            objs[9] = "1" + i;
            ;
            objs[10] = "1" + i;
            ;
            dataList.add(objs);
        }
        try {
            // 创建ExportExcel对象
            ExcelUtil excelUtil = new ExcelUtil();
            String fileName = new String("评分汇总文档.xls".getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
            excelUtil.exportExcel(title, rowsName, dataList, fileName, response);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
        }
    }

    private void manualExportExcelData(UserDto dto, String[] rowsName, List<Object[]> dataList, String year, String quarter, int counts, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dto.getDbtype());
        if (setTime != null) {

            //开始新的月度考核
            month = quarter;
            getUserDtos(dto, rowsName, dataList, setTime.getYear(), setTime.getMonth());

        }
    }

    private void getUserDtos(UserDto dto, String[] rowsName, List<Object[]> dataList, String year, String month) {
        List<UserDto> userDtos;
        dto.setYear(year);
        dto.setMonth(month);
        List<String> roleList = new ArrayList<>();
        roleList.add("100");
        roleList.add("200");
        roleList.add("300");
        roleList.add("50");
        userDtos = dtoService.selectUserDtoLike(dto, roleList);
        // 获取人员相关信息 如岗位 部门 分数
        getStationName(userDtos, dto.getDbtype());
        //获取评分汇总数据
        getUserDtosInfo(userDtos, rowsName, dataList);
    }

    private void automaticExportExcelData(UserDto dto, String[] rowsName, List<Object[]> dataList, String year, int counts) {
        String month;
        List<UserDto> userDtos;
        if (counts == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
        } else {
            month = String.valueOf(counts);
        }
        getUserDtos(dto, rowsName, dataList, year, month);
    }

    /**
     * 获取评分汇总数据
     *
     * @param userDtos
     * @param rowsName
     * @param dataList
     */
    private void getUserDtosInfo(List<UserDto> userDtos, String[] rowsName, List<Object[]> dataList) {
        Object[] objs;
        for (int i = 0; i < userDtos.size(); i++) {
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = userDtos.get(i).getUsername();
            objs[2] = userDtos.get(i).getMoneycard();
            objs[3] = userDtos.get(i).getDepartmentname();
            objs[4] = userDtos.get(i).getStationname();
            objs[5] = userDtos.get(i).getStatename();
            objs[6] = userDtos.get(i).getAScore();
            objs[7] = userDtos.get(i).getBScore();
            objs[8] = userDtos.get(i).getCScore();
            objs[9] = userDtos.get(i).getDScore();
            objs[10] = userDtos.get(i).getTotalScore();
            dataList.add(objs);
        }
    }

   /* @RequestMapping(value = "/exportHistoryScore", produces = "application/json;charset=utf-8")
    public void exportExcelData1(HttpServletResponse response, UserDto dto, String info) {
        List<UserDto> userDtos;
        //json字符串转对象
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        userDtos = dtoService.selectUserDtoLike(dto);
        getStationName(userDtos);
        // 创建ExportExcel对象
        try {
            // 獲取工作表
            Workbook workbook = exportBigDataExcel(userDtos);
            // 完成下載
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            downFile(os, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook exportBigDataExcel(List<UserDto> employeeList) {
        // 1.获取数据
        // 2.创建工作簿
        // 阈值，内存中的对象数量最大值，超过这个值会生成一个临时文件存放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("历史评分汇总");
        String[] titles = {"序号", "用户姓名", "发薪号", "所属部门", "所属岗位", "打分状态", "月结状态", "月节月度", "A", "B", "C", "D", "总分"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.从集合中取数据并赋值
        for (int i = 0; i < employeeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            UserDto employee = employeeList.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(employee.getUsername());
            row.createCell(2).setCellValue(employee.getMoneycard());
            row.createCell(3).setCellValue(employee.getDepartmentname());
            row.createCell(4).setCellValue(employee.getStationname());
            row.createCell(5).setCellValue(employee.getScorestatusname());
            row.createCell(6).setCellValue(employee.getStatename());
            row.createCell(7).setCellValue(employee.getYear() + "-" + employee.getMonth());
            if (employee.getAScore() == null) {
                employee.setAScore(0.0);
            }
            if (employee.getBScore() == null) {
                employee.setBScore(0.0);
            }
            if (employee.getCScore() == null) {
                employee.setCScore(0.0);
            }
            if (employee.getDScore() == null) {
                employee.setDScore(0.0);
            }
            if (employee.getTotalScore() == null) {
                employee.setTotalScore(0.0);
            }

            row.createCell(8).setCellValue(employee.getAScore());
            row.createCell(9).setCellValue(employee.getBScore());
            row.createCell(10).setCellValue(employee.getCScore());
            row.createCell(11).setCellValue(employee.getDScore());
            row.createCell(12).setCellValue(employee.getTotalScore());
        }
        return wb;

    }

    private void downFile(ByteArrayOutputStream os, HttpServletResponse response) throws IOException {
        String fileName = "历史评分汇总.xls";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }*/



   /* private void getCurrentgetGradeState(String state) {
        try {
            UserDto userDto = new UserDto();
            String year = CalendarUtil.getYear();
            String month = CalendarUtil.getMonth();
            String quarter = CalendarUtil.getQuarter(month);
            int i = Integer.parseInt(quarter) - 1;
            //获取当前系统时间
            String sysTime = DateUtil.getTime();
            if (state.equals("1")) {
                //手动考核
                manualGetGrade(userDto, year, quarter, i, sysTime);

            } else {
                //自动考核
                automaticgetGrage(userDto, year, i);
            }
            List<UserDto> userDtos = dtoService.gradeList(userDto);
            getGradeState(userDtos);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
        }

    }

    private void manualGetGrade(UserDto userDto, String year, String quarter, int i, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的月度考核
                month = quarter;
                userDto.setMonth(month);
                userDto.setYear(year);
            } else {
                //未到达指定考核时间，仍展示上一月度数据
                automaticgetGrage(userDto, year, i);
            }
        }else {
            if (i == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "4";
            } else {
                month = String.valueOf(i);
            }
            userDto.setYear(year);
            userDto.setMonth(month);
        }
    }*/

    private void automaticgetGrage(UserDto userDto, String year, int i) {
        String month;
        if (i == 0) {
            int lastYear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastYear);
            month = "12";
        } else {
            month = String.valueOf(i);
        }
        userDto.setYear(year);
        userDto.setMonth(month);
    }


   /* @RequestMapping(value = "/gradeList", produces = "application/json;charset=utf-8")
    public Object gradeList(HttpServletRequest req, UserDto dto, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<UserDto> userDtoList;
            try {
                getCurrentgetGradeState(state);
                PageHelper.startPage(pageNum, pageSize);
                userDtoList = dtoService.gradeList(dto);
//                getGradeState(userDtoList);
                PageInfo<UserDto> pageInfo = new PageInfo<>(userDtoList);
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "查询成功");
                map.put("data", userDtoList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }

    private void getGradeState(List<UserDto> userDtoList) {
        List<MonthSummary> list = new ArrayList<>();
        for (UserDto userDto : userDtoList) {
            MonthSummary summary = new MonthSummary();
            summary.setSerialno(userDto.getSerialno());
            userDto.setEmployeecode(userDto.getUsercode());
            int dtoTotalCount = dtoService.getTotalCount(userDto);
            String mserialno = userDto.getYear() + "-" + userDto.getMonth();
            int flowTotalCount = flowService.getTotalCount(mserialno, userDto.getUsercode());
            if (flowTotalCount == 0) {
                summary.setScorestatus("1");
                list.add(summary);
            } else if (flowTotalCount < dtoTotalCount) {
                summary.setScorestatus("2");
                list.add(summary);
            } else {
                summary.setScorestatus("3");
                list.add(summary);
            }
        }
        if (list.size() > 0) {
            summaryService.batchUpdate(list);
        }

    }
*/
/*
    @RequestMapping(value = "/exportGradeExcel", produces = "application/json;charset=utf-8")
    public void exportGradeExcel(HttpServletResponse response, UserDto dto, String info) {
        List<UserDto> userDtos;
        //json字符串转对象
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        userDtos = dtoService.gradeList(dto);
        getStationName(userDtos);
        // 创建ExportExcel对象
        try {
            // 獲取工作表
            Workbook workbook = exportGradeBigDataExcel(userDtos);
            // 完成下載
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            gradeDownFile(os, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private SXSSFWorkbook exportGradeBigDataExcel(List<UserDto> employeeList) {
        // 1.获取数据
        // 2.创建工作簿
        // 阈值，内存中的对象数量最大值，超过这个值会生成一个临时文件存放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("历史评分汇总");
        String[] titles = {"序号", "用户姓名", "发薪号", "所属部门", "所属岗位", "打分状态", "打分月度"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.从集合中取数据并赋值
        for (int i = 0; i < employeeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            UserDto employee = employeeList.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(employee.getUsername());
            row.createCell(2).setCellValue(employee.getMoneycard());
            row.createCell(3).setCellValue(employee.getDepartmentname());
            row.createCell(4).setCellValue(employee.getStationname());
            row.createCell(5).setCellValue(employee.getScorestatusname());
            row.createCell(6).setCellValue(employee.getYear() + "-" + employee.getMonth());
        }
        return wb;

    }

    private void gradeDownFile(ByteArrayOutputStream os, HttpServletResponse response) throws IOException {
        String fileName = "打分用户完成情况汇总.xls";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }*/


    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectUserDtoLike(HttpServletRequest req, ScoreHistory history, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<ScoreHistory> historieList;
            try {
                List<String> roleList = new ArrayList<>();
                if (history.getDbtype().equals("2")) {
                    if(history.getRolecode() == null || history.getRolecode().equals("")) {
                        roleList.add("300"); //普通用户
                        roleList.add("150"); //打分用户
                    } else {
                        if(history.getRolecode() != null && history.getRolecode().equals("150")) {
                            roleList.add("150"); //打分用户
                        }
                        if(history.getRolecode() != null && history.getRolecode().equals("300")) {
                            roleList.add("300"); //普通用户
                        }
                    }
                }
                ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth("", "", history.getDbtype());
                if (history.getYear() != null && !history.getYear().equals("") && history.getMonth() != null && !history.getMonth().equals("")) {
                    manualSetTime.setYear(history.getYear());
                    manualSetTime.setMonth(history.getMonth());
                }
                //初始化列表
                initUserList(map, state, roleList, manualSetTime);
                PageHelper.startPage(pageNum, pageSize);
                if (history.getDbtype().equals("1")) {
                    roleList = new ArrayList<>();
                }
                history.setYear(manualSetTime.getYear());
                history.setMonth(manualSetTime.getMonth());
                historieList = historyService.selectHistoryList(history, roleList);
                PageInfo<ScoreHistory> pageInfo = new PageInfo<>(historieList);
                historieList = pageInfo.getList();
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "查询历史评分成功");
                map.put("data", historieList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "查询历史评分失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }

    private void initUserList(ModelMap map, String state, List<String> roleList, ManualSetTime manualSetTime) throws ParseException {
        List<User> users = userService.findUserByRoleCode(roleList, manualSetTime.getDbtype());
//        List<User> users = userService.findUserAllBySummary();
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

        ScoreHistory scoreHistory = new ScoreHistory();
//        getCurrentYearAndMonth(state, manualSetTime.getYear(), manualSetTime.getMonth(), count, sysTime, scoreHistory, dbtype);
        scoreHistory.setYear(manualSetTime.getYear());
        scoreHistory.setMonth(manualSetTime.getMonth());
        scoreHistory.setDbtype(manualSetTime.getDbtype());
        List<ScoreHistory> historyList = historyService.selectUserHisotyList(scoreHistory, roleList);
        if (users.size() == 0) {
            map.put("msg", "数据为空");
            map.put("code", 0);
        } else if (users.size() > historyList.size()) {
            List<ScoreHistory> scoreHistoryList = new ArrayList<>();
            for (User user : users) {
                scoreHistory.setUsercode(user.getUsercode());
                ScoreHistory one = historyService.selectOneByHistory(scoreHistory);
                if (one == null) {
                    ScoreHistory score = new ScoreHistory();
                    score.setUsercode(user.getUsercode());
                    score.setYear(scoreHistory.getYear());
                    score.setMonth(scoreHistory.getMonth());
                    score.setScorestatus("1");
                    score.setAscore(0.0);
                    score.setBscore(0.0);
                    score.setCscore(0.0);
                    score.setDbtype(manualSetTime.getDbtype());
                    score.setDscore(0.0);
                    score.setEscore(0.0);
                    score.setFscore(0.0);
                    score.setTotalscore(0.0);
                    scoreHistoryList.add(score);
                }
            }
            if (scoreHistoryList.size() > 0) {
                historyService.batchInsert(scoreHistoryList);
            }

        }
    }

    /**
     * 获取打分用户历史评分数据
     *
     * @param req
     * @param history
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/gradeList", produces = "application/json;charset=utf-8")
    public Object gradeList(HttpServletRequest req, ScoreHistory history, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<ScoreHistory> userDtoList;
            try {
                ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth("", "", history.getDbtype());
                //初始化列表
                initGradeList(map, state, manualSetTime);
                PageHelper.startPage(pageNum, pageSize);
                history.setYear(manualSetTime.getYear());
                history.setMonth(manualSetTime.getMonth());
                userDtoList = historyService.gradeList(history);
                PageInfo<ScoreHistory> pageInfo = new PageInfo<>(userDtoList);
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "查询成功");
                map.put("data", userDtoList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }

    private void initGradeList(ModelMap map, String state, ManualSetTime manualSetTime) throws ParseException {
        List<User> users = userService.selectUserPfr(manualSetTime.getDbtype());
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

        ScoreHistory scoreHistory = new ScoreHistory();
//        getCurrentYearAndMonth(state, manualSetTime.getYear(), manualSetTime.getMonth(), count, sysTime, scoreHistory, dbtype);
        scoreHistory.setYear(manualSetTime.getYear());
        scoreHistory.setMonth(manualSetTime.getMonth());
        scoreHistory.setDbtype(manualSetTime.getDbtype());
        List<ScoreHistory> historyList = historyService.selectGradeHisotyList(scoreHistory);
        if (users.size() == 0) {
            map.put("msg", "故数据为空");
            map.put("code", 0);
        } else if (users.size() > historyList.size()) {
            List<ScoreHistory> scoreHistoryList = new ArrayList<>();
            for (User user : users) {
                scoreHistory.setUsercode(user.getUsercode());
                scoreHistory.setDbtype(manualSetTime.getDbtype());
                ScoreHistory one = historyService.selectOneByHistory(scoreHistory);
                if (one == null) {
                    ScoreHistory score = new ScoreHistory();
                    score.setUsercode(user.getUsercode());
                    score.setDbtype(manualSetTime.getDbtype());
                    score.setYear(scoreHistory.getYear());
                    score.setMonth(scoreHistory.getMonth());
                    score.setScorestatus("1");
                    scoreHistoryList.add(score);
                }
            }
            if (scoreHistoryList.size() > 0) {
                historyService.batchInsert(scoreHistoryList);
            }

        }
    }

//    private void getCurrentYearAndMonth(String state, String year, String quarter, int count, String sysTime, ScoreHistory scoreHistory, String dbtype) throws ParseException {
//        String month;
//
//        //手动考核-查看所有月节总结
//        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
//        if (setTime != null) {
//
//            //开始新的月度考核
//            month = quarter;
//            scoreHistory.setYear(setTime.getYear());
//            scoreHistory.setMonth(setTime.getMonth());
//            scoreHistory.setDbtype(dbtype);
//
//        }
//    }


    @RequestMapping(value = "/exportGradeExcel", produces = "application/json;charset=utf-8")
    public void exportGradeExcel(HttpServletResponse response, ScoreHistory dto, String info) {
        List<ScoreHistory> userDtos;
        //json字符串转对象
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        userDtos = historyService.gradeList(dto);
        // 创建ExportExcel对象
        try {
            // 獲取工作表
            Workbook workbook = exportGradeBigDataExcel(userDtos);
            // 完成下載
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            gradeDownFile(os, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private SXSSFWorkbook exportGradeBigDataExcel(List<ScoreHistory> employeeList) {
        // 1.获取数据
        // 2.创建工作簿
        // 阈值，内存中的对象数量最大值，超过这个值会生成一个临时文件存放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("历史评分汇总");
        String[] titles = {"序号", "用户姓名", "发薪号", "所属部门", "所属岗位", "打分状态", "打分季度"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.从集合中取数据并赋值
        for (int i = 0; i < employeeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            ScoreHistory employee = employeeList.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(employee.getUsername());
            row.createCell(2).setCellValue(employee.getMoneycard());
            row.createCell(3).setCellValue(employee.getDepartmentname());
            row.createCell(4).setCellValue(employee.getStationname());
            row.createCell(5).setCellValue(employee.getScorestatusname());
            row.createCell(6).setCellValue(employee.getYear() + "-" + employee.getMonth());
        }
        return wb;

    }

    private void gradeDownFile(ByteArrayOutputStream os, HttpServletResponse response) throws IOException {
        String fileName = "打分用户完成情况汇总.xlsx";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }


    @RequestMapping(value = "/exportHistoryScore", produces = "application/json;charset=utf-8")
    public void exportExcelData1(HttpServletResponse response, String info) {
        List<ScoreHistory> userDtos;
        //json字符串转对象
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        ScoreHistory dto = new ScoreHistory();
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        if (info1.getYear().equals("") || info1.getMonth().equals("")) {
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth("", "", info1.getDbtype());
            dto.setYear(manualSetTime.getYear());
            dto.setMonth(manualSetTime.getMonth());
        } else {
            dto.setYear(info1.getYear());
            dto.setMonth(info1.getMonth());
        }
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        dto.setDbtype(info1.getDbtype());
        List<String> roleList = new ArrayList<>();
        if (info1.getDbtype().equals("2")) {
            roleList.add("300");//普通用戶
            roleList.add("150");//打分用戶
        }
        userDtos = historyService.selectHistoryList(dto, roleList);
        // 创建ExportExcel对象
        try {
            // 獲取工作表
            Workbook workbook = exportBigDataExcel(userDtos, info1.getDbtype());
            // 完成下載
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            downFile(os, response, info1.getYear() + "-" + info1.getMonth() + "历史评分汇总.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook exportBigDataExcel(List<ScoreHistory> employeeList, String dbtype) {
        // 1.获取数据
        // 2.创建工作簿
        // 阈值，内存中的对象数量最大值，超过这个值会生成一个临时文件存放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("sheet1");
        String[] titles = null;
        if (dbtype.equals("2")) {
            titles = new String[]{"序号", "用户姓名", "发薪号", "所属部门", "所属岗位", "角色", "打分状态", "季结状态", "季结季度", "A", "B", "C", "D", "总分", "平均目标", "目标", "医德医风", "总分和"};
        } else {
            titles = new String[]{"序号", "用户姓名", "发薪号", "所属部门", "所属岗位", "角色", "打分状态", "季结状态", "季结季度", "A", "B", "C", "D", "总分"};
        }
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.从集合中取数据并赋值
        for (int i = 0; i < employeeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            ScoreHistory employee = employeeList.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(employee.getUsername());
            row.createCell(2).setCellValue(employee.getMoneycard());
            row.createCell(3).setCellValue(employee.getDepartmentname());
            row.createCell(4).setCellValue(employee.getStationname());
            row.createCell(5).setCellValue(employee.getRolename());
            row.createCell(6).setCellValue(employee.getScorestatusname());
            row.createCell(7).setCellValue(employee.getStatename());
            if (dbtype.equals("2")) {
                row.createCell(8).setCellValue(employee.getYear() + "-" + employee.getMonth());
            } else {
                row.createCell(8).setCellValue(employee.getYear() + "(第" + employee.getMonth() + "季度)");
            }
            if (employee.getAscore() == null) {
                employee.setAscore(0.0);
            }
            if (employee.getBscore() == null) {
                employee.setBscore(0.0);
            }
            if (employee.getCscore() == null) {
                employee.setCscore(0.0);
            }
            if (employee.getDscore() == null) {
                employee.setDscore(0.0);
            }
            if (employee.getTotalscore() == null) {
                employee.setTotalscore(0.0);
            }
            row.createCell(9).setCellValue(employee.getAscore());
            row.createCell(10).setCellValue(employee.getBscore());
            row.createCell(11).setCellValue(employee.getCscore());
            row.createCell(12).setCellValue(employee.getDscore());
            row.createCell(13).setCellValue(employee.getTotalscore());
            if (dbtype.equals("2")) {
                row.createCell(14).setCellValue(employee.getAvgMbScore());
                row.createCell(15).setCellValue(employee.getSumMbScore());
                row.createCell(16).setCellValue(employee.getDfScore());
                row.createCell(17).setCellValue(employee.getSumTotalScore());
            }
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


    /**
     * 一键导出当前月度未评分和未完成状态的数据
     *
     * @return
     */
    @RequestMapping("/oneClickDown")
    public void oneClickDown(HttpServletResponse response, String year, String month, String dbtype) {
//        ModelMap map = new ModelMap();
        try {
            List<String> roleList = new ArrayList<>();
            if (dbtype != null && dbtype.equals("2")) {
                roleList.add("300");//普通用戶
            }
//            roleList.add("150");//打分用戶
            List<ScoreHistory> list = historyService.oneClickDown(year, month, roleList, dbtype);
            Workbook workbook = exportBigDataExcel(list, dbtype);
            // 完成下載
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            downFile(os, response, year + "-" + month + "未评分与未完成人员表.xlsx");
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
//            map.put("msg", "一键导出失败");
//            map.put("error", e.getMessage());
//            map.put("eor", e.getStackTrace());
//            map.put("code", 1);
        }
//        return map;
    }
}
