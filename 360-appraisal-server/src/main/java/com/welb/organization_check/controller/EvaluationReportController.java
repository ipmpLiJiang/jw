package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserEvaluationDto;
import com.welb.organization_check.dto.UserScoreDto;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.util.CalendarUtil;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.JDBCType;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luoyaozu
 * @title: EvaluationReportController
 * @projectName xh-360appraisal-interface
 * @description: 评估报告控制类
 * @date 2019/6/199:50
 */
@RestController
@RequestMapping("/evaluation")
public class EvaluationReportController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IEvaluationReportService evaluationReportService;
    @Resource
    IUserService userService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IStationService stationService;
    @Resource
    IResultReportService resultReportService;
    @Resource
    IUserDtoService userDtoService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IScoreDetailService detailService;
    @Resource
    IManualSetTimeService setTimeService;
    @Autowired
    IUserScoreDtoService userScoreDtoService;

    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectReport(HttpServletRequest req, String usercode, String year, String month, String dbtype) {
        ModelMap map = new ModelMap();
        Map<String, Object> data = new LinkedHashMap<>();
        String usercode1 = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode1 != null) {
            EvaluationReport report = new EvaluationReport();
            ResultReport r1 = new ResultReport();
            ResultReport r2 = new ResultReport();
            User user = userService.findUserByUserCode(usercode);
            //保留两位小数
            DecimalFormat df = new DecimalFormat("0.00");
            //获取用户信息
            getUserInfo(report, user);
            try {
                if (year.equals("") && month.equals("")) {
                    //年份
//                    year = "";
                    //月份
//                    month = "";
                    //月度
//                    String quarter =month;// CalendarUtil.getQuarter(month);
//                    int count = Integer.parseInt(quarter) - 1;
                    //获取当前系统时间
                    String sysTime = DateUtil.getTime();

                        //手动考核-查看所有月节总结
                    manualSelectReport(usercode, year, map, data, report, r1, r2, user, df, sysTime, dbtype);

                } else if (!year.equals("") && !month.equals("")) {
                    getEvaluation(usercode, year, month, map, data, report, r1, r2, user, df,dbtype);

                } else {
                    map.put("msg", "年份和季度必须一起选择才可以搜索");
                    map.put("code", 1);
                }
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询测评报告失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "用户登录超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void manualSelectReport(String usercode, String year, ModelMap map, Map<String, Object> data, EvaluationReport report, ResultReport r1, ResultReport r2, User user, DecimalFormat df,  String sysTime, String dbtype) throws ParseException {
//        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                //开始新的月度考核
//                month = quarter;
        getEvaluation(usercode, setTime.getYear(), setTime.getMonth(), map, data, report, r1, r2, user, df,dbtype);
    }

    private void automaticSelectReport(String usercode, String year, ModelMap map, Map<String, Object> data, EvaluationReport report, ResultReport r1, ResultReport r2, User user, DecimalFormat df, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
            report.setYear(year);
        } else {
            report.setYear(year);
            month = String.valueOf(count);
        }
      //  getEvaluation(usercode, year, month, map, data, report, r1, r2, user, df);
    }

    private void getEvaluation(String usercode, String year, String month, ModelMap map, Map<String, Object> data, EvaluationReport report, ResultReport r1, ResultReport r2, User user, DecimalFormat df, String dbtype) {
        report.setYear(year);
        report.setMonth(month);
        report.setUsercode(usercode);
        report.setDbtype(dbtype);
        EvaluationReport evaluation = evaluationReportService.selectReportByUserCode(report);
        if (evaluation != null) {
            report.setBasicscore(evaluation.getBasicscore());
            report.setKeyscore(evaluation.getKeyscore());
            report.setZdScore(evaluation.getZdScore());
            report.setMbScore(evaluation.getMbScore());
            report.setSumMbAvgScore(evaluation.getSumMbAvgScore());
            report.setAvgscore(evaluation.getAvgscore());
            report.setTotalscore(evaluation.getTotalscore());
        } else {
            report.setBasicscore(0.0);
            report.setKeyscore(0.0);
            report.setZdScore(0.0);
            report.setMbScore(0.0);
            report.setSumMbAvgScore(0.0);
            report.setAvgscore(0.0);
            report.setTotalscore(0.0);
        }
        List<ScoreFlow> scoreFlows = flowService.selectScoreAllByScoredCode(report.getUsercode(), year, month,dbtype);
        getScoreInfo(report, df, user, year, month, scoreFlows, r1, r2, dbtype);
        getEvaluateReport(map, data, report, r1, r2,dbtype);
    }

    private void getEvaluateReport(ModelMap map, Map<String, Object> data, EvaluationReport report, ResultReport r1, ResultReport r2,String dbtype) {
        EvaluationReport evaluationReport = evaluationReportService.selectReportByUserCode(report);
        if (evaluationReport != null) {
            evaluationReport.setTotalscore(report.getTotalscore());
            evaluationReport.setKeyscore(report.getKeyscore());
            evaluationReport.setBasicscore(report.getBasicscore());
            evaluationReport.setMincomparelast(report.getMincomparelast());
            evaluationReport.setMaxcomparelast(report.getMaxcomparelast());
            evaluationReport.setTotalcomparelast(report.getTotalcomparelast());
            evaluationReport.setMaxscore(report.getMaxscore());
            evaluationReport.setMinscore(report.getMinscore());
            evaluationReport.setScoredifference(report.getScoredifference());
            evaluationReportService.updateByPrimaryKeySelective(evaluationReport);
//            int reportCount = evaluationReportService.selectMaxAndMinReportCount(report.getYear(), report.getMonth(),dbtype);
//            Double sumOfMaxScore = evaluationReportService.selectSumOfMaxScore(report.getYear(), report.getMonth(),dbtype);
//            Double sumOfMinScore = evaluationReportService.selectSumOfMinScore(report.getYear(), report.getMonth(),dbtype);
//            Double avgMaxScore = sumOfMaxScore == 0 ? 0.0 : sumOfMaxScore / reportCount;
//            Double avgMinScore = sumOfMinScore == 0 ? 0.0 : sumOfMinScore / reportCount;
//            Double maxCompareMark = report.getMaxscore() == null ? 0.0 : (report.getMaxscore() - avgMaxScore) / avgMaxScore;
//            Double minCompareMark = report.getMinscore() == null ? 0.0 : (report.getMinscore() - avgMinScore) / avgMinScore;
//            evaluationReport.setMaxcomparemark(maxCompareMark * 100);
//            evaluationReport.setMincomparemark(minCompareMark * 100);
//            evaluationReport.setMincomparelast(report.getMincomparelast());
//            evaluationReport.setMaxcomparelast(report.getMaxcomparelast());

//            第七步  计算个人得分较整体平均分差距半分比
//            double tt = 0.0;
//            if(report.getTotalscore()!=null && report.getAvgscore()!=null) {
//                tt = report.getTotalscore() - report.getAvgscore();
//            }
//            if(report.getTotalscore()!=null && report.getAvgscore()==null) {
//                tt = report.getTotalscore();
//            }
//            double totalCompareMark = tt == 0 ? 0.0 : tt / report.getAvgscore();
//            evaluationReport.setTotalcomparemark(totalCompareMark * 100);
//            evaluationReportService.updateByPrimaryKeySelective(evaluationReport);
//            ResultReport resultReport1 = resultReportService.selectResultReportByCode("0", evaluationReport.getId());
//            getResultReport(evaluationReport, r1, resultReport1);
//            ResultReport resultReport2 = resultReportService.selectResultReportByCode("1", evaluationReport.getId());
//            getResultReport(evaluationReport, r2, resultReport2);
            List<ResultReport> reports = resultReportService.selectResultReportByEvaluationCode(evaluationReport.getId());
            EvaluationReport newReprot = evaluationReportService.selectByPrimaryKey(evaluationReport.getId());
            newReprot.setMoneycard(report.getMoneycard());
            newReprot.setUsername(report.getUsername());
            newReprot.setStationname(report.getStationname());
            newReprot.setDepartmentname(report.getDepartmentname());
            if (reports.size() > 0) {
                data.put("reports", reports);
            } else {
                map.put("msg", "数据为空");
                map.put("code", 2);
            }
            this.getEvaluationReport0(newReprot);
            newReprot.setPostType(report.getPostType());
            data.put("evaluationReport", newReprot);
            map.put("msg", "查询测评报告成功");
            map.put("data", data);
            map.put("code", 0);

        } else {
            map.put("msg", "暂未生成个人估报告");
            map.put("code", 2);
        }
    }
    private  void  getEvaluationReport0(EvaluationReport newReprot){
        if(newReprot.getBasicscore() == null){
            newReprot.setBasicscore(0.0);
        }
        if(newReprot.getKeyscore() == null){
            newReprot.setKeyscore(0.0);
        }
        if(newReprot.getZdScore() == null){
            newReprot.setZdScore(0.0);
        }
        if(newReprot.getMbScore() == null){
            newReprot.setMbScore(0.0);
        }
        if(newReprot.getZfScore() == null){
            newReprot.setZfScore(0.0);
        }
        if(newReprot.getTotalscore() == null){
            newReprot.setTotalscore(0.0);
        }
        if(newReprot.getAvgscore() == null){
            newReprot.setAvgscore(0.0);
        }
        if(newReprot.getSumMbAvgScore() == null){
            newReprot.setSumMbAvgScore(0.0);
        }
    }

    private void getUserInfo(EvaluationReport report, User user) {
        report.setUsername(user.getUsername());
        report.setMoneycard(user.getMoneycard());
        report.setPostType(user.getPostType());
//        获取岗位和部门名称
        Station station = stationService.selectByStationCode(user.getStationcode());
        EvaluationReportController.getStationInfo(report, station, departmentService);
    }

    static void getStationInfo(EvaluationReport evaluationReport, Station station, IDepartmentService departmentService) {
        if (station != null) {
            evaluationReport.setStationname(station.getStationname());
            Department department = departmentService.selectByDeptCode(station.getDepartmentcode());
            if (department != null) {
                evaluationReport.setDepartmentname(department.getDepartmentname());
            } else {
                evaluationReport.setDepartmentname("无部门");
            }
        } else {
            evaluationReport.setStationname("无岗位");
        }
    }

    private void getResultReport(EvaluationReport evaluationReport, ResultReport r, ResultReport resultReport) {
        if (resultReport == null) {
            r.setEvaluationreportcode(evaluationReport.getId());
            resultReportService.insertSelective(r);
        } else {
            r.setId(resultReport.getId());
            resultReportService.updateByPrimaryKeySelective(r);
        }
    }

    private void getScoreInfo(EvaluationReport report, DecimalFormat df, User user, String year, String month, List<ScoreFlow> scoreFlows, ResultReport r1, ResultReport r2,String dbtype) {
        if (scoreFlows.size() > 0) {
            String mserialno = scoreFlows.get(0).getMserialno();
//            第二步  计算总分
//            getReportTotalScore(report, df, user, mserialno, r1, r2);
//            第三步  获取最高分和最低分
            Double maxScore = flowService.selectMaxScoreByScoredCode(report.getUsercode(), year, month,dbtype);
            Double minScore = flowService.selectMinScoreByScoredCode(report.getUsercode(), year, month,dbtype);
            report.setMaxscore(maxScore);
            report.setMinscore(minScore);
//            第四步  计算分值差
            double scoreDifference = maxScore - minScore;
            report.setScoredifference(scoreDifference);
            EvaluationReport report1 = new EvaluationReport();
            report1.setUsercode(report.getUsercode());
            report1.setDbtype(dbtype);
            int count1 = Integer.parseInt(month) - 1;
            if (count1 == 0) {
                int lastyear1 = Integer.parseInt(year) - 1;
                report1.setYear(String.valueOf(lastyear1));
                if(dbtype!=null&& dbtype.equals("2")) {
                    report1.setMonth("12");
                }
                else{// 党部是按季度考核
                    report1.setMonth("4");
                }
                EvaluationReport report2 = evaluationReportService.selectReportByUserCode(report1);
                getCompareLastInfo(report, maxScore, minScore, report2);
            } else {
                report1.setYear(year);
                report1.setMonth(String.valueOf(count1));
                EvaluationReport report2 = evaluationReportService.selectReportByUserCode(report1);
                getCompareLastInfo(report, maxScore, minScore, report2);
            }


        }

    }

    private void getCompareLastInfo(EvaluationReport report, Double maxScore, Double minScore, EvaluationReport report2) {
        if (report2 == null) {
            report.setMaxcomparelast(0.0);
            report.setMincomparelast(0.0);
            report.setTotalcomparelast(0.0);
        } else {
            if (report2.getMinscore() != null && report2.getMaxscore() != null) {
                double maxCompareLast = maxScore - report2.getMaxscore();
                double minCompareLast = minScore - report2.getMinscore();
                double tt = report.getTotalscore() - report2.getTotalscore();
                double totalCompareLast = tt == 0 ? 0.0 : tt / report2.getTotalscore();
                report.setMaxcomparelast(maxCompareLast);
                report.setMincomparelast(minCompareLast);
                report.setTotalcomparelast(totalCompareLast * 100);
            } else {
                report.setMaxcomparelast(0.0);
                report.setMincomparelast(0.0);
                report.setTotalcomparelast(0.0);
            }
        }
    }

    private void getBasicTotalScore(EvaluationReport report, User user, String mserialno, int Acount, int Bcount, int Ccount, int Dcount, DecimalFormat df, ResultReport r1) {
        Double basicAScore = detailService.getTotalScoreByType(mserialno, "A", "0");
        Double basicAScore1;
        Double totalRatio = 0.0;
        if (basicAScore == null) {
            basicAScore1 = 0.0;

        } else {
            basicAScore1 = Double.parseDouble(df.format(basicAScore == 0 ? 0.0 : basicAScore / Acount));
            totalRatio += user.getAratio();
        }
        Double basicBScore = detailService.getTotalScoreByType(mserialno, "B", "0");
        Double basicBScore1;
        if (basicBScore == null) {
            basicBScore1 = 0.0;
        } else {
            basicBScore1 = Double.parseDouble(df.format(basicBScore == 0 ? 0.0 : basicBScore / Bcount));
            totalRatio += user.getBratio();
        }
        Double basicCScore = detailService.getTotalScoreByType(mserialno, "C", "0");
        Double basicCScore1;
        if (basicCScore == null) {
            basicCScore1 = 0.0;
        } else {
            basicCScore1 = Double.parseDouble(df.format(basicCScore == 0 ? 0.0 : basicCScore / Ccount));
            totalRatio += user.getCratio();

        }
        Double basicDScore = detailService.getTotalScoreByType(mserialno, "D", "0");
        Double basicDScore1;
        if (basicDScore == null) {
            basicDScore1 = 0.0;
        } else {
            basicDScore1 = basicDScore == 0 ? 0.0 : Double.parseDouble(df.format(basicDScore == 0 ? 0.0 : basicDScore / Dcount));
            totalRatio += user.getDratio();
        }
        Double totalBasicScore = basicAScore1 * user.getAratio() + basicBScore1 * user.getBratio() + basicCScore1 * user.getCratio() + basicDScore1 * user.getDratio();
        String format = totalBasicScore == 0 ? "0" : df.format(totalBasicScore == 0 ? 0.0 : totalBasicScore / totalRatio);
        report.setBasicscore(Double.parseDouble(format));
        r1.setResultreportname("基础评分");
        r1.setResultreportcode("0");
        r1.setAscore(basicAScore1);
        r1.setBscore(basicBScore1);
        r1.setCscore(basicCScore1);
        r1.setDscore(basicDScore1);
        r1.setScore(report.getBasicscore());
        r1.setAvgscore(report.getAvgscore() * 0.2);

    }

    private void getReportTotalScore(EvaluationReport report, DecimalFormat df, User user, String mserialno, ResultReport r1, ResultReport r2,String dbtype) {
//      获取各类评分人的数量
        int Acount = flowService.getScoreByTypeCount(mserialno, "A",dbtype);
        int Bcount = flowService.getScoreByTypeCount(mserialno, "B",dbtype);
        int Ccount = flowService.getScoreByTypeCount(mserialno, "C",dbtype);
        int Dcount = flowService.getScoreByTypeCount(mserialno, "D",dbtype);
//     计算基础得分
        getBasicTotalScore(report, user, mserialno, Acount, Bcount, Ccount, Dcount, df, r1);

//     计算关键得分
        getTotalKeyScore(report, user, mserialno, Acount, Bcount, Ccount, Dcount, df, r2);

    }


    private void getTotalKeyScore(EvaluationReport report, User user, String mserialno, int Acount, int Bcount, int Ccount, int Dcount, DecimalFormat df, ResultReport r2) {
        Double keyAScore = detailService.getTotalScoreByType(mserialno, "A", "1");
        Double keyAScore1;
        Double totalRatio = 0.0;
        if (keyAScore == null) {
            keyAScore1 = 0.0;
        } else {
            keyAScore1 = Double.parseDouble(df.format(keyAScore == 0 ? 0.0 : keyAScore / Acount));
            totalRatio += user.getAratio();
        }
        Double keyBScore = detailService.getTotalScoreByType(mserialno, "B", "1");
        Double keyBScore1;
        if (keyBScore == null) {
            keyBScore1 = 0.0;
        } else {
            keyBScore1 = Double.parseDouble(df.format(keyBScore == 0 ? 0.0 : keyBScore / Bcount));
            totalRatio += user.getBratio();
        }
        Double keyCScore = detailService.getTotalScoreByType(mserialno, "C", "1");
        Double keyCScore1;
        if (keyCScore == null) {
            keyCScore1 = 0.0;
        } else {
            keyCScore1 = Double.parseDouble(df.format(keyCScore == 0 ? 0.0 : keyCScore / Ccount));
            totalRatio += user.getCratio();
        }
        Double keyDScore = detailService.getTotalScoreByType(mserialno, "D", "1");
        Double keyDScore1;
        if (keyDScore == null) {
            keyDScore1 = 0.0;
        } else {
            keyDScore1 = Double.parseDouble(df.format(keyDScore == 0 ? 0.0 : keyDScore / Dcount));
            totalRatio += user.getDratio();
        }
//        Double totalKeyScore = report.getTotalscore() - report.getBasicscore();

        Double totalBasicScore = keyAScore1 * user.getAratio() + keyBScore1 * user.getBratio() + keyCScore1 * user.getCratio() + keyDScore1 * user.getDratio();
        String format = totalBasicScore == 0 ? "0" : df.format(totalBasicScore == 0 ? 0.0 : totalBasicScore / totalRatio);
        report.setKeyscore(Double.parseDouble(format));

//        report.setKeyscore(Double.parseDouble(df.format(totalKeyScore)));
        r2.setResultreportcode("1");
        r2.setResultreportname("岗位评分");
        r2.setAscore(keyAScore1);
        r2.setBscore(keyBScore1);
        r2.setCscore(keyCScore1);
        r2.setDscore(keyDScore1);
        r2.setScore(report.getKeyscore());
        r2.setAvgscore(report.getAvgscore() * 0.8);
    }


    /**
     * 组织部查询所有人员测评报告
     *
     * @param req
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/selectAllReport", produces = "application/json;charset=utf-8")
    public Object selectAllReport(HttpServletRequest req, int pageNum, int pageSize, UserEvaluationDto evaluationDto, String dbtype) {
        ModelMap map = new ModelMap();
        UserDto dto = new UserDto();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null)
            try {
                List<UserEvaluationDto> evaluationReports;
                if (evaluationDto.getYear().equals("") && evaluationDto.getMonth().equals("")) {

                    //年份
                    String year = CalendarUtil.getYear();
                    //月份
                    String month = CalendarUtil.getMonth();
                    //月度
                    String quarter =month;// CalendarUtil.getQuarter(month);
                    int count = Integer.parseInt(quarter) - 1;
                    //获取当前系统时间
                    String sysTime = DateUtil.getTime();

                    //手动考核-查看所有月节总结
                    manualSelectAllReport(evaluationDto, year, quarter, count, sysTime, dbtype);
                }

                //计算整体平均分--2020.7.15修改
//                DecimalFormat formart = new DecimalFormat("0.00");
//                Double totalScore = evaluationReportService.selectReportTotalScore(evaluationDto.getYear(), evaluationDto.getMonth());
//                int reportCount = evaluationReportService.selectReportCount(evaluationDto.getYear(), evaluationDto.getMonth());
//                if (totalScore!=null){
//                double avgScore = totalScore == 0 ? 0.0 :totalScore / reportCount;
//                Double formatAvgScore = Double.valueOf(formart.format(avgScore));
//                evaluationReportService.updateAvgScore(formatAvgScore,evaluationDto.getYear(), evaluationDto.getMonth());
                PageHelper.startPage(pageNum, pageSize);
                List<String> roleList = new ArrayList<>();
                if(dbtype.equals("2")) {
                    roleList.add("300");//普通用戶
                }
                evaluationReports = evaluationReportService.selectAllEvaluationReportLike(evaluationDto,roleList);
                if (evaluationReports.size() > 0) {
                    for (UserEvaluationDto evaluation : evaluationReports) {
                        dto.setYear(evaluation.getYear());
                        dto.setMonth(evaluation.getMonth());
                        dto.setUsername(evaluation.getUsername());
                        dto.setUsercode(evaluation.getUsercode());
                        dto.setDbtype(dbtype);

                        List<UserDto> userDtos = userDtoService.selectUserDtoByUserCode(dto,roleList);
                        //获取岗位信息
                        getStationName(userDtos, dto, evaluationDto);
                        //获取各类评分的分数
                        getScoreTypeMarks(evaluation, userDtos, dbtype);
                       /* DecimalFormat formart = new DecimalFormat("0.00");
                        //计算整体平均分
                        Double totalScore = evaluationReportService.selectReportTotalScore(dto.getYear(), dto.getMonth());

                        int reportCount = evaluationReportService.selectReportCount(dto.getYear(), dto.getMonth());
                        double avgScore = totalScore / reportCount;
                        Double formatAvgScore = Double.valueOf(formart.format(avgScore));
                        evaluation.setAvgscore(formatAvgScore);*/
                        //填充evaluation信息
