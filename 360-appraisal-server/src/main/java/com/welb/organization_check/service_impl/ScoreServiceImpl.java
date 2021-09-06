package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.Score;
import com.welb.organization_check.mapper.ScoreMapper;
import com.welb.organization_check.service.IScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @author luoyaozu
 * @title: ScoreServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 评价关系业务层接口的实现类
 * @date 2019/5/2710:54
 */
@Service
@Transactional
public class ScoreServiceImpl implements  IScoreService {
    @Resource
    ScoreMapper scoreMapper;

    @Override
    public int deleteByPrimaryKey(String id) {
        return scoreMapper.deleteByPrimaryKey(id);
    }
    @Override
    public int insertSelective(Score score) {
        String maxId = scoreMapper.selectMaxId();
        if (maxId==null){
            score.setId("100");
        }else {
            int num = Integer.parseInt(maxId.trim());
            num++;
            String id = String.valueOf(num);
            score.setId(id);
        }
        return scoreMapper.insertSelective(score);
    }

    @Override
    public int updateByPrimaryKeySelective(Score score) {
        return scoreMapper.updateByPrimaryKeySelective(score);
    }




    @Override
    public List<Score> selectScoresByscorredAndUser(String scorredcode,String scoretype,
                                                 String stationcode,String username,String dbtype) {
        return scoreMapper.selectScoresByscorredAndUser(scorredcode,scoretype,stationcode,username,dbtype);
    }
    @Override
    public List<Score> selectScoresByscorredAndUserName(String scorredcode,String scoretype, String username,String dbtype) {
        return scoreMapper.selectScoresByscorredAndUserName(scorredcode,scoretype,username,dbtype);
    }
    @Override
    public List<Score> selectScoresByScorredCode(String scorredcode,String scoretype,String dbtype) {
        return scoreMapper.selectScoresByScorredCode(scorredcode, scoretype,dbtype);
    }
    @Override
    public List<Score> selectScoresByscorredAndUserStationCode(String scorredcode,String scoretype,
                                                    String stationcode,String dbtype) {
        return scoreMapper.selectScoresByscorredAndUserStationCode(scorredcode,scoretype,stationcode,dbtype);
    }


    @Override
    public List<Score> selectScoresByScorringCode(String scorredcode,String scoretype,String dbtype) {
        return scoreMapper.selectScoresByScorringCode(scorredcode,scoretype,dbtype);
    }
    @Override
    public List<Score> selectScoresByScorringAndUser(String scorredcode,String scoretype,
                                                  String stationcode,String username,String dbtype) {
        return scoreMapper.selectScoresByScorringAndUser(scorredcode,scoretype,stationcode, username,dbtype);
    }
    @Override
    public List<Score> selectScoresByScorringAndUserName(String scorredcode,String scoretype,String username,String dbtype) {
        return scoreMapper.selectScoresByScorringAndUserName(scorredcode,scoretype, username,dbtype);
    }

    @Override
    public List<Score> selectScoresByScorringAndUserStationCode(String scorredcode,String scoretype,String stationcode,String dbtype) {
        return scoreMapper.selectScoresByScorringAndUserStationCode(scorredcode,scoretype,stationcode,dbtype);
    }

    @Override
    public Score selectTypeByCode(String scorredcode, String scorringcode, String dbtype) {
        return scoreMapper.selectTypeByCode(scorredcode, scorringcode,dbtype);
    }

    @Override
    public List<Score> selectTypeByCodeList(String scorredcode, String scorringcode, String dbtype) {
        return scoreMapper.selectTypeByCodeList(scorredcode, scorringcode,dbtype);
    }

    @Override
    public List<Score> selectScoreByScorredCode(String scorredcode,String dbtype) {
        return scoreMapper.selectScoreByScorredCode(scorredcode,dbtype);
    }

    @Override
    public List<Score> selectScoreByCodeAndType(Score score) {
        return scoreMapper.selectScoreByCodeAndType(score);
    }

    @Override
    public Score selectByPrimaryKey(String id) {
        return scoreMapper.selectByPrimaryKey(id);
    }

    @Override
    public int batchDelete(List<String> scorredIds) {
        return scoreMapper.batchDelete(scorredIds);
    }

    @Override
    public List<Score> selectIdByScorredCode(String scorredcode) {
        return scoreMapper.selectIdByScorredCode(scorredcode);
    }

    @Override
    public List<Score> selectIdByScorringCode(String scorringcode) {
        return scoreMapper.selectIdByScorringCode(scorringcode);
    }

    @Override
    public List<Score> findScoreAll(String dbtype) {
        return scoreMapper.findScoreAll(dbtype);
    }

    @Override
    public List<Score> findScoreScorringInList(Score score,String[] scorringList){
        return scoreMapper.findScoreScorringInList(score,scorringList);
    }

    @Override
    public List<Score> findScoreDutyInList(Score score,String[] dutyCodeList){
        return scoreMapper.findScoreDutyInList(score,dutyCodeList);
    }

    @Override
    public int deleteDutyScore(Score score,String[] dutyCodeList){
        return scoreMapper.deleteDutyScore(score,dutyCodeList);
    }

    @Override
    public int deleteScoreDutyScorringUser(Score score) {
        List<Score> list = scoreMapper.selectDutyScoreList(score);
        if(list.size()>0){
            List<String> ids = new ArrayList<>();
            for (Score s:list){
                ids.add(s.getId());
            }
            return  scoreMapper.batchDelete(ids);
        }else{
            return 0;
        }
    }
}
