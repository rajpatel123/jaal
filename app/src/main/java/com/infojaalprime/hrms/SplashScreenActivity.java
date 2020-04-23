package com.infojaalprime.hrms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
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
import com.infojaalprime.hrms.models.app_version_model.AppVersionModel;
import com.infojaalprime.hrms.interfaces.ApiFetcher;
import com.infojaalprime.hrms.logger.Logger;
import com.infojaalprime.hrms.manager.ApiManager;
import com.infojaalprime.hrms.manager.SessionManager;
import com.infojaalprime.hrms.others.Utils;
import com.infojaalprime.hrms.urls.ServiceUrls;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashScreenActivity extends AppCompatActivity implements ApiFetcher {

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    ApiManager apiManager;

    public static Activity splashScreenActivity;

    CoordinatorLayout cl_splash;

    ArrayList al_permissions_all = new ArrayList();
    ArrayList al_permissions_to_request = new ArrayList();
    ArrayList al_permissions_rejected = new ArrayList();
    final static int ALL_PERMISSIONS_RESULT = 107;

    String unique_id = "", version_name = "", emp_id = "", company_id = "";
    int version_code;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashScreenActivity = this;

        cl_splash = (CoordinatorLayout) findViewById(R.id.cl_splash);

        apiManager = new ApiManager(this);
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        al_permissions_all.add(ACCESS_FINE_LOCATION);
        al_permissions_all.add(WRITE_EXTERNAL_STORAGE);
        al_permissions_all.add(CAMERA);
        al_permissions_all.add(READ_PHONE_STATE);

        emp_id=sessionManager.getEmpId();
        company_id=sessionManager.getCompId();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (Object perm : al_permissions_all) {
                if (!hasPermission((String) perm)) {
                    al_permissions_to_request.add(perm);
                }
            }

            if (al_permissions_to_request.size() > 0) {
                requestPermissions((String[]) al_permissions_to_request.toArray(new String[al_permissions_to_request.size()]), ALL_PERMISSIONS_RESULT);
            } else {
                checkNetworkStatus();
            }
        } else {
            checkNetworkStatus();
        }
    }

    public void checkNetworkStatus() {
        if (Utils.isNetwork(this)) {
//            demoFunction();

            turnOnGPS();

//            sendBroadcast(new Intent(getApplicationContext(), AlarmReceiver.class));
//
//            try {
//                PackageManager manager = this.getPackageManager();
//                PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
//                version_name = info.versionName;
//                version_code = info.versionCode;
//
//                Logger.e("version_name     " + version_name + "\nversion_code          " + version_code);
//            } catch (PackageManager.NameNotFoundException e) {
//                Logger.e("Exception     " + e.toString());
//            }
//
//            String[] key2 = {"fk_companyId", "AppVersion"};
//            String[] value2 = {session.getCompId(), version_code + ""};
//            apiManager.set_interface_context_post(key2, value2, "URL_APP_VERSION", ServiceUrls.URL_APP_VERSION);
        } else {
            Snackbar snackbar = Snackbar
                    .make(cl_splash, "" + getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(Color.RED)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkNetworkStatus();
                        }
                    });
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

