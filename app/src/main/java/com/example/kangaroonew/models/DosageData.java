package com.example.kangaroonew.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DosageData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("martial_status")
    @Expose
    private String martialStatus;
    @SerializedName("blood_group")
    @Expose
    private String bloodGroup;
    @SerializedName("education_level")
    @Expose
    private String educationLevel;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("attendant_title")
    @Expose
    private String attendantTitle;
    @SerializedName("medicine_usage")
    @Expose
    private String medicineUsage;
    @SerializedName("alergy")
    @Expose
    private String alergy;
    @SerializedName("staff_id")
    @Expose
    private Integer staffId;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(String martialStatus) {
        this.martialStatus = martialStatus;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAttendantTitle() {
        return attendantTitle;
    }

    public void setAttendantTitle(String attendantTitle) {
        this.attendantTitle = attendantTitle;
    }

    public String getMedicineUsage() {
        return medicineUsage;
    }

    public void setMedicineUsage(String medicineUsage) {
        this.medicineUsage = medicineUsage;
    }

    public String getAlergy() {
        return alergy;
    }

    public void setAlergy(String alergy) {
        this.alergy = alergy;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

}