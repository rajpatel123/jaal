package com.infojaalprime.hrms.leave_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.leave_model.LeaveBalanceModel;

import java.util.ArrayList;

public class ViewLeaveBalanceAdapter extends BaseAdapter {

    private ArrayList<LeaveBalanceModel> mDataset;

    public ViewLeaveBalanceAdapter(ArrayList<LeaveBalanceModel> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public int getCount() {
        return mDataset.size();
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
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_view_leave_balance, viewGroup, false);
            myHolder = new MyHolder();
            myHolder.txtLeaveId = (TextView) convertView.findViewById(R.id.txtLeaveId);
            myHolder.txtLeaveType = (TextView) convertView.findViewById(R.id.txtLeaveType);
            myHolder.txtShortDesc = (TextView) convertView.findViewById(R.id.txtShortDesc);
            myHolder.txtCurrentYrLeaves = (TextView) convertView.findViewById(R.id.txtCurrentYrLeaves);
            myHolder.txtTotalLeavesEarned = (TextView) convertView.findViewById(R.id.txtTotalLeavesEarned);
            myHolder.txtTotalLeave = (TextView) convertView.findViewById(R.id.txtTotalLeave);
            myHolder.txtLeavesAvailed = (TextView) convertView.findViewById(R.id.txtLeavesAvailed);
            myHolder.txtBalanceLeave = (TextView) convertView.findViewById(R.id.txtBalanceLeave);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.txtLeaveId.setText(mDataset.get(position).getLeaveId());
        myHolder.txtLeaveType.setText(mDataset.get(position).getLeaveType());
        myHolder.txtShortDesc.setText(mDataset.get(position).getShortDesc());
        myHolder.txtCurrentYrLeaves.setText(mDataset.get(position).getCurrentYearLeaves());
        myHolder.txtTotalLeavesEarned.setText(mDataset.get(position).getTotalLeavesEarned());
        myHolder.txtTotalLeave.setText(mDataset.get(position).getTotalLeave());
        myHolder.txtLeavesAvailed.setText(mDataset.get(position).getLeavesAvailed());
        myHolder.txtBalanceLeave.setText(mDataset.get(position).getBalanceLeave());
        return convertView;
    }

    public class MyHolder {
        TextView txtLeaveId, txtLeaveType, txtShortDesc, txtCurrentYrLeaves, txtTotalLeavesEarned, txtTotalLeave,
                txtLeavesAvailed, txtBalanceLeave;
    }
}
