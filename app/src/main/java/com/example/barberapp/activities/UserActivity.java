package com.example.barberapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        initViews();
    }

    private void initViews() {

        binding.userNewAppointmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AppointmentActivity.class);
            startActivity(intent);
        });
    }
}