package com.welb.organization_check.entity;

public class ScoreYdyf {

    private Integer id;

    private String moneyCard;

    private String year;

    private String month;

    private Double score;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMoneyCard() {
        return moneyCard;
    }

    public void setMoneyCard(String moneyCard) {
        this.moneyCard = moneyCard == null ? null : moneyCard.trim();
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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


}
