package com.infojaalprime.hrms.attendance_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.attendance_model.view_attendance_model.ViewAttendanceModel;

public class ViewAttendanceAdapter extends BaseAdapter {

    ViewAttendanceModel viewAttendanceModel;
    Context context;

    public ViewAttendanceAdapter(Context context, ViewAttendanceModel viewAttendanceModel) {
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
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_view_attendance, viewGroup, false);
            myHolder = new MyHolder();
            myHolder.txtAttendanceDate = (TextView) convertView.findViewById(R.id.txtAttendanceDate);
            myHolder.txtInTime = (TextView) convertView.findViewById(R.id.txtInTime);
            myHolder.txtOutTime = (TextView) convertView.findViewById(R.id.txtOutTime);
            myHolder.txtWorkingHrs = (TextView) convertView.findViewById(R.id.txtWorkingHrs);
            myHolder.txtDayStatus = (TextView) convertView.findViewById(R.id.txtDayStatus);
            myHolder.txtLateComing = (TextView) convertView.findViewById(R.id.txtLateComing);
            myHolder.txtShortAttendance = (TextView) convertView.findViewById(R.id.txtShortAttendance);
            myHolder.txtDayDescription = (TextView) convertView.findViewById(R.id.txtDayDescription);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.txtAttendanceDate.setText(viewAttendanceModel.getData().get(position).getDated());
        myHolder.txtInTime.setText(viewAttendanceModel.getData().get(position).getIntime());
        myHolder.txtOutTime.setText(viewAttendanceModel.getData().get(position).getOuttime());
        myHolder.txtWorkingHrs.setText(viewAttendanceModel.getData().get(position).getWorkhour());
        myHolder.txtDayStatus.setText(viewAttendanceModel.getData().get(position).getDaystatus());
        myHolder.txtLateComing.setText(viewAttendanceModel.getData().get(position).getLateComing());
        myHolder.txtShortAttendance.setText(viewAttendanceModel.getData().get(position).getShort());
        myHolder.txtDayDescription.setText(viewAttendanceModel.getData().get(position).getLatecount());
        return convertView;
    }

    public class MyHolder {
        TextView txtAttendanceDate, txtInTime, txtOutTime, txtWorkingHrs, txtDayStatus,
                txtLateComing, txtShortAttendance, txtDayDescription;
    }
}
