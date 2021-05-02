package com.example.barberapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.barberapp.R;
import com.example.barberapp.fragments.CalendarFragment;

public class ContainerActivity extends AppCompatActivity {
    private CalendarFragment calendarFragment;
    private FrameLayout container_upper_fragment;
    private FrameLayout container_lower_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        findViews();
        calendarFragment = new CalendarFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_upper_fragment, calendarFragment).commit();
    }

    private void findViews() {
        container_upper_fragment = findViewById(R.id.container_upper_fragment);
        container_lower_fragment = findViewById(R.id.container_lower_fragment);
    }
}