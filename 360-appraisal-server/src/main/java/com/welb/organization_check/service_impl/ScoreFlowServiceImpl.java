package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.ScoreFlow;
import com.welb.organization_check.mapper.ScoreFlowMapper;
import com.welb.organization_check.service.IScoreFlowService;
import com.welb.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: ScoreFlowServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 当前上一月度评分人给被评分人打分的业务层逻辑接口的实现类
 * @date 2019/6/516:54
 */
@Service
@Transactional
public class ScoreFlowServiceImpl implements IScoreFlowService {
    @Resource
    ScoreFlowMapper flowMapper;

    @Override
    public List<ScoreFlow> selectByScoreFlow(String mserialno, String scorringcode, String dbtype) {
        return flowMapper.selectByScoreFlow(mserialno, scorringcode, dbtype);
    }

    @Override
    public List<ScoreFlow> selectByScoredCodeFlow(String mserialno, String scoredcode, String dbtype) {
        return flowMapper.selectByScoredCodeFlow(mserialno, scoredcode, dbtype);
    }

    @Override
    public List<ScoreFlow> selectByScorringScoredFlow(String mserialno, String scorringcode, String scoredcode, String dbtype) {
        return flowMapper.selectByScorringScoredFlow(mserialno, scorringcode, scoredcode, dbtype);
    }

    @Override
    public Double getScoreByType(String mserialno, String scoretype, String dbtype) {
        return flowMapper.getScoreByType(mserialno, scoretype, dbtype);
    }

    @Override
    public int getScoreByTypeCount(String mserialno, String scoretype, String dbtype) {
        return flowMapper.getScoreByTypeCount(mserialno, scoretype, dbtype);
    }

    @Override
    public int updateByPrimaryKeySelective(ScoreFlow record) {
        return flowMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insertSelective(ScoreFlow record) {//主键自增长
        if (record.getSerialno() == null || record.getSerialno().equals("")) {
            String serialNo = StringUtil.getGuid();
            record.setSerialno(serialNo);
        }
        return flowMapper.insertSelective(record);
    }

    @Override
    public List<ScoreFlow> selectScoreAllByScoredCode(String scoredcode, String year, String month, String dbtype) {
        return flowMapper.selectScoreAllByScoredCode(scoredcode, year, month, dbtype);
    }

    @Override
    public Double selectMaxScoreByScoredCode(String scoredcode, String year, String month, String dbtype) {
        return flowMapper.selectMaxScoreByScoredCode(scoredcode, year, month, dbtype);
    }

    @Override
    public Double selectMinScoreByScoredCode(String scoredcode, String year, String month, String dbtype) {
        return flowMapper.selectMinScoreByScoredCode(scoredcode, year, month, dbtype);
    }

    @Override
    public List<ScoreFlow> selectFlowByScorredCode(String scorredcode, String dbtype) {
        return flowMapper.selectFlowByScorredCode(scorredcode, dbtype);
    }

    @Override
    public List<ScoreFlow> selectFlowByScorringCode(String scorringcode, String dbtype) {
        return flowMapper.selectFlowByScorringCode(scorringcode, dbtype);
    }

    @Override
    public List<ScoreFlow> getSingleTotalScoreAll(String mserialno, String scoretype, String dutytype, Integer orderid, String dbtype) {
        return flowMapper.getSingleTotalScoreAll(mserialno, scoretype, dutytype, orderid, dbtype);
    }

    @Override
    public int deleteByPrimaryKey(String serialno) {
        return flowMapper.deleteByPrimaryKey(serialno);
    }

    @Override
    public List<ScoreFlow> selectScoreByCodeAndType(ScoreFlow scoreFlow) {
        return flowMapper.selectScoreByCodeAndType(scoreFlow);
    }

    @Override
    public int batchDelete(List<String> scorringSerialnos) {
        return flowMapper.batchDelete(scorringSerialnos);
    }

    @Override
    public int batchUpdate(List<ScoreFlow> list) {
        return flowMapper.batchUpdate(list);
    }

    @Override
    public int batchInsert(List<ScoreFlow> list) {
        return flowMapper.batchInsert(list);
    }

    @Override
    public ScoreFlow selectFlowByMserialNoAndScorringCode(String mserialno, String scorringcode, String dbtype) {
        return flowMapper.selectFlowByMserialNoAndScorringCode(mserialno, scorringcode, dbtype);
    }

    @Override
    public Double getTypeAvgScore(String mserialno, String scoretype, String dbtype) {
        return flowMapper.getTypeAvgScore(mserialno, scoretype, dbtype);
    }

    @Override
    public int getTotalCount(String mserialno, String scorringcode, String dbtype) {
        return flowMapper.getTotalCount(mserialno, scorringcode, dbtype);
    }

    @Override
    public List<ScoreFlow> findFlowsByCode(String usercode, String dbtype) {
        return flowMapper.findFlowsByCode(usercode, dbtype);
    }

    @Override
    public List<ScoreFlow> selectByScoreFlowType(String mserialno, String scoreType, String dbtype) {
        return flowMapper.selectByScoreFlowType(mserialno, scoreType, dbtype);
    }

    @Override
    public List<ScoreFlow> selectFlowByMonthSummaryList(String year, String month, String dbtype, String postType,String userCode) {
        return flowMapper.selectFlowByMonthSummaryList(year, month, dbtype, postType,userCode);
    }

    @Override
    public List<ScoreFlow> selectScoreFlowScorringCode(String year, String month, String dbtype, String scorringcode) {
        return flowMapper.selectScoreFlowScorringCode(year, month, dbtype, scorringcode);
    }

    @Override
    public int deleteYM(String year, String month, String dbtype, String postType,String userCode) {
        return flowMapper.deleteYM(year, month, dbtype, postType,userCode);
    }

    @Override
    public List<ScoreFlow> selectSummaryFlowByYMTOrPTList(String year, String month, String dbtype, String postType,List<String> typeList) {
        return flowMapper.selectSummaryFlowByYMTOrPTList(year, month, dbtype, postType,typeList);
    }
}
