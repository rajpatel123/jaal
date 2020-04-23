
package com.infojaalprime.hrms.leave_model.get_half_day_club_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pk_halfdayid")
    @Expose
    private String pkHalfdayid;
    @SerializedName("halfdayclub")
    @Expose
    private String halfdayclub;

    public String getPkHalfdayid() {
        return pkHalfdayid;
    }

    public void setPkHalfdayid(String pkHalfdayid) {
        this.pkHalfdayid = pkHalfdayid;
    }

    public String getHalfdayclub() {
        return halfdayclub;
    }

    public void setHalfdayclub(String halfdayclub) {
        this.halfdayclub = halfdayclub;
    }

}
