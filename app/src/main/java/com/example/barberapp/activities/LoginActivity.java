package com.example.barberapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.barberapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText login_email_input;
    private TextInputEditText login_password_input;
    private CheckBox login_checkbox;
    private TextView login_forgot_password;
    private Button login_button;
    private Button login_google_button;
    private TextView login_sign_up;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initViews();
    }

    private void findViews() {
        login_email_input = findViewById(R.id.login_email_input);
        login_password_input = findViewById(R.id.login_password_input);
        login_checkbox = findViewById(R.id.login_checkbox);
        login_forgot_password = findViewById(R.id.login_forgot_password);
        login_button = findViewById(R.id.login_button);
        login_google_button = findViewById(R.id.login_google_button);
        login_sign_up = findViewById(R.id.login_sign_up);

    }

    private void initViews() {
        login_button.setOnClickListener(v -> {
            //todo: check valid email & password before moving to next page

            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
            finish();
        });

    }
}