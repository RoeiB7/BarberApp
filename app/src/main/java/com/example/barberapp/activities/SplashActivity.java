package com.example.barberapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivitySplashBinding;
import com.example.barberapp.utils.AppManager;
import com.example.barberapp.utils.FBManager;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private AppManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        manager = new AppManager(this);
        binding.lottieSplash.animate().translationY(1400).setDuration(3000).setStartDelay(2000);

        new Handler().postDelayed(() -> {
            if (FBManager.getInstance().getFirebaseAuth().getCurrentUser() != null) {
                manager.getUserData(this);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);

    }
}