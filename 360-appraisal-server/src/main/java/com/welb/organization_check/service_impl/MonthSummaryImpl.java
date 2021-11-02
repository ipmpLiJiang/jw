package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.MonthSummary;
import com.welb.organization_check.mapper.MonthSummaryMapper;
import com.welb.organization_check.service.IMonthSummaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: MonthSummaryImpl
 * @projectName xh-360appraisal-interface
 * @description: 个人月度总结业务层接口的实现类
 * @date 2019/5/2914:34
 */
@Service
@Transactional
public class MonthSummaryImpl implements IMonthSummaryService {
    @Resource
    MonthSummaryMapper monthSummaryMapper;

    @Override
    public int insertSelective(MonthSummary summary) {

        return monthSummaryMapper.insertSelective(summary);
    }

    @Override
    public int updateByPrimaryKeySelective(MonthSummary summary) {
        return monthSummaryMapper.updateByPrimaryKeySelective(summary);
    }

    @Override
    public int updateSubmitStateBySerialNo(String serialno,String dbtype) {
        return monthSummaryMapper.updateSubmitStateBySerialNo(serialno,dbtype);
    }

    @Override
    public int updateGradeStateBySerialNo(String serialno,String dbtype) {
        return monthSummaryMapper.updateGradeStateBySerialNo(serialno,dbtype);
    }

    @Override
    public int updateGradeStateBySerialNoZp(String serialno,String dbtype) {
        return monthSummaryMapper.updateGradeStateBySerialNoZp(serialno,dbtype);
    }

    @Override
    public int updateStateAll(String year,String month,String dbtype,String postType) {
        return monthSummaryMapper.updateStateAll(year,month,dbtype,postType);
    }
    @Override
    public int updateStateZpAll(String year,String month,String dbtype,String postType){
        return monthSummaryMapper.updateStateZpAll(year,month,dbtype,postType);
    }

    @Override
    public int updateFinishGradeBySerialNo(String serialno,String dbtype) {
        return monthSummaryMapper.updateFinishGradeBySerialNo(serialno,dbtype);
    }

    @Override
    public int updateFinishGradeAll(String year,String month,String dbtype,String postType) {
        return monthSummaryMapper.updateFinishGradeAll(year,month,dbtype,postType);
    }

    @Override
    public int deleteByPrimaryKey(String serialno,String dbtype) {
        return monthSummaryMapper.deleteByPrimaryKey(serialno,dbtype);
    }

    @Override
    public MonthSummary selectByPrimaryKey(String serialno,String dbtype) {
        return monthSummaryMapper.selectByPrimaryKey(serialno,dbtype);
    }

    @Override
    public List<MonthSummary> selectSummaryLikeAll(MonthSummary summary) {
        return monthSummaryMapper.selectSummaryLikeAll(summary);
    }

    @Override
    public int updateStateBySerialNo(MonthSummary summary) {
        return monthSummaryMapper.updateStateBySerialNo(summary);
    }

    @Override
    public List<MonthSummary> selectSummaryByYearAndMonth(String year, String month, String dbtype) {
        return monthSummaryMapper.selectSummaryByYearAndMonth(year, month, dbtype);
    }

    @Override
    public List<MonthSummary> selectSummaryListByYearAndMonth(String year, String month, String dbtype,String postType) {
        return monthSummaryMapper.selectSummaryListByYearAndMonth(year, month, dbtype,postType);
    }

    @Override
    public MonthSummary selectSummaryByYearAndMonthAndCode(String year, String month, String employeecode, String dbtype) {
        return monthSummaryMapper.selectSummaryByYearAndMonthAndCode(year, month, employeecode, dbtype);
    }

    @Override
    public List<MonthSummary> findMonthSummaryAll() {
        return monthSummaryMapper.findMonthSummaryAll();
    }

    @Override
    public int batchDelete(List<String> serialnos,String dbtype) {
        return monthSummaryMapper.batchDelete(serialnos,dbtype);
    }

    @Override
    public List<MonthSummary> selectSerialNoByEmployeeCode(String employeecode, String dbtype) {
        return monthSummaryMapper.selectSerialNoByEmployeeCode(employeecode,dbtype);
    }

    @Override
    public int batchInsert(List<MonthSummary> list) {
        return monthSummaryMapper.batchInsert(list);
    }

    @Override
    public int batchUpdate(List<MonthSummary> list) {
        return monthSummaryMapper.batchUpdate(list);
    }

    @Override
    public List<MonthSummary>selectSummaryByInEmployeeCode(String year,String month, String dbtype,List<String> codeList){
        return monthSummaryMapper.selectSummaryByInEmployeeCode(year,month, dbtype,codeList);
    }
    @Override
    public int deleteYM(String year,String month,String dbtype,String postType){
        return monthSummaryMapper.deleteYM(year,month,dbtype,postType);
    }
}
