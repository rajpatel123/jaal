
package com.infojaalprime.hrms.leave_model.leave_approval_status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pk_statusId")
    @Expose
    private String pkStatusId;
    @SerializedName("Status")
    @Expose
    private String status;

    public String getPkStatusId() {
        return pkStatusId;
    }

    public void setPkStatusId(String pkStatusId) {
        this.pkStatusId = pkStatusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
