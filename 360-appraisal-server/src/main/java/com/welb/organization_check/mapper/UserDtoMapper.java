package com.welb.organization_check.mapper;

import com.welb.organization_check.dto.UserDto;
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
public interface UserDtoMapper {


    List<UserDto>selectUserDtoLike(@Param("userDto")UserDto userDto,@Param("roleList")List<String> roleList);

    List<UserDto>selectUserDtoByUserCode(@Param("userDto")UserDto userDto,@Param("roleList")List<String> roleList);

    List<UserDto>gradeList(UserDto userDto);

    List<UserDto>selectUserDtoLikeToQuarter(UserDto userDto);

    int getTotalCount(UserDto dto);

    UserDto selectOneUserDto(UserDto dto);

    List<UserDto>selectUserByMonthSummaryList(@Param("userDto")UserDto userDto);

}
