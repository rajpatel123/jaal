package com.infojaalprime.hrms.attendance;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.attendance_adapter.ViewAttendanceAdapter;
import com.infojaalprime.hrms.attendance_model.populate_month_model.PopulateMonthModel;
import com.infojaalprime.hrms.attendance_model.populate_year_model.PopulateYearModel;
import com.infojaalprime.hrms.attendance_model.view_attendance_model.ViewAttendanceModel;
import com.infojaalprime.hrms.interfaces.ApiFetcher;
import com.infojaalprime.hrms.logger.Logger;
import com.infojaalprime.hrms.manager.ApiManager;
import com.infojaalprime.hrms.manager.SessionManager;
import com.infojaalprime.hrms.others.Utils;
import com.infojaalprime.hrms.urls.ServiceUrls;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ViewAttendanceActivity extends AppCompatActivity implements ApiFetcher {

    Spinner sp_month, sp_year;
    ListView lv_view_attendance;

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    ApiManager apiManager;

    String current_month = "", current_year = "", month_id = "", year_id = "", company_id = "", emp_id = "";

    ArrayList<String> al_month = new ArrayList<>();
    ArrayList<String> al_month_id = new ArrayList<>();
    ArrayList<String> al_year = new ArrayList<>();
    ArrayList<String> al_year_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        Toolbar toolbar_common = findViewById(R.id.tb_common);
        setSupportActionBar(toolbar_common);
        getSupportActionBar().setTitle("View Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_common.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sp_month = findViewById(R.id.sp_month);
        sp_year = findViewById(R.id.sp_year);
        lv_view_attendance = findViewById(R.id.lv_view_attendance);
        lv_view_attendance.setDivider(null);

        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        try {
            current_year = Calendar.getInstance().get(Calendar.YEAR) + "";
            SimpleDateFormat f = new SimpleDateFormat("MMMM");
            current_month = f.format(new Date());
            Logger.e(current_month + "   |    " + current_year);
        } catch (Exception e) {
            Logger.e("Exception         " + e.toString());
        }

        company_id = sessionManager.getCompId();
        emp_id = sessionManager.getEmpId();

        String[] key2 = {"fk_companyId"};
        String[] value2 = {company_id};
        apiManager.set_interface_context_post(key2, value2, "URL_POPULATE_MONTH", ServiceUrls.URL_POPULATE_MONTH);

        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month_id = al_month_id.get(i);

                if (month_id.equals("") || year_id.equals("")) {

                } else {
                    String[] key = {"fk_empid", "fk_monthId", "fk_yearId"};
                    String[] value = {emp_id, month_id, year_id};
                    apiManager.set_interface_context_post(key, value, "URL_VIEW_ATTENDANCE", ServiceUrls.URL_VIEW_ATTENDANCE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year_id = al_year_id.get(i);

                if (month_id.equals("") || year_id.equals("")) {

                } else {
                    String[] key = {"fk_empid", "fk_monthId", "fk_yearId"};
                    String[] value = {emp_id, month_id, year_id};
                    apiManager.set_interface_context_post(key, value, "URL_VIEW_ATTENDANCE", ServiceUrls.URL_VIEW_ATTENDANCE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
            if (apiName.equals("URL_POPULATE_MONTH")) {
                PopulateMonthModel attendanceRequiredModel = gson.fromJson(response, PopulateMonthModel.class);
                if (attendanceRequiredModel.getStatus().toString().equals("success")) {
                    al_month_id.clear();
                    al_month.clear();
                    for (int i = 0; i < attendanceRequiredModel.getData().size(); i++) {
                        al_month.add(attendanceRequiredModel.getData().get(i).getDescriptiion());
                        al_month_id.add(attendanceRequiredModel.getData().get(i).getPkMonthId());
                    }
                    sp_month.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_row, R.id.tv_spinner, al_month));

                    for (int j = 0; j < al_month.size(); j++) {
                        if (al_month.get(j).equals(current_month.toUpperCase()))
                            sp_month.setSelection(j);
                    }

                    String[] key = {"fk_companyId"};
                    String[] value = {company_id};
                    apiManager.set_interface_context_post(key, value, "URL_POPULATE_YEAR", ServiceUrls.URL_POPULATE_YEAR);

                } else {
                    Toast.makeText(this, "" + attendanceRequiredModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (apiName.equals("URL_POPULATE_YEAR")) {
                PopulateYearModel attendanceRequiredModel = gson.fromJson(response, PopulateYearModel.class);
                if (attendanceRequiredModel.getStatus().toString().equals("success")) {
                    al_year.clear();
                    al_year_id.clear();
                    for (int i = 0; i < attendanceRequiredModel.getData().size(); i++) {
                        al_year_id.add(attendanceRequiredModel.getData().get(i).getPkYearID());
                        al_year.add(attendanceRequiredModel.getData().get(i).getDescription());
                    }
                    sp_year.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_row, R.id.tv_spinner, al_year));

                    for (int j = 0; j < al_year.size(); j++) {
                        if (al_year.get(j).equals(current_year))
                            sp_year.setSelection(j);
                    }
                } else {
                    Toast.makeText(this, "" + attendanceRequiredModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (apiName.equals("URL_VIEW_ATTENDANCE")) {
                ViewAttendanceModel attendanceRequiredModel = gson.fromJson(response, ViewAttendanceModel.class);
                if (attendanceRequiredModel.getStatus().toString().equals("success")) {
                    lv_view_attendance.setAdapter(new ViewAttendanceAdapter(this, attendanceRequiredModel));
                } else {
                    Toast.makeText(this, "" + attendanceRequiredModel.getMessage(), Toast.LENGTH_SHORT).show();
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