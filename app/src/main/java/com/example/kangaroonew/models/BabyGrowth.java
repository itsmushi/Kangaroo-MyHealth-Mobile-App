package com.example.kangaroonew.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BabyGrowth {

    @SerializedName("stage")
    @Expose
    private String stage;
    @SerializedName("week")
    @Expose
    private int week;

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

}