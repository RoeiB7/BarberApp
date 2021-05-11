package com.example.barberapp.utils;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barberapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AppManager {
    private boolean visibility = false;


    public AppManager(AppCompatActivity activity) {


    }

    public boolean showEye(TextInputLayout layout, TextInputEditText editText) {
        if (isEmpty(editText)) {
            layout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            return true;
        } else {
            layout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
            return false;
        }
    }


    public boolean isEmpty(TextInputEditText editText) {
        return editText.getText().toString().trim().length() != 0;
    }


}
