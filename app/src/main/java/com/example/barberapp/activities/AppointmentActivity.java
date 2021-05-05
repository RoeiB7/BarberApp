package com.example.barberapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityAppointmentBinding;

public class AppointmentActivity extends AppCompatActivity {


    private ActivityAppointmentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment);
        initViews();
    }

    private void initViews() {
        binding.appointmentSearchButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TimeStampActivity.class);
            startActivity(intent);
        });
    }

    //todo: add count number for amout of time of treatment in design(TextView)

}