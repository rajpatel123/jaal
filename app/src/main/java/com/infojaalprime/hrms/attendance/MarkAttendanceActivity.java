package com.infojaalprime.hrms.attendance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.attendance_model.attendance_required_model.AttendanceRequiredModel;
import com.infojaalprime.hrms.attendance_model.mark_attendance_model.MarkAttendanceModel;
import com.infojaalprime.hrms.interfaces.ApiFetcher;
import com.infojaalprime.hrms.logger.Logger;
import com.infojaalprime.hrms.manager.ApiManager;
import com.infojaalprime.hrms.manager.SessionManager;
import com.infojaalprime.hrms.others.ImageCompressMode;
import com.infojaalprime.hrms.others.TrackingDatabase;
import com.infojaalprime.hrms.others.Utils;
import com.infojaalprime.hrms.urls.Ftp;
import com.infojaalprime.hrms.urls.ServiceUrls;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MarkAttendanceActivity extends AppCompatActivity implements ApiFetcher,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, Ftp {

    ArrayList<String> al_attendance_type_id = new ArrayList<>();
    ArrayList<String> al_attendance_type = new ArrayList<>();

    Spinner sp_attendance_type;
    TextView tv_capture_image;
    LinearLayout ll_capture_image;
    Button btn_capture, btn_mark_attendance;
    TextView tv_time, tv_no_file_choosen, tv_date, tv_lat_long, tv_address;
    ImageView iv_refresh;

    double latitude = 0.0, longitude = 0.0;

    float[] results = new float[1];

    long seconds, distance_api;

    String attendance_type_id = "", emp_id = "", latitude_api = "", longitude_api = "",
            isRequiredDistanceCheck = "NO", isRequiredPhotoCheck = "NO", file_path = "", file_name = "", file_extension = "",
            time = "", date = "";

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    ApiManager apiManager;
    ProgressDialog progressDialog;
    SessionManager sessionManager;


    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    Uri cameraImageUri;
    protected static final int RESULT_IMAGE_CLICK = 2;

    LocationRequest mLocationRequest;

//    private static int UPDATE_INTERVAL = 5000; // 10 sec
//    private static int FASTEST_INTERVAL = 5000; // 5 sec
//    private static int DISPLACEMENT = 10; // 10 meters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        Toolbar toolbar_common = findViewById(R.id.tb_common);
        setSupportActionBar(toolbar_common);
        getSupportActionBar().setTitle("Mark Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_common.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sp_attendance_type = findViewById(R.id.sp_attendance_type);
        tv_capture_image = findViewById(R.id.tv_capture_image);
        ll_capture_image = findViewById(R.id.ll_capture_image);
        btn_capture = findViewById(R.id.btn_capture);
        btn_mark_attendance = findViewById(R.id.btn_mark_attendance);
        tv_time = findViewById(R.id.tv_time);
        tv_date = findViewById(R.id.tv_date);
        tv_no_file_choosen = findViewById(R.id.tv_no_file_choosen);
        tv_address = findViewById(R.id.tv_address);
        tv_lat_long = findViewById(R.id.tv_lat_long);
        iv_refresh = findViewById(R.id.iv_refresh);

        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        emp_id = sessionManager.getEmpId();
        seconds = System.currentTimeMillis();

        al_attendance_type_id.add("");
        al_attendance_type_id.add("1");
        al_attendance_type_id.add("2");

        al_attendance_type.add("-- Select Attendance Type --");
        al_attendance_type.add("IN");
        al_attendance_type.add("OUT");

        sp_attendance_type.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_row, R.id.tv_spinner, al_attendance_type));

        time = new SimpleDateFormat("HH:mm:ss").format(new Date()).toString();
        date = new SimpleDateFormat("dd MMM yyyy").format(new Date()).toString();

        tv_time.setText(time);
        tv_date.setText(date);

        String[] key2 = {"fk_empid"};
        String[] value2 = {emp_id};
        apiManager.set_interface_context_post(key2, value2, "URL_ATTENDANCE_REQUIRED", ServiceUrls.URL_ATTENDANCE_REQUIRED);

        if (checkPlayServices()) {
            buildGoogleApiClient();
            turnOnGPS();
            createLocationRequest();
        }

        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                cameraImageUri = getCaptureImageOutputUri();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                startActivityForResult(intent, RESULT_IMAGE_CLICK);
            }
        });

        sp_attendance_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                attendance_type_id = al_attendance_type_id.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_mark_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (latitude_api.equals("") || longitude_api.equals("")) {
//                        Latitude and longitude are empty from backend so no need to get distance
                    } else {
                        Location.distanceBetween(Double.parseDouble(latitude_api), Double.parseDouble(longitude_api), latitude, longitude, results);
                    }

                    if (attendance_type_id.equals("")) {
                        Toast.makeText(MarkAttendanceActivity.this, "Please Select Attendance Type", Toast.LENGTH_SHORT).show();
                    }

