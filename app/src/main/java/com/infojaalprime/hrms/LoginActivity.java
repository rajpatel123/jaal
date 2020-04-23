package com.infojaalprime.hrms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infojaalprime.hrms.attendance_model.mark_attendance_model.MarkAttendanceModel;
import com.infojaalprime.hrms.interfaces.ApiFetcher;
import com.infojaalprime.hrms.logger.Logger;
import com.infojaalprime.hrms.models.login_model.LoginModel;
import com.infojaalprime.hrms.manager.ApiManager;
import com.infojaalprime.hrms.manager.SessionManager;
import com.infojaalprime.hrms.others.TrackingDatabase;
import com.infojaalprime.hrms.others.Utils;
import com.infojaalprime.hrms.urls.ServiceUrls;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ApiFetcher {

    EditText edtCode, edtPassword;
    Button btnLogin;
    ImageView image_hide, image_show;
    LinearLayout ll_password;
    TextView txtTitle;

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    ApiManager apiManager;
    LoginModel attendanceRequiredModel;

    String unique_id = "", version_name = "";
    int version_code;

    TrackingDatabase trackingDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        trackingDatabase = new TrackingDatabase(this);

        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        edtCode = findViewById(R.id.edtCode);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        ll_password = findViewById(R.id.ll_pass);
        txtTitle = findViewById(R.id.txtTitle);
        image_hide = findViewById(R.id.image_hide);
        image_show = findViewById(R.id.image_show);

        btnLogin.setOnClickListener(this);
        image_hide.setOnClickListener(this);
        image_show.setOnClickListener(this);

        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            unique_id = telephonyManager.getDeviceId();

            Logger.e("unique_id     " + unique_id);
        } catch (Exception e) {
            Logger.e("Exception     " + e.toString());
        }

        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version_name = info.versionName;
            version_code = info.versionCode;

            Logger.e("version_name     " + version_name + "\nversion_code          " + version_code);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("Exception     " + e.toString());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (edtCode.getText().toString().equals("")) {
                    Utils.showToastMessage(this, "Please Enter Employee Code");
                } else if (edtPassword.getText().toString().equals("")) {
                    Utils.showToastMessage(this, "Please Enter Password");
                } else {
                    String[] key2 = {"UserCode", "Password", "CompanyCode", "IMEINo", "OSVersion", "HandSetName", "AppVersion"};
                    String[] value2 = {edtCode.getText().toString(), edtPassword.getText().toString(), "gobolt", unique_id, Build.VERSION.RELEASE, android.os.Build.MODEL, version_code + ""};
                    apiManager.set_interface_context_post(key2, value2, "URL_LOGIN", ServiceUrls.URL_LOGIN);
                }
                break;
            case R.id.image_hide:
                String password = edtPassword.getText().toString();
                if (password.equals("")) {
                    Utils.showToastMessage(this, "Please Enter Password First");
                } else {
                    edtPassword.setTransformationMethod(new HideReturnsTransformationMethod());
                    edtPassword.setSelection(edtPassword.length());
                    image_show.setVisibility(View.VISIBLE);
                    image_hide.setVisibility(View.GONE);
                }
                break;
            case R.id.image_show:
                edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                edtPassword.setSelection(edtPassword.length());
                image_show.setVisibility(View.GONE);
                image_hide.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onAPIRunningState(int a, String apiName) {
        if (a == ApiFetcher.KEY_API_IS_RUNNING)
            progressDialog.show();
        if (a == ApiFetcher.KEY_API_IS_STOPPED)
            progressDialog.dismiss();
        if (a == ApiFetcher.KEY_API_IS_STOPPED_WITH_ERROR)
            progressDialog.dismiss();
    }

    @Override
    public void onFetchProgress(int progress) {

    }

    @Override
    public void onFetchComplete(String response, String apiName) {
        try {
            response = response.substring(76, response.length() - 9);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            if (apiName.equals("URL_LOGIN")) {
                 attendanceRequiredModel = gson.fromJson(response, LoginModel.class);
                if (attendanceRequiredModel.getStatus().toString().equals("success")) {

                    sessionManager.createLogin();
                    sessionManager.saveLogin(SessionManager.empId, attendanceRequiredModel.getData().get(0).getFkEmpid());
                    sessionManager.saveLogin(SessionManager.empId1, attendanceRequiredModel.getData().get(0).getEmpId());
                    sessionManager.saveLogin(SessionManager.empCode, attendanceRequiredModel.getData().get(0).getEmpcode());
                    sessionManager.saveLogin(SessionManager.empName, attendanceRequiredModel.getData().get(0).getEmpname());
                    sessionManager.saveLogin(SessionManager.compName, attendanceRequiredModel.getData().get(0).getCompname());
                    sessionManager.saveLogin(SessionManager.address, attendanceRequiredModel.getData().get(0).getAddress1());
                    sessionManager.saveLogin(SessionManager.locName, attendanceRequiredModel.getData().get(0).getLocname());
                    sessionManager.saveLogin(SessionManager.grade, attendanceRequiredModel.getData().get(0).getGrade());
                    sessionManager.saveLogin(SessionManager.department, attendanceRequiredModel.getData().get(0).getDepartment());
                    sessionManager.saveLogin(SessionManager.designation, attendanceRequiredModel.getData().get(0).getDesig());
                    sessionManager.saveLogin(SessionManager.compId, attendanceRequiredModel.getData().get(0).getFkCompanyId());
                    sessionManager.saveLogin(SessionManager.isLocReq, attendanceRequiredModel.getData().get(0).getReqLocation());
                    sessionManager.saveLogin(SessionManager.cLat, attendanceRequiredModel.getData().get(0).getLat());
                    sessionManager.saveLogin(SessionManager.cLong, attendanceRequiredModel.getData().get(0).getLong());
                    sessionManager.saveLogin(SessionManager.mobileDevice, attendanceRequiredModel.getData().get(0).getMobileDeviceID());
                    sessionManager.saveLogin(SessionManager.attendanceSource, attendanceRequiredModel.getData().get(0).getAttendanceSource());
                    sessionManager.saveLogin(SessionManager.menuText, attendanceRequiredModel.getData().get(0).getMenuText());

//                    postData();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "" + attendanceRequiredModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (apiName.equals("URL_INSERT_ATTENDANCE_TRACKING")) {
                MarkAttendanceModel markAttendanceModel = gson.fromJson(response, MarkAttendanceModel.class);
                if (markAttendanceModel.getStatus().toString().equals("Successed")) {
                    Utils.showLog("success", markAttendanceModel.getMessage());
                    trackingDatabase.clearTracking();

//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
                } else {
//                    sessionManager.createLogout();
                    Toast.makeText(this, "" + markAttendanceModel.getMessage(), Toast.LENGTH_SHORT).show();
                    Utils.showLog("Fail", markAttendanceModel.getMessage());
                }
            }
        } catch (Exception e) {
            Utils.showLog("Exception", e.toString());
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFetchFailed(ANError error, String apiName) {
        if (error.getErrorDetail().equals("connectionError")) {
            Utils.showToastMessage(this, getString(R.string.no_internet_connection));
        } else if (error.getErrorDetail().equals("responseFromServerError")) {
            Utils.showToastMessage(this, getString(R.string.server_error));
        }
    }

    private void postData() {

        if (trackingDatabase.getTrackingCount() == 0) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            String xml = makeInsertSuspectRequestXML();
            Logger.e("xml      " + xml);
            String[] key2 = {"Doc"};
            String[] value2 = {xml};
            apiManager.set_interface_context_post(key2, value2, "URL_INSERT_ATTENDANCE_TRACKING", ServiceUrls.URL_INSERT_ATTENDANCE_TRACKING);
        }
    }

    public String makeInsertSuspectRequestXML() {
        String xml = "<NewDataSet>";
        for (int i = 0; i < trackingDatabase.getTrackingCount(); i++) {
            xml = xml + "<SAL_AttendanceTracking_Mst>" +
                    "<pk_AttenId>" + i + "</pk_AttenId>" +
                    "<fk_empId>" + sessionManager.getEmpId() + "</fk_empId>" +
                    "<latitude>" + trackingDatabase.viewTracking().get(i).getLatitude() + "</latitude>" +
                    "<longitude>" + trackingDatabase.viewTracking().get(i).getLongitude() + "</longitude>" +
                    "<locationaddress>" + trackingDatabase.viewTracking().get(i).getLocation() + "</locationaddress>" +
                    "<internetStatus>" + trackingDatabase.viewTracking().get(i).getInternet_status() + "</internetStatus>" +
                    "<gpsStatus>" + trackingDatabase.viewTracking().get(i).getGps_status() + "</gpsStatus>" +
                    "<dated>" + trackingDatabase.viewTracking().get(i).getDate() + "T" + trackingDatabase.viewTracking().get(i).getTime() + "+" + "05:30" + "</dated>" +
                    "</SAL_AttendanceTracking_Mst>";
        }
        xml = xml + "</NewDataSet>";
        return xml;
    }
}