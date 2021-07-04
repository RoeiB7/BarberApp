package com.example.barberapp.objects;

import java.util.ArrayList;

public class Appointment {

    private String barberName, date, hour, contactNumber, clientName;
    private long appointmentTime;

    public Appointment() {

    }

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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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
