package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.Duty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DutyMapper {
    int deleteByPrimaryKey(String dutycode);

    int insert(Duty record);

    int insertSelective(Duty record);

    Duty selectByPrimaryKey(String dutycode);

    int updateByPrimaryKeySelective(Duty record);

    int updateByPrimaryKey(Duty record);

    List<Duty>selectDutyAll(Duty duty);

    List<Duty>selectDutyAll_db(Duty duty);

    String selectMaxDutyCode();

    List<Duty>selectDutyByStationCode(String stationcode,String dbtype);

    List<Duty>queryJiChu(String stationcode);

    List<Duty>queryYiBan(String stationcode);

    List<Duty> queryDutyByType(String dutyType,String stationcode);

    List<Duty> queryDutyByStationCode(String stationcode,String dbtype);

    List<Duty> queryDutyByScorringCode(@Param("scorringCode") String scorringCode,
                                       @Param("scorredCode") String scorredCode,
                                       @Param("scoreTypeList") List<String> scoreTypeList,
                                       @Param("dbtype") String dbtype);
}
