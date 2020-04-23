package com.infojaalprime.hrms.attendance_model.attendance_required_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("isRequiredDistanceCheck")
    @Expose
    private String isRequiredDistanceCheck;
    @SerializedName("isRequiredPhotoCheck")
    @Expose
    private String isRequiredPhotoCheck;

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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getIsRequiredDistanceCheck() {
        return isRequiredDistanceCheck;
    }

    public void setIsRequiredDistanceCheck(String isRequiredDistanceCheck) {
        this.isRequiredDistanceCheck = isRequiredDistanceCheck;
    }

    public String getIsRequiredPhotoCheck() {
        return isRequiredPhotoCheck;
    }

    public void setIsRequiredPhotoCheck(String isRequiredPhotoCheck) {
        this.isRequiredPhotoCheck = isRequiredPhotoCheck;
    }

}
