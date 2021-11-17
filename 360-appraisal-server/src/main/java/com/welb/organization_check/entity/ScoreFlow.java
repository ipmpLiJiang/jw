package com.welb.organization_check.entity;

import java.util.Date;

public class ScoreFlow {
    private String serialno;

    private String mserialno;

    private String scoredcode;

    private String scorringcode;

    private Date scoredate;

    private String scoretype;

    private Integer state;

    private Double score;

    private Double ratio;

    private String Scorringname;

    private String singlescore;

    private String dutyname;

    private  String stationname;

    private String deparmentname;

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno == null ? null : serialno.trim();
    }

    public String getMserialno() {
        return mserialno;
    }

    public void setMserialno(String mserialno) {
        this.mserialno = mserialno == null ? null : mserialno.trim();
    }

    public void setScoredcode(String scoredcode) {
        this.scoredcode = scoredcode == null ? null : scoredcode.trim();
    }

    public String getScorringcode() {
        return scorringcode;
    }

    public void setScorringcode(String scorringcode) {
        this.scorringcode = scorringcode == null ? null : scorringcode.trim();
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

    public Date getScoredate() {
        return scoredate;
    }

    public void setScoredate(Date scoredate) {
        this.scoredate = scoredate;
    }

    public String getScoretype() {
        return scoretype;
    }

    public void setScoretype(String scoretype) {
        this.scoretype = scoretype == null ? null : scoretype.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public String getScoredcode() {
        return scoredcode;
    }

    public String getScorringname() {
        return Scorringname;
    }

    public void setScorringname(String scorringname) {
        Scorringname = scorringname;
    }

    public String getSinglescore() {
        return singlescore;
    }

    public void setSinglescore(String singlescore) {
        this.singlescore = singlescore;
    }

    public String getDutyname() {
        return dutyname;
    }

    public void setDutyname(String dutyname) {
        this.dutyname = dutyname;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getDeparmentname() {
        return deparmentname;
    }

    public void setDeparmentname(String deparmentname) {
        this.deparmentname = deparmentname;
    }

    private String scoreState;

    public String getScoreState() {
        return scoreState;
    }

    public void setScoreState(String scoreState) {
        this.scoreState = scoreState == null ? "1" : scoreState.trim();
    }

    public String getStationcode() {
        return stationcode;
    }

    public void setStationcode(String stationcode) {
        this.stationcode = stationcode;
    }

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode;
    }

    public String getPosttype() {
        return posttype;
    }

    public void setPosttype(String posttype) {
        this.posttype = posttype;
    }

    public String getScoredname() {
        return scoredname;
    }

    public void setScoredname(String scoredname) {
        this.scoredname = scoredname;
    }

    private  String scoredname;
    private  String stationcode;
    private  String branchcode;
    private String posttype;

}
