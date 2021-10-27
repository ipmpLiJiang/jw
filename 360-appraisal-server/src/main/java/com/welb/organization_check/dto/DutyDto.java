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
public class DutyDto {
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

    private String dutycode;

    private String dutyname;

    private String dutytype;

    private String typename;

    private String scoretype;

    private String scorringname;



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
        this.dutyname = dutyname == null ? null : dutyname.trim();
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

    public String getDutytype() {
        return dutytype;
    }

    public void setDutytype(String dutytype) {
        this.dutytype = dutytype == null ? null : dutytype.trim();
    }

    public static Map<String, String> getDutytypes() {
        return dutytypes;
    }

    public static void setDutytypes(Map<String, String> dutytypes) {
        DutyDto.dutytypes = dutytypes;
    }

    public String getScoretype() {
        return scoretype;
    }

    public void setScoretype(String scoretype) {
        this.scoretype = scoretype == null ? null : scoretype.trim();
    }

    public String getScorringname() {
        return scorringname;
    }

    public void setScorringname(String scorringname) {
        this.scorringname = scorringname == null ? null : scorringname.trim();
    }
}
