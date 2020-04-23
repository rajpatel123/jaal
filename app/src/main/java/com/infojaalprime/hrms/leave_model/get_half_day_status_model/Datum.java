
package com.infojaalprime.hrms.leave_model.get_half_day_status_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pk_halfdayid")
    @Expose
    private String pkHalfdayid;
    @SerializedName("halfdaystatus")
    @Expose
    private String halfdaystatus;

    public String getPkHalfdayid() {
        return pkHalfdayid;
    }

    public void setPkHalfdayid(String pkHalfdayid) {
        this.pkHalfdayid = pkHalfdayid;
    }

    public String getHalfdaystatus() {
        return halfdaystatus;
    }

    public void setHalfdaystatus(String halfdaystatus) {
        this.halfdaystatus = halfdaystatus;
    }

}
