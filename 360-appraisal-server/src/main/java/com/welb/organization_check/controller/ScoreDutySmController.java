package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.ScoreDutySmTjDto;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.ScoreDutySm;
import com.welb.organization_check.entity.ScoreFlow;
import com.welb.organization_check.service.IManualSetTimeService;
import com.welb.organization_check.service.IScoreDutySmService;
import com.welb.organization_check.service.IScoreFlowService;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scoredutysm")
public class ScoreDutySmController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IScoreDutySmService scoreDutySmService;
    @Resource
    IScoreFlowService scoreFlowService;
    @Resource
    IManualSetTimeService setTimeService;

    @RequestMapping(value = "/scoreDutySmTjList", produces = "application/json;charset=utf-8")
    public Object selectScoreDutySmTjList(HttpServletRequest req, String year, String month, String dbtype, String postType) {
        ModelMap map = new ModelMap();
        String userCode = (String) req.getSession().getAttribute("usercode");
        if (userCode != null) {
            try {
                if (year == null || month == null || year.equals("") || month.equals("")) {
                    ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "", dbtype);
                    year = setTime.getYear();
                    month = setTime.getMonth();
                }
                ScoreDutySm query = new ScoreDutySm();
                query.setYear(year);
                query.setMonth(month);
                query.setDbtype(dbtype);
                List<ScoreDutySm> scoreDutySmList = scoreDutySmService.selectScoreDutySmList(query, postType);
                List<ScoreFlow> flowList = scoreFlowService.selectSummaryFlowByYMTOrPTList(year, month, dbtype, postType);
                List<ScoreDutySmTjDto> list = new ArrayList<>();
                ScoreDutySmTjDto dto1 = new ScoreDutySmTjDto();
                dto1.setNum(1);
                dto1.setScoreProj("行政后勤科室");
                List<ScoreDutySmTjDto> flowXzList = this.getFlowPostTypeList(flowList,scoreDutySmList,dto1,"3");

                ScoreDutySmTjDto dto2 = new ScoreDutySmTjDto();
                dto2.setNum(2);
                dto2.setScoreProj("临床科主任");
                List<ScoreDutySmTjDto> flowKzrList = this.getFlowPostTypeList(flowList,scoreDutySmList,dto2,"1");

                ScoreDutySmTjDto dto3 = new ScoreDutySmTjDto();
                dto3.setNum(3);
                dto3.setScoreProj("临床护士长");
                List<ScoreDutySmTjDto> flowHszList = this.getFlowPostTypeList(flowList,scoreDutySmList,dto3,"2");

                ScoreDutySmTjDto dto4 = new ScoreDutySmTjDto();
                dto4.setNum(4);
                dto4.setScoreProj("合计");
                dto4.setWwcrs(dto1.getWwcrs() + dto2.getWwcrs() + dto3.getWwcrs());
                dto4.setWcrs(dto1.getWcrs() + dto2.getWcrs() + dto3.getWcrs());
                dto4.setKhrs(dto1.getKhrs() + dto2.getKhrs() + dto3.getKhrs());
                if(dto1.getScorredname() != null) {
                    dto4.setScorredname(dto1.getScorredname());
                }
                if(dto2.getScorredname() != null) {
                    if(dto4.getScorredname() != null){
                        dto4.setScorredname(dto4.getScorredname() + "," + dto2.getScorredname());
                    } else {
                        dto4.setScorredname(dto2.getScorredname());
                    }
                }
                if(dto3.getScorredname() != null) {
                    if(dto4.getScorredname() != null){
                        dto4.setScorredname(dto4.getScorredname() + "," + dto3.getScorredname());
                    } else {
                        dto4.setScorredname(dto3.getScorredname());
                    }
                }
                list.add(dto1);
                list.add(dto2);
                list.add(dto3);
                list.add(dto4);
                map.put("totalPages", list.size());
                map.put("msg", "查询自评情况统计表成功");
                map.put("data", list);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "查询自评情况统计表失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }

    private List<ScoreDutySmTjDto> getFlowPostTypeList(List<ScoreFlow> flowList,List<ScoreDutySm> scoreDutySmList, ScoreDutySmTjDto dto ,String postType) {
        List<ScoreDutySmTjDto> list = new ArrayList<>();
        List<ScoreFlow> flowPostTypeList = flowList.stream().filter(s -> s.getPosttype().equals(postType)).collect(Collectors.toList());
        List<ScoreDutySm> query = new ArrayList<>();
        for (ScoreFlow flow : flowPostTypeList) {
            if (list.stream().filter(s -> s.getScorredcode().equals(flow.getScoredcode())).count() == 0) {
                query = scoreDutySmList.stream().filter(s->s.getScorredcode().equals(flow.getScoredcode())).collect(Collectors.toList());
                ScoreDutySmTjDto item = new ScoreDutySmTjDto();
                item.setScorredcode(flow.getScoredcode());
                item.setScorredname(flow.getScoredname());
                item.setPosttype(postType);
                item.setKhrs(1);
                dto.setKhrs(dto.getKhrs() + 1);
                if(query.size() > 0) {
                    item.setWcrs(1);
                    dto.setWcrs(dto.getWcrs() + 1);
                } else {
                    item.setWwcrs(1);
                    dto.setWwcrs(dto.getWwcrs() + 1);
                    if(dto.getScorredname() == null || dto.equals("")){
                        dto.setScorredname(flow.getScoredname());
                    } else {
                        dto.setScorredname(dto.getScorredname() + "," + flow.getScoredname());
                    }
                }
                list.add(item);
            }
        }
        return list;
    }

}
