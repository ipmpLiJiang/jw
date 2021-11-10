package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.Duty;
import com.welb.organization_check.mapper.DutyMapper;
import com.welb.organization_check.service.IDutyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: DutyServiceImpl
 * @projectName kao
 * @description: 指标管理业务层接口的实现类
 * @date 2019/5/2114:20
 */
@Service
@Transactional
//@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DutyServiceImpl implements IDutyService {
    @Resource
    DutyMapper dutyMapper;

    @Override
    public int deleteByPrimaryKey(String dutycode) {
        return dutyMapper.deleteByPrimaryKey(dutycode);
    }

    @Override
//    @Transactional
    public int insertSelective(Duty duty) {
        //实现dutycode自增
        String dutyCode = dutyMapper.selectMaxDutyCode();
        if (dutyCode==null){
            duty.setDutycode("100");
        }else {
            int num = Integer.parseInt(dutyCode.trim());
            num++;
            String dutycode = String.valueOf(num);
            duty.setDutycode(dutycode);
        }
        int count = dutyMapper.insertSelective(duty);
        return count;
    }

    @Override
    public Duty selectByPrimaryKey(String dutycode) {

        return dutyMapper.selectByPrimaryKey(dutycode);
    }

    @Override
    public int updateByPrimaryKeySelective(Duty duty) {

        return dutyMapper.updateByPrimaryKeySelective(duty);
    }

    @Override
    public List<Duty> selectDutyAll(Duty duty) {
        if(StringUtils.isNotEmpty(duty.getDbtype()) && duty.getDbtype().equals("1")) {
            return dutyMapper.selectDutyAll_db(duty);
        }
        else {
            return dutyMapper.selectDutyAll(duty);
        }
    }

    @Override
    public List<Duty> selectDutyByStationCode(String stationcode,String dbtype) {
        return dutyMapper.selectDutyByStationCode(stationcode,dbtype);
    }

    @Override
    public List<Duty> queryJiChu(String stationcode) {
        return dutyMapper.queryJiChu(stationcode);
    }
    @Override
    public List<Duty> queryYiBan(String stationcode) {
        return dutyMapper.queryYiBan(stationcode);
    }

    @Override
    public List<Duty> queryDutyByType(String dutyType,String stationcode) {
        return dutyMapper.queryDutyByType(dutyType,stationcode);
    }

    @Override
    public List<Duty> queryDutyByStationCode(String stationcode,String dbtype) {
        return dutyMapper.queryDutyByStationCode(stationcode,dbtype);
    }

    @Override
    public List<Duty> queryDutyByDbbkCode(String dbbk,String dbtype) {
        return dutyMapper.queryDutyByDbbkCode(dbbk,dbtype);
    }

    @Override
    public List<Duty> queryDutyByScorringCode(String scorringCode,String scorredCode,List<String> scoreTypeList,String dbtype){
        return dutyMapper.queryDutyByScorringCode(scorringCode,scorredCode,scoreTypeList,dbtype);
    }
    @Override
    public List<Duty> selectDutyAllByDbtype(String dbtype){
        return dutyMapper.selectDutyAllByDbtype(dbtype);
    }
}
