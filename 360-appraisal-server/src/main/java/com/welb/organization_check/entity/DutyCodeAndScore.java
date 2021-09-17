package com.welb.organization_check.entity;

/**
 * @author luoyaozu
 * @title: Test
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/1320:56
 */
public class DutyCodeAndScore {
    private String topicId;
    private String score;
    private String scoretype;
    private String cpsm;
    private String zpsm;


    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoretype() {
        return scoretype;
    }

    public void setScoretype(String scoretype) {
        this.scoretype = scoretype;
    }

    public String getZpsm() {
        return zpsm;
    }

    public void setZpsm(String zpsm) {
        this.zpsm = zpsm;
    }

    public String getCpsm() {
        return cpsm;
    }

    public void setCpsm(String cpsm) {
        this.cpsm = cpsm;
    }
}
