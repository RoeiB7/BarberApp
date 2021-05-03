package com.example.barberapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.barberapp.R;

public class AppointmentActivity extends AppCompatActivity {
    private TextView appointment_select_treatment;
    private TextView appointment_select_barber;
    private TextView appointment_contact_number_input;
    private Button appointment_search_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        findViews();
        initViews();
    }

    private void findViews() {
        appointment_select_treatment = findViewById(R.id.appointment_select_treatment);
        appointment_select_barber = findViewById(R.id.appointment_select_barber);
        appointment_contact_number_input = findViewById(R.id.appointment_contact_number_input);
        appointment_search_button = findViewById(R.id.appointment_search_button);
    }

    private void initViews() {
        appointment_search_button.setOnClickListener(v -> {
            Intent intent = new Intent(this, TimeStampActivity.class);
            startActivity(intent);
        });
    }


}