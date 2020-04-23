
package com.infojaalprime.hrms.attendance_model.populate_staff_employee_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pk_empid")
    @Expose
    private String pkEmpid;
    @SerializedName("empcode")
    @Expose
    private String empcode;

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

}
