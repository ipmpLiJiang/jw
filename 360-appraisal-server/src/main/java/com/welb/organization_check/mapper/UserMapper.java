package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(String usercode);

    int insertSelective(User record);

    User selectByPrimaryKey(String usercode);

    List<User> selectAllUsers();

    User selectByMoneyCard(String moneycard);
    int updateByPrimaryKeySelective(User record);

    User findUserByUserCode(String userncode);

    User findRaterUserByUserCode(String userncode);

    int updateUserPassword(User user);

    String selectMaxUserCode();

    List<User>selectUserAll(@Param("user") User user,@Param("roleList") List<String> roleList);

    List<User> selectUserScoreAll(@Param("user") User user,@Param("roleList") List<String> roleList);

    List<User> selectUserAllBpfr(String dbtype,String postType);

    List<User>selectUserByStationCode(String stationcode);

    List<User>selectUserByDepartmentCode(String departmentcode);

    List<User>findUserAll();

    List<User> findUserByRoleCode(@Param("qrcode") String qrcode,
                                  @Param("dbtype") String dbtype,
                                  @Param("postType") String postType,
                                  @Param("dbbk") String dbbk,
                                  @Param("year") String year,
                                  @Param("month") String month,
                                  @Param("isDq") boolean isDq);

    List<User>findUserAllBySummary();

    List<User>findFlagUsers();


    User findOne(String usercode);

    User selectPersonnelUserByMoneyCard(String moneycard);

    int deleteUserByMoneyCard(String moneycard);

    User findUserByOne(String usercode);

    List<User>getUserList(@Param("moneycard")String moneycard,@Param("departmentname") String departmentname);

    List<User>findUserByScoreFlowType(@Param("mserialNo") String mserialNo,@Param("scoreType") String scoreType,@Param("dbtype") String dbtype,@Param("dbbk") String dbbk);

    List<User>selectGradeUserList(@Param("dbtype") String dbtype);

    User getUserByMoneyCard(String moneycard);

    User selectUserByMoneyCard(String moneycard);

    User selectUserBuGwByMoneyCard(@Param("user") User user);

    User getUserByUserCode(String usercode);

    void add(User user);

    List<User> findUserBranchByDbbk(String dbbk);

    List<User> selectUserPfr(String dbtype,String year,String month,boolean isDq);


    List<User> findUserByCodeList(@Param("codeList") String[] codeList);

    List<User> selectUserScoreStationList();

    List<User> selectUserByInStationCode(@Param("codeList") String[] codeList,@Param("dbtype") String dbtype);
}
