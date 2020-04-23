package com.infojaalprime.hrms.leave;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.interfaces.ApiFetcher;
import com.infojaalprime.hrms.interfaces.SendListToActivityFromAdapter;
import com.infojaalprime.hrms.leave_adapter.CalculateDaysNewAdapter;
import com.infojaalprime.hrms.leave_model.apply_leave_model.ApplyLeaveModel;
import com.infojaalprime.hrms.leave_model.calculate_days_model.CalculateDaysModel2;
import com.infojaalprime.hrms.leave_model.get_half_day_club_model.GetHalfDayClubModel;
import com.infojaalprime.hrms.leave_model.get_half_day_status_model.GetHalfDayStatusModel;
import com.infojaalprime.hrms.leave_model.populate_leave_type_model.PopulateLeaveTypeModel;
import com.infojaalprime.hrms.logger.Logger;
import com.infojaalprime.hrms.manager.ApiManager;
import com.infojaalprime.hrms.manager.SessionManager;
import com.infojaalprime.hrms.others.Utils;
import com.infojaalprime.hrms.urls.ServiceUrls;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ApplyLeaveActivity extends AppCompatActivity implements
        ApiFetcher,
        SendListToActivityFromAdapter {

    Spinner sp_leave_type;
    EditText edtContact, edtContactDuringLeave, edtLeaveTravelPurpose;
    Button btn_calculate_days, btn_apply_leave;
    TextView tv_FromDate, tv_ToDate;
    ScrollView scrollview;
    Menu menu;
    ListView lv_view_calculate_days;

    ArrayList<String> al_is_half_day_1 = new ArrayList<>();
    ArrayList<String> al_is_half_day_2 = new ArrayList<>();
    ArrayList<String> al_half_day_status_id_1 = new ArrayList<>();
    ArrayList<String> al_half_day_club_id_1 = new ArrayList<>();

    ArrayList<String> al_half_day_club_id = new ArrayList<>();
    ArrayList<String> al_half_day_club_name = new ArrayList<>();

    ArrayList<String> al_half_day_status_id = new ArrayList<>();
    ArrayList<String> al_half_day_status_name = new ArrayList<>();

    ArrayList<String> al_leave_type_id = new ArrayList<>();
    ArrayList<String> al_leave_type_name = new ArrayList<>();

    ArrayList<String> al_sr_number = new ArrayList<>();
    ArrayList<String> al_date = new ArrayList<>();
    ArrayList<String> al_leave_name = new ArrayList<>();
    ArrayList<String> al_leave_id = new ArrayList<>();
    ArrayList<String> al_is_half_day = new ArrayList<>();
    ArrayList<String> al_is_enabled = new ArrayList<>();
    ArrayList<String> al_is_checked = new ArrayList<>();
    ArrayList<String> al_half_day_status = new ArrayList<>();

    SessionManager sessionManager;
    ApiManager apiManager;
    ProgressDialog progressDialog;

    String leave_id = "", fromDate = "", toDate = "", company_id = "", emp_id = "", fin_id = "", total_days = "", new_days = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        Toolbar toolbar_common = (Toolbar) findViewById(R.id.tb_common);
        setSupportActionBar(toolbar_common);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Apply Leave");
        toolbar_common.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        sp_leave_type = (Spinner) findViewById(R.id.sp_leave_type);

        tv_FromDate = (TextView) findViewById(R.id.tv_FromDate);
        tv_ToDate = (TextView) findViewById(R.id.tv_ToDate);
        edtContact = (EditText) findViewById(R.id.edtContact);
        edtLeaveTravelPurpose = (EditText) findViewById(R.id.edtLeaveTravelPurpose);
        edtContactDuringLeave = (EditText) findViewById(R.id.edtContactDuringLeave);
        btn_calculate_days = (Button) findViewById(R.id.btn_calculate_days);
        btn_apply_leave = (Button) findViewById(R.id.btn_apply_leave);
        lv_view_calculate_days = (ListView) findViewById(R.id.lv_view_calculate_days);
        lv_view_calculate_days.setDivider(null);

        company_id = sessionManager.getCompId();
        emp_id = sessionManager.getEmpId();
        fin_id = sessionManager.getFinId();

        String[] key2 = {"fk_companyId", "fk_empId"};
        String[] value2 = {company_id, emp_id};
        apiManager.set_interface_context_post(key2, value2, "URL_POPULATE_LEAVE_TYPE", ServiceUrls.URL_POPULATE_LEAVE_TYPE);

        String[] key = {"fk_companyId"};
        String[] value = {company_id};
        apiManager.set_interface_context_post(key, value, "URL_GET_HALF_DAY_CLUB", ServiceUrls.URL_GET_HALF_DAY_CLUB);

        String[] key1 = {"fk_companyId"};
        String[] value1 = {company_id};
        apiManager.set_interface_context_post(key1, value1, "URL_GET_HALF_DAY_STATUS", ServiceUrls.URL_GET_HALF_DAY_STATUS);

        tv_FromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker = new DatePickerDialog(ApplyLeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                            String dy = "";
                            if (day < 10) {
                                dy = "0" + day;
                            } else {
                                dy = "" + day;
                            }
                            tv_FromDate.setText(dy + "-" + Utils.getMonthName(month + 1) + "-" + year);
                            month = month + 1;
                            String mnth = "";
                            if (month < 10) {
                                mnth = "0" + month;
                            } else {
                                mnth = "" + month;
                            }
                            fromDate = year + "/" + mnth + "/" + dy;
                        }
                    }, mYear, mMonth, mDay);
