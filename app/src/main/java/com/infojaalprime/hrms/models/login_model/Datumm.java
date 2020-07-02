package com.infojaalprime.hrms.models.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datumm {

@SerializedName("pk_companyId")
@Expose
private String pkCompanyId;
@SerializedName("compcode")
@Expose
private String compcode;
@SerializedName("compname")
@Expose
private String compname;

public String getPkCompanyId() {
return pkCompanyId;
}

public void setPkCompanyId(String pkCompanyId) {
this.pkCompanyId = pkCompanyId;
}

public String getCompcode() {
return compcode;
}

public void setCompcode(String compcode) {
this.compcode = compcode;
}

public String getCompname() {
return compname;
}

public void setCompname(String compname) {
this.compname = compname;
}


    @Override
    public String toString() {
        return compname;
    }
}