
package com.infojaalprime.hrms.attendance_model.mark_attendance_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarkAttendanceModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
