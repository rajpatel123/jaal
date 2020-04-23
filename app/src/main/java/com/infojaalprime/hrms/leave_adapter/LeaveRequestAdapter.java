package com.infojaalprime.hrms.leave_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.attendance_model.view_attendance_model.ViewAttendanceModel;
import com.infojaalprime.hrms.leave_model.view_leave_request_model.ViewLeaveRequestModel;

import java.util.ArrayList;

public class LeaveRequestAdapter extends BaseAdapter {

    ViewLeaveRequestModel viewAttendanceModel;
    Context context;

    public LeaveRequestAdapter(Context context, ViewLeaveRequestModel viewAttendanceModel) {
        this.context = context;
        this.viewAttendanceModel = viewAttendanceModel;
    }

    @Override
    public int getCount() {
        return viewAttendanceModel.getData().size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyHolder myHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_view_leave_requests, viewGroup, false);
            myHolder = new MyHolder();
            myHolder.txtLeaveType = (TextView) convertView.findViewById(R.id.txtLeaveType);
            myHolder.txtDated = (TextView) convertView.findViewById(R.id.txtDated);
            myHolder.txtFromDate = (TextView) convertView.findViewById(R.id.txtFromDate);
            myHolder.txtToDate = (TextView) convertView.findViewById(R.id.txtToDate);
            myHolder.txtTotalDays = (TextView) convertView.findViewById(R.id.txtTotalDays);
            myHolder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.txtLeaveType.setText(viewAttendanceModel.getData().get(position).getLeavetype());
        myHolder.txtDated.setText(viewAttendanceModel.getData().get(position).getDated());
        myHolder.txtFromDate.setText(viewAttendanceModel.getData().get(position).getFromdate());
        myHolder.txtToDate.setText(viewAttendanceModel.getData().get(position).getTodate());
        myHolder.txtTotalDays.setText(viewAttendanceModel.getData().get(position).getTotdays());
        myHolder.txtStatus.setText(viewAttendanceModel.getData().get(position).getStatusname());
        return convertView;
    }

    public class MyHolder {
        TextView  txtLeaveType, txtDated, txtFromDate, txtToDate, txtTotalDays, txtStatus;
    }
}

