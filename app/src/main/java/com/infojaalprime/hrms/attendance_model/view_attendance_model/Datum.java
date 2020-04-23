
package com.infojaalprime.hrms.attendance_model.view_attendance_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("CID")
    @Expose
    private String cID;
    @SerializedName("pk_empid")
    @Expose
    private String pkEmpid;
    @SerializedName("empcode")
    @Expose
    private String empcode;
    @SerializedName("manualempcode")
    @Expose
    private String manualempcode;
    @SerializedName("empname")
    @Expose
    private String empname;
    @SerializedName("locname")
    @Expose
    private String locname;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("pk_inoutid")
    @Expose
    private String pkInoutid;
    @SerializedName("dated")
    @Expose
    private String dated;
    @SerializedName("intime")
    @Expose
    private String intime;
    @SerializedName("outtime")
    @Expose
    private String outtime;
    @SerializedName("daystatus")
    @Expose
    private String daystatus;
    @SerializedName("workhour")
    @Expose
    private String workhour;
    @SerializedName("latecount")
    @Expose
    private String latecount;
    @SerializedName("viewdated")
    @Expose
    private String viewdated;
    @SerializedName("LateComing")
    @Expose
    private String lateComing;
    @SerializedName("Short")
    @Expose
    private String _short;
    @SerializedName("OThour")
    @Expose
    private String oThour;
    @SerializedName("ShiftName")
    @Expose
    private String shiftName;
    @SerializedName("intimedate")
    @Expose
    private String intimedate;
    @SerializedName("outtimedate")
    @Expose
    private String outtimedate;

    public String getCID() {
        return cID;
    }

    public void setCID(String cID) {
        this.cID = cID;
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

    public String getManualempcode() {
        return manualempcode;
    }

    public void setManualempcode(String manualempcode) {
        this.manualempcode = manualempcode;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getLocname() {
        return locname;
    }

    public void setLocname(String locname) {
        this.locname = locname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPkInoutid() {
        return pkInoutid;
    }

    public void setPkInoutid(String pkInoutid) {
        this.pkInoutid = pkInoutid;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getDaystatus() {
        return daystatus;
    }

    public void setDaystatus(String daystatus) {
        this.daystatus = daystatus;
    }

    public String getWorkhour() {
        return workhour;
    }

    public void setWorkhour(String workhour) {
        this.workhour = workhour;
    }

    public String getLatecount() {
        return latecount;
    }

    public void setLatecount(String latecount) {
        this.latecount = latecount;
    }

    public String getViewdated() {
        return viewdated;
    }

    public void setViewdated(String viewdated) {
        this.viewdated = viewdated;
    }

    public String getLateComing() {
        return lateComing;
    }

    public void setLateComing(String lateComing) {
        this.lateComing = lateComing;
    }

    public String getShort() {
        return _short;
    }

    public void setShort(String _short) {
        this._short = _short;
    }

    public String getOThour() {
        return oThour;
    }

    public void setOThour(String oThour) {
        this.oThour = oThour;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getIntimedate() {
        return intimedate;
    }

    public void setIntimedate(String intimedate) {
        this.intimedate = intimedate;
    }

    public String getOuttimedate() {
        return outtimedate;
    }

    public void setOuttimedate(String outtimedate) {
        this.outtimedate = outtimedate;
    }

}
