package com.welb.organization_check.service_impl;

import com.welb.organization_check.dto.UserEvaluationSortDto;
import com.welb.organization_check.mapper.UserEvaluationSortDtoMapper;
import com.welb.organization_check.service.IUserEvaluationSortDtoService;
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
public class UserEvaluationSortDtoServiceImpl implements IUserEvaluationSortDtoService {
    @Resource
    UserEvaluationSortDtoMapper userScoreDtoBadMapper;
    @Override
    public List<UserEvaluationSortDto> selectUserEvaluationReportSortList(UserEvaluationSortDto dto){
        return  userScoreDtoBadMapper.selectUserEvaluationReportSortList(dto);
    }
}
