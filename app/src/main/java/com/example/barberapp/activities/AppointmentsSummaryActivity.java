package com.example.barberapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityAppointmentsSummaryBinding;
import com.example.barberapp.fragments.ActiveAppointmentsFragment;
import com.example.barberapp.fragments.CalendarFragment;
import com.example.barberapp.fragments.HoursFragment;

public class AppointmentsSummaryActivity extends AppCompatActivity {
    private ActivityAppointmentsSummaryBinding binding;
    private ActiveAppointmentsFragment active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointments_summary);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_first_fragment, active).commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container_second_fragment, hoursFragment).commit();
    }
}