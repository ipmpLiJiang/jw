package com.welb.organization_check.dto;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author luoyaozu
 * @title: UserEvaluationDto
 * @projectName xh-360appraisal-interface
 * @description: 用户和评估报告dto
 * @date 2019/7/1111:41
 */
public class UserEvaluationDto {

    public static Map<String, String> dbbks = new LinkedHashMap<>();
    static {
        dbbks.put("1","组织委员纪检委员");
        dbbks.put("2","宣传委员青年委员");
        dbbks.put("3","党支部书记");
        dbbks.put("4","党总支书记");
    }
    private Integer id;

    private String moneycard;

    private String usercode;

    private String username;

    private Double totalscore;

    private Double basicscore;

    private Double keyscore;

    private Double zdScore;

    private Double mbScore;

    private Double sumMbAvgScore;

    private Double avgscore;

    private Double maxscore;

    private Double minscore;

    private Double scoredifference;

    private Double totalcomparelast;

    private Double totalcomparemark;

    private String year;

    private String month;

    private Double maxcomparelast;

    private Double maxcomparemark;

    private Double mincomparelast;

    private Double mincomparemark;

    private Double plan;//进度

    private String stationname;

    private String departmentname;

    private Double ascore;

    private Double bscore;

    private Double cscore;

    private Double dscore;

    private Double escore;

    private Double fscore;

    private Double dfScore;

    private Double zfScore;

    private String dbtype;

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype == null ? null : dbtype.trim();
    }

    private String dbbkName;

    private String dbbk;

    public static Map<String, String> getDbbks() {
        return dbbks;
    }

    public static void setDbbks(Map<String, String> dbbks) {
        UserEvaluationDto.dbbks = dbbks;
    }

    public String getDbbk() {
        return dbbk;
    }

    public void setDbbk(String dbbk) {
        this.dbbk = dbbk == null ? null : dbbk.trim();
    }


    public String getDbbkName() {
        if (dbbk!=null){
            return dbbks.get(this.dbbk);
        }
        return dbbkName;
    }

    public void setDbbkName(String dbbkName) {
        this.dbbkName = dbbkName;
    }

    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    private String mserialno;

    public String getMserialno() {
        return mserialno;
    }

    public void setMserialno(String mserialno) {
        this.mserialno = mserialno == null ? null : mserialno.trim();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMoneycard() {
        return moneycard;
    }

    public void setMoneycard(String moneycard) {
        this.moneycard = moneycard;
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

    public Double getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(Double totalscore) {
        this.totalscore = totalscore;
    }

    public Double getBasicscore() {
        return basicscore;
    }

    public void setBasicscore(Double basicscore) {
        this.basicscore = basicscore;
    }

    public Double getKeyscore() {
        return keyscore;
    }

    public void setKeyscore(Double keyscore) {
        this.keyscore = keyscore;
    }

    public Double getZdScore() {
        return zdScore;
    }

    public void setZdScore(Double zdScore) {
        this.zdScore = zdScore;
    }

    public Double getMbScore() {
        return mbScore;
    }

    public void setMbScore(Double mbScore) {
        this.mbScore = mbScore;
    }

    public Double getDfScore() {
        return dfScore;
    }

    public void setDfScore(Double dfScore) {
        this.dfScore = dfScore;
    }

    public Double getZfScore() {
        return zfScore;
    }

    public void setZfScore(Double zfScore) {
        this.zfScore = zfScore;
    }

    public Double getAvgscore() {
        return avgscore;
    }

    public void setAvgscore(Double avgscore) {
        this.avgscore = avgscore;
    }

    public Double getMaxscore() {
        return maxscore;
    }

    public void setMaxscore(Double maxscore) {
        this.maxscore = maxscore;
    }

    public Double getMinscore() {
        return minscore;
    }

    public void setMinscore(Double minscore) {
        this.minscore = minscore;
    }

    public Double getScoredifference() {
        return scoredifference;
    }

    public void setScoredifference(Double scoredifference) {
        this.scoredifference = scoredifference;
    }

    public Double getTotalcomparelast() {
        return totalcomparelast;
    }

    public void setTotalcomparelast(Double totalcomparelast) {
        this.totalcomparelast = totalcomparelast;
    }

    public Double getTotalcomparemark() {
        return totalcomparemark;
    }

    public void setTotalcomparemark(Double totalcomparemark) {
        this.totalcomparemark = totalcomparemark;
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

    public Double getMaxcomparelast() {
        return maxcomparelast;
    }

    public void setMaxcomparelast(Double maxcomparelast) {
        this.maxcomparelast = maxcomparelast;
    }

    public Double getMaxcomparemark() {
        return maxcomparemark;
    }

    public void setMaxcomparemark(Double maxcomparemark) {
        this.maxcomparemark = maxcomparemark;
    }

    public Double getMincomparelast() {
        return mincomparelast;
    }

    public void setMincomparelast(Double mincomparelast) {
        this.mincomparelast = mincomparelast;
    }

    public Double getMincomparemark() {
        return mincomparemark;
    }

    public void setMincomparemark(Double mincomparemark) {
        this.mincomparemark = mincomparemark;
    }

    public Double getPlan() {
        return plan;
    }

    public void setPlan(Double plan) {
        this.plan = plan;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public Double getAscore() {
        return ascore;
    }

    public void setAscore(Double ascore) {
        this.ascore = ascore;
    }

    public Double getBscore() {
        return bscore;
    }

    public void setBscore(Double bscore) {
        this.bscore = bscore;
    }

    public Double getCscore() {
        return cscore;
    }

    public void setCscore(Double cscore) {
        this.cscore = cscore;
    }

    public Double getDscore() {
        return dscore;
    }

    public void setDscore(Double dscore) {
        this.dscore = dscore;
    }

    public Double getEscore() {
        return escore;
    }

    public void setEscore(Double escore) {
        this.escore = escore;
    }

    public Double getFscore() {
        return fscore;
    }

    public void setFscore(Double fscore) {
        this.fscore = fscore;
    }

    public Double getSumMbAvgScore() {
        return sumMbAvgScore;
    }

    public void setSumMbAvgScore(Double sumMbAvgScore) {
        this.sumMbAvgScore = sumMbAvgScore;
    }

    String postType;
    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    private String branchcode;

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode == null ? null : branchcode.trim();
    }

    private String branchname;

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }
}
