package com.welb.organization_check.dto;

/**
 * @author luoyaozu
 * @title: UserEvaluationDto
 * @projectName xh-360appraisal-interface
 * @description: 用户和评估报告dto
 * @date 2019/7/1111:41
 */
public class ScoreFlowScorringTjDto {

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getScoreProj() {
        return scoreProj;
    }

    public void setScoreProj(String scoreProj) {
        this.scoreProj = scoreProj;
    }

    public Integer getKhrs() {
        return khrs;
    }

    public void setKhrs(Integer khrs) {
        this.khrs = khrs;
    }

    public Integer getYcyrs() {
        return ycyrs;
    }

    public void setYcyrs(Integer ycyrs) {
        this.ycyrs = ycyrs;
    }

    public Integer getSjcyrs() {
        return sjcyrs;
    }

    public void setSjcyrs(Integer sjcyrs) {
        this.sjcyrs = sjcyrs;
    }

    public Integer getYcyzb() {
        return ycyzb;
    }

    public void setYcyzb(Integer ycyzb) {
        this.ycyzb = ycyzb;
    }

    public Integer getSjcyzb() {
        return sjcyzb;
    }

    public void setSjcyzb(Integer sjcyzb) {
        this.sjcyzb = sjcyzb;
    }

    public Integer getYcyABCrs() {
        return ycyABCrs;
    }

    public void setYcyABCrs(Integer ycyABCrs) {
        this.ycyABCrs = ycyABCrs;
    }

    public Integer getSjcyABCrs() {
        return sjcyABCrs;
    }

    public void setSjcyABCrs(Integer sjcyABCrs) {
        this.sjcyABCrs = sjcyABCrs;
    }

    public Integer getYcyDrs() {
        return ycyDrs;
    }

    public void setYcyDrs(Integer ycyDrs) {
        this.ycyDrs = ycyDrs;
    }

    public Integer getSjcyDrs() {
        return sjcyDrs;
    }

    public void setSjcyDrs(Integer sjcyDrs) {
        this.sjcyDrs = sjcyDrs;
    }

    public Integer getYcyEFrs() {
        return ycyEFrs;
    }

    public void setYcyEFrs(Integer ycyEFrs) {
        this.ycyEFrs = ycyEFrs;
    }

    public Integer getSjcyEFrs() {
        return sjcyEFrs;
    }

    public void setSjcyEFrs(Integer sjcyEFrs) {
        this.sjcyEFrs = sjcyEFrs;
    }

    public String getScorredcode() {
        return scorredcode;
    }

    public void setScorredcode(String scorredcode) {
        this.scorredcode = scorredcode;
    }

    public String getScorredname() {
        return scorredname;
    }

    public void setScorredname(String scorredname) {
        this.scorredname = scorredname;
    }

    public String getScorringcode() {
        return scorringcode;
    }

    public void setScorringcode(String scorringcode) {
        this.scorringcode = scorringcode;
    }

    public String getScorringname() {
        return scorringname;
    }

    public void setScorringname(String scorringname) {
        this.scorringname = scorringname;
    }

    public String getScoretype() {
        return scoretype;
    }

    public void setScoretype(String scoretype) {
        this.scoretype = scoretype;
    }

    public String getPosttype() {
        return posttype;
    }

    public void setPosttype(String posttype) {
        this.posttype = posttype;
    }

    private  Integer num;

    private String scoreProj;

    private Integer khrs = 0;

    //应参与人数
    private Integer ycyrs = 0;

    //实际参与人数
    private Integer sjcyrs = 0;

    //应参与指标
    private Integer ycyzb = 0;

    //实际参与指标
    private Integer sjcyzb = 0;

    //应参与人数ABC
    private Integer ycyABCrs = 0;

    //实际参与人数ABC
    private Integer sjcyABCrs = 0;

    //应参与人数D
    private Integer ycyDrs = 0;

    //实际参与人数D
    private Integer sjcyDrs = 0;

    //应参与人数EF
    private Integer ycyEFrs = 0;

    //实际参与人数EF
    private Integer sjcyEFrs = 0;

    private String scorredcode;

    private String scorredname;

    private String scorringcode;

    private String scorringname;

    private String scoretype;

    private String posttype;

    public String getScorestate() {
        return scorestate;
    }

    public void setScorestate(String scorestate) {
        this.scorestate = scorestate;
    }

    private String scorestate;

    public String getFserialno() {
        return fserialno;
    }

    public void setFserialno(String fserialno) {
        this.fserialno = fserialno;
    }

    private String fserialno;
}