//                    mDatePicker.getDatePicker().setMinDate(new Date().getTime() - 10000);
                    mDatePicker.setTitle(null);
                    mDatePicker.show();
                } catch (Exception e) {
                    Logger.e("Exception         " + e.toString());
                }
            }
        });

        tv_ToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker = new DatePickerDialog(ApplyLeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                            String dy = "";
                            if (day < 10) {
                                dy = "0" + day;
                            } else {
                                dy = "" + day;
                            }
                            tv_ToDate.setText(dy + "-" + Utils.getMonthName(month + 1) + "-" + year);
                            month = month + 1;
                            String mnth = "";
                            if (month < 10) {
                                mnth = "0" + month;
                            } else {
                                mnth = "" + month;
                            }
                            toDate = year + "/" + mnth + "/" + dy;
                        }
                    }, mYear, mMonth, mDay);
//                    mDatePicker.getDatePicker().setMinDate(new Date().getTime() - 10000);
                    mDatePicker.setTitle(null);
                    mDatePicker.show();
                } catch (Exception e) {
                    Logger.e("Exception         " + e.toString());
                }
            }
        });

        sp_leave_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                leave_id = al_leave_type_id.get(i);
                Logger.e("leave_id           " + leave_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_calculate_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (leave_id.equals("")) {
                        Utils.showToastMessage(ApplyLeaveActivity.this, "Please Select Leave Type");
                    } else if (fromDate.equals("")) {
                        Utils.showToastMessage(ApplyLeaveActivity.this, "Please Select From Date");
                    } else if (toDate.equals("")) {
                        Utils.showToastMessage(ApplyLeaveActivity.this, "Please Select To Date");
                    } else if (edtContact.getText().toString().equals("")) {
                        Utils.showToastMessage(ApplyLeaveActivity.this, "Please Enter Contact Number");
                        edtContact.requestFocus();
                    } else if (edtContactDuringLeave.getText().toString().equals("")) {
                        Utils.showToastMessage(ApplyLeaveActivity.this, "Please Enter Contact During Leave");
                        edtContactDuringLeave.requestFocus();
                    } else if (edtLeaveTravelPurpose.getText().toString().equals("")) {
                        Utils.showToastMessage(ApplyLeaveActivity.this, "Please Enter Leave Travel Purpose");
                        edtLeaveTravelPurpose.requestFocus();
                    } else if (edtContact.getText().toString().length() > 10 || edtContact.getText().toString().length() < 10) {
                        Utils.showToastMessage(ApplyLeaveActivity.this, "Contact Number should be of 10 digits");
                        edtContact.requestFocus();
                    } else {
                        String[] key2 = {"fk_empId", "fromdate", "todate", "fk_leaveid"};
                        String[] value2 = {emp_id, fromDate, toDate, leave_id};
                        apiManager.set_interface_context_post(key2, value2, "URL_CALCULATE_DAYS", ServiceUrls.URL_CALCULATE_DAYS);
                    }
                } catch (Exception e) {
                    Logger.e("Exception         " + e.toString());
                }
            }
        });

        btn_apply_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CalculateDaysNewAdapter(ApplyLeaveActivity.this).fun();
            }
        });
    }