//    public void findPermissionsToRequestAgain(ArrayList all_permissions) {
//        for (Object perm : all_permissions) {
//            if (!hasPermission((String) perm)) {
//                al_permissions_to_request.add(perm);
//            }
//        }
//    }

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
//                checkNetworkStatus();

                al_permissions_rejected.clear();
                for (Object perms : al_permissions_to_request) {
                    if (!hasPermission((String) perms)) {
                        al_permissions_rejected.add(perms);
                    }
                }

                Logger.e("al_permissions_rejected           " + al_permissions_rejected);

                if (al_permissions_rejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) al_permissions_rejected.get(0))) {
                            new AlertDialog.Builder(this)
                                    .setMessage("All permissions are mandatory for the app. Please allow access.")
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
                                    .setMessage("You refuse to allow access the permissions. Please allow access by go to settings app.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    } else {
                        checkNetworkStatus();
                    }
                } else {
                    checkNetworkStatus();
                }
        }
    }

    public void demoFunction() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    if (sessionManager.isLoggedIn()) {

//                        try {
//                            sendBroadcast(new Intent(getApplicationContext(), AlarmReceiver.class));
//                        } catch (Exception e) {
//                            methodForException(e.toString());
//                        }

                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    Logger.e("Exception         " + e.toString());
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    @Override
    public void onAPIRunningState(int a, String apiName) {
        if (a == ApiFetcher.KEY_API_IS_RUNNING) {
            if (apiName.equals("URL_APP_VERSION"))
                progressDialog.show();
        }
        if (a == ApiFetcher.KEY_API_IS_STOPPED) {
            if (apiName.equals("URL_APP_VERSION"))
                progressDialog.dismiss();
        }
        if (a == ApiFetcher.KEY_API_IS_STOPPED_WITH_ERROR) {
            if (apiName.equals("URL_APP_VERSION"))
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
            if (apiName.equals("URL_APP_VERSION")) {
                AppVersionModel appVersionModel = gson.fromJson(response, AppVersionModel.class);
                if (appVersionModel.getStatus().equals("Successed")) {
                    demoFunction();
                } else {
//                    Toast.makeText(this, "" + appVersionModel.getMessage(), Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(this)
                            .setMessage(appVersionModel.getMessage())
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            }).show();
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

                        try {
                            PackageManager manager = SplashScreenActivity.this.getPackageManager();
                            PackageInfo info = manager.getPackageInfo(SplashScreenActivity.this.getPackageName(), 0);
                            version_name = info.versionName;
                            version_code = info.versionCode;
                            Logger.e("version_name     " + version_name + "\nversion_code          " + version_code);
                        } catch (PackageManager.NameNotFoundException e) {
                            Logger.e("Exception     " + e.toString());
                        }

                        try {
                            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            if (ActivityCompat.checkSelfPermission(SplashScreenActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            unique_id = telephonyManager.getDeviceId();
                            Logger.e("unique_id     " + unique_id);
                        } catch (Exception e) {
                            Logger.e("Exception     " + e.toString());
                        }

//                        try {
//                            sendBroadcast(new Intent(getApplicationContext(), AlarmReceiver.class));
//                        } catch (Exception e) {
//                            methodForException(e.toString());
//                        }

                        String[] key2 = {"fk_companyId", "AppVersion"};
                        String[] value2 = {company_id, version_code + ""};
                        apiManager.set_interface_context_post(key2, value2, "URL_APP_VERSION", ServiceUrls.URL_APP_VERSION);

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Logger.e("RESOLUTION_REQUIRED       " + "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            status.startResolutionForResult(SplashScreenActivity.this, REQUEST_CHECK_SETTINGS);
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

//                        sendBroadcast(new Intent(getApplicationContext(), AlarmReceiver.class));

                        try {
                            PackageManager manager = this.getPackageManager();
                            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                            version_name = info.versionName;
                            version_code = info.versionCode;

                            Logger.e("version_name     " + version_name + "\nversion_code          " + version_code);
                        } catch (PackageManager.NameNotFoundException e) {
                            Logger.e("Exception     " + e.toString());
                        }
                        String[] key2 = {"fk_companyId", "AppVersion"};
                        String[] value2 = {company_id, version_code + ""};
                        apiManager.set_interface_context_post(key2, value2, "URL_APP_VERSION", ServiceUrls.URL_APP_VERSION);

                        break;
                    case Activity.RESULT_CANCELED:
                        Logger.e("RESULT_CANCELED       " + "User choose not to make required location settings changes.");
                        Toast.makeText(this, "App needs to turn on GPS", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                break;
        }
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
                        "<fk_empId>" + emp_id + "</fk_empId>" +
                        "<dated>" + date1 + "T" + time1 + "+" + "05:30" + "</dated>" +
                        "<devicename>" + android.os.Build.MODEL + "</devicename>" +
                        "<deviceID>" + unique_id + "</deviceID>" +
                        "<OSversion>" + Build.VERSION.RELEASE + "</OSversion>" +
                        "<exception>" + exception + "</exception>" +
                        "<Tag>" + "SplashScreenActivity" + "</Tag>" +
                        "</SAL_Mobile_Error_Mst>" +
                        "</NewDataSet>";
        return xml;
    }
}