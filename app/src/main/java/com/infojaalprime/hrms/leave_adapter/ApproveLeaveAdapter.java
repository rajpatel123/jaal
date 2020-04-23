package com.infojaalprime.hrms.leave_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.leave_model.get_pending_leave_for_approval_model.GetPendingLeaveForApprovalModel;
import com.infojaalprime.hrms.leave_model.view_leave_request_model.ViewLeaveRequestModel;
import com.infojaalprime.hrms.logger.Logger;

public class ApproveLeaveAdapter extends BaseAdapter {

    GetPendingLeaveForApprovalModel getPendingLeaveForApprovalModel;
    Context context;

    public ApproveLeaveAdapter(Context context, GetPendingLeaveForApprovalModel getPendingLeaveForApprovalModel) {
        this.context = context;
        this.getPendingLeaveForApprovalModel = getPendingLeaveForApprovalModel;
    }

    @Override
    public int getCount() {
        return getPendingLeaveForApprovalModel.getData().size();
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        MyHolder myHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_view_approve_leave, viewGroup, false);
            myHolder = new MyHolder();
            myHolder.tv_dated = (TextView) convertView.findViewById(R.id.tv_dated);
            myHolder.tv_emp_code = (TextView) convertView.findViewById(R.id.tv_emp_code);
            myHolder.tv_emp_name = (TextView) convertView.findViewById(R.id.tv_emp_name);
            myHolder.tv_designation = (TextView) convertView.findViewById(R.id.tv_designation);
            myHolder.tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
            myHolder.tv_from_date = (TextView) convertView.findViewById(R.id.tv_from_date);
            myHolder.tv_to_date = (TextView) convertView.findViewById(R.id.tv_to_date);
            myHolder.tv_total_days = (TextView) convertView.findViewById(R.id.tv_total_days);
            myHolder.tv_contact_number = (TextView) convertView.findViewById(R.id.tv_contact_number);
            myHolder.tv_leave_purose = (TextView) convertView.findViewById(R.id.tv_leave_purose);
            myHolder.iv_menu_adapter = (ImageView) convertView.findViewById(R.id.iv_menu_adapter);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.tv_dated.setText(getPendingLeaveForApprovalModel.getData().get(position).getDated());
        String emp_code[] = (getPendingLeaveForApprovalModel.getData().get(position).getEmpcode()).split("\\|");
        myHolder.tv_emp_code.setText(emp_code[0].trim());
        myHolder.tv_emp_name.setText(emp_code[1].trim());
        myHolder.tv_from_date.setText(getPendingLeaveForApprovalModel.getData().get(position).getFromdate());
        myHolder.tv_to_date.setText(getPendingLeaveForApprovalModel.getData().get(position).getTodate());
        myHolder.tv_total_days.setText(getPendingLeaveForApprovalModel.getData().get(position).getTotDays());
        myHolder.tv_leave_purose.setText(getPendingLeaveForApprovalModel.getData().get(position).getRemarks());

        myHolder.iv_menu_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPopupButtonClick(view, position);
            }
        });
        return convertView;
    }

    public class MyHolder {
        TextView tv_dated, tv_emp_code, tv_emp_name, tv_designation, tv_grade, tv_from_date, tv_to_date, tv_total_days, tv_contact_number, tv_leave_purose;
        ImageView iv_menu_adapter;
    }

    public void onPopupButtonClick(View button, final int position) {
        final PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater().inflate(R.menu.menu_approve_leave_item, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_approve_leave_action:
                        context.sendBroadcast(new Intent("ApproveLeave")
                                .putExtra("position", position+""));
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
}