package com.example.barberapp.utils;

import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AppManager {

    public AppManager(AppCompatActivity activity) {


    }


    public boolean validatePassword(AppCompatActivity activity, TextInputEditText input, TextInputLayout layout) {
        if (input.getText().toString().trim().isEmpty()) {
            layout.setError("Password is required");
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        } else if (input.getText().toString().length() < 8) {
            layout.setError("Password can't be less than 8 digit");
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        } else {
            layout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateEmail(AppCompatActivity activity, TextInputEditText input, TextInputLayout layout) {
        if (input.getText().toString().trim().isEmpty()) {
            layout.setErrorEnabled(false);
        } else {
            String emailId = input.getText().toString();
            boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
            if (!isValid) {
                layout.setError("Invalid Email address, ex: abc@example.com");
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                return false;
            } else {
                layout.setErrorEnabled(false);
            }
        }
        return true;
    }


}
