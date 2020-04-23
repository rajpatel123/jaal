
package com.infojaalprime.hrms.leave_model.get_pending_leave_for_approval_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pk_leaveappid")
    @Expose
    private String pkLeaveappid;
    @SerializedName("pk_empid")
    @Expose
    private String pkEmpid;
    @SerializedName("empcode")
    @Expose
    private String empcode;
    @SerializedName("dated")
    @Expose
    private String dated;
    @SerializedName("shortdesc")
    @Expose
    private String shortdesc;
    @SerializedName("fromdate")
    @Expose
    private String fromdate;
    @SerializedName("todate")
    @Expose
    private String todate;

    @SerializedName("totdays")
    @Expose
    private String totDays;

    @SerializedName("remarks")
    @Expose
    private String remarks;

    public String getPkLeaveappid() {
        return pkLeaveappid;
    }

    public void setPkLeaveappid(String pkLeaveappid) {
        this.pkLeaveappid = pkLeaveappid;
    }

    public String getPkEmpid() {
        return pkEmpid;
    }

    public void setPkEmpid(String pkEmpid) {
        this.pkEmpid = pkEmpid;
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTotDays() {
        return totDays;
    }

    public void setTotDays(String totDays) {
        this.totDays = totDays;
    }
}
