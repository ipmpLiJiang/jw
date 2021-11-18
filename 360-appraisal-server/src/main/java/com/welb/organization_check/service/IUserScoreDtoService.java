package com.welb.organization_check.service;

import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserScoreDto;
import com.welb.organization_check.entity.Duty;
import com.welb.organization_check.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IUserDtoService
 * @projectName xh-360appraisal-interface
 * @description: 用户dto的业务逻辑层接口
 * @date 2019/6/416:43
 */
public interface IUserScoreDtoService {

    List<UserScoreDto> findUserScore(String year,String month,List<String> typeList,String dbtype);

    List<UserScoreDto> findUserDutyScore(String year,String month,String employeeCode,List<String> typeList,String dbtype);

    List<UserScoreDto> getTypeUserDutyScore(List<UserScoreDto> list,boolean isJisuan);

    List<UserScoreDto> findUserFlowDetailScore(String year, String month, String dbtype, String employeeCode,String postType);

    List<UserScoreDto> selectUserDetailByYMDTAndPTAndSTList(String year, String month,String dbtype, String postType,List<String> typeList);
}
