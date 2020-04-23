
package com.infojaalprime.hrms.attendance_model.get_lat_long_datewise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pk_AttenId")
    @Expose
    private String pkAttenId;
    @SerializedName("fk_empId")
    @Expose
    private String fkEmpId;
    @SerializedName("dated")
    @Expose
    private String dated;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("locationaddress")
    @Expose
    private String locationaddress;

    public String getPkAttenId() {
        return pkAttenId;
    }

    public void setPkAttenId(String pkAttenId) {
        this.pkAttenId = pkAttenId;
    }

    public String getFkEmpId() {
        return fkEmpId;
    }

    public void setFkEmpId(String fkEmpId) {
        this.fkEmpId = fkEmpId;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
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

    public String getLocationaddress() {
        return locationaddress;
    }

    public void setLocationaddress(String locationaddress) {
        this.locationaddress = locationaddress;
    }

}