//    @Override
//    public void onItemClick(int position, View v) {
//        try {
//            int count = 0;
//            try {
//                if (list.get(0).getHalfDay().equals("true")) {
//                    count = count + 1;
//                } else {
//
//                }
//            } catch (Exception ex) {
//
//            }
//            try {
//                int size = list.size();
//                if (size >= 2) {
//                    if (list.get(size - 1).getHalfDay().equals("true")) {
//                        count = count + 1;
//                    } else {
//
//                    }
//                }
//            } catch (Exception ex) {
//
//            }
//
////            if (count == 2) {
////                double res = Integer.valueOf(resultDays) - 1;
////                //count = Integer.valueOf(totalDays) - 1;
//////                getCalculatedDays().setText("" + res);
////                totalDays = "" + res;
////            } else if (count == 0) {
////                count = Integer.valueOf(resultDays);
//////                getCalculatedDays().setText("" + count);
////                totalDays = "" + count;
////            } else if (count == 1) {
////                double res = Integer.valueOf(resultDays) - 0.5;
//////                getCalculatedDays().setText("" + res);
////                totalDays = "" + res;
////            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public String makeApplyLeaveXML() {

        String xml = "";
        try {
            String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date()).toString();
            xml = "<NewDataSet>" +
                    "<SAL_Leave_Apply>" +
                    "<pk_leaveappid>0</pk_leaveappid>" +
                    "<fk_empid>" + emp_id + "</fk_empid>" +
                    "<fk_finid>" + fin_id + "</fk_finid>" +
                    "<fk_leaveid>" + leave_id + "</fk_leaveid>" +
                    "<dated>" + date + "T00:00:00+05:30" + "</dated>" +
                    "<fromdate>" + fromDate + "T00:00:00+05:30" + "</fromdate>" +
                    "<todate>" + toDate + "T00:00:00+05:30" + "</todate>" +
                    "<totdays>" + new_days + "</totdays>" +
                    "<contactno>" + edtContact.getText().toString() + "</contactno>" +
                    "<contactduringleave>" + edtContactDuringLeave.getText().toString() + "</contactduringleave>" +
                    "<status>P</status>" +
                    "<remarks>" + edtLeaveTravelPurpose.getText().toString() + "</remarks>" +
                    "<costhead />" +
                    "<eligibility />" +
                    "<DepartTktClass />" +
                    "<FromLoc />" +
                    "<ToLoc />" +
                    "<ReturnTktClass />" +
                    "<FromLoc1 />" +
                    "<ToLoc1 />" +
                    "<eligible>true</eligible>" +
                    "<advance>0</advance>" +
                    "<remarks1 />" +
                    "<otherdetails />" +
                    "</SAL_Leave_Apply>";

            for (int i = 0; i < al_is_half_day_1.size(); i++) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(al_date.get(i)));
                int appliedDay = cal.get(Calendar.DAY_OF_MONTH);
                int appliedMonth = cal.get(Calendar.MONTH) + 1;
                int appliedYear = cal.get(Calendar.YEAR);

                String appMnth = "";
                if (appliedMonth < 10) {
                    appMnth = "0" + appliedMonth;
                } else {
                    appMnth = "" + appliedMonth;
                }
                String appDay = "";
                if (appliedDay < 10) {
                    appDay = "0" + appliedDay;
                } else {
                    appDay = "" + appliedDay;
                }
                String appliedDate = "" + appliedYear + "/" + appMnth + "/" + appDay;

                xml = xml + "<SAL_Leave_Apply_Details>" +
                        "    <fk_leaveappid>" + i + "</fk_leaveappid>" +
                        "    <sno>" + (i + 1) + "</sno>" +
                        "    <fk_leaveid>" + al_leave_id.get(i) + "</fk_leaveid>" +
                        "    <dated>" + appliedDate + "T00:00:00+05:30" + "</dated>" +
                        "    <clubcase>N</clubcase>" +
                        "    <covercase>N</covercase>" +
                        "    <ishalfday>" + al_is_half_day_2.get(i) + "</ishalfday>" +
                        "    <halfdaystatus>" + al_half_day_status_id_1.get(i) + "</halfdaystatus>" +
                        "    <remarks />" +
                        "    <halfdayclub>" + al_half_day_club_id_1.get(i) + "</halfdayclub>" +
                        "  </SAL_Leave_Apply_Details>";
            }
            xml = xml + "</NewDataSet>";
        } catch (Exception e) {
            Logger.e("Exception         " + e.toString());
        }
        return xml;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_apply_leave, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public void onAPIRunningState(int a, String apiName) {
        if (a == ApiFetcher.KEY_API_IS_RUNNING)
            if (apiName.equals("URL_POPULATE_LEAVE_TYPE"))
                progressDialog.show();
            else if (apiName.equals("URL_CALCULATE_DAYS"))
                progressDialog.show();
            else if (apiName.equals("URL_INSERT_APPLY_LEAVE"))
                progressDialog.show();
        if (a == ApiFetcher.KEY_API_IS_STOPPED)
            if (apiName.equals("URL_POPULATE_LEAVE_TYPE"))
                progressDialog.dismiss();
            else if (apiName.equals("URL_CALCULATE_DAYS"))
                progressDialog.dismiss();
            else if (apiName.equals("URL_INSERT_APPLY_LEAVE"))
                progressDialog.dismiss();
        if (a == ApiFetcher.KEY_API_IS_STOPPED_WITH_ERROR)
            if (apiName.equals("URL_POPULATE_LEAVE_TYPE"))
                progressDialog.dismiss();
            else if (apiName.equals("URL_CALCULATE_DAYS"))
                progressDialog.dismiss();
            else if (apiName.equals("URL_INSERT_APPLY_LEAVE"))
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

            if (apiName.equals("URL_GET_HALF_DAY_CLUB")) {

                al_half_day_club_id.clear();
                al_half_day_club_name.clear();

                GetHalfDayClubModel getHalfDayClubModel = gson.fromJson(response, GetHalfDayClubModel.class);
                if (getHalfDayClubModel.getStatus().toString().equals("success")) {
                    for (int i = 0; i < getHalfDayClubModel.getData().size(); i++) {
                        al_half_day_club_id.add(getHalfDayClubModel.getData().get(i).getPkHalfdayid());
                        al_half_day_club_name.add(getHalfDayClubModel.getData().get(i).getHalfdayclub());
                    }
                } else {
                    Logger.e("else          " + getHalfDayClubModel.getMessage());
                }
            } else if (apiName.equals("URL_GET_HALF_DAY_STATUS")) {

                al_half_day_status_id.clear();
                al_half_day_status_name.clear();

                GetHalfDayStatusModel getHalfDayStatusModel = gson.fromJson(response, GetHalfDayStatusModel.class);
                if (getHalfDayStatusModel.getStatus().toString().equals("success")) {
                    for (int i = 0; i < getHalfDayStatusModel.getData().size(); i++) {
                        al_half_day_status_id.add(getHalfDayStatusModel.getData().get(i).getPkHalfdayid());
                        al_half_day_status_name.add(getHalfDayStatusModel.getData().get(i).getHalfdaystatus());
                    }
                } else {
                    Logger.e("else          " + getHalfDayStatusModel.getMessage());
                }
            } else if (apiName.equals("URL_POPULATE_LEAVE_TYPE")) {

                al_leave_type_id.clear();
                al_leave_type_name.clear();

                PopulateLeaveTypeModel populateLeaveTypeModel = gson.fromJson(response, PopulateLeaveTypeModel.class);
                if (populateLeaveTypeModel.getStatus().toString().equals("success")) {
                    for (int i = 0; i < populateLeaveTypeModel.getData().size(); i++) {
                        al_leave_type_id.add(populateLeaveTypeModel.getData().get(i).getPkLeaveid());
                        al_leave_type_name.add(populateLeaveTypeModel.getData().get(i).getLeavetype());
                    }
                    sp_leave_type.setAdapter(new ArrayAdapter<>(ApplyLeaveActivity.this, R.layout.spinner_row, R.id.tv_spinner, al_leave_type_name));
                } else {
                    Logger.e("else          " + populateLeaveTypeModel.getMessage());
                }
            } else if (apiName.equals("URL_CALCULATE_DAYS")) {

                al_sr_number.clear();
                al_date.clear();
                al_leave_id.clear();
                al_leave_name.clear();
                al_is_checked.clear();
                al_is_enabled.clear();
                al_is_half_day.clear();
                al_half_day_status.clear();

                CalculateDaysModel2 calculateDaysModel = gson.fromJson(response, CalculateDaysModel2.class);
                if (calculateDaysModel.getStatus().toString().equals("success")) {
                    for (int i = 0; i < calculateDaysModel.getData().size(); i++) {
                        al_sr_number.add(calculateDaysModel.getData().get(i).getCID());
                        al_date.add(calculateDaysModel.getData().get(i).getDates());
                        if (calculateDaysModel.getData().get(i).getLeaveid().toString().equals("0")) {
                            al_leave_id.add(calculateDaysModel.getData().get(i).getLwp());
                        } else {
                            al_leave_id.add(calculateDaysModel.getData().get(i).getLeaveid());
                        }
                        al_leave_name.add(calculateDaysModel.getData().get(i).getLeaveType());
                        al_is_half_day.add(calculateDaysModel.getData().get(i).getIshalfday());
                        al_is_checked.add(calculateDaysModel.getData().get(i).getIschecked());
                        al_is_enabled.add(calculateDaysModel.getData().get(i).getIsenabled());
                        al_half_day_status.add(calculateDaysModel.getData().get(i).getHalpdaystatus());
                        total_days = calculateDaysModel.getData().get(i).getTotal_days();
                    }

                    lv_view_calculate_days.setVisibility(View.VISIBLE);
                    btn_apply_leave.setVisibility(View.VISIBLE);

                    scrollview.setVisibility(View.GONE);
                    btn_calculate_days.setVisibility(View.GONE);
                    lv_view_calculate_days.setAdapter(new CalculateDaysNewAdapter(this,
                            al_sr_number, al_date,
                            al_leave_id, al_leave_name,
                            al_is_half_day, al_is_checked,
                            al_is_enabled, al_half_day_status,
                            al_half_day_status_name, al_half_day_club_name,
                            al_half_day_status_id, al_half_day_club_id, total_days));
                } else {
                    Toast.makeText(this, calculateDaysModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (apiName.equals("URL_INSERT_APPLY_LEAVE")) {
                ApplyLeaveModel applyLeaveModel = gson.fromJson(response, ApplyLeaveModel.class);
                if (applyLeaveModel.getStatus().toString().equals("Successed")) {
                    Toast.makeText(this, "" + applyLeaveModel.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "" + applyLeaveModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Logger.e("Exception         " + e.toString());
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

    @Override
    public void onSendData(ArrayList<String> al_is_half_day_1, ArrayList<String> al_half_day_status_id_1, ArrayList<String> al_half_day_club_id_1, String new_days) {

        try {
            this.al_is_half_day_1 = al_is_half_day_1;
            this.al_half_day_status_id_1 = al_half_day_status_id_1;
            this.al_half_day_club_id_1 = al_half_day_club_id_1;

            this.new_days = new_days;

            al_is_half_day_2.clear();
            for (int i = 0; i < al_is_half_day_1.size(); i++) {
                if (al_is_half_day_1.get(i).equals("1")) {
                    al_is_half_day_2.add("true");
                } else {
                    al_is_half_day_2.add("false");
                }
            }

            Logger.e("al_is_half_day_2   fff        " + al_is_half_day_2);
            Logger.e("al_half_day_status_id_1  fff         " + al_half_day_status_id_1);
            Logger.e("al_half_day_club_id_1 fff          " + al_half_day_club_id_1);
        } catch (Exception e) {
            Logger.e("Exception         " + e.toString());
        }

        String xml = makeApplyLeaveXML();
        Logger.e("xml           " + xml);
        String[] key2 = {"xmlDoc"};
        String[] value2 = {xml};
        apiManager.set_interface_context_post(key2, value2, "URL_INSERT_APPLY_LEAVE", ServiceUrls.URL_INSERT_APPLY_LEAVE);
    }
}