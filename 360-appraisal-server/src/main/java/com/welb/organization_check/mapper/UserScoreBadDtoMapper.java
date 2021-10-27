package com.welb.organization_check.mapper;

import com.welb.organization_check.dto.UserScoreBadDto;
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
public interface UserScoreBadDtoMapper {

    List<UserScoreBadDto> selectUserScoreBadDto(UserScoreBadDto dto);
}
