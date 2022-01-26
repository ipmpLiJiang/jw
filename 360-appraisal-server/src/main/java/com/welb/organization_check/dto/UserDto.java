package com.welb.organization_check.dto;

import com.welb.organization_check.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author luoyaozu
 * @title: UserDto
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/414:56
 */
public class UserDto {
    //个人月度总结属性
    public static HashMap<String, String> states = new LinkedHashMap<>();
    public static HashMap<String, String> months = new LinkedHashMap<>();
    public static HashMap<String, String> scorestatusList = new LinkedHashMap<>();
    public static Map<String, String> roles = new LinkedHashMap<>();
    public static Map<String, String> dbbks = new LinkedHashMap<>();
    public static Map<String, String> postTypes = new LinkedHashMap<>();

    static {
        states.put("", "--");
        states.put("0", "未提交");
        states.put("1", "已提交");
//        states.put("5", "待提交");
        states.put("5", "自评中");
        states.put("6", "评分中");
        states.put("7", "评分完成");

        months.put("1", "第1季度");
        months.put("2", "第2季度");
        months.put("3", "第3季度");
        months.put("4", "第4季度");
//        months.put("1", "1月");
//        months.put("2", "2月");
//        months.put("3", "3月");
//        months.put("4", "4月");
//        months.put("5", "5月");
//        months.put("6", "6月");
//        months.put("7", "7月");
//        months.put("8", "8月");
//        months.put("9", "9月");
//        months.put("10", "10月");
//        months.put("11", "11月");
//        months.put("12", "12月");

        scorestatusList.put("1","未评分");
        scorestatusList.put("2","未完成");
        scorestatusList.put("3","已完成");


        roles.put("50","组织部部长");
        roles.put("100","组织部");
        roles.put("150","打分用户");
        roles.put("200","部门长");
        roles.put("300","普通用户");
        roles.put("400","考勤超级管理员");
        roles.put("500","考勤管理员");
        roles.put("1000","党办管理员");
        roles.put("2000","院领导");

        dbbks.put("1","组织委员纪检委员");
        dbbks.put("2","宣传委员青年委员");
        dbbks.put("3","党支部书记");
        dbbks.put("4","党总支书记");

        postTypes.put("1","科主任");
        postTypes.put("2","护士长");
        postTypes.put("3","行政");

    }

    private String year;

    private String month;

    private String monthname;

    private String state;

    private String title;

    private String content;

    private String statename;

    private String serialno;

    private String employeecode;

    private String employeename;

    private Date pubdate;

    private String savepath;

    private String filename;

    private String issend;

    private String roletype;

    //用户的属性
    private String usercode;//用户主键

    private String username;

    private String stationcode;

    private String stationname;

    private String fullstationcode;

    private String moneycard;

    private String departmentcode;

    private String departmentname;

    private String flag;

    private Double aratio;

    private Double bratio;

    private Double cratio;

    private Double dratio;

    private Double eratio;

    private Double fratio;

    private Double AScore;

    private Double BScore;

    private Double CScore;

    private Double DScore;

    private Double EScore;

    private Double FScore;

    private Double TotalScore;

    private String mobile;

    private String scorestatus="";

    private String scorestatusname;

    private String rolecode;

    private String rolename;

    private String dbtype;

    private String branchcode;

