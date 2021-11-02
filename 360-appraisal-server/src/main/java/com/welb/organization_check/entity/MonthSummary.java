package com.welb.organization_check.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MonthSummary {

    public static HashMap<String,String>states=new LinkedHashMap<>();
    public static HashMap<String,String>months=new LinkedHashMap<>();
    public static HashMap<String,String>scorestatusList=new LinkedHashMap<>();
    static {
        states.put("0","未提交");
        states.put("1","已提交");
        states.put("2","未评分");
        states.put("3","未完成评分");
//        states.put("5", "待提交");
        states.put("5", "自评中");
        states.put("6", "评分中");
        states.put("7", "评分完成");

        months.put("1","1月");
        months.put("2","2月");
        months.put("3","3月");
        months.put("4","4月");
        months.put("5","5月");
        months.put("6","6月");
        months.put("7","7月");
        months.put("8","8月");
        months.put("9","9月");
        months.put("10","10月");
        months.put("11","11月");
        months.put("12","12月");

        scorestatusList.put("1","未评分");
        scorestatusList.put("2","未完成");
        scorestatusList.put("3","已完成");
    }

    private String status;

    private String serialno;

    private String title;

    private String employeecode;

    private String employeename;

    private Date pubdate;

    private String year;

    private String month;

    private String monthname;

    private String state;

    private String statename;

    private String content;

    private byte[] formattext;

    private String savepath;

    private String filename;
    //是否已发送短信
    private String issend="0";

    //新增的score属性
    private String scorredcode;

    private String scorredname;

    private String scorringcode;


    private String scorestatus="1";

    private String scorestatusname;

    public String getScorestatus() {
        return scorestatus;
    }

    private String dbtype;

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype == null ? null : dbtype.trim();
    }


    private String dbbk;

    public String getDbbk() {
        return dbbk;
    }

    public void setDbbk(String dbbk) {
        this.dbbk = dbbk == null ? null : dbbk.trim();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getFormattext() {
        return formattext;
    }

    public void setFormattext(byte[] formattext) {
        this.formattext = formattext;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno == null ? null : serialno.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode == null ? null : employeecode.trim();
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year == null ? null : year.trim();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month == null ? null : month.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public static HashMap<String, String> getStates() {
        return states;
    }

    public static void setStates(HashMap<String, String> states) {
        MonthSummary.states = states;
    }

    public String getStatename() {
        if (state!=null){
            return states.get(this.state);
        }
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public static HashMap<String, String> getMonths() {
        return months;
    }

    public static void setMonths(HashMap<String, String> months) {
        MonthSummary.months = months;
    }

    public String getMonthname() {
        if (month!=null){
            return months.get(this.month);
        }
        return monthname;
    }

    public void setMonthname(String monthname) {
        this.monthname = monthname;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getScorredname() {
        return scorredname;
    }

    public void setScorredname(String scorredname) {
        this.scorredname = scorredname;
    }

    public String getScorredcode() {
        return scorredcode;
    }

    public void setScorredcode(String scorredcode) {
        this.scorredcode = scorredcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getScorringcode() {
        return scorringcode;
    }

    public void setScorringcode(String scorringcode) {
        this.scorringcode = scorringcode;
    }

    public String getIssend() {
        return issend;
    }

    public void setIssend(String issend) {
        this.issend = issend;
    }

    public Integer getIsSc() {
        return isSc;
    }

    public void setIsSc(Integer isSc) {
        this.isSc = isSc;
    }

    Integer isSc;
}
