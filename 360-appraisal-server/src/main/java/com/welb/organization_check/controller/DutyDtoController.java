package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.DutyDto;
import com.welb.organization_check.service.IDutyDtoService;
import com.welb.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author luoyaozu
 * @title: DutyController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/5/2115:38
 */
@RestController
@RequestMapping("/dutyDto")
public class DutyDtoController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IDutyDtoService dutyDtoService;
    @RequestMapping(value = "/dutylist", produces = "application/json;charset=utf-8")
    public Object selectDutyDtoBy(HttpServletRequest req, String station,String scorredcode, String stationcode,String dbtype,String username,int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        //pageNum:当前页  pageSize:每页总数
        if (usercode != null) {
            PageHelper.startPage(pageNum, pageSize);
            List<DutyDto> dutys;
            try {
                dutys = dutyDtoService.selectDutyDto(station,scorredcode, stationcode,username,dbtype);
                PageInfo<DutyDto> pageInfo = new PageInfo<>(dutys);
                dutys = pageInfo.getList();
                //获取总数据量
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "查询成功");
                map.put("data", dutys);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }
}
