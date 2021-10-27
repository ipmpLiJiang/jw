package com.welb.organization_check.service;

import com.welb.organization_check.entity.ScoreYdyf;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IScoreHistoryService
 * @projectName xh-360appraisal-interface
 * @description: 历史评分数据接口
 * @date 2020/7/21
 */

public interface IScoreYdyfService {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ScoreYdyf ydyf);

    ScoreYdyf selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScoreYdyf ydyf);

    List<ScoreYdyf> findYdyfList(ScoreYdyf ydyf);

    int deleteYM(String year,String month);
}
