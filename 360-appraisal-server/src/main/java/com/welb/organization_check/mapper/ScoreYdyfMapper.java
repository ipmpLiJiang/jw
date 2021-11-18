package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ScoreYdyf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreYdyfMapper {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(ScoreYdyf ydyf);

    ScoreYdyf selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScoreYdyf ydyf);

    List<ScoreYdyf> selectYdyfList(@Param("ydyf")ScoreYdyf ydyf,@Param("postType") String postType);

    int deleteYM(@Param("year") String year, @Param("month") String month);

}
