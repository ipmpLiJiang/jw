package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.User;
import com.welb.organization_check.mapper.UserMapper;
import com.welb.organization_check.service.IUserService;
import com.welb.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.welb.sysBase.util.Constant.INITIAL_PASSWORD;

/**
 * @author luoyaozu
 * @title: UserService
 * @projectName kao
 * @description: 用户业务层接口的实现类
 * @date 2019/5/160:40
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User findUserByUserCode(String usercode) {
        return userMapper.selectByPrimaryKey(usercode);
    }

    @Override
    public User findUserRoleByUserCode(String usercode) {
        return userMapper.findUserByUserCode(usercode);
    }

    @Override
    public List<User> selectAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    public List<User> selectUserAll(User user,List<String> roleList) {
        return userMapper.selectUserAll(user,roleList);
    }

    @Override
    public List<User> selectUserScoreAll(User user,List<String> roleList){
        return userMapper.selectUserScoreAll(user,roleList);
    }


    @Override
    public int deleteByPrimaryKey(String usercode) {
        return userMapper.deleteByPrimaryKey(usercode);
    }

    @Override
    public int insertSelective(User user) {
        //实现主键usercode自增
        String userCode = userMapper.selectMaxUserCode();
        if (userCode==null){
            user.setUsercode("100");
        }else {

            int num = Integer.parseInt(userCode.trim());
            num++;
            String usercode = String.valueOf(num);
            user.setUsercode(usercode);
        }
        //新添加的数据是初始密码为jw123456
        user.setPassword(INITIAL_PASSWORD);
        //对密码进行加密
        String password = MD5.md5(user.getPassword());
        user.setPassword(password);
        user.setFlag("0");
        return userMapper.insertSelective(user);
    }

    @Override
    public int updateByPrimaryKeySelective(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int updateUserPassword(User user) {
        return userMapper.updateUserPassword(user);
    }

    @Override
    public List<User> selectUserByStationCode(String stationcode) {
        return userMapper.selectUserByStationCode(stationcode);
    }

    @Override
    public List<User> selectUserByDepartmentCode(String departmentcode) {
        return userMapper.selectUserByDepartmentCode(departmentcode);
    }

    @Override
    public User selectByMoneyCard(String moneycard) {
        return userMapper.selectByMoneyCard(moneycard);
    }

    @Override
    public List<User> findUserAll() {
        return userMapper.findUserAll();
    }

    @Override
    public List<User> selectUserAllBpfr(String dbtype,String postType){
        return userMapper.selectUserAllBpfr(dbtype,postType);
    }

    //查询当前评分关系人员数据
    @Override
    public List<User> findUserByRoleCode(String qrcode,String dbtype,String postType,String dbbk,String year,String month,boolean isDq) {
        return userMapper.findUserByRoleCode(qrcode, dbtype,postType,dbbk,year,month,isDq);
    }

    @Override
    public User selectUserBuGwByMoneyCard(User user){
        return userMapper.selectUserBuGwByMoneyCard(user);
    }

    @Override
    public List<User> findFlagUsers() {
        return userMapper.findFlagUsers();
    }

    @Override
    public User findOne(String usercode) {
        return userMapper.findOne(usercode);
    }

    @Override
    public User findRaterUserByUserCode(String userncode) {
        return userMapper.findRaterUserByUserCode(userncode);
    }

    @Override
    public User selectPersonnelUserByMoneyCard(String moneycard) {
        return userMapper.selectPersonnelUserByMoneyCard(moneycard);
    }

    @Override
    public int deleteUserByMoneyCard(String moneycard) {
        return userMapper.deleteUserByMoneyCard(moneycard);
    }

    @Override
    public User findUserByOne(String usercode) {
        return userMapper.findUserByOne(usercode);
    }

    @Override
    public List<User> getUserList(String moneycard,String departmentname) {
        return userMapper.getUserList(moneycard,departmentname);
    }

    @Override
    public List<User> findUserAllBySummary() {
        return userMapper.findUserAllBySummary();
    }

    @Override
    public List<User> selectGradeUserList(String dbtype) {
        return userMapper.selectGradeUserList(dbtype);
    }

    @Override
    public User getUserByMoneyCard(String moneycard) {
        return userMapper.getUserByMoneyCard(moneycard);
    }

    @Override
    public User selectUserByMoneyCard(String moneycard) {
        return userMapper.selectUserByMoneyCard(moneycard);
    }

    @Override
    public User getUserByUserCode(String usercode) {
        return userMapper.getUserByUserCode(usercode);
    }

    @Override
    public void add(User user) {
        userMapper.add(user);
    }

    @Override
    public List<User>findUserByScoreFlowType(String mserialNo, String scoreType,String dbtype,String dbbk){
        return  userMapper.findUserByScoreFlowType(mserialNo,scoreType,dbtype,dbbk);
    }

    @Override
    public List<User> findUserBranchByDbbk(User user){
        return  userMapper.findUserBranchByDbbk(user);
    }

    @Override
    public List<User>  selectUserPfr(String dbtype,String year,String month,boolean isDq){
        return  userMapper.selectUserPfr(dbtype,year,month,isDq);
    }

    @Override
    public List<User> findUserByCodeList(String[] codeList){
        return  userMapper.findUserByCodeList(codeList);
    }

    @Override
    public List<User> selectUserScoreStationList(){
        return  userMapper.selectUserScoreStationList();
    }

    @Override
    public List<User> selectUserByInStationCode(String[] codeList,String dbtype){
        return userMapper.selectUserByInStationCode(codeList,dbtype);
    }

}
