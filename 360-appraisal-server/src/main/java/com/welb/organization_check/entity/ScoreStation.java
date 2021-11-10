package com.welb.organization_check.entity;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScoreStation {
    public static Map<String, String> scoretypes = new LinkedHashMap<>();

    static {
        scoretypes.put("A", "A类评分");
        scoretypes.put("B", "B类评分");
        scoretypes.put("C", "C类评分");
        scoretypes.put("D", "D类评分");
        scoretypes.put("E", "E类评分");
        scoretypes.put("F", "F类评分");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScorredstationcode() {
        return scorredstationcode;
    }

    public void setScorredstationcode(String scorredstationcode) {
        this.scorredstationcode = scorredstationcode == null ? null : scorredstationcode.trim();
    }

    public String getScorredstationname() {
        return scorredstationname;
    }

    public void setScorredstationname(String scorredstationname) {
        this.scorredstationname = scorredstationname;
    }

    public String getScorringstationcode() {
        return scorringstationcode;
    }

    public void setScorringstationcode(String scorringstationcode) {
        this.scorringstationcode = scorringstationcode == null ? null : scorringstationcode.trim();
    }

    public String getScorringstationname() {
        return scorringstationname;
    }

    public void setScorringstationname(String scorringstationname) {
        this.scorringstationname = scorringstationname;
    }

    public String getScoretype() {
        return scoretype;
    }

    public void setScoretype(String scoretype) {
        this.scoretype = scoretype == null ? null : scoretype.trim();
    }

    public String getScoretypename() {
        return scoretypename;
    }

    public void setScoretypename(String scoretypename) {
        this.scoretypename = scoretypename;
    }

    public String getDutycode() {
        return dutycode;
    }

    public void setDutycode(String dutycode) {
        this.dutycode = dutycode == null ? null : dutycode.trim();
    }

    public String getDutyname() {
        return dutyname;
    }

    public void setDutyname(String dutyname) {
        this.dutyname = dutyname;
    }

    public String getDepartmentcode1() {
        return departmentcode1;
    }

    public void setDepartmentcode1(String departmentcode1) {
        this.departmentcode1 = departmentcode1;
    }

    public String getDepartmentcode2() {
        return departmentcode2;
    }

    public void setDepartmentcode2(String departmentcode2) {
        this.departmentcode2 = departmentcode2;
    }

    public String getDepartmentname1() {
        return departmentname1;
    }

    public void setDepartmentname1(String departmentname1) {
        this.departmentname1 = departmentname1;
    }

    public String getDepartmentname2() {
        return departmentname2;
    }

    public void setDepartmentname2(String departmentname2) {
        this.departmentname2 = departmentname2;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype == null ? null : dbtype.trim();
    }

    public String getDbbk() {
        return dbbk;
    }

    public void setDbbk(String dbbk) {
        this.dbbk = dbbk == null ? null : dbbk.trim();
    }

    private Integer id;

    private String scorredstationcode;

    private String scorredstationname;

    private String scorringstationcode;

    private String scorringstationname;

    private String scoretype;

    private String scoretypename;

    private String dutycode;

    private String dutyname;

    //被评分人部门code
    private String departmentcode1;
    //被评分人部门code
    private String departmentcode2;
    //被评分人部门名称
    private String departmentname1;
    //被评分人部门名称
    private String departmentname2;

    private String dbtype;

    private String dbbk;

    public Integer getIsEF() {
        return isEF;
    }

    public void setIsEF(Integer isEF) {
        this.isEF = isEF;
    }

    private Integer isEF;
}
