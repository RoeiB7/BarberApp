package com.example.barberapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPManager {
    private static SPManager instance;
    private SharedPreferences prefs;

    private SPManager(Context context) {
        prefs = context.getSharedPreferences("com.example.barberapp", Context.MODE_PRIVATE);
    }

    public static SPManager getInstance() {
        return instance;
    }


    public static void init(Context context) {
        if (instance == null) {
            instance = new SPManager(context.getApplicationContext());
        }
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String def) {
        return prefs.getString(key, def);
    }

    public void removeKey(String key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

}
