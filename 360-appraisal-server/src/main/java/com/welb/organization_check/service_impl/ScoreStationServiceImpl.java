package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.ScoreStation;
import com.welb.organization_check.mapper.ScoreStationMapper;
import com.welb.organization_check.service.IScoreStationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author luoyaozu
 * @title: ScoreServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 评价关系业务层接口的实现类
 * @date 2019/5/2710:54
 */
@Service
@Transactional
public class ScoreStationServiceImpl implements IScoreStationService {
    @Resource
    ScoreStationMapper scoreStationMapper;

    @Override
    public int insert(ScoreStation record) {
        return scoreStationMapper.insert(record);
    }

    @Override
    public int insertSelective(ScoreStation scoreStation) {
        return scoreStationMapper.insertSelective(scoreStation);
    }

    @Override
    public int updateByPrimaryKeySelective(ScoreStation scoreStation) {
        return scoreStationMapper.updateByPrimaryKeySelective(scoreStation);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return scoreStationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchDelete(List<String> scorredIds) {
        return scoreStationMapper.batchDelete(scorredIds);
    }

    @Override
    public ScoreStation selectByPrimaryKey(Integer id) {
        return scoreStationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(ScoreStation record) {
        return scoreStationMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<ScoreStation> findScoreStationAll(String dbtype){
        return  scoreStationMapper.findScoreStationAll(dbtype);
    }

    @Override
    public List<ScoreStation> findScoreStationByScorredStationCode(String scorredstationcode,String dbtype){
        return  scoreStationMapper.findScoreStationByScorredStationCode(scorredstationcode,dbtype);
    }

    @Override
    public List<ScoreStation> selectScoreStationScorringLeft(ScoreStation scoreStation,String departmentcode){
        return  scoreStationMapper.selectScoreStationScorringLeft(scoreStation,departmentcode);
    }

    @Override
    public ScoreStation selectTypeByCodeDutyIsNull(String scorredstationcode,String scorringstationcode,String dbtype){
        return scoreStationMapper.selectTypeByCodeDutyIsNull(scorredstationcode,scorringstationcode,dbtype);
    }

    @Override
    public ScoreStation selectTypeByCodeDuty(String scorredstationcode,String scorringstationcode,String dutycode,String dbtype){
        return scoreStationMapper.selectTypeByCodeDuty(scorredstationcode,scorringstationcode,dutycode,dbtype);
    }

    @Override
    public List<ScoreStation> selectTypeByCodeList(String scorredstationcode,String scorringstationcode,String dbtype){
        return scoreStationMapper.selectTypeByCodeList(scorredstationcode,scorringstationcode,dbtype);
    }

    @Override
    public List<ScoreStation> findScoreStationScorringInList(ScoreStation scoreStation,String[] scorringStationList){
        return scoreStationMapper.findScoreStationScorringInList(scoreStation,scorringStationList);
    }

    @Override
    public List<ScoreStation> findScoreStationDutyInList(ScoreStation scoreStation,String[] dutyCodeList){
        return scoreStationMapper.findScoreStationDutyInList(scoreStation,dutyCodeList);
    }

    @Override
    public List<ScoreStation> selectScoreStationByScorredTypeDuty(String scorredstationcode,String scoretype,String dutycode, String dbtype){
        return scoreStationMapper.selectScoreStationByScorredTypeDuty(scorredstationcode,scoretype,dutycode,dbtype);
    }

}
