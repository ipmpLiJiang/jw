package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.MonthSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MonthSummaryMapper {
    int deleteByPrimaryKey(String serialno,String dbtype);

    int insert(MonthSummary summary);

    int insertSelective(MonthSummary summary);

    int updateSubmitStateBySerialNo(String serialno,String dbtype);

    int updateGradeStateBySerialNo(String serialno,String dbtype);

    int updateGradeStateBySerialNoZp(String serialno,String dbtype);

    int updateStateZpAll(String year,String month,String dbtype,String postType);

    int updateStateAll(String year,String month,String dbtype,String postType);

    int updateFinishGradeBySerialNo(String serialno,String dbtype);

    int updateFinishGradeAll(String year,String month,String dbtype,String postType);

    int updateFinishGradeScorringAll(String year,String month,String dbtype);

    MonthSummary selectByPrimaryKey(String serialno,String dbtype);

    int updateByPrimaryKeySelective(MonthSummary summary);

    int updateByPrimaryKey(MonthSummary summary);

    List<MonthSummary>selectSummaryLikeAll(MonthSummary summary);

    int updateStateBySerialNo(MonthSummary summary);

    List<MonthSummary>selectSummaryByYearAndMonth(@Param("year")String year,
                                                  @Param("month")String month,
                                                  @Param("dbtype")String dbtype);

    List<MonthSummary>selectSummaryListByYearAndMonth(@Param("year") String year,
                                                      @Param("month") String month,
                                                      @Param("dbtype") String dbtype,
                                                      @Param("postType") String postType);

    MonthSummary selectSummaryByYearAndMonthAndCode(@Param("year")String year,@Param("month")String month,@Param("employeecode")String employeecode, @Param("dbtype")String dbtype);

    List<MonthSummary>findMonthSummaryAll();

    int batchDelete(@Param("serialnos")List<String>serialnos,@Param("dbtype")String dbtype);

    List<MonthSummary>selectSerialNoByEmployeeCode(String employeecode, @Param("dbtype")String dbtype);

    int batchInsert(@Param("list") List<MonthSummary>list);

    int batchUpdate(@Param("list") List<MonthSummary>list);

    List<MonthSummary>selectListByYearAndMonth(@Param("year")String year,@Param("month")String month, @Param("dbtype")String dbtype);

    List<MonthSummary>selectSummaryByInEmployeeCode(@Param("year")String year,@Param("month")String month, @Param("dbtype")String dbtype,@Param("codeList") List<String> codeList);

    int deleteYM(@Param("year") String year,@Param("month") String month,@Param("dbtype") String dbtype,@Param("postType") String postType);
}
