package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ScoreHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ScoreHistory history);

    ScoreHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScoreHistory history);

    ScoreHistory selectOneByHistory(ScoreHistory history);

    List<ScoreHistory>selectHistoryList(@Param("history") ScoreHistory history,@Param("roleList") List<String> roleList);

    List<ScoreHistory>gradeList(ScoreHistory history);

    List<ScoreHistory>selectGradeHisotyList(ScoreHistory history);

    List<ScoreHistory>selectUserHisotyList(@Param("history") ScoreHistory history,@Param("roleList") List<String> roleList);

    int batchInsert(List<ScoreHistory>list);

    ScoreHistory selectScoreHistoryByUserCode(String usercode,String dbtype);

    List<ScoreHistory>oneClickDown(@Param("year")String year,@Param("month")String month,@Param("roleList") List<String> roleList, @Param("dbtype")String dbtype);

    List<ScoreHistory>findScoreHistoryList(@Param("year")String year,@Param("month")String month);

    List<ScoreHistory> findUserScoreHistory(@Param("year")String year,@Param("month")String month,@Param("dbtype")String dbtype,@Param("dbbk")String dbbk);

    List<ScoreHistory>selectHistoryByInUserCode(@Param("year")String year,@Param("month")String month,@Param("dbtype")String dbtype,@Param("codeList")List<String> codeList);

    List<ScoreHistory> selectHistoryByMonthSummaryList(@Param("year")String year,@Param("month")String month,@Param("dbtype")String dbtype);
}
