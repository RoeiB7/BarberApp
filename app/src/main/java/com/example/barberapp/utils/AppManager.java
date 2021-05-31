package com.example.barberapp.utils;

import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barberapp.activities.UserActivity;
import com.example.barberapp.objects.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

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

    public boolean validatePhone(AppCompatActivity activity, TextInputEditText input, TextInputLayout layout) {
        int len = input.getText().toString().trim().length();
        if (len != 13) {
            layout.setError("Your contact number must contain 10 digits");
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        } else {
            layout.setErrorEnabled(false);
            return true;
        }

    }

    public void moveToUserActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, UserActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void getUserData(AppCompatActivity activity) {
        final DocumentReference docRef = FBManager.getInstance().getFirebaseFirestore()
                .collection(FBManager.USERS)
                .document(FBManager.getInstance().getUserID());
        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.d("ptt", "user data listen failed.");
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                Map<String, Object> userData = snapshot.getData();
                User.getInstance().setFirstName((String) userData.get("First Name"));
                User.getInstance().setLastName((String) userData.get("Last Name"));
                User.getInstance().setEmail((String) userData.get("Email"));
                User.getInstance().setContactNumber((String) userData.get("Contact Number"));
                User.getInstance().setImageUri((String) userData.get("Profile Pic"));

                moveToUserActivity(activity);
            } else {
                Log.d("ptt", "user data: null");
            }
        });

    }


}
