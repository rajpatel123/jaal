
package com.infojaalprime.hrms.attendance_model.populate_month_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pk_MonthId")
    @Expose
    private String pkMonthId;
    @SerializedName("descriptiion")
    @Expose
    private String descriptiion;

    public String getPkMonthId() {
        return pkMonthId;
    }

    public void setPkMonthId(String pkMonthId) {
        this.pkMonthId = pkMonthId;
    }

    public String getDescriptiion() {
        return descriptiion;
    }

    public void setDescriptiion(String descriptiion) {
        this.descriptiion = descriptiion;
    }

}
