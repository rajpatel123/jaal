package com.infojaalprime.hrms.leave_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.infojaalprime.hrms.R;
import com.infojaalprime.hrms.interfaces.SendListToActivityFromAdapter;
import com.infojaalprime.hrms.logger.Logger;

import java.util.ArrayList;

public class CalculateDaysNewAdapter extends BaseAdapter {

    ArrayList<String> al_half_day_club_id = new ArrayList<>();
    ArrayList<String> al_half_day_club_name = new ArrayList<>();
    ArrayList<String> al_half_day_status_id = new ArrayList<>();
    ArrayList<String> al_half_day_status_name = new ArrayList<>();

    ArrayList<String> al_sr_number = new ArrayList<>();
    ArrayList<String> al_date = new ArrayList<>();
    ArrayList<String> al_leave_name = new ArrayList<>();
    ArrayList<String> al_leave_id = new ArrayList<>();
    ArrayList<String> al_is_half_day = new ArrayList<>();
    ArrayList<String> al_is_enabled = new ArrayList<>();
    ArrayList<String> al_is_checked = new ArrayList<>();
    ArrayList<String> al_half_day_status = new ArrayList<>();

    public static ArrayList<String> al_is_half_day_1 = new ArrayList<>();
    public static ArrayList<String> al_half_day_status_id_1 = new ArrayList<>();
    public static ArrayList<String> al_half_day_club_id_1 = new ArrayList<>();

    Context context;

    public static String total_days = "";

    SendListToActivityFromAdapter sendListToActivityFromAdapter;

    public CalculateDaysNewAdapter(SendListToActivityFromAdapter sendListToActivityFromAdapter) {
        this.sendListToActivityFromAdapter = sendListToActivityFromAdapter;
    }

    public CalculateDaysNewAdapter(Context context, ArrayList<String> al_sr_number, ArrayList<String> al_date,
                                   ArrayList<String> al_leave_id, ArrayList<String> al_leave_name,
                                   ArrayList<String> al_is_half_day, ArrayList<String> al_is_checked,
                                   ArrayList<String> al_is_enabled, ArrayList<String> al_half_day_status,
                                   ArrayList<String> al_half_day_status_name, ArrayList<String> al_half_day_club_name,
                                   ArrayList<String> al_half_day_status_id, ArrayList<String> al_half_day_club_id,
                                   String total_days) {
        this.context = context;
        this.al_sr_number = al_sr_number;
        this.al_date = al_date;
        this.al_leave_id = al_leave_id;
        this.al_leave_name = al_leave_name;
        this.al_is_half_day = al_is_half_day;
        this.al_is_checked = al_is_checked;
        this.al_is_enabled = al_is_enabled;
        this.al_half_day_status = al_half_day_status;
        this.al_half_day_status_name = al_half_day_status_name;
        this.al_half_day_club_name = al_half_day_club_name;
        this.al_half_day_status_id = al_half_day_status_id;
        this.al_half_day_club_id = al_half_day_club_id;
        this.total_days = total_days;

        Logger.e("total_days    " + total_days);

        al_is_half_day_1.clear();
        al_is_half_day_1 = al_is_half_day;

//        Logger.e("is_half_day_1     " + al_is_half_day_1);

        al_half_day_club_id_1.clear();
        al_half_day_status_id_1.clear();

        for (int i = 0; i < al_sr_number.size(); i++) {
            al_half_day_club_id_1.add("");
            al_half_day_status_id_1.add("");
        }

//        Logger.e("al_half_day_club_id_1     " + al_half_day_club_id_1);
//        Logger.e("al_half_day_status_id_1     " + al_half_day_status_id_1);
    }

    public void fun() {
        sendListToActivityFromAdapter.onSendData(al_is_half_day_1, al_half_day_status_id_1, al_half_day_club_id_1,total_days);
    }

    @Override
    public int getCount() {
        return al_sr_number.size();
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
        final MyHolder myHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_view_calculate_days_new, viewGroup, false);
            myHolder = new MyHolder();
            myHolder.tv_sr_number = (TextView) convertView.findViewById(R.id.tv_sr_number);
            myHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            myHolder.tv_leave_type = (TextView) convertView.findViewById(R.id.tv_leave_type);
            myHolder.sp_half_day_status = (Spinner) convertView.findViewById(R.id.sp_half_day_status);
            myHolder.sp_half_day_club = (Spinner) convertView.findViewById(R.id.sp_half_day_club);
            myHolder.cb_half_day = (CheckBox) convertView.findViewById(R.id.cb_half_day);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.tv_sr_number.setText(al_sr_number.get(position));
        myHolder.tv_date.setText(al_date.get(position));
        myHolder.tv_leave_type.setText(al_leave_name.get(position));

        myHolder.sp_half_day_club.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_row, R.id.tv_spinner, al_half_day_club_name));
        myHolder.sp_half_day_status.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_row, R.id.tv_spinner, al_half_day_status_name));

        String is_half_day = al_is_half_day.get(position);

        if (is_half_day.equals("1")) {
            myHolder.cb_half_day.setChecked(true);
//            myHolder.cb_half_day.setEnabled(false);

            myHolder.sp_half_day_club.setEnabled(true);
            myHolder.sp_half_day_status.setEnabled(true);
        } else {
            myHolder.cb_half_day.setChecked(false);

            myHolder.sp_half_day_club.setEnabled(false);
            myHolder.sp_half_day_status.setEnabled(false);
        }

        myHolder.cb_half_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox chk = (CheckBox) view;
                if (chk.isChecked()) {
                    al_is_half_day_1.set(position, "1");

                    Logger.e("is_half_day_1     " + al_is_half_day_1);

                    double d = Double.parseDouble(total_days) - 0.5;
                    total_days = String.valueOf(d);

                    Logger.e("total_days if    " + total_days);

                    myHolder.sp_half_day_club.setEnabled(true);
                    myHolder.sp_half_day_status.setEnabled(true);
                } else {
                    al_is_half_day_1.set(position, "0");

                    Logger.e("is_half_day_1     " + al_is_half_day_1);

                    double d = Double.parseDouble(total_days) + 0.5;
                    total_days = String.valueOf(d);

                    Logger.e("total_days else    " + total_days);

                    myHolder.sp_half_day_club.setEnabled(false);
                    myHolder.sp_half_day_status.setEnabled(false);
                }
            }
        });

        myHolder.sp_half_day_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                al_half_day_status_id_1.set(position, al_half_day_status_id.get(pos));

//                Logger.e("al_half_day_status_id_1     " + al_half_day_status_id_1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        myHolder.sp_half_day_club.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                al_half_day_club_id_1.set(position, al_half_day_club_id.get(pos));

//                Logger.e("al_half_day_club_id_1     " + al_half_day_club_id_1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return convertView;
    }

    public class MyHolder {
        TextView tv_sr_number, tv_date, tv_leave_type;
        Spinner sp_half_day_status, sp_half_day_club;
        CheckBox cb_half_day;
    }
}