
package com.infojaalprime.hrms.attendance_model.populate_year_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pk_yearID")
    @Expose
    private String pkYearID;
    @SerializedName("description")
    @Expose
    private String description;

    public String getPkYearID() {
        return pkYearID;
    }

    public void setPkYearID(String pkYearID) {
        this.pkYearID = pkYearID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
