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


    List<DutyDto> selectDutyDto(@Param("station") String station, @Param("scorredcode") String scorredcode, @Param("stationcode") String stationcode, @Param("username") String username,@Param("dbtype") String dbtype);

}
