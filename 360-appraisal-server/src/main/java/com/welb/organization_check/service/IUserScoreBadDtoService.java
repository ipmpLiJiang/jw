package com.welb.organization_check.service;

import com.welb.organization_check.dto.UserScoreBadDto;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IUserDtoService
 * @projectName xh-360appraisal-interface
 * @description: 用户dto的业务逻辑层接口
 * @date 2019/6/416:43
 */
public interface IUserScoreBadDtoService {

    List<UserScoreBadDto> selectUserScoreBadDto(UserScoreBadDto dto);
}
