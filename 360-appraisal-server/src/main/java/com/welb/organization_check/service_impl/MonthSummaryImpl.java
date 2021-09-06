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
    public int updateSubmitStateBySerialNo(String serialno) {
        return monthSummaryMapper.updateSubmitStateBySerialNo(serialno);
    }

    @Override
    public int updateGradeStateBySerialNo(String serialno) {
        return monthSummaryMapper.updateGradeStateBySerialNo(serialno);
    }

    @Override
    public int updateStateAll(String dbtype) {
        return monthSummaryMapper.updateStateAll(dbtype);
    }

    @Override
    public int updateFinishGradeBySerialNo(String serialno) {
        return monthSummaryMapper.updateFinishGradeBySerialNo(serialno);
    }

    @Override
    public int updateFinishGradeAll(String dbtype) {
        return monthSummaryMapper.updateFinishGradeAll(dbtype);
    }

    @Override
    public int deleteByPrimaryKey(String serialno) {
        return monthSummaryMapper.deleteByPrimaryKey(serialno);
    }

    @Override
    public MonthSummary selectByPrimaryKey(String serialno) {
        return monthSummaryMapper.selectByPrimaryKey(serialno);
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
    public List<MonthSummary> selectSummaryListByYearAndMonth(String year, String month, String dbtype) {
        return monthSummaryMapper.selectSummaryListByYearAndMonth(year, month, dbtype);
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
    public int batchDelete(List<String> serialnos) {
        return monthSummaryMapper.batchDelete(serialnos);
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
    public List<MonthSummary> selectListByYearAndMonth(String year, String month, String dbtype) {
        return monthSummaryMapper.selectSummaryListByYearAndMonth(year, month, dbtype);
    }

    @Override
    public List<MonthSummary>selectSummaryByInEmployeeCode(String year,String month, String dbtype,List<String> codeList){
        return monthSummaryMapper.selectSummaryByInEmployeeCode(year,month, dbtype,codeList);
    }
}
