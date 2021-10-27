package com.welb.organization_check.mapper;

import com.welb.organization_check.dto.DutyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author luoyaozu
 * @title: UserDtoMapper
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/415:16
 */
@Mapper
public interface DutyDtoMapper {


    List<DutyDto> selectDutyDto(@Param("station") String station, @Param("scorredcode") String scorredcode,
                                @Param("stationcode") String stationcode, @Param("username") String username,
                                @Param("scoreType") String scoreType,@Param("dbtype") String dbtype);

    List<DutyDto> selectStationDutyDto(@Param("scorredstationcode") String scorredstationcode, @Param("departmentcode") String departmentcode,
                                       @Param("stationname") String stationname,@Param("scoreType") String scoreType,@Param("dbtype") String dbtype);
}
