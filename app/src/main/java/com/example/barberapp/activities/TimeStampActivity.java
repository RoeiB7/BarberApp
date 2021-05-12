package com.example.barberapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityTimeStampBinding;
import com.example.barberapp.fragments.CalendarFragment;
import com.example.barberapp.fragments.HoursFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class TimeStampActivity extends AppCompatActivity {

    private CalendarFragment calendarFragment;
    private HoursFragment hoursFragment;
    private ActivityTimeStampBinding binding;
    private String chosenHour, chosenDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_stamp);
        calendarFragment = new CalendarFragment();
        hoursFragment = new HoursFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_upper_fragment, calendarFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_lower_fragment, hoursFragment).commit();
    }

    public void openBooking() {
        String barberName = getIntent().getStringExtra("barber");
        String contactNumber = getIntent().getStringExtra("phone");
        ArrayList<String> treatments = getIntent().getStringArrayListExtra("treatments");
        String treatmentsList = Arrays.toString(treatments.toArray()).replace("[", "").replace("]", "");

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );
        builder.setTitle("Appointment Summary");
        builder.setMessage("\n" + "Treatments: " + treatmentsList + "\n\n"
                + "Barber: " + barberName + "\n\n"
                + "Contact Number: " + contactNumber + "\n\n"
                + "Date: " + chosenDate + "\n\n"
                + "Time: " + chosenHour);
        builder.setCancelable(false);
        builder.setPositiveButton("Book", (dialog, which) -> {
            //todo:save to firebase for my user - treatments, barber name, contact number, date, time for each user
            Toast.makeText(this, "Appointment booked!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            Toast.makeText(this, "Appointment canceled!", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    public void getDate(String date) {
        chosenDate = date;
    }

    public void getHour(String hour) {
        chosenHour = hour;
    }


}