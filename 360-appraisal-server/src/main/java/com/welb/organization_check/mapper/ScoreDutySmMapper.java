package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ScoreDutySm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreDutySmMapper {

    int deleteByPrimaryKey(Integer Id);

    int insert(ScoreDutySm record);

    int insertSelective(ScoreDutySm record);

    int batchInset(List<ScoreDutySm> list);

    ScoreDutySm selectByPrimaryKey(Integer Id);

    int updateByPrimaryKey(ScoreDutySm record);

    int updateByYmSc(ScoreDutySm record);

    List<ScoreDutySm> selectScoreDutySmList(ScoreDutySm record);

    int deleteYM(@Param("year") String year, @Param("month") String month, @Param("dbtype") String dbtype);
}
