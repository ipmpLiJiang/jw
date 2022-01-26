package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ScoreStation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreStationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScoreStation record);

    int insertSelective(ScoreStation record);

    ScoreStation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScoreStation record);

    int updateByPrimaryKey(ScoreStation record);

    int batchDelete(List<String> scorredIds);


    List<ScoreStation> findScoreStationAll(String dbtype);

    List<ScoreStation> findScoreStationByScorredStationCode(String scorredstationcode,String dbtype);

    List<ScoreStation> selectScoreStationScorringLeft(@Param("scoreStation") ScoreStation scoreStation, @Param("departmentcode") String departmentcode);

    ScoreStation selectTypeByCodeDutyIsNull(String scorredstationcode, String scorringstationcode, String dbtype);

    ScoreStation selectTypeByCodeDuty(String scorredstationcode, String scorringstationcode, String dutycode, String dbtype);

    List<ScoreStation> selectTypeByCodeList(String scorredstationcode, String scorringstationcode, String dbtype);

    List<ScoreStation> findScoreStationScorringInList(@Param("scoreStation") ScoreStation scoreStation, @Param("scorringStationList") String[] scorringStationList);


    List<ScoreStation> findScoreStationDutyInList(@Param("scoreStation") ScoreStation scoreStation, @Param("dutyCodeList") String[] dutyCodeList);

    List<ScoreStation> selectScoreStationByScorredTypeDuty(String scorredstationcode,String scoretype,String dutycode, String dbtype);

}
