package com.welb.organization_check.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.UserScoreBadDto;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.Station;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IManualSetTimeService;
import com.welb.organization_check.service.IUserScoreBadDtoService;
import com.welb.organization_check.service.IUserService;
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
 * @title: UserDtoController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/416:48
 */
@RestController
@RequestMapping("/scoreBad")
public class UserScoreBadDtoController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IUserService userService;

    @Resource
    IUserScoreBadDtoService userScoreBadDtoService;

    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object findStationAll(HttpServletRequest req, int pageNum, int pageSize, UserScoreBadDto dto) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            try {
                if (dto.getYear() == null || dto.getMonth() == null || dto.getYear().equals("") || dto.getMonth().equals("")) {
                    ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "",dto.getDbtype());
                    dto.setYear(setTime.getYear());
                    dto.setMonth(setTime.getMonth());
                }
                //pageNum:当前所在的页面  pageSize:每页有多少条数据
                PageHelper.startPage(pageNum, pageSize);
                List<UserScoreBadDto> userScoreBadDtos  = userScoreBadDtoService.selectUserScoreBadDto(dto);

                PageInfo<UserScoreBadDto> pageInfo = new PageInfo<>(userScoreBadDtos);
                List<UserScoreBadDto> userScoreBadList = pageInfo.getList();
                //分页后再赋值
                List<User> userList = new ArrayList<>();
                List<User> userQueryList = new ArrayList<>();
                for (UserScoreBadDto item : userScoreBadList) {
                    this.getUserName(item,userList,userQueryList);
                }

                //数据总量userScoreBadList
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", userScoreBadList);
                map.put("msg", "查询差评评分汇总成功 ");
                map.put("code", 0);
            } catch (Exception e) {
                log.error(e.getMessage() , e);
                map.put("msg", "查询差评评分汇总失败 ");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void getUserName(UserScoreBadDto item,List<User> userList,List<User> userQueryList){
        userQueryList = userList.stream().filter(s->s.getUsercode().equals(item.getUsercode())).collect(Collectors.toList());
        if(userQueryList.size() == 0) {
            User userQuery = new User();
            userQuery.setUsercode(item.getUsercode());
            User user = userService.selectUserBuGwByMoneyCard(userQuery);
            if (user != null) {
                userList.add(user);
            }
            item.setStationcode(user.getStationcode());
            item.setStationname(user.getStationname());
            item.setDepartmentcode(user.getDepartmentcode());
            item.setDepartmentname(user.getDepartmentname());
        } else {
            item.setStationcode(userQueryList.get(0).getStationcode());
            item.setStationname(userQueryList.get(0).getStationname());
            item.setDepartmentcode(userQueryList.get(0).getDepartmentcode());
            item.setDepartmentname(userQueryList.get(0).getDepartmentname());
        }

        userQueryList = userList.stream().filter(s->s.getUsercode().equals(item.getScorringCode())).collect(Collectors.toList());
        if(userQueryList.size() == 0) {
            User userQuery = new User();
            userQuery.setUsercode(item.getScorringCode());
            User user = userService.selectUserBuGwByMoneyCard(userQuery);
            if (user != null) {
                userList.add(user);
            }
            item.setScorringName(user.getUsername());
            item.setScorringStationcode(user.getStationcode());
            item.setScorringStationname(user.getStationname());
            item.setScorringDepartmentcode(user.getDepartmentcode());
            item.setScorringDepartmentname(user.getDepartmentname());
        } else {
            item.setScorringName(userQueryList.get(0).getUsername());
            item.setScorringStationcode(userQueryList.get(0).getStationcode());
            item.setScorringStationname(userQueryList.get(0).getStationname());
            item.setScorringDepartmentcode(userQueryList.get(0).getDepartmentcode());
            item.setScorringDepartmentname(userQueryList.get(0).getDepartmentname());
        }

    }
}
