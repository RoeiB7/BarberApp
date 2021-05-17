package com.example.barberapp.utils;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.barberapp.objects.User;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SPManager.init(this);
        FBManager.init();
        User.init();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }
}
