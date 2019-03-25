package com.softcodeinfotech.helpapp.verifyPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("aadhar")
    @Expose
    private String aadhar;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("kyc_status")
    @Expose
    private String kycStatus;
    @SerializedName("wphone")
    @Expose
    private String wphone;
    @SerializedName("g_name")
    @Expose
    private String gName;
    @SerializedName("gphone")
    @Expose
    private String gphone;
    @SerializedName("profession")
    @Expose
    private String profession;
    @SerializedName("yimage")
    @Expose
    private String yimage;
    @SerializedName("gimage")
    @Expose
    private String gimage;
    @SerializedName("afront")
    @Expose
    private String afront;
    @SerializedName("aback")
    @Expose
    private String aback;
    @SerializedName("eimage")
    @Expose
    private String eimage;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getWphone() {
        return wphone;
    }

    public void setWphone(String wphone) {
        this.wphone = wphone;
    }

    public String getGName() {
        return gName;
    }

    public void setGName(String gName) {
        this.gName = gName;
    }

    public String getGphone() {
        return gphone;
    }

    public void setGphone(String gphone) {
        this.gphone = gphone;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getYimage() {
        return yimage;
    }

    public void setYimage(String yimage) {
        this.yimage = yimage;
    }

    public String getGimage() {
        return gimage;
    }

    public void setGimage(String gimage) {
        this.gimage = gimage;
    }

    public String getAfront() {
        return afront;
    }

    public void setAfront(String afront) {
        this.afront = afront;
    }

    public String getAback() {
        return aback;
    }

    public void setAback(String aback) {
        this.aback = aback;
    }

    public String getEimage() {
        return eimage;
    }

    public void setEimage(String eimage) {
        this.eimage = eimage;
    }
}
