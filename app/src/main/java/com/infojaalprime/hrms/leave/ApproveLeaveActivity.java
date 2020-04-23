package com.infojaalprime.hrms.leave;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.interfaces.ApiFetcher;
import com.infojaalprime.hrms.leave_adapter.ApproveLeaveAdapter;
import com.infojaalprime.hrms.leave_model.approve_leave_model.ApproveLeaveModel;
import com.infojaalprime.hrms.leave_model.get_pending_leave_for_approval_model.GetPendingLeaveForApprovalModel;
import com.infojaalprime.hrms.leave_model.leave_approval_status.LeaveApprovalStatus;
import com.infojaalprime.hrms.logger.Logger;
import com.infojaalprime.hrms.manager.ApiManager;
import com.infojaalprime.hrms.manager.SessionManager;
import com.infojaalprime.hrms.others.Utils;
import com.infojaalprime.hrms.urls.ServiceUrls;

import java.util.ArrayList;

public class ApproveLeaveActivity extends AppCompatActivity implements ApiFetcher {

    ListView lv_approve_leave;

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    ApiManager apiManager;
    GetPendingLeaveForApprovalModel getPendingLeaveForApprovalModel;

    String emp_id = "", company_id = "", status_id = "";
    int position;

    ArrayList<String> al_status_id = new ArrayList<>();
    ArrayList<String> al_status_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_leave);

        Toolbar toolbar_common = (Toolbar) findViewById(R.id.tb_common);
        setSupportActionBar(toolbar_common);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Approve Leave");
        toolbar_common.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lv_approve_leave = findViewById(R.id.lv_approve_leave);
        lv_approve_leave.setDivider(null);

        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        emp_id = sessionManager.getEmpId();
        company_id = sessionManager.getCompId();

        String[] key2 = {"fk_empId"};
        String[] value2 = {emp_id};
        apiManager.set_interface_context_post(key2, value2, "URL_GET_PENDING_LEAVE_APPROVAL", ServiceUrls.URL_GET_PENDING_LEAVE_APPROVAL);

//        lv_approve_leave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                position = i;
//                String[] key2 = {"fk_companyId"};
//                String[] value2 = {company_id};
//                apiManager.set_interface_context_post(key2, value2, "URL_LEAVE_APPROVAL_STATUS", ServiceUrls.URL_LEAVE_APPROVAL_STATUS);
//            }
//        });
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
            if (apiName.equals("URL_GET_PENDING_LEAVE_APPROVAL")) {
                getPendingLeaveForApprovalModel = gson.fromJson(response, GetPendingLeaveForApprovalModel.class);
                if (getPendingLeaveForApprovalModel.getStatus().toString().equals("success")) {

                    lv_approve_leave.setAdapter(new ApproveLeaveAdapter(this, getPendingLeaveForApprovalModel));
                } else {
                    Toast.makeText(this, "" + getPendingLeaveForApprovalModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (apiName.equals("URL_LEAVE_APPROVAL_STATUS")) {
                LeaveApprovalStatus leaveApprovalStatus = gson.fromJson(response, LeaveApprovalStatus.class);
                if (leaveApprovalStatus.getStatus().toString().equals("success")) {

                    al_status_id.clear();
                    al_status_name.clear();

                    for (int i = 0; i < leaveApprovalStatus.getData().size(); i++) {
                        al_status_id.add(leaveApprovalStatus.getData().get(i).getPkStatusId());
                        al_status_name.add(leaveApprovalStatus.getData().get(i).getStatus());
                    }
                    dialog_for_approve_leave();
                } else {
                    Toast.makeText(this, "" + leaveApprovalStatus.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (apiName.equals("URL_APPROVE_LEAVE")) {
                ApproveLeaveModel approveLeaveModel = gson.fromJson(response, ApproveLeaveModel.class);
                if (approveLeaveModel.getStatus().toString().equals("Successed")) {
                    Toast.makeText(this, "" + approveLeaveModel.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "" + approveLeaveModel.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void dialog_for_approve_leave() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_for_approve_leave);
        dialog.setTitle("Approve Leave");
        final Spinner sp_approve_leave = (Spinner) dialog.findViewById(R.id.sp_approve_leave);
        final EditText edt_remarks = (EditText) dialog.findViewById(R.id.edt_remarks);
        final Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);

        sp_approve_leave.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_row, R.id.tv_spinner, al_status_name));

        sp_approve_leave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status_id = al_status_id.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String remarks = edt_remarks.getText().toString();

                if (status_id.equals("")) {
                    Toast.makeText(ApproveLeaveActivity.this, "Please Select Status", Toast.LENGTH_SHORT).show();
                } else if (remarks.equals("")) {
                    Toast.makeText(ApproveLeaveActivity.this, "Please Enter Remarks", Toast.LENGTH_SHORT).show();
                } else {
                    String[] key = {"fk_leaveappid", "fk_empId", "ApproveStatus", "Remarks"};
                    String[] value = {getPendingLeaveForApprovalModel.getData().get(position).getPkLeaveappid(), emp_id, status_id, remarks};
                    apiManager.set_interface_context_post(key, value, "URL_APPROVE_LEAVE", ServiceUrls.URL_APPROVE_LEAVE);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                String i = intent.getExtras().getString("position");
                position = Integer.parseInt(i);
                String[] key2 = {"fk_companyId"};
                String[] value2 = {company_id};
                apiManager.set_interface_context_post(key2, value2, "URL_LEAVE_APPROVAL_STATUS", ServiceUrls.URL_LEAVE_APPROVAL_STATUS);
            } catch (Exception e) {
                Logger.e("Exception         " + e.toString());
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter("ApproveLeave"));
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}