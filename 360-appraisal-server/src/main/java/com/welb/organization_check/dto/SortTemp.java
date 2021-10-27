package com.welb.organization_check.dto;

public class SortTemp {
    public Integer getNum() {
        return num;
    }

    public Double getScore() {
        return Score;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setScore(Double score) {
        Score = score;
    }



    Integer num;
    Double Score;

    public Integer getEvCode() {
        return evCode;
    }

    public void setEvCode(Integer evCode) {
        this.evCode = evCode;
    }

    public String getRrCode() {
        return rrCode;
    }

    public void setRrCode(String rrCode) {
        this.rrCode = rrCode;
    }

    Integer evCode;
    String rrCode;

}
