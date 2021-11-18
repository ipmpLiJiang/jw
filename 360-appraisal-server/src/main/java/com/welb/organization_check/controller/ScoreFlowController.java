package com.welb.organization_check.controller;

import com.welb.organization_check.dto.ScoreFlowScorringTjDto;
import com.welb.organization_check.dto.UserScoreDto;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.ScoreFlow;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IManualSetTimeService;
import com.welb.organization_check.service.IScoreFlowService;
import com.welb.organization_check.service.IUserScoreDtoService;
import com.welb.organization_check.service.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scoreflow")
public class ScoreFlowController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IScoreFlowService scoreFlowService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IUserScoreDtoService userScoreDtoService;
    @Resource
    IUserService userService;

    @RequestMapping(value = "/scoreFlowTjList", produces = "application/json;charset=utf-8")
    public Object selectScoreFlowTjList(HttpServletRequest req, String year, String month, String dbtype, String postType) {
        ModelMap map = new ModelMap();
        String userCode = (String) req.getSession().getAttribute("usercode");
        if (userCode != null) {
            try {
                if (year == null || month == null || year.equals("") || month.equals("")) {
                    ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                    year = setTime.getYear();
                    month = setTime.getMonth();
                }

                List<ScoreFlow> flowList = scoreFlowService.selectSummaryFlowByYMTOrPTList(year, month, dbtype, postType);
                List<ScoreFlowScorringTjDto> list = new ArrayList<>();
                ScoreFlowScorringTjDto dto1 = new ScoreFlowScorringTjDto();
                dto1.setNum(1);
                dto1.setScoreProj("行政后勤科室");
                dto1.setPosttype("3");
                this.getFlowABCDEFPostTypeList(flowList, dto1, dto1.getPosttype());

                ScoreFlowScorringTjDto dto2 = new ScoreFlowScorringTjDto();
                dto2.setNum(2);
                dto2.setScoreProj("临床科主任");
                dto2.setPosttype("1");
                this.getFlowABCDEFPostTypeList(flowList, dto2, dto2.getPosttype());

                ScoreFlowScorringTjDto dto3 = new ScoreFlowScorringTjDto();
                dto3.setNum(3);
                dto3.setScoreProj("临床护士长");
                dto3.setPosttype("2");
                this.getFlowABCDEFPostTypeList(flowList, dto3, dto3.getPosttype());

                ScoreFlowScorringTjDto dto4 = new ScoreFlowScorringTjDto();
                dto4.setNum(4);
                dto4.setScoreProj("合计");
                dto4.setKhrs(dto1.getKhrs() + dto2.getKhrs() + dto3.getKhrs());
                dto4.setYcyABCrs(dto1.getYcyABCrs() + dto2.getYcyABCrs() + dto3.getYcyABCrs());
                dto4.setYcyDrs(dto1.getYcyDrs() + dto2.getYcyDrs() + dto3.getYcyDrs());
                dto4.setYcyEFrs(dto1.getYcyEFrs() + dto2.getYcyEFrs() + dto3.getYcyEFrs());
                dto4.setSjcyABCrs(dto1.getSjcyABCrs() + dto2.getSjcyABCrs() + dto3.getSjcyABCrs());
                dto4.setSjcyDrs(dto1.getSjcyDrs() + dto2.getSjcyDrs() + dto3.getSjcyDrs());
                dto4.setSjcyEFrs(dto1.getSjcyEFrs() + dto2.getSjcyEFrs() + dto3.getSjcyEFrs());
                list.add(dto1);
                list.add(dto2);
                list.add(dto3);
                list.add(dto4);
                map.put("totalPages", list.size());
                map.put("msg", "测评打分情况统计表成功");
                map.put("data", list);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "测评打分情况统计表失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }

    private void getFlowABCDEFPostTypeList(List<ScoreFlow> flowList, ScoreFlowScorringTjDto dto, String postType) {
//        List<ScoreFlowScorringTjDto> list = new ArrayList<>();
//        List<ScoreFlowScorringTjDto> query = new ArrayList<>();
        List<ScoreFlow> flowPostTypeList = new ArrayList<>();
        List<ScoreFlow> flowABCList = new ArrayList<>();
        List<ScoreFlow> flowDList = new ArrayList<>();
        List<ScoreFlow> flowEFList1 = new ArrayList<>();
        List<ScoreFlow> flowEFList = new ArrayList<>();
        List<ScoreFlow> list = new ArrayList<>();
        List<ScoreFlow> flowABCDEFList = new ArrayList<>();
        flowPostTypeList = flowList.stream().filter(s -> s.getPosttype().equals(postType)).collect(Collectors.toList());
        if (flowPostTypeList.size() > 0) {
            flowABCList = flowPostTypeList.stream().filter(s -> s.getScoretype().equals("A") || s.getScoretype().equals("B") ||
                    s.getScoretype().equals("C")).collect(Collectors.toList());

            flowDList = flowPostTypeList.stream().filter(s -> s.getScoretype().equals("D")).collect(Collectors.toList());

            flowEFList1 = flowPostTypeList.stream().filter(s -> s.getScoretype().equals("E") || s.getScoretype().equals("F")).collect(Collectors.toList());

            for (ScoreFlow flow : flowEFList1) {
                if (flowEFList.stream().filter(s -> s.getScoredcode().equals(flow.getScoredcode()) &&
                        s.getScorringcode().equals(flow.getScorringcode()) && s.getScoreState().equals(flow.getScoreState())).count() == 0){
                    ScoreFlow flowEF = new ScoreFlow();
                    flowEF.setScoredcode(flow.getScoredcode());
                    flowEF.setScorringcode(flow.getScorringcode());
                    flowEF.setScoreState(flow.getScoreState());
                    flowEFList.add(flowEF);
                }
            }
            int count = 0;
            count = flowPostTypeList.stream().collect(Collectors.groupingBy(ScoreFlow::getScoredcode)).size();
            dto.setKhrs(count);

//            count = flowABCList.stream().collect(Collectors.groupingBy(ScoreFlow::getScorringcode)).size();
//            dto.setYcyABCrs(count);
//            count = flowDList.stream().collect(Collectors.groupingBy(ScoreFlow::getScorringcode)).size();
//            dto.setYcyDrs(count);
//            count = flowEFList.stream().collect(Collectors.groupingBy(ScoreFlow::getScorringcode)).size();
//            dto.setYcyEFrs(count);
            for (ScoreFlow flow : flowABCList) {
                if (list.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode())).count() == 0) {
                    dto.setYcyABCrs(dto.getYcyABCrs() + 1);
                    flowABCDEFList = flowABCList.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode()) &&
                            s.getScoreState().equals("2")).collect(Collectors.toList());
                    if(flowABCDEFList.size() > 0) {
                        dto.setSjcyABCrs(dto.getSjcyABCrs() + 1);
                    }
                    ScoreFlow item = new ScoreFlow();
                    item.setScorringcode(flow.getScorringcode());
                    list.add(item);
                }
            }
            flowABCDEFList.clear();
            list.clear();
            for (ScoreFlow flow : flowDList) {
                if (list.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode())).count() == 0) {
                    dto.setYcyDrs(dto.getYcyDrs() + 1);
                    flowABCDEFList = flowDList.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode()) &&
                            s.getScoreState().equals("2")).collect(Collectors.toList());
                    if(flowABCDEFList.size() > 0) {
                        dto.setSjcyDrs(dto.getSjcyDrs() + 1);
                    }
                    ScoreFlow item = new ScoreFlow();
                    item.setScorringcode(flow.getScorringcode());
                    list.add(item);
                }
            }
            flowABCDEFList.clear();
            list.clear();
            for (ScoreFlow flow : flowEFList) {
                if (list.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode())).count() == 0) {
                    dto.setYcyEFrs(dto.getYcyEFrs() + 1);
                    flowABCDEFList = flowEFList.stream().filter(s -> s.getScorringcode().equals(flow.getScorringcode()) &&
                            s.getScoreState().equals("2")).collect(Collectors.toList());
                    if(flowABCDEFList.size() > 0) {
                        dto.setSjcyEFrs(dto.getSjcyEFrs() + 1);
                    }
                    ScoreFlow item = new ScoreFlow();
                    item.setScorringcode(flow.getScorringcode());
                    list.add(item);
                }
            }

