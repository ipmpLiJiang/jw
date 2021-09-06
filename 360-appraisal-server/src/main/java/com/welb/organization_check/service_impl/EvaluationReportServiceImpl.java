package com.welb.organization_check.service_impl;

import com.welb.organization_check.dto.UserEvaluationDto;
import com.welb.organization_check.entity.EvaluationReport;
import com.welb.organization_check.mapper.EvaluationReportMapper;
import com.welb.organization_check.service.IEvaluationReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: EvaluationReportServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/199:47
 */
@Service
@Transactional
public class EvaluationReportServiceImpl implements IEvaluationReportService {
    @Resource
    EvaluationReportMapper evaluationReportMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return evaluationReportMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(EvaluationReport evaluationReport) {
        return evaluationReportMapper.insertSelective(evaluationReport);
    }

    @Override
    public int updateByPrimaryKeySelective(EvaluationReport evaluationReport) {
        return evaluationReportMapper.updateByPrimaryKeySelective(evaluationReport);
    }

    @Override
    public EvaluationReport selectByPrimaryKey(Integer id) {
        return evaluationReportMapper.selectByPrimaryKey(id);
    }

    @Override
    public EvaluationReport selectReportByUserCode(EvaluationReport report) {
        return evaluationReportMapper.selectReportByUserCode(report);
    }

    @Override
    public Double selectReportTotalScore(String year,String month, String dbtype) {
        return evaluationReportMapper.selectReportTotalScore(year, month,dbtype);
    }

    @Override
    public int selectReportCount(String year,String month, String dbtype) {
        return evaluationReportMapper.selectReportCount(year, month,dbtype);
    }

    @Override
    public int selectMaxAndMinReportCount(String year, String month,String dbtype) {
        return evaluationReportMapper.selectMaxAndMinReportCount(year,month,dbtype);
    }

    @Override
    public List<EvaluationReport> selectAllEvaluationReport() {
        return evaluationReportMapper.selectAllEvaluationReport();
    }

    @Override
    public List<UserEvaluationDto> selectAllEvaluationReportLike(UserEvaluationDto evaluationDto,List<String> roleList) {
        return evaluationReportMapper.selectAllEvaluationReportLike(evaluationDto,roleList);
    }

    @Override
    public int selectEvacationReportCount() {
        return evaluationReportMapper.selectEvacationReportCount();
    }

    @Override
    public Double selectSumOfMaxScore(String year,String month,String dbtype) {
        return evaluationReportMapper.selectSumOfMaxScore(year, month,dbtype);
    }

    @Override
    public Double selectSumOfMinScore(String year,String month,String dbtype) {
        return evaluationReportMapper.selectSumOfMinScore(year, month,dbtype);
    }

    @Override
    public int selectMaxId() {
        return evaluationReportMapper.selectMaxId();
    }

    @Override
    public List<EvaluationReport> selectEvaluationByUserCode(String usercode) {
        return evaluationReportMapper.selectEvaluationByUserCode(usercode);
    }

    @Override
    public int batchDelete(List<Integer> reportIds) {
        return evaluationReportMapper.batchDelete(reportIds);
    }

    @Override
    public int updateAvgScore(double avgscore, String year, String month, String dbtype) {
        return evaluationReportMapper.updateAvgScore(avgscore, year, month,dbtype);
    }
    @Override
    public List<EvaluationReport> selectEvaluationReportList(EvaluationReport report){
        return  evaluationReportMapper.selectEvaluationReportList(report);
    }

    @Override
    public List<EvaluationReport>selectEvaluationReportByInUserCode(String year,String month, String dbtype,List<String> codeList){
        return evaluationReportMapper.selectEvaluationReportByInUserCode(year,month, dbtype,codeList);
    }
}
