package com.welb.organization_check.service_impl;

import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserScoreDto;
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
    public List<UserScoreDto> getTypeUserDutyScore(List<UserScoreDto> list, boolean isJiSuan) {
        List<String> scoreTypeList = new ArrayList<>();
        List<UserScoreDto> dutyAndRatioList = new ArrayList<>();
        List<UserScoreDto> query = new ArrayList<>();
        Double sumScore = 0.0;
        scoreTypeList.add("A");
        scoreTypeList.add("B");
        scoreTypeList.add("C");
        scoreTypeList.add("D");
        Double aScore = 0.0;
        Double bScore = 0.0;
        Double cScore = 0.0;
        Double dScore = 0.0;
        for (UserScoreDto item : list) {
            if (dutyAndRatioList.stream().filter(s -> s.getDserialNo().equals(item.getDserialNo())).count() == 0) {
                UserScoreDto dar = new UserScoreDto();
                dar.setDserialNo(item.getDserialNo());
                dar.setDutyType(item.getDutyType());
                for (String type : scoreTypeList) {
                    sumScore = 0.0;
                    //指标、评分类型 A、B、C、D
                    query = list.stream().filter(s -> s.getDserialNo().equals(item.getDserialNo()) && s.getScoreType().equals(type)).collect(Collectors.toList());
                    if (query.size() > 0) {
                        sumScore = query.stream().mapToDouble(UserScoreDto::getScore).sum();
                        // 指标总和 除以个数
                        if(sumScore > 0 && query.size() > 1) {
                            sumScore = sumScore / query.size();
                        }
                        if (type.equals("A")) {
                            dar.setARatio(query.get(0).getRatio());
                            dar.setAScore(sumScore);
                        }
                        if (type.equals("B")) {
                            dar.setBRatio(query.get(0).getRatio());
                            dar.setBScore(sumScore);
                        }
                        if (type.equals("C")) {
                            dar.setCRatio(query.get(0).getRatio());
                            dar.setCScore(sumScore);
                        }
                        if (type.equals("D")) {
                            dar.setDRatio(query.get(0).getRatio());
                            dar.setDScore(sumScore);
                        }
                    } else {
                        if (type.equals("A")) {
                            dar.setARatio(0.0);
                            dar.setAScore(0.0);
                        }
                        if (type.equals("B")) {
                            dar.setBRatio(0.0);
                            dar.setBScore(0.0);
                        }
                        if (type.equals("C")) {
                            dar.setCRatio(0.0);
                            dar.setCScore(0.0);
                        }
                        if (type.equals("D")) {
                            dar.setDRatio(0.0);
                            dar.setDScore(0.0);
                        }
                    }
                }
                dar.setRatio(dar.getARatio() + dar.getBRatio() + dar.getCRatio() + dar.getDRatio());
                if (dar.getRatio() > 0) {
                    if (isJiSuan) {
                        dar.setAScore(dar.getAScore() * dar.getARatio() / dar.getRatio());
                        dar.setBScore(dar.getBScore() * dar.getBRatio() / dar.getRatio());
                        dar.setCScore(dar.getCScore() * dar.getCRatio() / dar.getRatio());
                        dar.setDScore(dar.getDScore() * dar.getDRatio() / dar.getRatio());

                        dar.setScore(dar.getAScore() + dar.getBScore() + dar.getCScore() + dar.getDScore());
                    } else {
                        dar.setAScore(dar.getAScore());
                        dar.setBScore(dar.getBScore());
                        dar.setCScore(dar.getCScore());
                        dar.setDScore(dar.getDScore());
                        aScore = dar.getAScore() * dar.getARatio() / dar.getRatio();
                        bScore = dar.getBScore() * dar.getBRatio() / dar.getRatio();
                        cScore = dar.getCScore() * dar.getCRatio() / dar.getRatio();
                        dScore = dar.getDScore() * dar.getDRatio() / dar.getRatio();
                        dar.setScore(aScore + bScore + cScore + dScore );
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
