package com.infojaalprime.hrms.interfaces;

import java.util.ArrayList;
import java.util.List;

public interface SendListToActivityFromAdapter {

    void onSendData(ArrayList<String> al_is_half_day_1, ArrayList<String> al_half_day_status_id_1, ArrayList<String> al_half_day_club_id_1, String days);
}