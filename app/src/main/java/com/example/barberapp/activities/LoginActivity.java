package com.example.barberapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityLoginBinding;
import com.example.barberapp.utils.AppManager;
import com.example.barberapp.utils.SPManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private final String PASSWORD = "password";
    private final String EMAIL = "email";
    private boolean eye = false;
    private boolean crossEye = false;
    private final int DRAWABLE_RIGHT = 2;
    private AppManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        manager = new AppManager(this);
        checkCredentials();
        initViews();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        binding.loginButton.setOnClickListener(v -> {
            if (!manager.isEmpty(binding.loginEmailInput)) {
                binding.loginEmailInput.setError("Email is required");
                Log.d("ptt","email is empty");
                return;
            }

            if (!manager.isEmpty(binding.loginPasswordInput)) {
                binding.loginPasswordInput.setError("Password is required");
                return;
            }

//            fAuth.signInWithEmailAndPassword(binding.loginEmailInput.getText().toString().trim(), binding.loginPasswordInput.getText().toString().trim())
//
//                    .addOnSuccessListener(authResult -> {
//                        manager.moveToNav(this);
//                    })
//
//                    .addOnFailureListener(e -> Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show());


            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
            finish();
        });

        binding.loginSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
        binding.loginCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && manager.isEmpty(binding.loginEmailInput)
                    && manager.isEmpty(binding.loginPasswordInput)
                    && binding.loginEmailInput.getText().toString().contains("@")
                    && binding.loginPasswordInput.getText().toString().length() > 7) {
                String mail = binding.loginEmailInput.getText().toString().trim();
                String pass = binding.loginPasswordInput.getText().toString().trim();
                SPManager.getInstance().putString(EMAIL, mail);
                SPManager.getInstance().putString(PASSWORD, pass);
            }
        });
        binding.loginPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count != before && !crossEye) {
                    eye = manager.showEye(binding.loginPasswordInput);
                    crossEye = false;
                } else {
                    crossEye = true;
                    if (s.toString().isEmpty()) {
                        eye = manager.showEye(binding.loginPasswordInput);
                        binding.loginPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        crossEye = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.loginPasswordInput.setOnTouchListener((v, event) -> {
            if (eye) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= (binding.loginPasswordInput.getWidth() - binding.loginPasswordInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        manager.switchPasswordVisibility(binding.loginPasswordInput);
                        return true;
                    }
                }
            }
            return false;
        });


    }


    private void checkCredentials() {
        String email = SPManager.getInstance().getString(EMAIL, "NA");
        String password = SPManager.getInstance().getString(PASSWORD, "NA");

        if (!email.equals("NA") && !password.equals("NA")) {
            binding.loginEmailInput.setText(email);
            binding.loginPasswordInput.setText(password);
        }

    }
}