//                        EvaluationReport report = getEvaluationReport(evaluation);
//                        report.setAvgscore(evaluation.getAvgscore());
//                        evaluationReportService.updateByPrimaryKeySelective(report);

                    }

                    PageInfo<UserEvaluationDto> pageInfo = new PageInfo<>(evaluationReports);
                    evaluationReports = pageInfo.getList();
                    map.put("totalPages", pageInfo.getTotal());
                    map.put("msg", "查询成功");
                    map.put("data", evaluationReports);
                    map.put("code", 0);
                }else {
                    map.put("msg", "数据为空");
                    map.put("code", 0);
                }
//                } else {
//                    map.put("msg", "数据为空");
//                    map.put("code", 0);
//                }

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        else {
            map.put("msg", "登录用户超时，请重新登陆");
            map.put("code", 810);
        }
        return map;
    }

    private void manualSelectAllReport(UserEvaluationDto evaluationDto, String year, String quarter, int count, String sysTime, String dbtype) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
        if (setTime != null) {

                //开始新的月度考核
                month = quarter;
                evaluationDto.setYear(setTime.getYear());
                evaluationDto.setMonth(setTime.getMonth());
        }
    }

    private void automaticSelectAllReport(UserEvaluationDto evaluationDto, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";

        } else {
            month = String.valueOf(count);
        }
        evaluationDto.setYear(year);
        evaluationDto.setMonth(month);
    }

    private EvaluationReport getEvaluationReport(UserEvaluationDto evaluation) {
        EvaluationReport report = new EvaluationReport();
        report.setId(evaluation.getId());
        report.setAscore(evaluation.getAscore());
        report.setBscore(evaluation.getBscore());
        report.setCscore(evaluation.getCscore());
        report.setDscore(evaluation.getDscore());
        report.setDepartmentname(evaluation.getDepartmentname());
        report.setStationname(evaluation.getStationname());
        //计算后是最终值不需要再重新赋值
//        report.setTotalscore(evaluation.getTotalscore());
        report.setAvgscore(evaluation.getAvgscore());
        return report;
    }

    private void getScoreTypeMarks(UserEvaluationDto evaluation, List<UserDto> userDtos, String dbtype) {
        if (userDtos.size() > 0) {
            for (UserDto dto1 : userDtos) {
                if (dto1.getUsercode().equals(evaluation.getUsercode())) {
                    evaluation.setAscore(dto1.getAScore());
                    evaluation.setBscore(dto1.getBScore());
                    evaluation.setCscore(dto1.getCScore());
                    evaluation.setDscore(dto1.getDScore());
                    evaluation.setEscore(dto1.getEScore());
                    evaluation.setFscore(dto1.getFScore());
                    evaluation.setDbtype(dbtype);
                    evaluation.setMoneycard(dto1.getMoneycard());
                    evaluation.setStationname(dto1.getStationname());
                    evaluation.setDepartmentname(dto1.getDepartmentname());
                    evaluation.setMserialno(dto1.getSerialno());
                }
            }
        } else {
            evaluation.setDbtype(dbtype);
            evaluation.setTotalscore(0.0);
            evaluation.setAscore(0.0);
            evaluation.setBscore(0.0);
            evaluation.setCscore(0.0);
            evaluation.setDscore(0.0);
            evaluation.setEscore(0.0);
            evaluation.setFscore(0.0);
        }
    }


    private void getStationName(List<UserDto> userDtoList, UserDto dto, UserEvaluationDto evaluationDto) {
        //保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            for (UserDto userDto : userDtoList) {
                if (userDto.getUsercode().equals(dto.getUsercode())) {
//                    if(dto.getDbtype().equals("1")) {
                        //获取各类评分的总人数和分数
                        Double Ascore = flowService.getScoreByType(userDto.getSerialno(), "A", dto.getDbtype());
                        int Acount = flowService.getScoreByTypeCount(userDto.getSerialno(), "A", dto.getDbtype());
                        Double totalRatio = 0.0;
                        Double Ascore1;
                        if (Ascore == null) {
                            Ascore1 = 0.0;
                            userDto.setAScore(Ascore1);
                        } else {
                            Ascore1 = Ascore == 0 ? 0.0 : Ascore / Acount;//A类总分/A类人数
                            userDto.setAScore(Double.parseDouble(df.format(Ascore1)));
                            totalRatio += userDto.getAratio();
                        }
                        Double Bscore = flowService.getScoreByType(userDto.getSerialno(), "B", dto.getDbtype());
                        int Bcount = flowService.getScoreByTypeCount(userDto.getSerialno(), "B", dto.getDbtype());
                        Double Bscore1;
                        if (Bscore == null) {
                            Bscore1 = 0.0;
                            userDto.setBScore(Bscore1);
                        } else {
                            Bscore1 = Bscore == 0 ? 0.0 : Bscore / Bcount;
                            userDto.setBScore(Double.parseDouble(df.format(Bscore1)));
                            totalRatio += userDto.getBratio();
                        }
                        Double Cscore = flowService.getScoreByType(userDto.getSerialno(), "C", dto.getDbtype());
                        int Ccount = flowService.getScoreByTypeCount(userDto.getSerialno(), "C", dto.getDbtype());
                        Double Cscore1;
                        if (Cscore == null) {
                            Cscore1 = 0.0;
                            userDto.setCScore(Cscore1);
                        } else {
                            Cscore1 = Cscore == 0 ? 0.0 : Cscore / Ccount;
                            userDto.setCScore(Double.parseDouble(df.format(Cscore1)));
                            totalRatio += userDto.getCratio();
                        }
                        Double Dscore = flowService.getScoreByType(userDto.getSerialno(), "D", dto.getDbtype());
                        int Dcount = flowService.getScoreByTypeCount(userDto.getSerialno(), "D", dto.getDbtype());
                        Double Dscore1;
                        if (Dscore == null) {
                            Dscore1 = 0.0;
                            userDto.setDScore(Dscore1);
                        } else {
                            Dscore1 = Dscore == 0 ? 0.0 : Dscore / Dcount;
                            userDto.setDScore(Double.parseDouble(df.format(Dscore1)));
                            totalRatio += userDto.getDratio();
                        }
                        Double Escore = flowService.getScoreByType(userDto.getSerialno(), "E", dto.getDbtype());
                        int Ecount = flowService.getScoreByTypeCount(userDto.getSerialno(), "E", dto.getDbtype());
                        Double Escore1;
                        if (Escore == null) {
                            Escore1 = 0.0;
                            userDto.setEScore(Escore1);
                        } else {
                            Escore1 = Escore == 0 ? 0.0 : Escore / Ecount;
                            userDto.setEScore(Double.parseDouble(df.format(Escore1)));
                            totalRatio += userDto.getEratio();
                        }
                        Double Fscore = flowService.getScoreByType(userDto.getSerialno(), "F", dto.getDbtype());
                        int Fcount = flowService.getScoreByTypeCount(userDto.getSerialno(), "F", dto.getDbtype());
                        Double Fscore1;
                        if (Fscore == null) {
                            Fscore1 = 0.0;
                            userDto.setFScore(Fscore1);
                        } else {
                            Fscore1 = Fscore == 0 ? 0.0 : Fscore / Fcount;
                            userDto.setFScore(Double.parseDouble(df.format(Fscore1)));
                            totalRatio += userDto.getFratio();
                        }
                        //获取总分  总分= A类总分 X A类评分人系数 +  B类总分 X B类评分人系数 + C类总分 X C类评分人系数 + D类总分 X D类评分人系数

                        Double totalscore = userDto.getAScore() * userDto.getAratio() + userDto.getBScore() * userDto.getBratio() +
                                userDto.getCScore() * userDto.getCratio() + userDto.getDScore() * userDto.getDratio() +
                                userDto.getEScore() * userDto.getEratio() + userDto.getFScore() * userDto.getFratio();
                        if (totalscore == 0.0 || totalscore == null) {
                            userDto.setTotalScore(0.0);
                        } else {
                            userDto.setTotalScore(Double.parseDouble(df.format(totalscore == 0 ? 0.0 : totalscore / totalRatio)));
                        }
//                    }else{
//                        List<UserScoreDto> scoreDtoList = userScoreDtoService.findUserDutyScore(dto.getYear(),dto.getMonth(),dto.getUsercode(),null,dto.getDbtype());
//                        List<UserScoreDto> dutyAndRatioList = userScoreDtoService.getTypeUserDutyScore(scoreDtoList,false);
//                        Double sumScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getScore).sum();
//                        Double aScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getAscore).sum();
//                        Double bScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getBscore).sum();
//                        Double cScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getCscore).sum();
//                        Double dScore = dutyAndRatioList.stream().mapToDouble(UserScoreDto::getDscore).sum();
//                        userDto.setAScore(Double.parseDouble(df.format(aScore)));
//                        userDto.setBScore(Double.parseDouble(df.format(bScore)));
//                        userDto.setCScore(Double.parseDouble(df.format(cScore)));
//                        userDto.setDScore(Double.parseDouble(df.format(dScore)));
//                        userDto.setTotalScore(Double.parseDouble(df.format(sumScore)));
//                    }
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
        }
    }


    /**
     * 部门长查询部门下的所有人员测评报告
     *
     * @return
     */


    @RequestMapping(value = "/selectDeptReport", produces = "application/json;charset=utf-8")
    public Object selectDeptReport(HttpServletRequest req, String dbtype) throws ParseException {
        ModelMap map = new ModelMap();
        //获取登录用户
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            //创建userdto对象
            UserDto dto = new UserDto();
            //创建评估报告集合
            List<EvaluationReport> reports = new ArrayList<>();
            //通过用户code查找用户信息
            User user = userService.findUserByUserCode(usercode);
            //通过岗位code查找岗位信息
            Station station = stationService.selectByStationCode(user.getStationcode());
            if (station != null) {
                //查询登录用户所在部门下的所有员工信息
                List<User> users = userService.selectUserByDepartmentCode(station.getDepartmentcode());
                if (users.size() > 0) {
                    String year = CalendarUtil.getYear();
                    String month = CalendarUtil.getMonth();
                    String quarter = CalendarUtil.getQuarter(month);
                    int count = Integer.parseInt(quarter.trim()) - 1;
                    //获取当前系统时间
                    String sysTime = DateUtil.getTime();

                        //手动考核-部门长查询部门下的所有人员测评报告
                        manualSelectDeptReport(reports, users, year, quarter, count, sysTime, dbtype);

                    for (EvaluationReport report : reports) {
                        User user1 = userService.findUserByUserCode(report.getUsercode());
                        report.setUsername(user1.getUsername());
                        Station stationReport = stationService.selectByStationCode(user1.getStationcode());
                        if (stationReport != null) {
                            report.setStationname(stationReport.getStationname());
                        } else {
                            report.setStationname("");
                        }

                    }
                    map.put("msg", "查询成功");
                    map.put("data", reports);
                    map.put("code", 0);
                } else {
                    map.put("msg", "数据为空");
                    map.put("code", 2);
                }
            } else {
                map.put("msg", "没有该岗位");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时，请重新登陆");
            map.put("code", 810);
        }
        return map;
    }

    private void manualSelectDeptReport(List<EvaluationReport> reports, List<User> users, String year, String quarter, int count, String sysTime, String dbtype) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
        if (setTime != null) {
                //开始新的月度考核
                month = quarter;
                getReports(reports, users, setTime.getYear(), setTime.getMonth(),dbtype);

        }

    }

    private void getReports(List<EvaluationReport> reports, List<User> users, String year, String month, String dbtype) {
        for (User user1 : users) {
            EvaluationReport report = new EvaluationReport();
            report.setMonth(month);
            report.setYear(year);
            report.setUsercode(user1.getUsercode());

            //计算整体平均分--2020.7.15修改
            DecimalFormat formart = new DecimalFormat("0.00");
            Double totalScore = evaluationReportService.selectReportTotalScore(report.getYear(), report.getMonth(), dbtype);
            int reportCount = evaluationReportService.selectReportCount(report.getYear(), report.getMonth(), dbtype);
            double avgScore = totalScore == 0 ? 0.0 : totalScore / reportCount;
            Double formatAvgScore = Double.valueOf(formart.format(avgScore));
            evaluationReportService.updateAvgScore(formatAvgScore,report.getYear(), report.getMonth(), dbtype);

            //查找个人测评报告
            EvaluationReport report1 = evaluationReportService.selectReportByUserCode(report);
            if (report1 != null) {
                reports.add(report1);
            }
        }
    }

}
