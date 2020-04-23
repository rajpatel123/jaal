package com.infojaalprime.hrms.leave;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.interfaces.ApiFetcher;
import com.infojaalprime.hrms.leave_adapter.LeaveRequestAdapter;
import com.infojaalprime.hrms.leave_model.view_leave_request_model.ViewLeaveRequestModel;
import com.infojaalprime.hrms.logger.Logger;
import com.infojaalprime.hrms.manager.ApiManager;
import com.infojaalprime.hrms.manager.SessionManager;
import com.infojaalprime.hrms.others.Utils;
import com.infojaalprime.hrms.urls.ServiceUrls;

public class ViewLeaveRequestsActivity extends AppCompatActivity implements ApiFetcher {

    ListView lv_view_leave_request;

    SessionManager sessionManager;
    ApiManager apiManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leave_requests);

        sessionManager = new SessionManager(this);
        apiManager = new ApiManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        Toolbar toolbar_common = (Toolbar) findViewById(R.id.tb_common);
        setSupportActionBar(toolbar_common);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Leave Requests");
        toolbar_common.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lv_view_leave_request = (ListView) findViewById(R.id.lv_view_leave_request);
        lv_view_leave_request.setDivider(null);

        String emp_id = sessionManager.getEmpId();
        String fin_id = sessionManager.getFinId();
        String company_id = sessionManager.getCompId();

        String[] key = {"pageindex", "pagesize", "fk_empid", "fk_finid", "fk_companyId"};
        String[] value = {"0", "1000", emp_id, fin_id, company_id};
        apiManager.set_interface_context_post(key, value, "URL_VIEW_LEAVE_REQUEST", ServiceUrls.URL_VIEW_LEAVE_REQUEST);
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
            if (apiName.equals("URL_VIEW_LEAVE_REQUEST")) {
                ViewLeaveRequestModel attendanceRequiredModel = gson.fromJson(response, ViewLeaveRequestModel.class);
                if (attendanceRequiredModel.getStatus().toString().equals("success")) {
                    lv_view_leave_request.setAdapter(new LeaveRequestAdapter(this, attendanceRequiredModel));
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