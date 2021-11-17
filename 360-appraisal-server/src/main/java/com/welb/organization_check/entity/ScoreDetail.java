// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   ScoreDetail.java

package com.welb.organization_check.entity;


public class ScoreDetail
{

    private String serialNo;
    private String FSerialNo;
    private String DSerialNo;
    private Double ratio;
    private String score;
    private String scoretype;

    private String cpsm;
    private String zpsm;

    private String dbtype;
    private String dbbk;

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

    public ScoreDetail()
    {
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getFSerialNo()
    {
        return FSerialNo;
    }

    public void setFSerialNo(String FSerialNo)
    {
        this.FSerialNo = FSerialNo != null ? FSerialNo.trim() : null;
    }

    public String getDSerialNo()
    {
        return DSerialNo;
    }

    public void setDSerialNo(String DSerialNo)
    {
        this.DSerialNo = DSerialNo != null ? DSerialNo.trim() : null;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public String getScore()
    {
        return score;
    }

    public void setScore(String score)
    {
        this.score = score != null ? score.trim() : null;
    }

    public String getScoretype() {
        return scoretype;
    }

    public void setScoretype(String scoretype) {
        this.scoretype = scoretype == null ? null : scoretype.trim();
    }


    public String getCpsm() {
        return cpsm;
    }

    public void setCpsm(String cpsm) {
        this.cpsm = cpsm == null ? null : cpsm.trim();
    }

    public String getZpsm() {
        return zpsm;
    }

    public void setZpsm(String zpsm) {
        this.zpsm = zpsm == null ? null : zpsm.trim();
    }


    private String scoredcode;

    private String scorringcode;

    private String scorringname;

    private String mserialno;

    private String scoreState;

    public String getScoredcode() {
        return scoredcode;
    }

    public void setScoredcode(String scoredcode) {
        this.scoredcode = scoredcode;
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

    public String getMserialno() {
        return mserialno;
    }

    public void setMserialno(String mserialno) {
        this.mserialno = mserialno;
    }

    public String getScoreState() {
        return scoreState;
    }

    public void setScoreState(String scoreState) {
        this.scoreState = scoreState;
    }

}
