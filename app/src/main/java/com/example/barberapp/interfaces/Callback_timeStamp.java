package com.example.barberapp.interfaces;

import java.util.ArrayList;

public interface Callback_timeStamp {
    void getRecords(ArrayList<String> arrayList);

    void getDate(String date, long _miliDate);

    void getFlag(int flag);
}
