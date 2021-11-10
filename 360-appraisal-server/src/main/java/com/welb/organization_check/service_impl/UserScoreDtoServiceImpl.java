package com.welb.organization_check.service_impl;

import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserScoreDto;
import com.welb.organization_check.entity.Duty;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.mapper.UserDtoMapper;
import com.welb.organization_check.mapper.UserScoreDtoMapper;
import com.welb.organization_check.service.IUserDtoService;
import com.welb.organization_check.service.IUserScoreDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luoyaozu
 * @title: UserDtoServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/416:46
 */
@Service
@Transactional
public class UserScoreDtoServiceImpl implements IUserScoreDtoService {
    @Resource
    UserScoreDtoMapper userScoreDtoMapper;

    @Override
    public List<UserScoreDto> findUserScore(String year, String month, List<String> typeList, String dbtype) {
        return userScoreDtoMapper.findUserScore(year, month, typeList, dbtype);
    }

    @Override
    public List<UserScoreDto> findUserDutyScore(String year, String month, String employeeCode, List<String> typeList, String dbtype) {
        return userScoreDtoMapper.findUserDutyScore(year, month, employeeCode, typeList, dbtype);
    }

    @Override
    public  List<UserScoreDto> findUserFlowDetailScore(String year,String month,String dbtype,String employeeCode){
        return userScoreDtoMapper.findUserFlowDetailScore(year,month,dbtype,employeeCode);
    }

    @Override
    public List<UserScoreDto> getTypeUserDutyScore(List<UserScoreDto> list, boolean isJiSuan) {
        List<String> scoreTypeList = new ArrayList<>();
        List<UserScoreDto> dutyAndRatioList = new ArrayList<>();
        List<UserScoreDto> query = new ArrayList<>();
        Double sumScore = 0.0;
        scoreTypeList.add("A");
        scoreTypeList.add("B");
        scoreTypeList.add("C");
        scoreTypeList.add("D");
        scoreTypeList.add("E");
        scoreTypeList.add("F");
        Double aScore = 0.0;
        Double bScore = 0.0;
        Double cScore = 0.0;
        Double dScore = 0.0;
        Double eScore = 0.0;
        Double fScore = 0.0;
        for (UserScoreDto item : list) {
            if (dutyAndRatioList.stream().filter(s -> s.getDserialNo().equals(item.getDserialNo())).count() == 0) {
                UserScoreDto dar = new UserScoreDto();
                dar.setDserialNo(item.getDserialNo());
                dar.setDutyType(item.getDutyType());
                for (String type : scoreTypeList) {
                    sumScore = 0.0;
                    //指标、评分类型 A、B、C、D、E、F
                    query = list.stream().filter(s -> s.getDserialNo().equals(item.getDserialNo()) && s.getScoreType().equals(type)).collect(Collectors.toList());
                    if (query.size() > 0) {
                        sumScore = query.stream().mapToDouble(UserScoreDto::getScore).sum();
                        // 指标总和 除以个数
                        if(sumScore > 0 && query.size() > 1) {
                            sumScore = sumScore / query.size();
                        }
                        if (type.equals("A")) {
                            dar.setAratio(query.get(0).getRatio());
                            dar.setAscore(sumScore);
                        }
                        if (type.equals("B")) {
                            dar.setBratio(query.get(0).getRatio());
                            dar.setBscore(sumScore);
                        }
                        if (type.equals("C")) {
                            dar.setCratio(query.get(0).getRatio());
                            dar.setCscore(sumScore);
                        }
                        if (type.equals("D")) {
                            dar.setDratio(query.get(0).getRatio());
                            dar.setDscore(sumScore);
                        }
                        if (type.equals("E")) {
                            dar.setEratio(query.get(0).getRatio());
                            dar.setEscore(sumScore);
                        }
                        if (type.equals("F")) {
                            dar.setFratio(query.get(0).getRatio());
                            dar.setFscore(sumScore);
                        }
                    } else {
                        if (type.equals("A")) {
                            dar.setAratio(0.0);
                            dar.setAscore(0.0);
                        }
                        if (type.equals("B")) {
                            dar.setBratio(0.0);
                            dar.setBscore(0.0);
                        }
                        if (type.equals("C")) {
                            dar.setCratio(0.0);
                            dar.setCscore(0.0);
                        }
                        if (type.equals("D")) {
                            dar.setDratio(0.0);
                            dar.setDscore(0.0);
                        }
                        if (type.equals("E")) {
                            dar.setEratio(0.0);
                            dar.setEscore(0.0);
                        }
                        if (type.equals("F")) {
                            dar.setFratio(0.0);
                            dar.setFscore(0.0);
                        }
                    }
                }
                dar.setRatio(dar.getAratio() + dar.getBratio() + dar.getCratio() + dar.getDratio() + dar.getEratio() + dar.getFratio());
                if (dar.getRatio() > 0) {
                    if (isJiSuan) {
                        dar.setAscore(dar.getAscore() * dar.getAratio() / dar.getRatio());
                        dar.setBscore(dar.getBscore() * dar.getBratio() / dar.getRatio());
                        dar.setCscore(dar.getCscore() * dar.getCratio() / dar.getRatio());
                        dar.setDscore(dar.getDscore() * dar.getDratio() / dar.getRatio());
                        dar.setEscore(dar.getEscore() * dar.getEratio() / dar.getRatio());
                        dar.setFscore(dar.getFscore() * dar.getFratio() / dar.getRatio());

                        dar.setScore(dar.getAscore() + dar.getBscore() + dar.getCscore() + dar.getDscore() + dar.getEscore() + dar.getFscore());
                    } else {
                        dar.setAscore(dar.getAscore());
                        dar.setBscore(dar.getBscore());
                        dar.setCscore(dar.getCscore());
                        dar.setDscore(dar.getDscore());
                        dar.setEscore(dar.getEscore());
                        dar.setFscore(dar.getFscore());
                        aScore = dar.getAscore() * dar.getAratio() / dar.getRatio();
                        bScore = dar.getBscore() * dar.getBratio() / dar.getRatio();
                        cScore = dar.getCscore() * dar.getCratio() / dar.getRatio();
                        dScore = dar.getDscore() * dar.getDratio() / dar.getRatio();
                        eScore = dar.getEscore() * dar.getEratio() / dar.getRatio();
                        fScore = dar.getFscore() * dar.getFratio() / dar.getRatio();
                        dar.setScore(aScore + bScore + cScore + dScore + eScore + fScore );
                    }
                } else {
                    dar.setScore(0.0);
                }
                dutyAndRatioList.add(dar);
            }
        }

        return dutyAndRatioList;
    }
}
