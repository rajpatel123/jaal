
package com.infojaalprime.hrms.leave_model.populate_leave_type_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pk_leaveid")
    @Expose
    private String pkLeaveid;
    @SerializedName("leavetype")
    @Expose
    private String leavetype;

    public String getPkLeaveid() {
        return pkLeaveid;
    }

    public void setPkLeaveid(String pkLeaveid) {
        this.pkLeaveid = pkLeaveid;
    }

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

}
