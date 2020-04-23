
package com.infojaalprime.hrms.leave_model.view_leave_request_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("CID")
    @Expose
    private String cID;
    @SerializedName("pk_leaveappid")
    @Expose
    private String pkLeaveappid;
    @SerializedName("leavetype")
    @Expose
    private String leavetype;
    @SerializedName("dated")
    @Expose
    private String dated;
    @SerializedName("fromdate")
    @Expose
    private String fromdate;
    @SerializedName("todate")
    @Expose
    private String todate;
    @SerializedName("totdays")
    @Expose
    private String totdays;
    @SerializedName("statusname")
    @Expose
    private String statusname;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusimagename")
    @Expose
    private String statusimagename;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public String getCID() {
        return cID;
    }

    public void setCID(String cID) {
        this.cID = cID;
    }

    public String getPkLeaveappid() {
        return pkLeaveappid;
    }

    public void setPkLeaveappid(String pkLeaveappid) {
        this.pkLeaveappid = pkLeaveappid;
    }

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getTotdays() {
        return totdays;
    }

    public void setTotdays(String totdays) {
        this.totdays = totdays;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusimagename() {
        return statusimagename;
    }

    public void setStatusimagename(String statusimagename) {
        this.statusimagename = statusimagename;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
