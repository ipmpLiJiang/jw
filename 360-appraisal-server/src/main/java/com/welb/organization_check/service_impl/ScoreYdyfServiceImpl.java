package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.ScoreYdyf;
import com.welb.organization_check.mapper.ScoreYdyfMapper;
import com.welb.organization_check.service.IScoreYdyfService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: ScoreHistoryServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 历史评分数据接口的实现类
 * @date 2020/7/21
 */
@Service
@Transactional
public class ScoreYdyfServiceImpl implements IScoreYdyfService {
    @Resource
    ScoreYdyfMapper ydyfMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return ydyfMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(ScoreYdyf ydyf) {
        return ydyfMapper.insertSelective(ydyf);
    }

    @Override
    public ScoreYdyf selectByPrimaryKey(Integer id) {
        return ydyfMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ScoreYdyf ydyf) {
        return ydyfMapper.updateByPrimaryKeySelective(ydyf);
    }

    @Override
    public List<ScoreYdyf> findYdyfList(ScoreYdyf ydyf) {
        return ydyfMapper.selectYdyfList(ydyf);
    }


    @Override
    public int deleteYM(String year,String month){
        return ydyfMapper.deleteYM(year,month);
    }
}
