package com.example.kangaroonew.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PregnancyDate {

    @SerializedName("pregnancy_date")
    @Expose
    private String pregnancyDate;

    public String getPregnancyDate() {
        return pregnancyDate;
    }

    public void setPregnancyDate(String pregnancyDate) {
        this.pregnancyDate = pregnancyDate;
    }

}