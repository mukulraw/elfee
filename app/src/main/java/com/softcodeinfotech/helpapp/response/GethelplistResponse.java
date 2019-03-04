package com.softcodeinfotech.helpapp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GethelplistResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("Information")
    @Expose
    private List<Information> information = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public GethelplistResponse withStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public GethelplistResponse withMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public List<Information> getInformation() {
        return information;
    }

    public void setInformation(List<Information> information) {
        this.information = information;
    }

    public GethelplistResponse withInformation(List<Information> information) {
        this.information = information;
        return this;
    }


    public class Information {

        @SerializedName("help_title")
        @Expose
        private String helpTitle;
        @SerializedName("help_description")
        @Expose
        private String helpDescription;
        @SerializedName("help_category_id")
        @Expose
        private Integer helpCategoryId;
        @SerializedName("help_category")
        @Expose
        private String helpCategory;
        @SerializedName("timestamp")
        @Expose
        private String timestamp;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("phone")
        @Expose
        private String phone;

        public String getHelpTitle() {
            return helpTitle;
        }

        public void setHelpTitle(String helpTitle) {
            this.helpTitle = helpTitle;
        }

        public String getHelpDescription() {
            return helpDescription;
        }

        public void setHelpDescription(String helpDescription) {
            this.helpDescription = helpDescription;
        }

        public Integer getHelpCategoryId() {
            return helpCategoryId;
        }

        public void setHelpCategoryId(Integer helpCategoryId) {
            this.helpCategoryId = helpCategoryId;
        }

        public String getHelpCategory() {
            return helpCategory;
        }

        public void setHelpCategory(String helpCategory) {
            this.helpCategory = helpCategory;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
