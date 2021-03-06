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
    @Autowired
    IEvaluationReportService evaluationReportService;
    @Autowired
    IResultReportService resultReportService;
    @Resource
    IBranchService branchService;

    @RequestMapping(value = "/scoreShengCheng", produces = "application/json;charset=utf-8")
    public Object scoreShengChengUser(HttpServletRequest req, String dbtype, String postType, String userCode) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            try {
                ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                if (setTime != null) {
                    postType = dbtype.equals("2") ? postType : null;
                    List<MonthSummary> summaryList = summaryService.selectSummaryListByYearAndMonth(setTime.getYear(), setTime.getMonth(), dbtype, postType, null);

//                    this.shengChengDelete(setTime.getYear(), setTime.getMonth(), dbtype, postType, userCode, summaryList);
                    long count = 0;
                    List<User> scorringUserList = userService.selectUserPfr(dbtype, setTime.getYear(), setTime.getMonth(), true);
                    if (scorringUserList.size() > 0) {
                        List<User> users = userService.findUserByRoleCode(null, dbtype, postType, null, null, null, true);
                        for (User u : scorringUserList) {
                            count = users.stream().filter(s -> s.getUsercode().equals(u.getUsercode())).count();
                            if (count == 0) {
                                users.add(u);
                            }
                        }
                        if (users.size() > 0) {
                            if (summaryList.size() != users.size()) {
                                addMonthSummary(setTime.getYear(), setTime.getMonth(), users, dbtype);
                            }
                            if(userCode!=null && !userCode.equals("")) {
                                this.getShengCheng(setTime.getYear(), setTime.getMonth(), dbtype, postType, userCode);
                            }
                            map.put("msg", "??????????????????");
                            map.put("code", 0);
                        }
                    } else {
                        map.put("msg", "????????????????????????.");
                        map.put("code", 1);
                    }
                } else {
                    map.put("msg", "?????????????????????setTime?????????.");
                    map.put("code", 1);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "??????????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
            map.put("code", 810);
        }
        return map;
    }

    private void shengChengDelete(String year, String month, String dbtype, String postType, String userCode, List<MonthSummary> monthSummaryList) {
        long count = 0;
        if (userCode != null && !userCode.equals("")) {
            List<MonthSummary> query = monthSummaryList.stream().filter(s -> s.getEmployeecode().equals(userCode)).collect(Collectors.toList());
            if (query.size() > 0) {
                MonthSummary monthSummary = query.get(0);
                if (monthSummary.getState().equals("0")) {
                    count = 0;
                } else {
                    count = 1;
                }
            } else {
                count = 1;
            }
        } else {
            count = monthSummaryList.stream().filter(s -> !s.getState().equals("0")).count();
        }
        if (count == 0) {
            resultReportService.deleteYM(year, month, dbtype, postType, userCode);
            evaluationReportService.deleteYM(year, month, dbtype, postType, userCode);
            scoreDetailService.deleteYM(year, month, dbtype, postType, userCode);
            historyService.deleteYM(year, month, dbtype, postType, userCode);
            flowService.deleteYM(year, month, dbtype, postType, userCode);
            summaryService.deleteYM(year, month, dbtype, postType, userCode);
            scoreDutySmService.deleteYM(year, month, dbtype, postType, userCode);
        }
    }

    @RequestMapping(value = "/oneShengChengDelete", produces = "application/json;charset=utf-8")
    public Object oneShengChengDelete1(HttpServletRequest req, String dbtype, String userCode,String type) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            try {
                ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                if (setTime != null) {
                    List<MonthSummary> summaryList = summaryService.selectSummaryListByYearAndMonth(setTime.getYear(), setTime.getMonth(), dbtype, null, userCode);
                    if (summaryList.size() > 0) {
                        MonthSummary monthSummary = summaryList.get(0);
                        if (monthSummary.getState().equals("0")) {
                            this.oneShengChengDelete(setTime.getYear(), setTime.getMonth(), dbtype, userCode);
                            if(type.equals("1")) {
                                MonthSummary update = new MonthSummary();
                                update.setSerialno(monthSummary.getSerialno());
                                update.setState("0");
                                update.setIsSc(0);
                                update.setDbtype(dbtype);
                                summaryService.updateByPrimaryKeySelective(update);
                            } else {
                                summaryService.deleteByPrimaryKey(monthSummary.getSerialno(),dbtype);
                            }
                            map.put("msg", "??????????????????.");
                            map.put("code", 0);
                        } else {
                            map.put("msg", "?????????????????????????????????????????????????????????????????????.");
                            map.put("code", 1);
                        }
                    } else {
                        map.put("msg", "?????????????????????monthSummary null.");
                        map.put("code", 1);
                    }
                } else {
                    map.put("msg", "?????????????????????setTime?????????.");
                    map.put("code", 1);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "??????????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
            map.put("code", 810);
        }
        return map;
    }

    private void oneShengChengDelete(String year, String month, String dbtype, String userCode) {
        resultReportService.deleteYM(year, month, dbtype, null, userCode);
        evaluationReportService.deleteYM(year, month, dbtype, null, userCode);
        scoreDetailService.deleteYM(year, month, dbtype, null, userCode);
        historyService.deleteYM(year, month, dbtype, null, userCode);
        flowService.deleteYM(year, month, dbtype, null, userCode);
        scoreDutySmService.deleteYM(year, month, dbtype, null, userCode);

    }


    /**
     * ??????????????????????????????  ??????????????????????????????
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
                ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                if (setTime != null) {
                    getUserDtos(dto, map, setTime.getYear(), setTime.getMonth(), pageNum, pageSize, dbtype);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "??????????????????????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
            map.put("code", 810);
        }
        return map;
    }

    private void addMonthSummary(String year, String month, List<User> users, String dbtype) {
        List<MonthSummary> list = new ArrayList<>();
        for (User user : users) {
            String serialno = year + "-" + month + "-" + dbtype + "-" + user.getUsercode();
            MonthSummary monthSummary = summaryService.selectByPrimaryKey(serialno, dbtype);
            if (monthSummary == null) {
                MonthSummary summary = new MonthSummary();
                summary.setSerialno(serialno);
                summary.setMonth(month);
                summary.setIssend("0");
                summary.setYear(year);
                summary.setDbtype(dbtype);
                summary.setEmployeecode(user.getUsercode());
                summary.setState("0");
                summary.setIsSc(0);
                list.add(summary);
            }
        }
        if (list.size() > 0) {
            summaryService.batchInsert(list);
        }
    }

    private void getUserDtos(UserDto dto, ModelMap map, String year, String month, int pageNum, int pageSize, String dbtype) {
        dto.setYear(year);
        dto.setMonth(month);
        dto.setDbtype(dbtype);
        if (dbtype.equals("1") && dto.getDbbk() != null && !dto.getDbbk().equals("")) {
            dto.setDbbk(dto.getDbbk());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<UserDto> userDtos = dtoService.selectUserDtoLike(dto, "bpfr");
        this.handleUsersMsg(userDtos);
        //getStationName(userDtos);
        PageInfo<UserDto> pageInfo = new PageInfo<>(userDtos);
        userDtos = pageInfo.getList();
        map.put("totalPages", pageInfo.getTotal());
        map.put("msg", "??????????????????????????????");
        map.put("data", userDtos);
        map.put("code", 0);
    }

    private void handleUsersMsg(List<UserDto> userDtos) {
        if (userDtos.size() > 0) {
            for (int i = 0; i < userDtos.size(); i++) {
                //???????????????????????????
                Branch branch = branchService.selectByPrimaryKey(userDtos.get(i).getBranchcode());
                if (branch != null) {
                    userDtos.get(i).setBranchname(branch.getBranchname());
                } else {
                    userDtos.get(i).setBranchname("");
                }

            }
        }
    }

    private void getShengCheng(String year, String month, String dbtype, String postType, String userCode) {
        postType = dbtype.equals("2") ? postType : null;
        UserDto query = new UserDto();
        query.setYear(year);
        query.setMonth(month);
        query.setDbtype(dbtype);
        query.setPostType(postType);
        if (userCode != null && !userCode.equals("")) {
            query.setUsercode(userCode);
        }
        List<UserDto> userDtoList = dtoService.selectUserByMonthSummaryList(query);
        List<UserDto> bdfrUserDboList = new ArrayList<>();
        //?????????????????????
        if (dbtype.equals("1")) {
            bdfrUserDboList = userDtoList.stream().filter(s -> s.getDbbk() != null && (
                    s.getDbbk().equals("3") || s.getDbbk().equals("4"))).collect(Collectors.toList());
        } else {
            bdfrUserDboList = userDtoList.stream().filter(s -> s.getRolecode().equals("300")).collect(Collectors.toList());
        }
        if (bdfrUserDboList.size() > 0) {
            List<ScoreHistory> bdfrHistoryList = historyService.selectHistoryByMonthSummaryList(year, month, dbtype, postType, userCode);
            List<ScoreFlow> bdfrFlowList = flowService.selectFlowByMonthSummaryList(year, month, dbtype, postType, userCode);
            List<ScoreDetail> bdfrDetailList = scoreDetailService.selectDetailByMonthSummaryList(year, month, dbtype, postType, userCode);
            Duty dutyQuery = new Duty();
            dutyQuery.setDbtype(dbtype);
            List<Duty> dutyList = dutyService.selectDutyAll(dutyQuery);
            List<Score> scoreList = scoreService.findScoreAll(dbtype, postType, userCode);
//            ????????????
//            ScoreDutySm queryDutySm = new ScoreDutySm();
//            queryDutySm.setYear(year);
//            queryDutySm.setMonth(month);
//            queryDutySm.setDbtype(dbtype);
//            List<ScoreDutySm> dutySmList = scoreDutySmService.selectScoreDutySmList(queryDutySm,postType);

            // History
            List<ScoreHistory> insertHistoryList = new ArrayList<>();
            List<ScoreHistory> updateHistoryList = new ArrayList<>();
            List<ScoreHistory> historyList = new ArrayList<>();
            // Flow
            List<ScoreFlow> insertFlowList = new ArrayList<>();
            List<ScoreFlow> updateFlowList = new ArrayList<>();
            // Detail
            List<ScoreDetail> insertDetailList = new ArrayList<>();
            //????????????
//            List<ScoreDutySm> insertDutySmList = new ArrayList<>();

            List<String> scoreTypeList = new ArrayList<>();
            scoreTypeList.add("A");
            scoreTypeList.add("B");
            scoreTypeList.add("C");
            scoreTypeList.add("D");
            scoreTypeList.add("E");
            scoreTypeList.add("F");

            if (dbtype.equals("1")) {
//                this.getShengChengResult(year, month, scoreTypeList, bdfrUserDboList, dutyList, scoreList, historyList,
//                        bdfrHistoryList, bdfrFlowList, bdfrDetailList, insertFlowList, updateFlowList, insertHistoryList, updateHistoryList, insertDetailList, dutySmList, insertDutySmList, dbtype);
                this.getShengChengResult(year, month, scoreTypeList, bdfrUserDboList, dutyList, scoreList, historyList,
                        bdfrHistoryList, bdfrFlowList, bdfrDetailList, insertFlowList, updateFlowList, insertHistoryList, updateHistoryList, insertDetailList, dbtype);
            } else {
//                this.getDutyShengChengResult(year, month, scoreTypeList, bdfrUserDboList, dutyList, scoreList, historyList,
//                        bdfrHistoryList, bdfrFlowList, bdfrDetailList, insertFlowList, updateFlowList, insertHistoryList, updateHistoryList, insertDetailList, dutySmList, insertDutySmList, dbtype);
                this.getDutyShengChengResult(year, month, scoreTypeList, bdfrUserDboList, dutyList, scoreList, historyList,
                        bdfrHistoryList, bdfrFlowList, bdfrDetailList, insertFlowList, updateFlowList, insertHistoryList, updateHistoryList, insertDetailList, dbtype);
            }
            if (insertHistoryList.size() > 0) {
                for (ScoreHistory item : insertHistoryList) {
                    historyService.insertSelective(item);
                }
//                historyService.batchInsert(insertHistoryList);
            }
            if (updateHistoryList.size() > 0) {
                for (ScoreHistory history : updateHistoryList)
                    historyService.updateByPrimaryKeySelective(history);
            }
            if (insertFlowList.size() > 0) {
                for (ScoreFlow item : insertFlowList) {
                    flowService.insertSelective(item);
                }
//                flowService.batchInsert(insertFlowList);
            }
            if (updateFlowList.size() > 0) {
                for (ScoreFlow flow : updateFlowList)
                    flowService.updateByPrimaryKeySelective(flow);
            }
            if (insertDetailList.size() > 0) {
                for (ScoreDetail item : insertDetailList) {
                    scoreDetailService.insertSelective(item);
                }
//                scoreDetailService.batchInset(insertDetailList);
            }
//            ????????????
//            if (insertDutySmList.size() > 0) {
//                for(ScoreDutySm item:insertDutySmList){
//                    scoreDutySmService.insertSelective(item);
//                }
//            }
            for (UserDto dto : userDtoList) {
                MonthSummary update = new MonthSummary();
                update.setSerialno(dto.getSerialno());
                update.setDbtype(dbtype);
//                update.setYear(year);
//                update.setMonth(month);
//                update.setEmployeecode(dto.getUsercode());
                update.setIsSc(1);
                summaryService.updateByPrimaryKeySelective(update);
            }
        }
    }

//    private void getDutyShengChengResult(String year, String month, List<String> scoreTypeList, List<UserDto> bdfrUserDboList,
//                                         List<Duty> dutyList, List<Score> scoreList,
//                                         List<ScoreHistory> historyList, List<ScoreHistory> bdfrHistoryList,
//                                         List<ScoreFlow> bdfrFlowList, List<ScoreDetail> bdfrDetailList,
//                                         List<ScoreFlow> insertFlowList, List<ScoreFlow> updateFlowList,
//                                         List<ScoreHistory> insertHistoryList, List<ScoreHistory> updateHistoryList,
//                                         List<ScoreDetail> insertDetailList, List<ScoreDutySm> dutySmList, List<ScoreDutySm> insertDutySmList, String dbtype) {

    private void getDutyShengChengResult(String year, String month, List<String> scoreTypeList, List<UserDto> bdfrUserDboList,
                                         List<Duty> dutyList, List<Score> scoreList,
                                         List<ScoreHistory> historyList, List<ScoreHistory> bdfrHistoryList,
                                         List<ScoreFlow> bdfrFlowList, List<ScoreDetail> bdfrDetailList,
                                         List<ScoreFlow> insertFlowList, List<ScoreFlow> updateFlowList,
                                         List<ScoreHistory> insertHistoryList, List<ScoreHistory> updateHistoryList,
                                         List<ScoreDetail> insertDetailList, String dbtype) {
        List<Score> queryScoreListEF = new ArrayList<>();
        // ??????
        List<Duty> queryDutyList = new ArrayList<>();
        List<Score> queryScoreList = new ArrayList<>();
        List<ScoreHistory> queryHistoryList = new ArrayList<>();
        List<ScoreFlow> queryFlowList = new ArrayList<>();
        List<ScoreFlow> queryHbFlowList = new ArrayList<>();
        List<ScoreDetail> queryDetailList = new ArrayList<>();
//        ????????????
//        List<ScoreDutySm> queryDutySmList = new ArrayList<>();

        Double sumScore = 0.0;
        Double ratio = 0.0;
        long count = 0;
//        ????????????
//        boolean isDutySm = false;
        List<String> fSerialNoList = new ArrayList<>();
        boolean isEFSerialNo = false;
        for (UserDto dto : bdfrUserDboList) {
            queryDutyList = dutyList.stream().filter(s -> s.getStationcode().equals(dto.getStationcode())).collect(Collectors.toList());
//        ????????????
//            isDutySm = false;
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
//                queryDutySmList = dutySmList.stream().filter(s -> s.getScorredcode().equals(dto.getUsercode())).collect(Collectors.toList());
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

                            if (scoreType.equals("E") || scoreType.equals("F")) {
                                queryScoreListEF = scoreList.stream().filter(s -> s.getScorredcode().equals(score.getScorredcode()) &&
                                        s.getScorringcode().equals(score.getScorringcode()) &&
                                        s.getScoretype().equals(scoreType) &&
                                        s.getDutycode() != null).collect(Collectors.toList());
                            } else {
                                queryScoreListEF = new ArrayList<>();
                            }
                            if (queryFlowList.size() == 0 && count == 0) {
                                ratio = 0.0;
                                ScoreFlow scoreFlow = new ScoreFlow();
                                scoreFlow.setSerialno(StringUtil.getGuid());
                                scoreFlow.setMserialno(dto.getSerialno());
                                scoreFlow.setScoredcode(score.getScorredcode());
                                scoreFlow.setScorringcode(score.getScorringcode());
                                scoreFlow.setScoretype(score.getScoretype());
                                //????????? 1 ?????????
                                scoreFlow.setScoreState("1");
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
                                    count = queryScoreListEF.stream().filter(s -> s.getDutycode().equals(duty.getDutycode())).count();

                                    if ((!this.isEF(score.getScoretype()) && count == 0) ||
                                            (this.isEF(score.getScoretype()) && count > 0)) {
                                        ScoreDetail detail = new ScoreDetail();
                                        detail.setSerialNo(StringUtil.generatorId());
                                        detail.setFSerialNo(scoreFlow.getSerialno());
                                        detail.setDSerialNo(duty.getDutycode());
                                        detail.setScore(duty.getDefScore() == null ? "0" : duty.getDefScore().toString());
                                        detail.setScoretype(scoreType);
                                        detail.setRatio(scoreFlow.getRatio());
                                        detail.setDbtype(dbtype);
                                        insertDetailList.add(detail);
                                        sumScore += duty.getDefScore() == null ? 0 : Double.parseDouble(duty.getDefScore());
//????????????
//                                        if (queryDutySmList.size() == 0 && !isDutySm) {
//                                            if(insertDutySmList.stream().filter(s-> s.getScorredcode().equals(dto.getUsercode()) &&
//                                                            s.getDutycode().equals(duty.getDutycode()) && s.getDbtype().equals(dbtype)).count() == 0) {
//                                                ScoreDutySm dutySm = new ScoreDutySm();
//                                                dutySm.setYear(year);
//                                                dutySm.setMonth(month);
//                                                dutySm.setDutycode(duty.getDutycode());
//                                                dutySm.setScorredcode(dto.getUsercode());
//                                                dutySm.setZpsm("");
//                                                dutySm.setDbtype(dbtype);
//                                                insertDutySmList.add(dutySm);
//                                            }
//                                        }
                                    }
                                }
                                scoreFlow.setScore(sumScore);
                                insertFlowList.add(scoreFlow);

                            } else {
                                isEFSerialNo = false;
                                if (count == 0) {
                                    ScoreFlow updateFlow = queryFlowList.get(0);
                                    if (this.isEF(score.getScoretype())) {
                                        if (fSerialNoList.stream().filter(s -> s.equals(updateFlow.getSerialno())).count() == 0) {
                                            fSerialNoList.add(updateFlow.getSerialno());
                                        } else {
                                            isEFSerialNo = true;
                                        }
                                    }
                                    sumScore = 0.0;
                                    if (!isEFSerialNo) {
                                        for (Duty duty : queryDutyList) {
                                            count = queryScoreListEF.stream().filter(s -> s.getDutycode().equals(duty.getDutycode())).count();

                                            if ((!this.isEF(score.getScoretype()) && count == 0) || (this.isEF(score.getScoretype()) && count > 0)) {
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
                                                    sumScore += duty.getDefScore() == null ? 0 : Double.parseDouble(duty.getDefScore());
//????????????
//                                                    if (queryDutySmList.size() == 0 && !isDutySm) {
//                                                        if(insertDutySmList.stream().filter(s-> s.getScorredcode().equals(dto.getUsercode()) &&
//                                                                s.getDutycode().equals(duty.getDutycode()) && s.getDbtype().equals(dbtype)).count() == 0) {
//                                                            ScoreDutySm dutySm = new ScoreDutySm();
//                                                            dutySm.setYear(year);
//                                                            dutySm.setMonth(month);
//                                                            dutySm.setDutycode(duty.getDutycode());
//                                                            dutySm.setScorredcode(dto.getUsercode());
//                                                            dutySm.setZpsm("");
//                                                            dutySm.setDbtype(dbtype);
//                                                            insertDutySmList.add(dutySm);
//                                                        }
//                                                    }
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
        Double eScore = 0.0;
        Double fScore = 0.0;
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
            eScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getEscore).sum();
            fScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getFscore).sum();

            history.setTotalscore(sumScore);
            history.setAscore(aScore);
            history.setBscore(bScore);
            history.setCscore(cScore);
            history.setDscore(dScore);
            history.setEscore(eScore);
            history.setFscore(fScore);

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
            eScore = 0.0;
            fScore = 0.0;
        }
    }

    private boolean isEF(String scoretype) {
        if (scoretype.equals("E") || scoretype.equals("F")) {
            return true;
        }
        return false;
    }

    //    private void getShengChengResult(String year, String month, List<String> scoreTypeList, List<UserDto> bdfrUserDboList,
//                                     List<Duty> dutyList, List<Score> scoreList,
//                                     List<ScoreHistory> historyList, List<ScoreHistory> bdfrHistoryList,
//                                     List<ScoreFlow> bdfrFlowList, List<ScoreDetail> bdfrDetailList,
//                                     List<ScoreFlow> insertFlowList, List<ScoreFlow> updateFlowList,
//                                     List<ScoreHistory> insertHistoryList, List<ScoreHistory> updateHistoryList,
//                                     List<ScoreDetail> insertDetailList, List<ScoreDutySm> dutySmList, List<ScoreDutySm> insertDutySmList, String dbtype) {
    private void getShengChengResult(String year, String month, List<String> scoreTypeList, List<UserDto> bdfrUserDboList,
                                     List<Duty> dutyList, List<Score> scoreList,
                                     List<ScoreHistory> historyList, List<ScoreHistory> bdfrHistoryList,
                                     List<ScoreFlow> bdfrFlowList, List<ScoreDetail> bdfrDetailList,
                                     List<ScoreFlow> insertFlowList, List<ScoreFlow> updateFlowList,
                                     List<ScoreHistory> insertHistoryList, List<ScoreHistory> updateHistoryList,
                                     List<ScoreDetail> insertDetailList, String dbtype) {
        // ??????
        List<Duty> queryDutyList = new ArrayList<>();
        List<Score> queryScoreList = new ArrayList<>();
        List<ScoreHistory> queryHistoryList = new ArrayList<>();
        List<ScoreFlow> queryFlowList = new ArrayList<>();
        List<ScoreFlow> queryHbFlowList = new ArrayList<>();
        List<ScoreDetail> queryDetailList = new ArrayList<>();
//        ????????????
//        List<ScoreDutySm> queryDutySmList = new ArrayList<>();

        Double sumScore = 0.0;
        Double ratio = 0.0;
//        ????????????
//        boolean isDutySm = false;
        for (UserDto dto : bdfrUserDboList) {
            queryDutyList = dutyList.stream().filter(s -> s.getDbbk().equals(dto.getDbbk())).collect(Collectors.toList());
//        ????????????
//            isDutySm = false;
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
//                ????????????
//                queryDutySmList = dutySmList.stream().filter(s -> s.getScorredcode().equals(dto.getUsercode())).collect(Collectors.toList());
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
                                //????????? 1 ?????????
                                scoreFlow.setScoreState("1");
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
                                    sumScore += duty.getDefScore() == null ? 0 : Double.parseDouble(duty.getDefScore());
//                                  ????????????
//                                    if (queryDutySmList.size() == 0 && !isDutySm) {
//                                        ScoreDutySm dutySm = new ScoreDutySm();
//                                        dutySm.setYear(year);
//                                        dutySm.setMonth(month);
//                                        dutySm.setDutycode(duty.getDutycode());
//                                        dutySm.setScorredcode(dto.getUsercode());
//                                        dutySm.setZpsm("");
//                                        dutySm.setDbtype(dbtype);
//                                        insertDutySmList.add(dutySm);
//                                    }
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
                                        sumScore += duty.getDefScore() == null ? 0 : Double.parseDouble(duty.getDefScore());
//                                      ????????????
//                                        if (queryDutySmList.size() == 0 && !isDutySm) {
//                                            ScoreDutySm dutySm = new ScoreDutySm();
//                                            dutySm.setYear(year);
//                                            dutySm.setMonth(month);
//                                            dutySm.setDutycode(duty.getDutycode());
//                                            dutySm.setScorredcode(dto.getUsercode());
//                                            dutySm.setZpsm("");
//                                            dutySm.setDbtype(dbtype);
//                                            insertDutySmList.add(dutySm);
//                                        }

                                    }
                                }
                                if (sumScore != 0.0) {
                                    updateFlow.setScore(updateFlow.getScore() + sumScore);
                                    updateFlowList.add(updateFlow);
                                }
                            }
//                            ????????????
//                            isDutySm = true;
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
     * ???????????????????????? ????????? ?????? ??????
     *
     * @param userDtoList
     */
    private void getStationName(List<UserDto> userDtoList, String dbtype) {
        //??????????????????
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
                    //????????????  ??????= A????????? X A?????????????????? +  B????????? X B?????????????????? + C????????? X C?????????????????? + D????????? X D??????????????????

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
     * ??????????????????????????????
     *
     * @param serialnos
     * @return
     */
    @RequestMapping(value = "/updateFinishGradeBySerialNo", produces = "application/json;charset=utf-8")
    public Object updateFinishGradeBySerialNo(String serialnos, String dbtype) {
        ModelMap map = new ModelMap();
        String[] serialno = serialnos.split(",");

        int counts = 0;
        for (int i = 0; i < serialno.length; i++) {

            int count = summaryService.updateFinishGradeBySerialNo(serialno[i], dbtype);
            counts += count;
        }
        if (counts > 0) {
            map.put("msg", "??????????????????????????????????????????");
            map.put("code", 0);
        } else {
            map.put("msg", "??????????????????????????????????????????");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @return
     */
    @RequestMapping(value = "/updateFinishGradeAll", produces = "application/json;charset=utf-8")
    public Object updateFinishGradeAll(String dbtype, String postType) {
        ModelMap map = new ModelMap();
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
        if (setTime != null) {
            int count = summaryService.updateFinishGradeAll(setTime.getYear(), setTime.getMonth(), dbtype, postType);
            summaryService.updateFinishGradeScorringAll(setTime.getYear(), setTime.getMonth(), dbtype);
            if (count > 0) {
                map.put("msg", "????????????????????????????????????");
                map.put("code", 0);
            } else {
                map.put("msg", "????????????????????????????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "????????????????????????????????????");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * ??????????????????excel???
     *
     * @param response
     */
    @RequestMapping(value = "exportScore", produces = "application/json;charset=utf-8")
    public void exportExcelData(HttpServletRequest req, HttpServletResponse response, UserDto dto, String info) throws ParseException {
        String state = (String) req.getSession().getAttribute("state");
        // ??????????????????
        String title = "????????????-????????????";
        //??????????????????
        String[] rowsName = new String[]{"??????", "????????????", "?????????", "????????????", "????????????", "????????????", "A", "B", "C", "D", "??????"};
        //??????????????????
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs = null;
        //json??????????????????
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        dto.setDbtype(info1.getDbtype());
        //????????????
        String year = CalendarUtil.getYear();
        //????????????
        String month = CalendarUtil.getMonth();
        //??????????????????
        String quarter = month;//CalendarUtil.getQuarter(month);
        int counts = Integer.parseInt(quarter.trim()) - 1;
        //????????????????????????
        String sysTime = DateUtil.getTime();

        //????????????-????????????????????????
        manualExportExcelData(dto, rowsName, dataList, year, quarter, counts, sysTime);

        try {
            // ??????ExportExcel??????
            ExcelUtil excelUtil = new ExcelUtil();
            String fileName = new String("??????????????????.xls".getBytes("UTF-8"), "iso-8859-1");    //??????word??????????????????
            excelUtil.exportExcel(title, rowsName, dataList, fileName, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @RequestMapping(value = "exportHistoryGrade", produces = "application/json;charset=utf-8")
    public void exportHistoryGrade(HttpServletRequest req, HttpServletResponse response) throws ParseException {
        String state = (String) req.getSession().getAttribute("state");
        // ??????????????????
        String title = "????????????-????????????";
        //??????????????????
        String[] rowsName = new String[]{"??????", "????????????", "?????????", "????????????", "????????????", "????????????", "A", "B", "C", "D", "??????"};
        //??????????????????
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
            // ??????ExportExcel??????
            ExcelUtil excelUtil = new ExcelUtil();
            String fileName = new String("??????????????????.xls".getBytes("UTF-8"), "iso-8859-1");    //??????word??????????????????
            excelUtil.exportExcel(title, rowsName, dataList, fileName, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void manualExportExcelData(UserDto dto, String[] rowsName, List<Object[]> dataList, String year, String quarter, int counts, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dto.getDbtype());
        if (setTime != null) {

            //????????????????????????
            month = quarter;
            getUserDtos(dto, rowsName, dataList, setTime.getYear(), setTime.getMonth());

        }
    }

    private void getUserDtos(UserDto dto, String[] rowsName, List<Object[]> dataList, String year, String month) {
        List<UserDto> userDtos;
        dto.setYear(year);
        dto.setMonth(month);
//        List<String> roleList = new ArrayList<>();
//        roleList.add("100");
//        roleList.add("200");
//        roleList.add("300");
//        roleList.add("50");
        userDtos = dtoService.selectUserDtoLike(dto, "bpfr");
        // ???????????????????????? ????????? ?????? ??????
        getStationName(userDtos, dto.getDbtype());
        //????????????????????????
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
     * ????????????????????????
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
        //json??????????????????
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
        // ??????ExportExcel??????
        try {
            // ???????????????
            Workbook workbook = exportBigDataExcel(userDtos);
            // ????????????
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            downFile(os, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook exportBigDataExcel(List<UserDto> employeeList) {
        // 1.????????????
        // 2.???????????????
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("??????????????????");
        String[] titles = {"??????", "????????????", "?????????", "????????????", "????????????", "????????????", "????????????", "????????????", "A", "B", "C", "D", "??????"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.??????????????????????????????
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
        String fileName = "??????????????????.xls";
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
            //????????????????????????
            String sysTime = DateUtil.getTime();
            if (state.equals("1")) {
                //????????????
                manualGetGrade(userDto, year, quarter, i, sysTime);

            } else {
                //????????????
                automaticgetGrage(userDto, year, i);
            }
            List<UserDto> userDtos = dtoService.gradeList(userDto);
            getGradeState(userDtos);

        } catch (Exception e) {
            log.error(e.getMessage() , e);
        }

    }

    private void manualGetGrade(UserDto userDto, String year, String quarter, int i, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //????????????????????????
                month = quarter;
                userDto.setMonth(month);
                userDto.setYear(year);
            } else {
                //?????????????????????????????????????????????????????????
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
                map.put("msg", "????????????");
                map.put("data", userDtoList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(e.getMessage() , e);
                map.put("error", e.getMessage());
                map.put("msg", "????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
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
        //json??????????????????
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
        // ??????ExportExcel??????
        try {
            // ???????????????
            Workbook workbook = exportGradeBigDataExcel(userDtos);
            // ????????????
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            gradeDownFile(os, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private SXSSFWorkbook exportGradeBigDataExcel(List<UserDto> employeeList) {
        // 1.????????????
        // 2.???????????????
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("??????????????????");
        String[] titles = {"??????", "????????????", "?????????", "????????????", "????????????", "????????????", "????????????"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.??????????????????????????????
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
        String fileName = "??????????????????????????????.xls";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }*/


    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectUserDtoLike(HttpServletRequest req, ScoreHistory history, String postType, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<ScoreHistory> historieList;
            try {
                postType = history.getDbtype().equals("2") ? postType : null;
                String qrcode = "bpfr";
                String year = history.getYear();
                String month = history.getMonth();
                boolean isDq = true;
                ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth("", "", history.getDbtype());
                if (year != null && !year.equals("") && month != null && !month.equals("")) {
                    if (!manualSetTime.getYear().equals(year) || !manualSetTime.getMonth().equals(month)) {
                        manualSetTime.setYear(year);
                        manualSetTime.setMonth(month);
                        manualSetTime.setDbtype(history.getDbtype());
                        isDq = false;
                    }
                }
                //??????????????????????????????????????? ???????????????????????????
                //???????????????
//                initUserList(map, state, qrcode, manualSetTime, postType,history.getDbbk(),isDq);
                PageHelper.startPage(pageNum, pageSize);

                history.setYear(manualSetTime.getYear());
                history.setMonth(manualSetTime.getMonth());
                historieList = historyService.selectHistoryList(history, qrcode, postType, isDq);
                PageInfo<ScoreHistory> pageInfo = new PageInfo<>(historieList);
                historieList = pageInfo.getList();
                this.handleUsersMsgH(historieList);
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "????????????????????????");
                map.put("data", historieList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("error", e.getMessage());
                map.put("msg", "????????????????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
            map.put("code", 810);
        }
        return map;

    }

    private void handleUsersMsgH(List<ScoreHistory> scoreHistoryList) {
        if (scoreHistoryList.size() > 0) {
            for (int i = 0; i < scoreHistoryList.size(); i++) {
                //???????????????????????????
                Branch branch = branchService.selectByPrimaryKey(scoreHistoryList.get(i).getBranchcode());
                if (branch != null) {
                    scoreHistoryList.get(i).setBranchname(branch.getBranchname());
                } else {
                    scoreHistoryList.get(i).setBranchname("");
                }

            }
        }
    }

    //??????????????????????????????????????? ???????????????????????????
    private void initUserList(ModelMap map, String state, String qrcode, ManualSetTime manualSetTime, String postType, String dbbk, boolean isDq) throws ParseException {
        List<User> users = userService.findUserByRoleCode(
                qrcode, manualSetTime.getDbtype(), postType, dbbk, manualSetTime.getYear(), manualSetTime.getMonth(), isDq);
//        List<User> users = userService.findUserAllBySummary();
        //??????????????????
        String year = CalendarUtil.getYear();
        //??????????????????
        String month = CalendarUtil.getMonth();
        //??????????????????
        String quarter = month;//CalendarUtil.getQuarter(month);
        //?????????????????????
        int count = Integer.parseInt(quarter.trim()) - 1;
        //????????????????????????
        String sysTime = DateUtil.getTime();

        ScoreHistory scoreHistory = new ScoreHistory();
//        getCurrentYearAndMonth(state, manualSetTime.getYear(), manualSetTime.getMonth(), count, sysTime, scoreHistory, dbtype);
        scoreHistory.setYear(manualSetTime.getYear());
        scoreHistory.setMonth(manualSetTime.getMonth());
        scoreHistory.setDbtype(manualSetTime.getDbtype());
        if (scoreHistory.getDbtype().equals("1") && dbbk != null && !dbbk.equals("")) {
            scoreHistory.setDbbk(dbbk);
        }
        List<ScoreHistory> historyList = historyService.selectUserHisotyList(scoreHistory, qrcode, postType, isDq);
        if (users.size() == 0) {
            map.put("msg", "????????????");
            map.put("code", 0);
        } else if (users.size() > historyList.size() && isDq) {
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
     * ????????????????????????????????????
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
                String year = history.getYear();
                String month = history.getMonth();
                boolean isDq = true;
                ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth("", "", history.getDbtype());
                if (year != null && !year.equals("") && month != null && !month.equals("")) {
                    if (!manualSetTime.getYear().equals(year) || !manualSetTime.getMonth().equals(month)) {
                        manualSetTime.setYear(year);
                        manualSetTime.setMonth(month);
                        manualSetTime.setDbtype(history.getDbtype());
                        isDq = false;
                    }
                }
                //???????????????
                initGradeList(map, state, manualSetTime, isDq);
                PageHelper.startPage(pageNum, pageSize);
                history.setYear(manualSetTime.getYear());
                history.setMonth(manualSetTime.getMonth());
                userDtoList = historyService.gradeList(history, isDq);
                PageInfo<ScoreHistory> pageInfo = new PageInfo<>(userDtoList);
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "????????????");
                map.put("data", userDtoList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("error", e.getMessage());
                map.put("msg", "????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
            map.put("code", 810);
        }
        return map;

    }

    private void initGradeList(ModelMap map, String state, ManualSetTime manualSetTime, boolean isDq) throws ParseException {
        List<User> users = userService.selectUserPfr(manualSetTime.getDbtype(), manualSetTime.getYear(), manualSetTime.getMonth(), isDq);
        //??????????????????selectUserPfr
        String year = CalendarUtil.getYear();
        //??????????????????
        String month = CalendarUtil.getMonth();
        //??????????????????
        String quarter = month;// CalendarUtil.getQuarter(month);
        //?????????????????????
        int count = Integer.parseInt(quarter.trim()) - 1;
        //????????????????????????
        String sysTime = DateUtil.getTime();

        ScoreHistory scoreHistory = new ScoreHistory();
//        getCurrentYearAndMonth(state, manualSetTime.getYear(), manualSetTime.getMonth(), count, sysTime, scoreHistory, dbtype);
        scoreHistory.setYear(manualSetTime.getYear());
        scoreHistory.setMonth(manualSetTime.getMonth());
        scoreHistory.setDbtype(manualSetTime.getDbtype());
        // ??????????????????
        List<ScoreHistory> historyList = historyService.selectGradeHisotyList(scoreHistory, isDq);
        if (users.size() == 0) {
            map.put("msg", "???????????????");
            map.put("code", 0);
        } else if (users.size() > historyList.size() && isDq) {
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
//        //????????????-????????????????????????
//        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
//        if (setTime != null) {
//
//            //????????????????????????
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
        //json??????????????????
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        userDtos = historyService.gradeList(dto, false);
        // ??????ExportExcel??????
        try {
            // ???????????????
            Workbook workbook = exportGradeBigDataExcel(userDtos);
            // ????????????
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            gradeDownFile(os, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private SXSSFWorkbook exportGradeBigDataExcel(List<ScoreHistory> employeeList) {
        // 1.????????????
        // 2.???????????????
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("??????????????????");
        String[] titles = {"??????", "????????????", "?????????", "????????????", "????????????", "????????????", "????????????"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.??????????????????????????????
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
        String fileName = "??????????????????????????????.xlsx";
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
        //json??????????????????
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
        userDtos = historyService.selectHistoryList(dto, null, null, true);
        // ??????ExportExcel??????
        try {
            // ???????????????
            Workbook workbook = exportBigDataExcel(userDtos, info1.getDbtype());
            // ????????????
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            downFile(os, response, dto.getYear() + "-" + dto.getMonth() + "??????????????????.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook exportBigDataExcel(List<ScoreHistory> employeeList, String dbtype) {
        // 1.????????????
        // 2.???????????????
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("sheet1");
        String[] titles = null;
        if (dbtype.equals("2")) {
            titles = new String[]{"??????", "????????????", "?????????", "????????????", "????????????", "??????", "????????????", "????????????", "????????????", "A", "B", "C", "D", "??????", "????????????", "??????", "????????????", "?????????"};
        } else {
            titles = new String[]{"??????", "????????????", "?????????", "????????????", "????????????", "??????", "????????????", "????????????", "????????????", "A", "B", "C", "D", "??????"};
        }
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.??????????????????????????????
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
                row.createCell(8).setCellValue(employee.getYear() + "(???" + employee.getMonth() + "??????)");
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
     * ????????????????????????????????????????????????????????????
     *
     * @return
     */
    @RequestMapping("/oneClickDown")
    public void oneClickDown(HttpServletResponse response, String year, String month, String dbtype) {
//        ModelMap map = new ModelMap();
        try {
            List<String> roleList = new ArrayList<>();
            if (dbtype != null && dbtype.equals("2")) {
                roleList.add("300");//????????????
            }
//            roleList.add("150");//????????????
            List<ScoreHistory> list = historyService.oneClickDown(year, month, roleList, dbtype);
            Workbook workbook = exportBigDataExcel(list, dbtype);
            // ????????????
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            downFile(os, response, year + "-" + month + "??????????????????????????????.xlsx");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
//            map.put("msg", "??????????????????");
//            map.put("error", e.getMessage());
//            map.put("eor", e.getStackTrace());
//            map.put("code", 1);
        }
//        return map;
    }
}
