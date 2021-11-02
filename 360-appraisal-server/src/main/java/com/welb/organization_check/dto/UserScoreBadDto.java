package com.welb.organization_check.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserScoreBadDto {

    public static Map<String, String> dutytypes = new LinkedHashMap<>();
    static {
        dutytypes.put("0", "基础指标");
        dutytypes.put("1", "岗位职责");
        dutytypes.put("2", "重点任务");
        dutytypes.put("3", "目标任务");
        dutytypes.put("4", "政治建设");
        dutytypes.put("5", "思想建设");
        dutytypes.put("6", "组织建设");
        dutytypes.put("7", "党建创新");
        dutytypes.put("8", "作风建设");
    }
    private Integer id;

    private String mserialNo;

    private String fserialNo;

    private String year;

    private String month;

    private String usercode;

    private String username;

    private String stationcode;

    private String stationname;

    private String moneycard;

    private String departmentcode;

    private String departmentname;

    private String scoredCode;

    private String scorringCode;

    private String scorringName;

    private String postType;

    private String dutyCode;

    private String dutyName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMserialNo() {
        return mserialNo;
    }

    public void setMserialNo(String mserialNo) {
        this.mserialNo = mserialNo;
    }

    public String getFserialNo() {
        return fserialNo;
    }

    public void setFserialNo(String fserialNo) {
        this.fserialNo = fserialNo;
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

    public String getScoredCode() {
        return scoredCode;
    }

    public void setScoredCode(String scoredCode) {
        this.scoredCode = scoredCode;
    }

    public String getScorringCode() {
        return scorringCode;
    }

    public void setScorringCode(String scorringCode) {
        this.scorringCode = scorringCode;
    }

    public String getScorringName() {
        return scorringName;
    }

    public void setScorringName(String scorringName) {
        this.scorringName = scorringName;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getDutyCode() {
        return dutyCode;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }

    public String getCpsm() {
        return cpsm;
    }

    public void setCpsm(String cpsm) {
        this.cpsm = cpsm;
    }

    private String dutyType;

    private String cpsm;

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    private String scoreType;

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    private  String dbtype;

    public static Map<String, String> getDutytypes() {
        return dutytypes;
    }

    public static void setDutytypes(Map<String, String> dutytypes) {
        dutytypes = dutytypes;
    }

    private String dutyTypeName;
    public String getDutyTypeName() {
        if (this.dutyType != null) {
            return dutytypes.get(this.dutyType);
        }
        return dutyTypeName;
    }

    public void setDutyTypeName(String dutyTypeName) {
        this.dutyTypeName = dutyTypeName;
    }


    private String scorringstationcode;

    public String getScorringStationcode() {
        return scorringstationcode;
    }

    public void setScorringStationcode(String scorringstationcode) {
        this.scorringstationcode = scorringstationcode;
    }

    public String getScorringStationname() {
        return scorringstationname;
    }

    public void setScorringStationname(String scorringstationname) {
        this.scorringstationname = scorringstationname;
    }

    public String getScorringMoneycard() {
        return scorringmoneycard;
    }

    public void setScorringMoneycard(String scorringmoneycard) {
        this.scorringmoneycard = scorringmoneycard;
    }

    public String getScorringDepartmentcode() {
        return scorringdepartmentcode;
    }

    public void setScorringDepartmentcode(String scorringdepartmentcode) {
        this.scorringdepartmentcode = scorringdepartmentcode;
    }

    public String getScorringDepartmentname() {
        return scorringdepartmentname;
    }

    public void setScorringDepartmentname(String scorringdepartmentname) {
        this.scorringdepartmentname = scorringdepartmentname;
    }

    private String scorringstationname;

    private String scorringmoneycard;

    private String scorringdepartmentcode;

    private String scorringdepartmentname;

    public String getDbbk() {
        return dbbk;
    }

    public void setDbbk(String dbbk) {
        this.dbbk = dbbk;
    }

    private  String dbbk;
}
