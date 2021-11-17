package com.welb.organization_check.dto;

/**
 * @author luoyaozu
 * @title: UserEvaluationDto
 * @projectName xh-360appraisal-interface
 * @description: 用户和评估报告dto
 * @date 2019/7/1111:41
 */
public class ScoreDutySmTjDto {

    private  Integer num;

    private String scoreProj;

    private Integer khrs = 0;

    private Integer wcrs = 0;

    private Integer wwcrs = 0;

    private String wwcyy;

    private String scorredcode;

    private String scorredname;

    private String posttype;

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

    public Integer getWcrs() {
        return wcrs;
    }

    public void setWcrs(Integer wcrs) {
        this.wcrs = wcrs;
    }

    public Integer getWwcrs() {
        return wwcrs;
    }

    public void setWwcrs(Integer wwcrs) {
        this.wwcrs = wwcrs;
    }

    public String getWwcyy() {
        return wwcyy;
    }

    public void setWwcyy(String wwcyy) {
        this.wwcyy = wwcyy;
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

    public String getPosttype() {
        return posttype;
    }

    public void setPosttype(String posttype) {
        this.posttype = posttype;
    }
}