//            for (ScoreFlow flow : flowPostTypeList) {
//                if (list.stream().filter(s -> s.getScorredcode().equals(flow.getScoredcode())).count() == 0) {
//                    dto.setKhrs(dto.getKhrs() + 1);
//                    flowABCDEFList = flowABCList.stream().filter(s -> s.getScoredcode().equals(flow.getScoredcode())).collect(Collectors.toList());
//                    for (ScoreFlow sbcFlow : flowABCDEFList) {
//                        if (sbcFlow.getScoreState().equals("2")) {
//                            dto.setSjcyABCrs(dto.getSjcyABCrs() + 1);
//                        }
//                    }
//                    flowABCDEFList = flowDList.stream().filter(s -> s.getScoredcode().equals(flow.getScoredcode())).collect(Collectors.toList());
//                    for (ScoreFlow dflow : flowABCDEFList) {
//                        if (dflow.getScoreState().equals("2")) {
//                            dto.setSjcyDrs(dto.getSjcyDrs() + 1);
//                        }
//                    }
//                    flowABCDEFList = flowEFList.stream().filter(s -> s.getScoredcode().equals(flow.getScoredcode())).collect(Collectors.toList());
//                    for (ScoreFlow efFlow : flowABCDEFList) {
//                        if (efFlow.getScoreState().equals("2")) {
//                            dto.setSjcyEFrs(dto.getSjcyEFrs() + 1);
//                        }
//                    }
//                    ScoreFlowScorringTjDto item = new ScoreFlowScorringTjDto();
//                    item.setScorredcode(flow.getScoredcode());
//                    list.add(item);
//                }
//            }
        }
    }

    @RequestMapping(value = "/scoreFlowDetailTjList", produces = "application/json;charset=utf-8")
    public Object selectScoreFlowTjList(HttpServletRequest req, String year, String month, String dbtype, String postType, String scoreType) {
        ModelMap map = new ModelMap();
        String userCode = (String) req.getSession().getAttribute("usercode");
        if (userCode != null) {
            try {
                if (year == null || month == null || year.equals("") || month.equals("")) {
                    ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                    year = setTime.getYear();
                    month = setTime.getMonth();
                }
                List<String> typeList = this.getTypeList(scoreType);
                List<UserScoreDto> usdto = userScoreDtoService.selectUserDetailByYMDTAndPTAndSTList(year, month, dbtype, postType, typeList);
                List<ScoreFlowScorringTjDto> list = this.getScorringTjDto(usdto);
                for (ScoreFlowScorringTjDto item : list) {
                    User userQuery = new User();
                    userQuery.setUsercode(item.getScorringcode());
                    User user = userService.selectUserBuGwByMoneyCard(userQuery);
                    if (user != null)
                        item.setScorringname(user.getUsername());
                }
                if (list.size() > 0) {
                    list = list.stream().sorted(Comparator.comparing(ScoreFlowScorringTjDto::getYcyrs).reversed()).collect(Collectors.toList());
                }
                map.put("totalPages", list.size());
                map.put("msg", "打分详情统计表成功");
                map.put("data", list);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "打分详情统计表失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }

    private List<ScoreFlowScorringTjDto> getScorringTjDto(List<UserScoreDto> usdtos) {
        List<ScoreFlowScorringTjDto> list = new ArrayList<>();
        List<ScoreFlowScorringTjDto> ssfList = new ArrayList<>();
        List<UserScoreDto> query = new ArrayList<>();
        List<ScoreFlowScorringTjDto> tjQuery = new ArrayList<>();
        for (UserScoreDto dto : usdtos) {
            if (ssfList.stream().filter(s -> s.getFserialno().equals(dto.getFserialNo())).count() == 0) {
                query = usdtos.stream().filter(s -> s.getFserialNo().equals(dto.getFserialNo())).collect(Collectors.toList());
                ScoreFlowScorringTjDto item = new ScoreFlowScorringTjDto();
                item.setFserialno(dto.getFserialNo());
                item.setScorringcode(dto.getScorringCode());
                item.setScorredcode(dto.getScoredCode());
                item.setScorestate(dto.getScoreState());
                item.setScoretype(dto.getScoreType());
                item.setYcyzb(query.size());
                query = query.stream().filter(s -> s.getScoreState().equals("2")).collect(Collectors.toList());
                item.setSjcyzb(query.size());
                item.setScorestate(dto.getScoreState());
                ssfList.add(item);
            }
        }

        int count = 0;
        double sum = 0.0;
        for (ScoreFlowScorringTjDto tjDto : ssfList) {
            if (list.stream().filter(s -> s.getScorringcode().equals(tjDto.getScorringcode())).count() == 0) {
                tjQuery = ssfList.stream().filter(s -> s.getScorringcode().equals(tjDto.getScorringcode())).collect(Collectors.toList());
                ScoreFlowScorringTjDto item = new ScoreFlowScorringTjDto();
                item.setScorringcode(tjDto.getScorringcode());
                count = tjQuery.stream().collect(Collectors.groupingBy(ScoreFlowScorringTjDto::getScorredcode)).size();
                item.setYcyrs(count);
                count = tjQuery.stream().filter(s -> s.getScorestate().equals("2")).collect(Collectors.groupingBy(ScoreFlowScorringTjDto::getScorredcode)).size();
                item.setSjcyrs(count);
                sum = tjQuery.stream().mapToDouble(ScoreFlowScorringTjDto::getYcyzb).sum();
                item.setYcyzb((int) sum);
                sum = tjQuery.stream().mapToDouble(ScoreFlowScorringTjDto::getSjcyzb).sum();
                item.setSjcyzb((int) sum);
                list.add(item);
            }
        }
        return list;
    }

    private List<String> getTypeList(String scoreType) {
        List<String> typeList = new ArrayList<>();
        if (scoreType.equals("ABC")) {
            typeList.add("A");
            typeList.add("B");
            typeList.add("C");
        } else if (scoreType.equals("D")) {
            typeList.add("D");
        } else {
            typeList.add("E");
            typeList.add("F");
        }
        return typeList;
    }

}
