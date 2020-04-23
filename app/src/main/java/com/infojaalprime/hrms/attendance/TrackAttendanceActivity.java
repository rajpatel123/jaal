package com.infojaalprime.hrms.attendance;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.attendance_model.get_lat_long_datewise.GetLatLongDatewise;
import com.infojaalprime.hrms.attendance_model.populate_staff_employee_model.PopulateStaffEmployeeModel;
import com.infojaalprime.hrms.interfaces.ApiFetcher;
import com.infojaalprime.hrms.logger.Logger;
import com.infojaalprime.hrms.manager.ApiManager;
import com.infojaalprime.hrms.manager.SessionManager;
import com.infojaalprime.hrms.others.RouteClass;
import com.infojaalprime.hrms.others.Utils;
import com.infojaalprime.hrms.urls.ServiceUrls;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TrackAttendanceActivity extends AppCompatActivity implements OnMapReadyCallback, ApiFetcher {

    TextView tv_date, tv_no_record_found;
    Spinner sp_user;
    MapView mapView;

    ApiManager apiManager;
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    ArrayList<LatLng> mMarkerPoints = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();

    String emp_id = "", staff_emp_id = "", date123 = "";

    ArrayList<String> al_staff_employee_id = new ArrayList<>();
    ArrayList<String> al_staff_employee_code = new ArrayList<>();

    ArrayList<Double> al_latitude = new ArrayList<>();
    ArrayList<Double> al_longitude = new ArrayList<>();

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_attendance);

        Toolbar toolbar_common = findViewById(R.id.tb_common);
        setSupportActionBar(toolbar_common);
        getSupportActionBar().setTitle("Track Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_common.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_date = findViewById(R.id.tv_date);
        tv_no_record_found = findViewById(R.id.tv_no_record_found);
        sp_user = findViewById(R.id.sp_user);

        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        emp_id = sessionManager.getEmpId();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date123 = df.format(Calendar.getInstance().getTime());
        tv_date.setText(date123);

        String[] key2 = {"fk_empid"};
        String[] value2 = {emp_id};
        apiManager.set_interface_context_post(key2, value2, "URL_STAFF_EMPLOYEE", ServiceUrls.URL_STAFF_EMPLOYEE);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(TrackAttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date1 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
                        Date date = null;
                        try {
                            date = originalFormat.parse(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        date123 = targetFormat.format(date);
                        tv_date.setText(date123);

                        String[] key2 = {"fk_empid", "dated"};
                        String[] value2 = {staff_emp_id, date123};
                        apiManager.set_interface_context_post(key2, value2, "URL_GET_LAT_LONG_DATEWISE", ServiceUrls.URL_GET_LAT_LONG_DATEWISE);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        sp_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                staff_emp_id = al_staff_employee_id.get(i);

                String[] key2 = {"fk_empid", "dated"};
                String[] value2 = {staff_emp_id, date123};
                apiManager.set_interface_context_post(key2, value2, "URL_GET_LAT_LONG_DATEWISE", ServiceUrls.URL_GET_LAT_LONG_DATEWISE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        try {
            googleMap.clear();
            MapsInitializer.initialize(this);
            switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext())) {
                case ConnectionResult.SUCCESS:
                    googleMap.getUiSettings().setZoomControlsEnabled(false);
                    googleMap.getUiSettings().setZoomGesturesEnabled(true);
                    googleMap.getUiSettings().setCompassEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    googleMap.getUiSettings().setRotateGesturesEnabled(false);
                    googleMap.getUiSettings().setScrollGesturesEnabled(true);
                    googleMap.getUiSettings().setTiltGesturesEnabled(false);
                    googleMap.setMaxZoomPreference(18.0f);
//                    googleMap.setTrafficEnabled(false);

                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    mMarkerPoints.clear();
                    markers.clear();

                    for (int i = 0; i < al_latitude.size(); i++) {

                        double latitude = al_latitude.get(i);
                        double longitude = al_longitude.get(i);

                        if (latitude == 0.0 && longitude == 0.0) {

                        } else {
                            mMarkerPoints.add(new LatLng(latitude, longitude));

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(latitude, longitude));

                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location));
                            markers.add(googleMap.addMarker(markerOptions));
                        }
                    }

                    Logger.e("markers size          " + markers.size());

                    if (markers.isEmpty()) {
                        mapView.setVisibility(View.GONE);
                        tv_no_record_found.setVisibility(View.VISIBLE);
                    } else {

                        mapView.setVisibility(View.VISIBLE);
                        tv_no_record_found.setVisibility(View.GONE);

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (Marker marker : markers) {
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                        int padding = 130;
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        googleMap.animateCamera(cu);

                        try {
                            for (int i = 0; i < mMarkerPoints.size(); i++) {

                                if (i == mMarkerPoints.size() - 1) {
                                    Logger.e("if chal gya");
                                } else {
                                    RouteClass routeClass = new RouteClass();
                                    routeClass.makeRoute(mMarkerPoints.get(i), mMarkerPoints.get(i + 1), googleMap);
                                }
                            }
                        } catch (Exception e) {
                            Logger.e("Exception         " + e);
                        }
                    }
                    break;
                case ConnectionResult.SERVICE_MISSING:
                    Toast.makeText(getApplicationContext(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                    Toast.makeText(getApplicationContext(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Logger.e("Exception         " + e.toString());
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
            if (apiName.equals("URL_STAFF_EMPLOYEE")) {
                PopulateStaffEmployeeModel attendanceRequiredModel = gson.fromJson(response, PopulateStaffEmployeeModel.class);
                if (attendanceRequiredModel.getStatus().toString().equals("success")) {
                    al_staff_employee_code.clear();
                    al_staff_employee_id.clear();
                    for (int i = 0; i < attendanceRequiredModel.getData().size(); i++) {
                        al_staff_employee_id.add(attendanceRequiredModel.getData().get(i).getPkEmpid());
                        al_staff_employee_code.add(attendanceRequiredModel.getData().get(i).getEmpcode());
                    }
                    sp_user.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_row, R.id.tv_spinner, al_staff_employee_code));
                } else {
                    Toast.makeText(this, "" + attendanceRequiredModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (apiName.equals("URL_GET_LAT_LONG_DATEWISE")) {
                GetLatLongDatewise markAttendanceModel = gson.fromJson(response, GetLatLongDatewise.class);
                if (markAttendanceModel.getStatus().toString().equals("success")) {

                    al_longitude.clear();
                    al_latitude.clear();

                    for (int i = 0; i < markAttendanceModel.getData().size(); i++) {
                        al_latitude.add(Double.parseDouble(markAttendanceModel.getData().get(i).getLatitude()));
                        al_longitude.add(Double.parseDouble(markAttendanceModel.getData().get(i).getLongitude()));
                    }

                    mapView.setVisibility(View.VISIBLE);
                    tv_no_record_found.setVisibility(View.GONE);

                    mapView.getMapAsync(this);
                } else {
                    mapView.setVisibility(View.GONE);
                    tv_no_record_found.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            Logger.e("Exception     " + e);
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
}