    private String branchname;

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode == null ? null : branchcode.trim();
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype == null ? null : dbtype.trim();
    }

    public static Map<String, String> getDbbks() {
        return dbbks;
    }

    public static void setDbbks(Map<String, String> dbbks) {
        UserDto.dbbks = dbbks;
    }

    private String dbbk;

    public String getDbbk() {
        return dbbk;
    }

    public void setDbbk(String dbbk) {
        this.dbbk = dbbk == null ? null : dbbk.trim();
    }


    private String dbbkName;

    public String getDbbkName() {
        if (dbbk!=null){
            return dbbks.get(this.dbbk);
        }
        return dbbkName;
    }

    public void setDbbkName(String dbbkName) {
        this.dbbkName = dbbkName;
    }

    public String getScorestatus() {
        return scorestatus;
    }

    public void setScorestatus(String scorestatus) {
        this.scorestatus = scorestatus;
    }

    public static HashMap<String, String> getScoreStatusList() {
        return scorestatusList;
    }

    public static void setScoreStatusList(HashMap<String, String> scoreStatusList) {
        scorestatusList = scorestatusList;
    }

    public String getScorestatusname() {
        if(scorestatus!=null){
            return scorestatusList.get(scorestatus);
        }
        return scorestatusname;
    }

    public void setScorestatusname(String scorestatusname) {
        this.scorestatusname = scorestatusname;
    }

    public static HashMap<String, String> getStates() {
        return states;
    }

    public static void setStates(HashMap<String, String> states) {
        UserDto.states = states;
    }

    public static HashMap<String, String> getMonths() {
        return months;
    }

    public static void setMonths(HashMap<String, String> months) {
        UserDto.months = months;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthname() {
        if (month != null) {
            return months.get(this.month);
        }
        return monthname;
    }

    public void setMonthname(String monthname) {
        this.monthname = monthname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatename() {
        if (state != null) {
            return states.get(this.state);
        }
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

  /*  public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }*/

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStationcode() {
        return stationcode;
    }

    public void setStationcode(String stationcode) {
        this.stationcode = stationcode;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getFullstationcode() {
        return fullstationcode;
    }

    public void setFullstationcode(String fullstationcode) {
        this.fullstationcode = fullstationcode;
    }

    public String getMoneycard() {
        return moneycard;
    }

    public void setMoneycard(String moneycard) {
        this.moneycard = moneycard;
    }

    public String getDepartmentcode() {
        return departmentcode;
    }

    public void setDepartmentcode(String departmentcode) {
        this.departmentcode = departmentcode;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public Double getAratio() {
        return aratio;
    }

    public void setAratio(Double aratio) {
        this.aratio = aratio;
    }

    public Double getBratio() {
        return bratio;
    }

    public void setBratio(Double bratio) {
        this.bratio = bratio;
    }

    public Double getCratio() {
        return cratio;
    }

    public void setCratio(Double cratio) {
        this.cratio = cratio;
    }

    public Double getDratio() {
        return dratio;
    }

    public void setDratio(Double dratio) {
        this.dratio = dratio;
    }

    public Double getEratio() {
        return eratio;
    }

    public void setEratio(Double eratio) {
        this.eratio = eratio;
    }

    public Double getFratio() {
        return fratio;
    }

    public void setFratio(Double fratio) {
        this.fratio = fratio;
    }

    public Double getAScore() {
        return AScore;
    }

    public void setAScore(Double AScore) {
        this.AScore = AScore;
    }

    public Double getBScore() {
        return BScore;
    }

    public void setBScore(Double BScore) {
        this.BScore = BScore;
    }

    public Double getCScore() {
        return CScore;
    }

    public void setCScore(Double CScore) {
        this.CScore = CScore;
    }

    public Double getDScore() {
        return DScore;
    }

    public void setDScore(Double DScore) {
        this.DScore = DScore;
    }

    public Double getEScore() {
        return EScore;
    }

    public void setEScore(Double EScore) {
        this.EScore = EScore;
    }

    public Double getFScore() {
        return FScore;
    }

    public void setFScore(Double FScore) {
        this.FScore = FScore;
    }

    public Double getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(Double totalScore) {
        TotalScore = totalScore;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public String getSavepath() {
        return savepath;
    }

    public void setSavepath(String savepath) {
        this.savepath = savepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIssend() {
        return issend;
    }

    public void setIssend(String issend) {
        this.issend = issend;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    public static HashMap<String, String> getScorestatusList() {
        return scorestatusList;
    }

    public static void setScorestatusList(HashMap<String, String> scorestatusList) {
        UserDto.scorestatusList = scorestatusList;
    }

    public static Map<String, String> getRoles() {
        return roles;
    }

    public static void setRoles(Map<String, String> roles) {
        UserDto.roles = roles;
    }

    public String getRolecode() {
        return rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }

    public String getRolename() {
        if(rolecode!=null){
            return this.roles.get(rolecode);
        }
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    String postType;
    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public Integer getIsSc() {
        return isSc;
    }

    public void setIsSc(Integer isSc) {
        this.isSc = isSc;
    }

    Integer isSc;

    private String postTypeName;

    public String getPostTypeName() {
        if (postType!=null){
            return postTypes.get(this.postType);
        }
        return postTypeName;
    }

    public void setPostTypeName(String postTypeName) {
        this.postTypeName = postTypeName;
    }

    public static Map<String, String> getPostTypes() {
        return postTypes;
    }

    public static void setPostTypes(Map<String, String> postTypes) {
        UserDto.postTypes = postTypes;
    }
}
