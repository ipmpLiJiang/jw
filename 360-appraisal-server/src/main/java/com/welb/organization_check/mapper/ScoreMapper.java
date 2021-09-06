package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreMapper {
    int deleteByPrimaryKey(String id);

    int insert(Score record);

    int insertSelective(Score record);

    Score selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Score record);

    int updateByPrimaryKey(Score record);

    String selectMaxId();

    List<Score>selectScoresByscorredAndUser(@Param("scorredcode")String scorredcode,
                                         @Param("scoretype")String scoretype,
                                         @Param("stationcode")String stationcode,
                                         @Param("username") String username,
                                            @Param("dbtype")String dbtype);

    List<Score>selectScoresByScorredCode(@Param("scorredcode")String scorredcode,
                                         @Param("scoretype")String scoretype,
                                         @Param("dbtype")String dbtype);

    List<Score>selectScoresByscorredAndUserName(@Param("scorredcode")String scorredcode,
                                                @Param("scoretype")String scoretype,
                                                @Param("username") String username,
                                                @Param("dbtype")String dbtype);

    List<Score>selectScoresByscorredAndUserStationCode(@Param("scorredcode")String scorredcode,
                                                       @Param("scoretype")String scoretype,
                                                       @Param("stationcode")String stationcode,
                                                       @Param("dbtype")String dbtype);


    List<Score>selectScoresByScorringCode(@Param("scorredcode")String scorredcode,@Param("scoretype")String scoretype,
                                          @Param("dbtype")String dbtype);

    List<Score>selectScoresByScorringAndUser(@Param("scorredcode")String scorredcode,
                                          @Param("scoretype")String scoretype,
                                          @Param("stationcode")String stationcode,
                                          @Param("username") String username,
                                             @Param("dbtype")String dbtype);
    List<Score>selectScoresByScorringAndUserName(@Param("scorredcode")String scorredcode,
                                          @Param("scoretype")String scoretype,
                                          @Param("username") String username,
                                                 @Param("dbtype")String dbtype);

    List<Score>selectScoresByScorringAndUserStationCode(@Param("scorredcode")String scorredcode,
                                             @Param("scoretype")String scoretype,
                                             @Param("stationcode")String stationcode,
                                                        @Param("dbtype")String dbtype);

    Score selectTypeByCode(@Param("scorredcode")String scorredcode,@Param("scorringcode")String scorringcode,@Param("dbtype")String dbtype);

    List<Score> selectTypeByCodeList(@Param("scorredcode")String scorredcode,@Param("scorringcode")String scorringcode,@Param("dbtype")String dbtype);

    List<Score>selectScoreByScorredCode(String scorredcode,String dbtype);

    List<Score>selectIdByScorredCode(String scorredcode);

    List<Score>selectIdByScorringCode(String scorringcode);

    List<Score> selectScoreByCodeAndType(Score score);

    int batchDelete(List<String>scorredIds);


    List<Score>findScoreAll(String dbtype);

    List<Score> findScoreScorringInList(@Param("score")Score score,@Param("scorringList") String[] scorringList);

    List<Score> findScoreDutyInList(@Param("score")Score score,@Param("dutyCodeList") String[] dutyCodeList);

    int deleteDutyScore(@Param("score")Score score,@Param("dutyCodeList")  String[] dutyCodeList);

    List<Score> selectDutyScoreList(@Param("score") Score score);




}
