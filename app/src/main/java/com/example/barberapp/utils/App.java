package com.example.barberapp.utils;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.barberapp.objects.User;
import com.google.android.gms.common.SignInButton;

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
