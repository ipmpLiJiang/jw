package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.ScoreDutySm;
import com.welb.organization_check.mapper.ScoreDutySmMapper;
import com.welb.organization_check.service.IScoreDutySmService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ScoreDutySmServiceImpl implements IScoreDutySmService {
    @Resource
    private ScoreDutySmMapper scoreDutySmMapper;
    @Override
    public int deleteByPrimaryKey(Integer Id) {
        return scoreDutySmMapper.deleteByPrimaryKey(Id);
    }

    @Override
    public int insert(ScoreDutySm record) {
        return scoreDutySmMapper.insert(record);
    }

    @Override
    public int insertSelective(ScoreDutySm record) {
        return scoreDutySmMapper.insertSelective(record);
    }

    @Override
    public int batchInset(List<ScoreDutySm> list){
        return scoreDutySmMapper.batchInset(list);
    }

    @Override
    public ScoreDutySm selectByPrimaryKey(Integer Id) {
        return scoreDutySmMapper.selectByPrimaryKey(Id);
    }

    @Override
    public int updateByPrimaryKey(ScoreDutySm record) {
        return scoreDutySmMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateByYmSc(ScoreDutySm record) {
        return scoreDutySmMapper.updateByYmSc(record);
    }

    @Override
    public int batchUpdate(List<ScoreDutySm> list) {
        for(ScoreDutySm item : list) {
            scoreDutySmMapper.updateByPrimaryKey(item);
        }
        return 0;
    }

    @Override
    public List<ScoreDutySm> selectScoreDutySmList(ScoreDutySm record,String postType) {
        return scoreDutySmMapper.selectScoreDutySmList(record,postType);
    }

    @Override
    public int deleteYM(String year,String month,String dbtype,String postType){
        return scoreDutySmMapper.deleteYM(year,month,dbtype,postType);
    }
}
