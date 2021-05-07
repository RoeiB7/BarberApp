package com.example.barberapp.utils;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barberapp.R;

public class AppManager {
    private boolean visibility = false;


    public AppManager(AppCompatActivity activity) {


    }

    public boolean showEye(EditText editText) {
        if (isEmpty(editText)) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
            return true;
        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return false;
        }
    }


    public void switchPasswordVisibility(EditText editText) {
        if (!visibility) {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0);
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            visibility = true;
        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            visibility = false;
        }
    }

    public boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() != 0;
    }


}
