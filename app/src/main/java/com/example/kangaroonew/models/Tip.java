package com.example.kangaroonew.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Tip {

    @SerializedName("month")
    @Expose
    private int month;
    @SerializedName("description")
    @Expose
    private String description;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
