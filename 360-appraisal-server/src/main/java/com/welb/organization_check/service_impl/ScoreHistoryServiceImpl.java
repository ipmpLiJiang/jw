package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.ScoreHistory;
import com.welb.organization_check.mapper.ScoreHistoryMapper;
import com.welb.organization_check.service.IScoreHistoryService;
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
public class ScoreHistoryServiceImpl implements IScoreHistoryService {
    @Resource
    ScoreHistoryMapper historyMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return historyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(ScoreHistory history) {
        return historyMapper.insertSelective(history);
    }

    @Override
    public ScoreHistory selectByPrimaryKey(Integer id) {
        return historyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ScoreHistory history) {
        return historyMapper.updateByPrimaryKeySelective(history);
    }

    @Override
    public ScoreHistory selectOneByHistory(ScoreHistory history) {
        return historyMapper.selectOneByHistory(history);
    }

    @Override
    public List<ScoreHistory> selectHistoryList(ScoreHistory history,String qrcode,String postType,boolean isDq) {
        return historyMapper.selectHistoryList(history,qrcode,postType,isDq);
    }

    @Override
    public List<ScoreHistory> gradeList(ScoreHistory history,boolean isDq) {
        return historyMapper.gradeList(history,isDq);
    }

    @Override
    public List<ScoreHistory> selectGradeHisotyList(ScoreHistory history,boolean isDq) {
        return historyMapper.selectGradeHisotyList(history,isDq);
    }

    @Override
    public List<ScoreHistory> selectUserHisotyList(ScoreHistory history,String qrcode,String postType,boolean isDq) {
        return historyMapper.selectUserHisotyList(history,qrcode,postType,isDq);
    }

    @Override
    public int batchInsert(List<ScoreHistory> list) {
        return historyMapper.batchInsert(list);
    }

    @Override
    public ScoreHistory selectScoreHistoryByUserCode(String usercode,String dbtype) {
        return historyMapper.selectScoreHistoryByUserCode(usercode,dbtype);
    }

    @Override
    public List<ScoreHistory> oneClickDown(String year, String month,List<String> roleList, String dbtype) {
        return historyMapper.oneClickDown(year, month,roleList, dbtype);
    }
    @Override
    public List<ScoreHistory> findScoreHistoryList(String year, String month) {
        return historyMapper.findScoreHistoryList(year, month);
    }

    @Override
    public List<ScoreHistory> findUserScoreHistory(String year, String month,String dbtype,String dbbk,String postType) {
        return historyMapper.findUserScoreHistory(year, month,dbtype,dbbk,postType);
    }

    @Override
    public List<ScoreHistory>selectHistoryByInUserCode(String year,String month, String dbtype,List<String> codeList){
        return historyMapper.selectHistoryByInUserCode(year,month, dbtype,codeList);
    }

    @Override
    public List<ScoreHistory> selectHistoryByMonthSummaryList(String year,String month, String dbtype,String postType,String userCode){
        return historyMapper.selectHistoryByMonthSummaryList(year,month,dbtype,postType,userCode);
    }

    @Override
    public int deleteYM(String year,String month,String dbtype,String postType,String userCode){
        return historyMapper.deleteYM(year,month,dbtype,postType,userCode);
    }
}
