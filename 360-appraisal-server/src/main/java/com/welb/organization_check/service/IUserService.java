package com.welb.organization_check.service;

import com.welb.organization_check.entity.User;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IUserService
 * @projectName kao
 * @description: 用户业务层接口
 * @date 2019/5/160:11
 */
public interface IUserService {
    /**
     * 通过usercode查找用户信息
     * @param usercode
     * @return
     */
    User findUserByUserCode(String usercode);

    User findUserRoleByUserCode(String usercode);

    List<User> selectAllUsers();

    /**
     * 查找所有用户信息 包括模糊查询匹配
     * @param user
     * @return
     */
    List<User> selectUserAll(User user,List<String> roleList);

    List<User> selectUserScoreAll(User user,List<String> roleList);

    /**
     * 通过usercode删除用户信息
     * @param usercode
     * @return
     */
    int deleteByPrimaryKey(String usercode);

    /**
     * 添加用户
     * @param user
     * @return
     */
    int insertSelective(User user);

    /**
     * 修改用户信息
     * @param user
     * @return
     */

    int updateByPrimaryKeySelective(User user);

    /**
     * 修改用户密码
     * @param user
     * @return
     */
    int updateUserPassword(User user);

    /**
     * 通过stationcode查找所用用户
     * @param stationcode
     * @return
     */
    List<User>selectUserByStationCode(String stationcode);

    /**
     * 通过部门code查找用户
     * @param departmentcode
     * @return
     */
    List<User>selectUserByDepartmentCode(String departmentcode);

    /**
     * 通过发薪号查找用户
     * @param moneycard
     * @return
     */
    User selectByMoneyCard(String moneycard);

    /**
     * 查找所有用户
     * @return
     */
    List<User>findUserAll();

    List<User> findUserByRoleCode(String qrcode, String dbtype,String postType,String dbbk,String year,String month,boolean isDq);

    User selectUserBuGwByMoneyCard(User user);

    /**
     * 查找已删除用户
     * @return
     */
    List<User>findFlagUsers();

    List<User> selectUserAllBpfr(String dbtype,String postType);


    User findOne(String usercode);


    User findRaterUserByUserCode(String userncode);


    /**
     * 通过发薪号查找人事部用户
     * @param moneycard
     * @return
     */
    User selectPersonnelUserByMoneyCard(String moneycard);

    /**
     *通过发薪号删除用户
     * @param moneycard
     * @return
     */
    int deleteUserByMoneyCard(String moneycard);

    User findUserByOne(String usercode);

    /**
     * 通过部门名称查询人员
     * @param moneycard
     * @param departmentname
     * @return
     */
    List<User>getUserList(String moneycard,String departmentname);


    /**
     * 查询所有与月度总结有关的数据
     * @return
     */
    List<User>findUserAllBySummary();


    /**
     * 查找所有的打分用户
     * @return
     */
    List<User>selectGradeUserList(String dbtype);


    /**
     * 通过发信号查找用户
     * @param moneycard
     * @return
     */
    User getUserByMoneyCard(String moneycard);


    /**
     * 登录
     * @param moneycard
     * @return
     */
    User selectUserByMoneyCard(String moneycard);

    /**
     * 通过usercode和roleType查找拥有问卷权限用户信息
     */
    User getUserByUserCode(String usercode);

    void add(User user);

    List<User>findUserByScoreFlowType(String mserialNo, String scoreType,String dbtype,String dbbk);


    List<User> findUserBranchByDbbk(User user);

    List<User> selectUserPfr(String dbtype,String year,String month,boolean isDq);

    List<User> findUserByCodeList(String[] codeList);

    List<User> selectUserScoreStationList();

    List<User> selectUserByInStationCode(String[] codeList,String dbtype);

}
