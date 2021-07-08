package com.example.barberapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityAppointmentsSummaryBinding;
import com.example.barberapp.fragments.ActiveAppointmentsFragment;
import com.example.barberapp.fragments.PastAppointmentFragment;

public class AppointmentsSummaryActivity extends AppCompatActivity {
    private ActivityAppointmentsSummaryBinding binding;
    private ActiveAppointmentsFragment active;
    private PastAppointmentFragment past;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointments_summary);
        active = new ActiveAppointmentsFragment();
        past = new PastAppointmentFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_first_fragment, active).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_second_fragment, past).commit();

    }
}