//                    else if (tv_address.getText().toString().equals("")) {
//                        Toast.makeText(MarkAttendanceActivity.this, "Please Refresh to get location!!", Toast.LENGTH_SHORT).show();
//                    }

                    else if (!isRequiredDistanceCheck.equals("NO") && (results[0] > distance_api)) {
                        Toast.makeText(MarkAttendanceActivity.this, "You are Out of range!!", Toast.LENGTH_SHORT).show();
                    } else if (!isRequiredPhotoCheck.equals("NO") && file_path.equals("")) {
                        Toast.makeText(MarkAttendanceActivity.this, "Please Capture Image", Toast.LENGTH_SHORT).show();
                    }

//                    else {
//                        postData();
//                    }

                    else if (!isRequiredPhotoCheck.equals("NO")) {
                        new SendFileToServer().execute();
                    } else {
                        String xml = makeInsertSuspectRequestXML();
                        Utils.showLog("xml", xml);
                        String[] key2 = {"Doc"};
                        String[] value2 = {xml};
                        apiManager.set_interface_context_post(key2, value2, "URL_INSERT_APPLY_ATTENDANCE", ServiceUrls.URL_INSERT_APPLY_ATTENDANCE);
                    }
                } catch (Exception e) {
                    Toast.makeText(MarkAttendanceActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    buildGoogleApiClient();
                    turnOnGPS();
                } catch (Exception e) {
                    Toast.makeText(MarkAttendanceActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
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

//            if (apiName.equals("URL_INSERT_ATTENDANCE_TRACKING")) {
//                MarkAttendanceModel markAttendanceModel = gson.fromJson(response, MarkAttendanceModel.class);
//                if (markAttendanceModel.getStatus().toString().equals("Successed")) {
//                    Utils.showLog("success", markAttendanceModel.getMessage());
//                    trackingDatabase.clearTracking();
//
//                    if (!isRequiredPhotoCheck.equals("NO")) {
//                        new SendFileToServer().execute();
//                    } else {
//                        String xml = makeInsertSuspectRequestXML();
//                        Utils.showLog("xml", xml);
//                        String[] key2 = {"Doc"};
//                        String[] value2 = {xml};
//                        apiManager.set_interface_context_post(key2, value2, "URL_INSERT_APPLY_ATTENDANCE", ServiceUrls.URL_INSERT_APPLY_ATTENDANCE);
//                    }
//                } else {
//                    Toast.makeText(this, "" + markAttendanceModel.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            } else

            if (apiName.equals("URL_ATTENDANCE_REQUIRED")) {
                AttendanceRequiredModel attendanceRequiredModel = gson.fromJson(response, AttendanceRequiredModel.class);
                if (attendanceRequiredModel.getStatus().toString().equals("success")) {
                    latitude_api = attendanceRequiredModel.getData().get(0).getLatitude();
                    longitude_api = attendanceRequiredModel.getData().get(0).getLongitude();
                    String distance_api_1 = attendanceRequiredModel.getData().get(0).getDistance();

                    if (distance_api_1.equals("")) {

                    } else {
                        distance_api = Integer.parseInt(distance_api_1);
                    }

                    isRequiredDistanceCheck = attendanceRequiredModel.getData().get(0).getIsRequiredDistanceCheck();
                    isRequiredPhotoCheck = attendanceRequiredModel.getData().get(0).getIsRequiredPhotoCheck();

                    if (isRequiredPhotoCheck.equals("NO")) {
                        tv_capture_image.setVisibility(View.GONE);
                        ll_capture_image.setVisibility(View.GONE);
                    } else {
                        tv_capture_image.setVisibility(View.VISIBLE);
                        ll_capture_image.setVisibility(View.VISIBLE);
                    }

//                    createLocationRequest();
//                    startLocationUpdates();

                } else {
                    Toast.makeText(this, "" + attendanceRequiredModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (apiName.equals("URL_INSERT_APPLY_ATTENDANCE")) {
                MarkAttendanceModel markAttendanceModel = gson.fromJson(response, MarkAttendanceModel.class);
                if (markAttendanceModel.getStatus().toString().equals("Successed")) {
                    Toast.makeText(this, "" + markAttendanceModel.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "" + markAttendanceModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception :"+e.toString(), Toast.LENGTH_SHORT).show();
            Utils.showLog("Exception", e + "");
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
        String date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        String time1 = new SimpleDateFormat("HH:mm:ss").format(new Date()).toString();

        String xml =
                "<NewDataSet>" +
                        "<SAL_ApplyAttendance_Mst>" +
                        "<pk_applyAttenId>0</pk_applyAttenId>" +
                        "<fk_empId>" + emp_id + "</fk_empId>" +
                        "<dated>" + date1 + "T" + time1 + "+" + "05:30" + "</dated>" +
                        "<attentime>" + date1 + "T" + time1 + "+" + "05:30" + "</attentime>" +
                        "<attenType>" + attendance_type_id + "</attenType>" +
                        "<marktime>" + time1 + "</marktime>" +
                        "<remarks>" + "" + "</remarks>" +
                        "<latitude>" + latitude + "" + "</latitude>" +
                        "<longitude>" + longitude + "" + "</longitude>" +
                        "<filePath>" + file_name + "</filePath>" +
                        "<locAddress>" + tv_address.getText().toString() + "</locAddress>" +
                        "</SAL_ApplyAttendance_Mst>" +
                        "</NewDataSet>";
        return xml;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Utils.showLog("Connection failed", connectionResult.getErrorCode() + "");
    }

    @Override
    public void onLocationChanged(Location location) {
        Logger.e(" onLocationChanged chal gya        ");

        try {
            if (latitude == 0.0 && longitude == 0.0) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Utils.showLog("lat long", latitude + ", " + longitude + "");
                tv_lat_long.setText(latitude + "  |  " + longitude);

                new GetLocationAsyncFromMap().execute();

//                if (isRequiredDistanceCheck.equals("NO")) {
//
//                } else {
//                    Location.distanceBetween(Double.parseDouble(latitude_api), Double.parseDouble(longitude_api), latitude, longitude, results);
//                }
            } else {
                Logger.e("lat long have value");
                stopLocationUpdates();
                displayLocation();
            }
        } catch (Exception e) {
            Logger.e("Exception         " + e.toString());
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(10);
    }

    public void turnOnGPS() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
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
                        mGoogleApiClient.connect();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Logger.e("RESOLUTION_REQUIRED       " + "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            status.startResolutionForResult(MarkAttendanceActivity.this, REQUEST_CHECK_SETTINGS);
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

    private void displayLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Utils.showLog("mLastLocation", mLastLocation + "");
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                Utils.showLog("lat long", latitude + ", " + longitude + "");
                tv_lat_long.setText(latitude + "  |  " + longitude);

                new GetLocationAsyncFromMap().execute();

//                if (isRequiredDistanceCheck.equals("NO")) {
//
//                } else {
//                    Location.distanceBetween(Double.parseDouble(latitude_api), Double.parseDouble(longitude_api), latitude, longitude, results);
//                }
            } else {
                Logger.e("Could not get the location. Make sure location is enabled on the device");
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private Uri getCaptureImageOutputUri() {
        File imageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + "IMG_" + seconds + ".jpg");
        Uri outputFileUri = Uri.fromFile(imageFile);
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Logger.e("RESULT_OK       " + "User agreed to make required location settings changes.");
                        mGoogleApiClient.connect();
                        break;
                    case Activity.RESULT_CANCELED:
                        Logger.e("RESULT_CANCELED       " + "User choose not to make required location settings changes.");
                        finish();
                        break;
                }
                break;

            case RESULT_IMAGE_CLICK:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        file_path = new ImageCompressMode(this).compressImage(cameraImageUri);

                        int pos = file_path.lastIndexOf(".");
                        if (pos > 0) {
                            file_extension = file_path.substring(pos + 1);
                        }

                        String date2 = new SimpleDateFormat("dd-MM-yyyy").format(new Date()).toString();
                        file_name = emp_id + "_" + date2 + "_" + seconds + "." + file_extension;

                        Logger.e("file_path         " + file_path);
                        Logger.e("file_extension           " + file_extension);
                        Logger.e("file_name         " + file_name);

                        tv_no_file_choosen.setText(file_name);
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    public class SendFileToServer extends AsyncTask<String, Void, String> {

        String result = "";

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            FTPClient ftp = new FTPClient();
            try {
                ftp.connect(FTP_HOST);
                ftp.login(FTP_USER, FTP_PASS);
                ftp.enterLocalPassiveMode();
                ftp.setFileType(2);
//                ftp.cwd("Desp");
                int reply = ftp.getReplyCode();
                if (FTPReply.isPositiveCompletion(reply)) {
                    boolean flag = ftp.storeFile(file_name, new FileInputStream(new File(file_path)));
                    if (flag == true) {
                        result = "Upload Successfully";
                    } else if (flag == false) {
                        result = "Upload Not Successfully";
                    }
                } else if (FTPReply.isNegativePermanent(reply)) {
                    result = "FTP Connection Error";
                }
                ftp.logout();
                ftp.disconnect();
            } catch (SocketException e) {
                Logger.e("SocketException        " + e.toString());
            } catch (UnknownHostException e) {
                Logger.e("UnknownHostException        " + e.toString());
            } catch (IOException e) {
                Logger.e("IOException        " + e.toString());
            } catch (Exception e) {
                Logger.e("Exception        " + e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            super.onPostExecute(result1);
            progressDialog.dismiss();

            Logger.e("result            " + result1);

            if (result1.equals("Upload Successfully")) {
                String xml = makeInsertSuspectRequestXML();
                Logger.e("xml      " + xml);
                String[] key2 = {"Doc"};
                String[] value2 = {xml};
                apiManager.set_interface_context_post(key2, value2, "URL_INSERT_APPLY_ATTENDANCE", ServiceUrls.URL_INSERT_APPLY_ATTENDANCE);
            } else {
                Toast.makeText(MarkAttendanceActivity.this, "File Upload Un-Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetLocationAsyncFromMap extends AsyncTask<String, Void, String> {

        StringBuilder str;

        @Override
        protected void onPreExecute() {
            tv_address.setHint("Getting Location ... ");
        }

        @Override
        protected String doInBackground(String... params) {
            str = new StringBuilder();
            try {
                Geocoder geocoder = new Geocoder(MarkAttendanceActivity.this, Locale.ENGLISH);
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);
                    String addressLine = returnAddress.getAddressLine(0);
                    String addressLine1 = returnAddress.getAddressLine(1);

                    Logger.e("addressLine     " + addressLine);
                    Logger.e("addressLine1     " + addressLine1);

                    if (addressLine1 == null) {
                        str.append(addressLine);
                    } else {
                        str.append(addressLine + ", " + addressLine1);
                    }

//                    String addressLine2 = returnAddress.getAddressLine(2);
//                    String city = returnAddress.getLocality();

                } else {
                }
            } catch (Exception e) {
                Logger.e("Exception     " + e.toString());
            }
            return str.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result.equals("")) {
//                    new GetLocationAsyncFromMap().execute();
                } else {
                    tv_address.setText(result);
                }
            } catch (Exception e) {
                Logger.e("Exception     " + e.toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (mGoogleApiClient.isConnected()) {
                startLocationUpdates();
            } else {
                Logger.e("GoogleApiClient is not connected yet.");
            }
        } catch (Exception e) {
            Logger.e("Exception      " + e.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            stopLocationUpdates();
        } catch (Exception e) {
            Logger.e("Exception      " + e.toString());
        }
    }

//    private void postData() {
//
//        if (trackingDatabase.getTrackingCount() == 0) {
//
//            Logger.e("Not any value in data");
//
//            if (!isRequiredPhotoCheck.equals("NO")) {
//                new SendFileToServer().execute();
//            } else {
//                String xml = makeInsertSuspectRequestXML();
//                Utils.showLog("xml", xml);
//                String[] key2 = {"Doc"};
//                String[] value2 = {xml};
//                apiManager.set_interface_context_post(key2, value2, "URL_INSERT_APPLY_ATTENDANCE", ServiceUrls.URL_INSERT_APPLY_ATTENDANCE);
//            }
//        } else {
//            String xml = makeInsertSuspectRequestXMLTracking();
//            Logger.e("xml      " + xml);
//            String[] key2 = {"Doc"};
//            String[] value2 = {xml};
//            apiManager.set_interface_context_post(key2, value2, "URL_INSERT_ATTENDANCE_TRACKING", ServiceUrls.URL_INSERT_ATTENDANCE_TRACKING);
//        }
//    }

//    public String makeInsertSuspectRequestXMLTracking() {
//        String xml = "<NewDataSet>";
//        for (int i = 0; i < trackingDatabase.getTrackingCount(); i++) {
//            xml = xml + "<SAL_AttendanceTracking_Mst>" +
//                    "<pk_AttenId>" + i + "</pk_AttenId>" +
//                    "<fk_empId>" + sessionManager.getEmpId() + "</fk_empId>" +
//                    "<latitude>" + trackingDatabase.viewTracking().get(i).getLatitude() + "</latitude>" +
//                    "<longitude>" + trackingDatabase.viewTracking().get(i).getLongitude() + "</longitude>" +
//                    "<locationaddress>" + trackingDatabase.viewTracking().get(i).getLocation() + "</locationaddress>" +
//                    "<internetStatus>" + trackingDatabase.viewTracking().get(i).getInternet_status() + "</internetStatus>" +
//                    "<gpsStatus>" + trackingDatabase.viewTracking().get(i).getGps_status() + "</gpsStatus>" +
//                    "<dated>" + trackingDatabase.viewTracking().get(i).getDate() + "T" + trackingDatabase.viewTracking().get(i).getTime() + "+" + "05:30" + "</dated>" +
//                    "</SAL_AttendanceTracking_Mst>";
//        }
//        xml = xml + "</NewDataSet>";
//        return xml;
//    }
}
