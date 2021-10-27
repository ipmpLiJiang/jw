package com.welb.organization_check.service;

import com.welb.organization_check.entity.ScoreDutySm;

import java.util.List;

public interface IScoreDutySmService {
    int deleteByPrimaryKey(Integer Id);

    int insert(ScoreDutySm record);

    int insertSelective(ScoreDutySm record);

    int batchInset(List<ScoreDutySm> list);

    ScoreDutySm selectByPrimaryKey(Integer Id);

    int updateByPrimaryKey(ScoreDutySm record);

    int updateByYmSc(ScoreDutySm record);

    int batchUpdate(List<ScoreDutySm>list);

    List<ScoreDutySm> selectScoreDutySmList(ScoreDutySm record);

    int deleteYM(String year,String month,String dbtype);
}
