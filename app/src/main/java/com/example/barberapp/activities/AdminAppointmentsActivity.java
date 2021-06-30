package com.example.barberapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityAdminAppointmentsBinding;

public class AdminAppointmentsActivity extends AppCompatActivity {

    private ActivityAdminAppointmentsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_appointments);
        setFilters();
    }

    private void setFilters() {
        String[] months = getResources().getStringArray(R.array.months);
        String[] years = getResources().getStringArray(R.array.years);
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, months);
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, years);
        binding.adminMonthFilter.setAdapter(monthsAdapter);
        binding.adminYearFilter.setAdapter(yearsAdapter);
        //todo: finish admin all appointment
    }


}