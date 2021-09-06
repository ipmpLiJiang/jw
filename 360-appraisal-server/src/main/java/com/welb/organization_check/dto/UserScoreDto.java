package com.welb.organization_check.dto;

import java.util.Date;

/**
 * @author luoyaozu
 * @title: UserEvaluationDto
 * @projectName xh-360appraisal-interface
 * @description: 用户和评估报告dto
 * @date 2019/7/1111:41
 */
public class UserScoreDto {
    private String year;

    private String month;

    private String scoredCode;

    private String scorringCode;

    private String scoreType;

    private String mserialNo;

    private String fserialNo;

    private String dserialNo;

    private String dutyType;

    private Double ratio;

    private Double score;

    private Double aratio;
    private Double bratio;
    private Double cratio;
    private Double dratio;

    private Double ascore;
    private Double bscore;
    private Double cscore;
    private Double dscore;

    private String dbbk;
    private String branchCode;


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

    public String getScoredCode() {
        return scoredCode;
    }

    public void setScoredCode(String scoredCode) {
        this.scoredCode = scoredCode == null ? null : scoredCode.trim();
    }

    public String getDbbk() {
        return dbbk;
    }

    public void setDbbk(String dbbk) {
        this.dbbk = dbbk == null ? null : dbbk.trim();
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode == null ? null : branchCode.trim();
    }

    public String getScorringCode() {
        return scorringCode;
    }

    public void setScorringCode(String scorringCode) {
        this.scorringCode = scorringCode == null ? null : scorringCode.trim();
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType == null ? null : scoreType.trim();
    }

    public String getFSerialNo() {
        return fserialNo;
    }

    public void setFSerialNo(String fserialNo) {
        this.fserialNo = fserialNo == null ? null : fserialNo.trim();
    }

    public String getMserialNo() {
        return mserialNo;
    }

    public void setMserialNo(String mserialNo) {
        this.mserialNo = mserialNo == null ? null : mserialNo.trim();
    }

    public String getDserialNo() {
        return dserialNo;
    }

    public void setDserialNo(String dserialNo) {
        this.dserialNo = dserialNo == null ? null : dserialNo.trim();
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType == null ? null : dutyType.trim();
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getARatio() {
        return aratio;
    }

    public void setARatio(Double aratio) {
        this.aratio = aratio;
    }

    public Double getBRatio() {
        return bratio;
    }

    public void setBRatio(Double bratio) {
        this.bratio = bratio;
    }

    public Double getCRatio() {
        return cratio;
    }

    public void setCRatio(Double cratio) {
        this.cratio = cratio;
    }

    public Double getDRatio() {
        return dratio;
    }

    public void setDRatio(Double dratio) {
        this.dratio = dratio;
    }


    public Double getAScore() {
        return ascore;
    }

    public void setAScore(Double ascore) {
        this.ascore = ascore;
    }

    public Double getBScore() {
        return bscore;
    }

    public void setBScore(Double bscore) {
        this.bscore = bscore;
    }

    public Double getCScore() {
        return cscore;
    }

    public void setCScore(Double cscore) {
        this.cscore = cscore;
    }

    public Double getDScore() {
        return dscore;
    }

    public void setDScore(Double dscore) {
        this.dscore = dscore;
    }
}
