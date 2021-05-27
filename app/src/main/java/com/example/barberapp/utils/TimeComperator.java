package com.example.barberapp.utils;

import com.example.barberapp.objects.Appointment;

import java.util.Comparator;

public class TimeComperator implements Comparator<Appointment> {

    @Override
    public int compare(Appointment o1, Appointment o2) {
        long time1 = o1.getAppointmentTime();
        long time2 = o2.getAppointmentTime();
        return Long.compare(time1, time2);
    }
}
