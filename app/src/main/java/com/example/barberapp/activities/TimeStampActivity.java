package com.example.barberapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityTimeStampBinding;
import com.example.barberapp.fragments.CalendarFragment;
import com.example.barberapp.fragments.HoursFragment;

public class TimeStampActivity extends AppCompatActivity {

    private CalendarFragment calendarFragment;
    private HoursFragment hoursFragment;
    private ActivityTimeStampBinding binding;


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
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );
        builder.setTitle("Book appointment");
        builder.setMessage("Are you sure you want to book?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            //todo:get treatments, barber name, contact number from Appointment Activity to here
            //todo:get date and time from Calendar & Hours fragments
            //todo:save to firebase for my user - treatments, barber name, contact number, date, time for each user
            Toast.makeText(this, "appointment booked!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }


}