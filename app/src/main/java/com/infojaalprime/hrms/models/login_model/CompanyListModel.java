package com.infojaalprime.hrms.models.login_model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyListModel {

@SerializedName("status")
@Expose
private String status;
@SerializedName("Message")
@Expose
private String message;
@SerializedName("Data")
@Expose
private List<Datumm> data = null;

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

public List<Datumm> getData() {
return data;
}

public void setData(List<Datumm> data) {
this.data = data;
}

}