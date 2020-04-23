
package com.infojaalprime.hrms.leave_model.calculate_days_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("CID")
    @Expose
    private String cID;
    @SerializedName("dates")
    @Expose
    private String dates;
    @SerializedName("weekoff")
    @Expose
    private String weekoff;
    @SerializedName("nholiday")
    @Expose
    private String nholiday;
    @SerializedName("lwp")
    @Expose
    private String lwp;
    @SerializedName("leaveid")
    @Expose
    private String leaveid;
    @SerializedName("club_nh")
    @Expose
    private String clubNh;
    @SerializedName("cover_nh")
    @Expose
    private String coverNh;
    @SerializedName("club_woff")
    @Expose
    private String clubWoff;
    @SerializedName("cover_woff")
    @Expose
    private String coverWoff;
    @SerializedName("ishalfday")
    @Expose
    private String ishalfday;
    @SerializedName("isenabled")
    @Expose
    private String isenabled;
    @SerializedName("ischecked")
    @Expose
    private String ischecked;
    @SerializedName("halpdaystatus")
    @Expose
    private String halpdaystatus;
    @SerializedName("LeaveType")
    @Expose
    private String leaveType;

    @SerializedName("totDays")
    @Expose
    private String total_days;

    public String getCID() {
        return cID;
    }

    public void setCID(String cID) {
        this.cID = cID;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getWeekoff() {
        return weekoff;
    }

    public void setWeekoff(String weekoff) {
        this.weekoff = weekoff;
    }

    public String getNholiday() {
        return nholiday;
    }

    public void setNholiday(String nholiday) {
        this.nholiday = nholiday;
    }

    public String getLwp() {
        return lwp;
    }

    public void setLwp(String lwp) {
        this.lwp = lwp;
    }

    public String getLeaveid() {
        return leaveid;
    }

    public void setLeaveid(String leaveid) {
        this.leaveid = leaveid;
    }

    public String getClubNh() {
        return clubNh;
    }

    public void setClubNh(String clubNh) {
        this.clubNh = clubNh;
    }

    public String getCoverNh() {
        return coverNh;
    }

    public void setCoverNh(String coverNh) {
        this.coverNh = coverNh;
    }

    public String getClubWoff() {
        return clubWoff;
    }

    public void setClubWoff(String clubWoff) {
        this.clubWoff = clubWoff;
    }

    public String getCoverWoff() {
        return coverWoff;
    }

    public void setCoverWoff(String coverWoff) {
        this.coverWoff = coverWoff;
    }

    public String getIshalfday() {
        return ishalfday;
    }

    public void setIshalfday(String ishalfday) {
        this.ishalfday = ishalfday;
    }

    public String getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(String isenabled) {
        this.isenabled = isenabled;
    }

    public String getIschecked() {
        return ischecked;
    }

    public void setIschecked(String ischecked) {
        this.ischecked = ischecked;
    }

    public String getHalpdaystatus() {
        return halpdaystatus;
    }

    public void setHalpdaystatus(String halpdaystatus) {
        this.halpdaystatus = halpdaystatus;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getTotal_days() {
        return total_days;
    }

    public void setTotal_days(String total_days) {
        this.total_days = total_days;
    }
}
