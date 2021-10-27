package com.welb.organization_check.service_impl;

import com.welb.organization_check.dto.DutyDto;
import com.welb.organization_check.mapper.DutyDtoMapper;
import com.welb.organization_check.service.IDutyDtoService;
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
public class DutyDtoServiceImpl implements IDutyDtoService {
    @Resource
    DutyDtoMapper dutyDtoMapper;

    @Override
    public List<DutyDto> selectDutyDto(String station, String scorredcode, String stationcode, String username,
                                       String scoreType, String dbtype){
        return dutyDtoMapper.selectDutyDto(station,scorredcode, stationcode, username,scoreType,dbtype);
    }

    @Override
    public List<DutyDto> selectStationDutyDto(String scorredstationcode, String departmentcode,
                                       String stationname,String scoreType,String dbtype){
        return dutyDtoMapper.selectStationDutyDto(scorredstationcode,departmentcode,stationname,scoreType,dbtype);
    }
}
