package com.welb.organization_check.service_impl;

import com.welb.organization_check.dto.UserScoreBadDto;
import com.welb.organization_check.mapper.UserScoreBadDtoMapper;
import com.welb.organization_check.service.IUserScoreBadDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: UserDtoServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/416:46
 */
@Service
@Transactional
public class UserScoreBadDtoServiceImpl implements IUserScoreBadDtoService {
    @Resource
    UserScoreBadDtoMapper userScoreDtoBadMapper;

    @Override
    public List<UserScoreBadDto> selectUserScoreBadDto(UserScoreBadDto dto) {
        return userScoreDtoBadMapper.selectUserScoreBadDto(dto);
    }
}
