package com.example.barberapp.objects;

import java.util.ArrayList;

public class Appointment {

    String barberName, date, hour;
    long appointmentTime;

    public Appointment(String barberName, String date, String hour, long appointmentTime, ArrayList<String> treatments) {

        this.barberName = barberName;
        this.date = date;
        this.hour = hour;
        this.appointmentTime = appointmentTime;
        this.treatments = treatments;
    }


    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public long getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(long appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public ArrayList<String> getTreatments() {
        return treatments;
    }

    public void setTreatments(ArrayList<String> treatments) {
        this.treatments = treatments;
    }

    ArrayList<String> treatments;
}
