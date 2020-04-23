package com.infojaalprime.hrms;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infojaalprime.hrms.attendance.MarkAttendanceActivity;
import com.infojaalprime.hrms.attendance.TrackAttendanceActivity;
import com.infojaalprime.hrms.attendance.ViewAttendanceActivity;
import com.infojaalprime.hrms.attendance_model.mark_attendance_model.MarkAttendanceModel;
import com.infojaalprime.hrms.interfaces.ApiFetcher;
import com.infojaalprime.hrms.leave.ApplyLeaveActivity;
import com.infojaalprime.hrms.leave.ApproveLeaveActivity;
import com.infojaalprime.hrms.leave.ViewLeaveRequestsActivity;
import com.infojaalprime.hrms.logger.Logger;
import com.infojaalprime.hrms.models.app_version_model.AppVersionModel;
import com.infojaalprime.hrms.models.logout_model.LogoutModel;
import com.infojaalprime.hrms.manager.ApiManager;
import com.infojaalprime.hrms.manager.SessionManager;
import com.infojaalprime.hrms.others.AlarmReceiver;
import com.infojaalprime.hrms.others.TrackingDatabase;
import com.infojaalprime.hrms.others.Utils;
import com.infojaalprime.hrms.urls.ServiceUrls;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements ApiFetcher {

    LinearLayout ll_mark_attendance, ll_view_attendance, ll_track_attendance;

    TextView tv_name;

    ProgressDialog progressDialog;
    ApiManager apiManager;
    SessionManager sessionManager;
    TrackingDatabase trackingDatabase;

    String unique_id = "";

    ArrayList al_permissions_all = new ArrayList();
    ArrayList al_permissions_to_request = new ArrayList();
    ArrayList al_permissions_rejected = new ArrayList();
    final static int ALL_PERMISSIONS_RESULT = 107;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar_common = findViewById(R.id.tb_common);
        setSupportActionBar(toolbar_common);
        getSupportActionBar().setTitle("HRMS");
        toolbar_common.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_mark_attendance = findViewById(R.id.ll_mark_attendance);
        ll_view_attendance = findViewById(R.id.ll_view_attendance);
        ll_track_attendance = findViewById(R.id.ll_track_attendance);
        tv_name = findViewById(R.id.tv_name);

        trackingDatabase = new TrackingDatabase(this);
        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        tv_name.setText(sessionManager.getEmpName() + " | " + sessionManager.getEmpCode());

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

//        try {
//            int a=5/0;
//        } catch (Exception e) {
//            methodForException(e.toString());
//        }

        try {
            sendBroadcast(new Intent(getApplicationContext(), AlarmReceiver.class));
        } catch (Exception e) {
            methodForException(e.toString());
        }

        try {
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (5 * 60 * 1000), pendingIntent);
        } catch (Exception e) {
            Logger.e("Exception     " + e.toString());
            methodForException(e.toString());
        }

        try {
            postData();
        } catch (Exception e) {
            Utils.showLog("Exception", e.toString());
            methodForException(e.toString());
        }

        ll_mark_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionFunction();
            }
        });

        ll_view_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewAttendanceActivity.class));
            }
        });

        ll_track_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TrackAttendanceActivity.class));
            }
        });

        findViewById(R.id.ll_apply_leave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ApplyLeaveActivity.class));
            }
        });

        findViewById(R.id.ll_approve_leave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ApproveLeaveActivity.class));
            }
        });

        findViewById(R.id.ll_leave_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewLeaveRequestsActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {

            logoutDialog();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logoutDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Logout");
        alert.setMessage("Are you sure?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                sessionManager.createLogout();
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                finish();

                postDataLogout();

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    private void postDataLogout() {
        if (trackingDatabase.getTrackingCount() == 0) {
            Utils.showLog("No value in database", "");

            String[] key2 = {"fk_empid", "IMEINo", "OSVersion", "HandSetName"};
            String[] value2 = {sessionManager.getEmpId(), unique_id, "", ""};
            apiManager.set_interface_context_post(key2, value2, "URL_LOGOUT", ServiceUrls.URL_LOGOUT);

//            sessionManager.createLogout();
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            finish();
        } else {
            String xml = makeInsertSuspectRequestXML();
            Logger.e("xml      " + xml);
            String[] key2 = {"Doc"};
            String[] value2 = {xml};
            apiManager.set_interface_context_post(key2, value2, "URL_INSERT_ATTENDANCE_TRACKING_LOGOUT", ServiceUrls.URL_INSERT_ATTENDANCE_TRACKING);
        }
    }

    void permissionFunction() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            al_permissions_all.clear();
            al_permissions_to_request.clear();

            al_permissions_all.add(ACCESS_FINE_LOCATION);
            al_permissions_all.add(READ_EXTERNAL_STORAGE);
            al_permissions_all.add(CAMERA);

            for (Object perm : al_permissions_all) {
                if (!hasPermission((String) perm)) {
                    al_permissions_to_request.add(perm);
                }
            }

            if (al_permissions_to_request.size() > 0) {
                requestPermissions((String[]) al_permissions_to_request.toArray(new String[al_permissions_to_request.size()]), ALL_PERMISSIONS_RESULT);
            } else {
                turnOnGPS();
            }
        } else {
            turnOnGPS();
        }
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            Logger.e("hasPermissions        " + "Version is less than M");
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:

                al_permissions_rejected.clear();
                for (Object perms : al_permissions_to_request) {
                    if (!hasPermission((String) perms)) {
                        al_permissions_rejected.add(perms);
                    }
                }

                if (al_permissions_rejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) al_permissions_rejected.get(0))) {
                            new AlertDialog.Builder(this)
                                    .setMessage("These permissions are mandatory for the application. Please allow access.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) al_permissions_rejected.toArray(new String[al_permissions_rejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {
                            new AlertDialog.Builder(this)
                                    .setMessage("You refuse to allow access the permissions. Please allow access to go to in settings")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                } else {
                    Logger.e("User Agreed to access all permissions");
                    turnOnGPS();
                }
        }
    }

    public void turnOnGPS() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Logger.e("SUCCESS       " + "Location settings are satisfied.");
                        startActivity(new Intent(MainActivity.this, MarkAttendanceActivity.class));
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Logger.e("RESOLUTION_REQUIRED       " + "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Logger.e("Exception         " + "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Logger.e("SETTINGS_CHANGE_UNAVAILABLE       " + "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Logger.e("RESULT_OK       " + "User agreed to make required location settings changes.");
                        startActivity(new Intent(MainActivity.this, MarkAttendanceActivity.class));
                        break;
                    case Activity.RESULT_CANCELED:
                        Logger.e("RESULT_CANCELED       " + "User choose not to make required location settings changes.");
                        Toast.makeText(this, "App needs to turn on GPS", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    private void postData() {

        if (trackingDatabase.getTrackingCount() == 0) {
            Utils.showLog("No value in database", "");
        } else {
            String xml = makeInsertSuspectRequestXML();
            Logger.e("xml      " + xml);
            String[] key2 = {"Doc"};
            String[] value2 = {xml};
            apiManager.set_interface_context_post(key2, value2, "URL_INSERT_ATTENDANCE_TRACKING", ServiceUrls.URL_INSERT_ATTENDANCE_TRACKING);
        }
    }

    @Override
    public void onAPIRunningState(int a, String apiName) {
        if (a == ApiFetcher.KEY_API_IS_RUNNING) {
            if (apiName.equals("URL_INSERT_ATTENDANCE_TRACKING_LOGOUT"))
                progressDialog.show();
            if (apiName.equals("URL_LOGOUT"))
                progressDialog.show();
        } else if (a == ApiFetcher.KEY_API_IS_STOPPED) {
            if (apiName.equals("URL_INSERT_ATTENDANCE_TRACKING_LOGOUT"))
                progressDialog.dismiss();
            if (apiName.equals("URL_LOGOUT"))
                progressDialog.dismiss();
        } else if (a == ApiFetcher.KEY_API_IS_STOPPED_WITH_ERROR) {
            if (apiName.equals("URL_INSERT_ATTENDANCE_TRACKING_LOGOUT"))
                progressDialog.dismiss();
            if (apiName.equals("URL_LOGOUT"))
                progressDialog.dismiss();
        }
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
            if (apiName.equals("URL_INSERT_ATTENDANCE_TRACKING")) {
                MarkAttendanceModel markAttendanceModel = gson.fromJson(response, MarkAttendanceModel.class);
                if (markAttendanceModel.getStatus().toString().equals("Successed")) {
                    Utils.showLog("success", markAttendanceModel.getMessage());
                    trackingDatabase.clearTracking();
                } else {
                    Utils.showLog("Fail", markAttendanceModel.getMessage());
                }
            } else if (apiName.equals("URL_INSERT_ATTENDANCE_TRACKING_LOGOUT")) {
                MarkAttendanceModel markAttendanceModel = gson.fromJson(response, MarkAttendanceModel.class);
                if (markAttendanceModel.getStatus().toString().equals("Successed")) {
                    Utils.showLog("success", markAttendanceModel.getMessage());
                    trackingDatabase.clearTracking();

                    String[] key2 = {"fk_empid", "IMEINo", "OSVersion", "HandSetName"};
                    String[] value2 = {sessionManager.getEmpId(), unique_id, Build.VERSION.RELEASE, android.os.Build.MODEL};
                    apiManager.set_interface_context_post(key2, value2, "URL_LOGOUT", ServiceUrls.URL_LOGOUT);

//                    sessionManager.createLogout();
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    finish();
                } else {
                    Utils.showLog("Fail", markAttendanceModel.getMessage());
                }
            } else if (apiName.equals("URL_LOGOUT")) {
                LogoutModel logoutModel = gson.fromJson(response, LogoutModel.class);
                if (logoutModel.getStatus().equals("success")) {
                    Utils.showLog("success", logoutModel.getMessage());

                    Toast.makeText(this, "Logout Successfully!!", Toast.LENGTH_SHORT).show();

                    sessionManager.createLogout();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, logoutModel.getMessage(), Toast.LENGTH_SHORT).show();
                    Utils.showLog("Fail", logoutModel.getMessage());
                }
            }

            else if (apiName.equals("URL_MOBILE_ERROR")){
                AppVersionModel appVersionModel = gson.fromJson(response, AppVersionModel.class);
                if (appVersionModel.getStatus().equals("Successed")) {
                    Logger.e("msg  "+appVersionModel.getMessage());
                } else {
                    Logger.e("msg  "+appVersionModel.getMessage());
                }
            }
        } catch (Exception e) {
            Utils.showLog("Exception", e.toString());
            methodForException(e.toString());
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

    void methodForException(String exception) {
        String xml = makeXmlForException(exception);
        Utils.showLog("xml", xml);
        String[] key2 = {"Doc","CompanyCode"};
        String[] value2 = {xml,"prime"};
        apiManager.set_interface_context_post(key2, value2, "URL_MOBILE_ERROR", ServiceUrls.URL_MOBILE_ERROR);
    }

    public String makeXmlForException(String exception) {
        String date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        String time1 = new SimpleDateFormat("HH:mm:ss").format(new Date()).toString();
        String xml =
                "<NewDataSet>" +
                        "<SAL_Mobile_Error_Mst>" +
                        "<pk_mobileerrorId>" + 0 + "</pk_mobileerrorId>" +
                        "<fk_empId>" + sessionManager.getEmpId() + "</fk_empId>" +
                        "<dated>" + date1 + "T" + time1 + "+" + "05:30" + "</dated>" +
                        "<devicename>" + android.os.Build.MODEL + "</devicename>" +
                        "<deviceID>" + unique_id + "</deviceID>" +
                        "<OSversion>" + Build.VERSION.RELEASE + "</OSversion>" +
                        "<exception>" + exception + "</exception>" +
                        "<Tag>" + "MainActivity" + "</Tag>" +
                        "</SAL_Mobile_Error_Mst>" +
                        "</NewDataSet>";
        return xml;
    }
}