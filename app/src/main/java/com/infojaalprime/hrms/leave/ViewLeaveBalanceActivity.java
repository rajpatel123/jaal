package com.infojaalprime.hrms.leave;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.leave_adapter.ViewLeaveBalanceAdapter;
import com.infojaalprime.hrms.leave_model.LeaveBalanceModel;
import com.infojaalprime.hrms.manager.SessionManager;
import com.infojaalprime.hrms.others.Utils;
import com.infojaalprime.hrms.urls.ServiceUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewLeaveBalanceActivity extends AppCompatActivity {

    ListView lv_view_leave_balance;

    ArrayList<LeaveBalanceModel> list = new ArrayList();

    SessionManager sessionManager;
    ViewLeaveBalanceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leave_balance);

        Toolbar toolbar_common = (Toolbar) findViewById(R.id.tb_common);
        setSupportActionBar(toolbar_common);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Leave Balance");
        toolbar_common.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sessionManager = new SessionManager(this);

        lv_view_leave_balance = (ListView) findViewById(R.id.lv_view_leave_balance);
        lv_view_leave_balance.setDivider(null);
        mAdapter = new ViewLeaveBalanceAdapter(list);
        lv_view_leave_balance.setAdapter(mAdapter);

        makeLeaveBalanceRequest();
    }

    private void makeLeaveBalanceRequest() {
        if (Utils.isNetwork(this)) {
//            String url = ServiceUrls.ViewLeaveBalanceDetail + "EmpID=" + sessionManager.getEmpId() + "&C1=&C2=&C3=&C4=";
//            new WebServiceManager(this, url, apiCallsListener, "Attendance").execute();
        } else {
            Utils.showToastMessage(this, "Please Check your Internet");
        }
    }

//    ApiCallsListener apiCallsListener = new ApiCallsListener() {
//        @Override
//        public void onTaskDone(String responseData, String requestFor) {
//            if (requestFor.equals("Attendance")) {
//                LeaveBalanceResult(responseData);
//            } else {
//
//            }
//        }
//
//        @Override
//        public void onError(String responseData, String requestFor) {
//
//        }
//    };

//    private void LeaveBalanceResult(String str) {
//
//        JSONArray array ;
//        try {
//            JSONObject obj = new JSONObject(str);
//            String status = obj.optString(ServiceUrls.Status);
//            if (status.equals(ServiceUrls.Success)) {
//                array = new JSONArray(obj.optString(ServiceUrls.Data));
//                if (array.length() > 0) {
//                    for (int i = 0; i < array.length(); i++) {
//                        obj = array.getJSONObject(i);
//                        LeaveBalanceModel model = new LeaveBalanceModel();
//                        model.setLeaveId(obj.optString("fk_leaveid"));
//                        model.setLeaveType(obj.optString("leavetype"));
//                        model.setShortDesc(obj.optString("shortdesc"));
//                        model.setCurrentYearLeaves(obj.optString("currentyearleaves"));
//                        model.setTotalLeavesEarned(obj.optString("totalleavesearned"));
//                        model.setTotalLeave(obj.optString("totalleave"));
//                        model.setLeavesAvailed(obj.optString("leaveavailed"));
//                        model.setBalanceLeave(obj.optString("BalLeave"));
//                        list.add(model);
//                    }
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
