package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ScoreFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreFlowMapper {
    int deleteByPrimaryKey(String serialno);

    int insert(ScoreFlow record);

    int insertSelective(ScoreFlow record);

    ScoreFlow selectByPrimaryKey(String serialno);

    int updateByPrimaryKeySelective(ScoreFlow record);

    int updateByPrimaryKey(ScoreFlow record);

    List<ScoreFlow> selectByScoreFlow(@Param("mserialno") String mserialno,
                                      @Param("scorringcode") String scorringcode,
                                      @Param("dbtype") String dbtype);

    List<ScoreFlow> selectByScoredCodeFlow(@Param("mserialno") String mserialno,
                                           @Param("scoredcode") String scoredcode,
                                           @Param("dbtype") String dbtype);

    List<ScoreFlow> selectByScorringScoredFlow(@Param("mserialno") String mserialno,
                                               @Param("scorringcode") String scorringcode,
                                               @Param("scoredcode") String scoredcode,
                                               @Param("dbtype") String dbtype);


    ScoreFlow getTotal(ScoreFlow scoreflow);

    Double getScoreByType(@Param("mserialno") String mserialno, @Param("scoretype") String scoretype, @Param("dbtype") String dbtype);

    int getScoreByTypeCount(@Param("mserialno") String mserialno, @Param("scoretype") String scoretype, @Param("dbtype") String dbtype);

    String selectMaxSerialNo();

    List<ScoreFlow> selectScoreAllByScoredCode(@Param("scoredcode") String scoredcode, @Param("year") String year, @Param("month") String month, @Param("dbtype") String dbtype);

    Double selectMaxScoreByScoredCode(@Param("scoredcode") String scoredcode, @Param("year") String year, @Param("month") String month, @Param("dbtype") String dbtype);

    Double selectMinScoreByScoredCode(@Param("scoredcode") String scoredcode, @Param("year") String year, @Param("month") String month, @Param("dbtype") String dbtype);

    List<ScoreFlow> selectFlowByScorredCode(String scorredcode, @Param("dbtype") String dbtype);

    List<ScoreFlow> selectFlowByScorringCode(String scorringcode, @Param("dbtype") String dbtype);

    List<ScoreFlow> getSingleTotalScoreAll(@Param("mserialno") String mserialno, @Param("scoretype") String scoretype,
                                           @Param("dutytype") String dutytype, @Param("orderid") Integer orderid, @Param("dbtype") String dbtype);

    List<ScoreFlow> selectScoreByCodeAndType(ScoreFlow scoreFlow);

    int batchDelete(List<String> scorringSerialnos);

    int batchUpdate(@Param("list") List<ScoreFlow> list);

    int batchInsert(@Param("list") List<ScoreFlow> list);

    ScoreFlow selectFlowByMserialNoAndScorringCode(@Param("mserialno") String mserialno, @Param("scorringcode") String scorringcode, @Param("dbtype") String dbtype);

    Double getTypeAvgScore(@Param("mserialno") String mserialno, @Param("scoretype") String scoretype, @Param("dbtype") String dbtype);

    int getTotalCount(@Param("mserialno") String mserialno, @Param("scorringcode") String scorringcode, @Param("dbtype") String dbtype);

    List<ScoreFlow> findFlowsByCode(String usercode, @Param("dbtype") String dbtype);

    List<ScoreFlow> selectByScoreFlowType(@Param("mserialno") String mserialno, @Param("scoreType") String scoreType, @Param("dbtype") String dbtype);


    List<ScoreFlow> selectFlowByMonthSummaryList(String year,
                                                 String month,
                                                 String dbtype,
                                                 String postType,
                                                 String userCode);

    List<ScoreFlow> selectScoreFlowScorringCode(@Param("year") String year,
                                                @Param("month") String month,
                                                @Param("dbtype") String dbtype,
                                                @Param("scorringcode") String scorringcode);

    int deleteYM(String year, String month, String dbtype,String postType,String userCode);

    List<ScoreFlow> selectSummaryFlowByYMTOrPTList(@Param("year") String year, @Param("month") String month,
                                                   @Param("dbtype") String dbtype,@Param("postType") String postType,
                                                   @Param("typeList") List<String> typeList);

}
