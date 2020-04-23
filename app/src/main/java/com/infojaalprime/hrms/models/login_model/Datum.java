
package com.infojaalprime.hrms.models.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pk_passwordid")
    @Expose
    private String pkPasswordid;
    @SerializedName("fk_empid")
    @Expose
    private String fkEmpid;
    @SerializedName("empcode")
    @Expose
    private String empcode;
    @SerializedName("empname")
    @Expose
    private String empname;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("fk_locid")
    @Expose
    private String fkLocid;
    @SerializedName("contactno")
    @Expose
    private String contactno;
    @SerializedName("dailyAttAllowEmp")
    @Expose
    private String dailyAttAllowEmp;
    @SerializedName("prepassword")
    @Expose
    private String prepassword;
    @SerializedName("dated")
    @Expose
    private String dated;
    @SerializedName("attempt")
    @Expose
    private String attempt;
    @SerializedName("locked")
    @Expose
    private String locked;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("ChangePWD")
    @Expose
    private String changePWD;
    @SerializedName("compname")
    @Expose
    private String compname;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("LastLoginTime")
    @Expose
    private String lastLoginTime;
    @SerializedName("CurLoginTime")
    @Expose
    private String curLoginTime;
    @SerializedName("dephod")
    @Expose
    private String dephod;
    @SerializedName("MPAppAuth")
    @Expose
    private String mPAppAuth;
    @SerializedName("locname")
    @Expose
    private String locname;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("desig")
    @Expose
    private String desig;
    @SerializedName("fk_companyId")
    @Expose
    private String fkCompanyId;
    @SerializedName("Emp_Id")
    @Expose
    private String empId;
    @SerializedName("PType")
    @Expose
    private String pType;
    @SerializedName("AttendanceSource")
    @Expose
    private String attendanceSource;
    @SerializedName("MobileDeviceID")
    @Expose
    private String mobileDeviceID;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("ReqLocation")
    @Expose
    private String reqLocation;
    @SerializedName("Intime")
    @Expose
    private String intime;
    @SerializedName("Outtime")
    @Expose
    private String outtime;
    @SerializedName("MenuText")
    @Expose
    private String menuText;
    @SerializedName("MarkAttendance")
    @Expose
    private String markAttendance;
    @SerializedName("AppVersion")
    @Expose
    private String appVersion;
    @SerializedName("MobileDeviceIDVerify")
    @Expose
    private String mobileDeviceIDVerify;

    public String getPkPasswordid() {
        return pkPasswordid;
    }

    public void setPkPasswordid(String pkPasswordid) {
        this.pkPasswordid = pkPasswordid;
    }

    public String getFkEmpid() {
        return fkEmpid;
    }

    public void setFkEmpid(String fkEmpid) {
        this.fkEmpid = fkEmpid;
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFkLocid() {
        return fkLocid;
    }

    public void setFkLocid(String fkLocid) {
        this.fkLocid = fkLocid;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getDailyAttAllowEmp() {
        return dailyAttAllowEmp;
    }

    public void setDailyAttAllowEmp(String dailyAttAllowEmp) {
        this.dailyAttAllowEmp = dailyAttAllowEmp;
    }

    public String getPrepassword() {
        return prepassword;
    }

    public void setPrepassword(String prepassword) {
        this.prepassword = prepassword;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getChangePWD() {
        return changePWD;
    }

    public void setChangePWD(String changePWD) {
        this.changePWD = changePWD;
    }

    public String getCompname() {
        return compname;
    }

    public void setCompname(String compname) {
        this.compname = compname;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getCurLoginTime() {
        return curLoginTime;
    }

    public void setCurLoginTime(String curLoginTime) {
        this.curLoginTime = curLoginTime;
    }

    public String getDephod() {
        return dephod;
    }

    public void setDephod(String dephod) {
        this.dephod = dephod;
    }

    public String getMPAppAuth() {
        return mPAppAuth;
    }

    public void setMPAppAuth(String mPAppAuth) {
        this.mPAppAuth = mPAppAuth;
    }

    public String getLocname() {
        return locname;
    }

    public void setLocname(String locname) {
        this.locname = locname;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesig() {
        return desig;
    }

    public void setDesig(String desig) {
        this.desig = desig;
    }

    public String getFkCompanyId() {
        return fkCompanyId;
    }

    public void setFkCompanyId(String fkCompanyId) {
        this.fkCompanyId = fkCompanyId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getPType() {
        return pType;
    }

    public void setPType(String pType) {
        this.pType = pType;
    }

    public String getAttendanceSource() {
        return attendanceSource;
    }

    public void setAttendanceSource(String attendanceSource) {
        this.attendanceSource = attendanceSource;
    }

    public String getMobileDeviceID() {
        return mobileDeviceID;
    }

    public void setMobileDeviceID(String mobileDeviceID) {
        this.mobileDeviceID = mobileDeviceID;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getReqLocation() {
        return reqLocation;
    }

    public void setReqLocation(String reqLocation) {
        this.reqLocation = reqLocation;
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

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public String getMarkAttendance() {
        return markAttendance;
    }

    public void setMarkAttendance(String markAttendance) {
        this.markAttendance = markAttendance;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getMobileDeviceIDVerify() {
        return mobileDeviceIDVerify;
    }

    public void setMobileDeviceIDVerify(String mobileDeviceIDVerify) {
        this.mobileDeviceIDVerify = mobileDeviceIDVerify;
    }

}
