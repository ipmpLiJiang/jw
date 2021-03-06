package com.welb.organization_check.entity;

import java.util.LinkedHashMap;
import java.util.Map;

public class Duty {
    public static Map<String, String> dutytypes = new LinkedHashMap<>();
    public static Map<String, String> dbbks = new LinkedHashMap<>();

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

        dbbks.put("1","组织委员纪检委员");
        dbbks.put("2","宣传委员青年委员");
        dbbks.put("3","党支部书记");
        dbbks.put("4","党总支书记");
    }

    private String dutycode;

    private String stationcode;

    private String stationname;

    private String dutyname;

    private String dutytype;

    private String scoretype;

    private String cpsm;

    private String zpsm;

    private String typename;

    private Integer orderid;

    private String updutycode;

    private String bscore;

    private String ascore;

    private String cscore;

    private String dscore;

    private String fullstationcode;
    //scoredetail的分数  duty表的主键关联的是scoredetail表的DSerialNo
    private String score;

    private String dbtype;

    private String defScore;

    public String getDefScore() {
        return defScore;
    }

    public void setDefScore(String defScore) {
        this.defScore = defScore == null ? "0" : defScore.trim();
    }

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

    public String getDutycode() {
        return dutycode;
    }

    public void setDutycode(String dutycode) {
        this.dutycode = dutycode == null ? null : dutycode.trim();
    }

    public String getStationcode() {
        return stationcode;
    }

    public void setStationcode(String stationcode) {
        this.stationcode = stationcode == null ? null : stationcode.trim();
    }

    public String getDutyname() {
        return dutyname;
    }

    public void setDutyname(String dutyname) {
        this.dutyname = dutyname == null ? null : dutyname.trim();
    }

    public String getDutytype() {
        return dutytype;
    }

    public void setDutytype(String dutytype) {
        this.dutytype = dutytype == null ? null : dutytype.trim();
    }

    public String getScoretype() {
        return scoretype;
    }

    public void setScoretype(String scoretype) {
        this.scoretype = scoretype == null ? null : scoretype.trim();
    }


    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getUpdutycode() {
        return updutycode;
    }

    public void setUpdutycode(String updutycode) {
        this.updutycode = updutycode == null ? null : updutycode.trim();
    }

    public String getBscore() {
        return bscore;
    }

    public void setBscore(String bscore) {
        this.bscore = bscore == null ? null : bscore.trim();
    }

    public String getAscore() {
        return ascore;
    }

    public void setAscore(String ascore) {
        this.ascore = ascore == null ? null : ascore.trim();
    }

    public String getCscore() {
        return cscore;
    }

    public void setCscore(String cscore) {
        this.cscore = cscore == null ? null : cscore.trim();
    }

    public String getDscore() {
        return dscore;
    }

    public void setDscore(String dscore) {
        this.dscore = dscore == null ? null : dscore.trim();
    }

    public static Map<String, String> getDutytypes() {
        return dutytypes;
    }

    public static void setDutytypes(Map<String, String> dutytypes) {
        Duty.dutytypes = dutytypes;
    }

    public String getTypename() {
        if (this.dutytype != null) {
            return dutytypes.get(this.dutytype);
        }
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public static Map<String, String> getDbbks() {
        return dbbks;
    }

    public static void setDbbks(Map<String, String> dbbks) {
        User.dbbks = dbbks;
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
}
