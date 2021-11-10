package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.Duty;
import com.welb.organization_check.entity.ScoreStation;
import com.welb.organization_check.entity.Station;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IDutyService;
import com.welb.organization_check.service.IScoreStationService;
import com.welb.organization_check.service.IStationService;
import com.welb.organization_check.service.IUserService;
import com.welb.util.LogUtil;
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

/**
 * @author luoyaozu
 * @title: StationController
 * @projectName kao
 * @description: 岗位控制器
 * @date 2019/5/1716:47
 */
@RestController
@RequestMapping("/station")
public class StationController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IStationService stationService;
    @Resource
    IDutyService dutyService;
    @Resource
    IScoreStationService scoreStationService;
    @Resource
    IUserService userService;

    /**
     * 查询所有，包含条件模糊查询
     *
     * @param station
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object findStationAll(HttpServletRequest req, int pageNum, int pageSize, Station station, String dbtype) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {

            //pageNum:当前所在的页面  pageSize:每页有多少条数据
            PageHelper.startPage(pageNum, pageSize);
            try {
                List<Station> stations = stationService.selectStationByLike(station);
                PageInfo<Station> pageInfo = new PageInfo<>(stations);
                List<Station> stationList = pageInfo.getList();
                if (station.getIsEF() != null && dbtype != null && !dbtype.equals("") && stationList.size() > 0) {
                    String[] codeList = new String[stationList.size()];
                    for (int i = 0; i < stationList.size(); i++) {
                        codeList[i] = stationList.get(i).getStationcode();
                    }
                    List<User> userList = userService.selectUserByInStationCode(codeList, dbtype);
                    List<User> query = new ArrayList<>();
                    for (Station item : stationList) {
                        query = userList.stream().filter(s -> s.getStationcode() !=null && s.getStationcode().equals(item.getStationcode())).collect(Collectors.toList());
                        if (query.size() > 0) {
                            item.setAratio(query.get(0).getAratio());
                            item.setBratio(query.get(0).getBratio());
                            item.setCratio(query.get(0).getCratio());
                            item.setDratio(query.get(0).getDratio());
                            item.setEratio(query.get(0).getEratio());
                            item.setFratio(query.get(0).getFratio());
                        }
                    }
                }
                //数据总量
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", stationList);
                map.put("msg", "查询岗位成功 ");
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "查询岗位失败 ");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    @RequestMapping(value = "/listDuty", produces = "application/json;charset=utf-8")
    public Object findStationDutyAll(HttpServletRequest req, int pageNum, int pageSize, Station station) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {

            //pageNum:当前所在的页面  pageSize:每页有多少条数据
            PageHelper.startPage(pageNum, pageSize);
            try {
                List<Station> stations = stationService.selectStationByLike(station);
                PageInfo<Station> pageInfo = new PageInfo<>(stations);
                List<Station> stationList = pageInfo.getList();
                //数据总量
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", stationList);
                map.put("msg", "查询岗位成功 ");
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "查询岗位失败 ");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }


    /**
     * 添加岗位
     *
     * @param station
     * @return
     */
    @RequestMapping(value = "/add", produces = "application/json;charset=utf-8")
    public Object addStation(Station station) {
        ModelMap map = new ModelMap();
        int count = stationService.insertSelective(station);
        if (count > 0) {
            map.put("msg", "添加岗位成功");
            map.put("code", 0);
        } else {
            map.put("msg", "添加岗位失败");
            map.put("code", 1);
        }
        return map;
    }

    @RequestMapping(value = "/getDutyScorringStationEF", produces = "application/json;charset=utf-8")
    public Object getDutyScorringStation(HttpServletRequest req, String scorredStationCode, String dutycode, String scoretype, String dbtype) {
        ModelMap map = new ModelMap();
        String userCode = (String) req.getSession().getAttribute("usercode");
        if (userCode != null) {
            try {
                List<Station> stationList = stationService.selectStationByNotEF(scorredStationCode);
                List<ScoreStation> scoreStationList = scoreStationService.selectScoreStationByScorredTypeDuty(scorredStationCode, scoretype, dutycode, dbtype);
                if (scoreStationList.size() > 0) {
                    List<ScoreStation> query = new ArrayList<>();
                    for (Station station : stationList) {
                        query = scoreStationList.stream().filter(s -> s.getScorringstationcode().equals(station.getStationcode())).collect(Collectors.toList());
                        if (query.size() > 0) {
                            station.setScoreid(query.get(0).getId());
                            station.setScoretype(query.get(0).getScoretype());
                        } else {
                            station.setScoretype("");
                        }
                    }
                }
                if (scoreStationList.size() > 0) {
                    stationList = stationList.stream().sorted(Comparator.comparing(Station::getScoretype).reversed()).collect(Collectors.toList());
                }
                map.put("totalPages", stationList.size());
                map.put("msg", "查询评分岗位成功");
                map.put("data", stationList);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                map.put("msg", "查询评分岗位失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }


    /**
     * 修改岗位信息
     *
     * @param station
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateStation(Station station) {
        ModelMap map = new ModelMap();
        int count = stationService.updateByPrimaryKeySelective(station);
        if (count > 0) {
            map.put("msg", "修改岗位成功");
            map.put("code", 0);
        } else {
            map.put("msg", "修改岗位失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 通过stationcode删除岗位信息
     *
     * @param stationcode
     * @return
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object deleteStation(String stationcode, String dbtype) {
        ModelMap map = new ModelMap();
        //删除与岗位关联的指标
        List<Duty> duties = dutyService.selectDutyByStationCode(stationcode, dbtype);
        for (Duty duty : duties
        ) {
            dutyService.deleteByPrimaryKey(duty.getDutycode());
        }
        int count = stationService.deleteByPrimaryKey(stationcode);
        if (count > 0) {
            map.put("msg", "删除岗位成功");
            map.put("code", 0);
        } else {
            map.put("msg", "删除岗位失败");
            map.put("code", 1);
        }
        return map;
    }

}
