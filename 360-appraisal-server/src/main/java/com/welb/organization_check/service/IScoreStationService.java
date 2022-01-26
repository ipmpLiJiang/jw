package com.welb.organization_check.service;


import com.welb.organization_check.entity.ScoreStation;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IScoreService
 * @projectName xh-360appraisal-interface
 * @description: 评价关系业务层接口
 * @date 2019/5/2710:48
 */
public interface IScoreStationService {

    int insert(ScoreStation record);

    /**
     * 添加评分关系数据
     * @param scoreStation
     * @return
     */
    int insertSelective(ScoreStation scoreStation);

    /**
     * 修改评分关系数据
     * @param scoreStation
     * @return
     */
    int updateByPrimaryKeySelective(ScoreStation scoreStation);

    /**
     * 删除数据
     * @param id
     * @return
     */

    int deleteByPrimaryKey(Integer id);


    /**
     * 批量删除
     * @param scorredIds
     * @return
     */
    int batchDelete(List<String> scorredIds);

    ScoreStation selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ScoreStation record);

    List<ScoreStation> findScoreStationAll(String dbtype);

    List<ScoreStation> findScoreStationByScorredStationCode(String scorredstationcode,String dbtype);

    List<ScoreStation> selectScoreStationScorringLeft(ScoreStation scoreStation,String departmentcode);

    ScoreStation selectTypeByCodeDutyIsNull(String scorredstationcode,String scorringstationcode,String dbtype);

    ScoreStation selectTypeByCodeDuty(String scorredstationcode,String scorringstationcode,String dutycode,String dbtype);

    List<ScoreStation> selectTypeByCodeList(String scorredstationcode,String scorringstationcode,String dbtype);

    List<ScoreStation> findScoreStationScorringInList(ScoreStation scoreStation,String[] scorringStationList);

    List<ScoreStation> findScoreStationDutyInList(ScoreStation scoreStation,String[] dutyCodeList);

    List<ScoreStation> selectScoreStationByScorredTypeDuty(String scorredstationcode,String scoretype,String dutycode, String dbtype);

}
