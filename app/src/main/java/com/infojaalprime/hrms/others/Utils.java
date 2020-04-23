package com.infojaalprime.hrms.others;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.infojaalprime.hrms.logger.Logger;

import java.util.Calendar;

public class Utils {

    public static String getMonthName(int month) {
        if (month == 1) {
            return "Jan";
        } else if (month == 2) {
            return "Feb";
        } else if (month == 3) {
            return "Mar";
        } else if (month == 4) {
            return "Apr";
        } else if (month == 5) {
            return "May";
        } else if (month == 6) {
            return "Jun";
        } else if (month == 7) {
            return "Jul";
        } else if (month == 8) {
            return "Aug";
        } else if (month == 9) {
            return "Sep";
        } else if (month == 10) {
            return "Oct";
        } else if (month == 11) {
            return "Nov";
        } else if (month == 12) {
            return "Dec";
        }
        return "";
    }

    public static void showToastMessage(Context context, String Msg) {
        Toast.makeText(context, Msg, Toast.LENGTH_LONG).show();
    }

    public static void showLog(String key, String value) {
        Logger.e(key + "          " + value);
    }

    public static boolean isNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            } else {
                Logger.e("NetworkInfo[]   " + info);
            }
        } else {
            Logger.e("ConnectivityManager   " + manager);
        }
        return false;
    }
}