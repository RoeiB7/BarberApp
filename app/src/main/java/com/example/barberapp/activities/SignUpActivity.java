package com.example.barberapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivitySignUpBinding;
import com.example.barberapp.utils.AppManager;
import com.example.barberapp.utils.FBManager;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private boolean isPasswordValid = false;
    private boolean isNameValid = false;
    private boolean isEmailValid = false;
    private boolean isRePasswordValid = false;
    private boolean isPhoneValid = false;
    private AppManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        manager = new AppManager(this);
        initViews();
    }

    private void initViews() {
        binding.signUpButton.setOnClickListener(v -> {
            if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid && isRePasswordValid) {
                FBManager.getInstance().getFirebaseAuth().createUserWithEmailAndPassword(binding.signUpEmailInput.getText().toString().trim(), binding.signUpPasswordInput.getText().toString().trim())
                        .addOnSuccessListener(authResult -> {
                            addUserToFB();
                            Intent intent = new Intent(this, UserActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_LONG).show());

            } else {
                Toast.makeText(this, "Error! please check validation of fields", Toast.LENGTH_LONG).show();

            }

        });

        binding.signUpLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        binding.signUpFNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isNameValid = validateName();
            }
        });

        binding.signUpEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmailValid = manager.validateEmail(SignUpActivity.this, binding.signUpEmailInput, binding.signUpEmailLayout);
            }
        });

        binding.signUpPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isPasswordValid = manager.validatePassword(SignUpActivity.this, binding.signUpPasswordInput, binding.signUpPasswordLayout);
            }
        });

        binding.signUpRePasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isRePasswordValid = validateRePassword();
            }
        });

        binding.signUpContactNumberInput.addTextChangedListener(new TextWatcher() {
            int keyDel;


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.signUpContactNumberInput.setOnKeyListener((v, keyCode, event) -> {

                    if (keyCode == KeyEvent.KEYCODE_DEL)
                        keyDel = 1;
                    return false;
                });

                if (keyDel == 0) {
                    int len = binding.signUpContactNumberInput.getText().length();
                    if (len == 3) {
                        binding.signUpContactNumberInput.setText(binding.signUpContactNumberInput.getText().toString() + " - ");
                        binding.signUpContactNumberInput.setSelection(binding.signUpContactNumberInput.getText().length());
                    }
                } else {
                    keyDel = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                isPhoneValid = manager.validatePhone(SignUpActivity.this,
                        binding.signUpContactNumberInput, binding.signUpContactNumberLayout);

            }
        });

    }

    private void addUserToFB() {
        DocumentReference documentReference = FBManager.getInstance().getFirebaseFirestore().collection("users").document(FBManager.getInstance().getUserID());
        Map<String, Object> user = new HashMap<>();
        user.put("First Name", binding.signUpFNameInput.getText().toString().trim());
        user.put("Last Name", binding.signUpLNameInput.getText().toString().trim());
        user.put("Email", binding.signUpEmailInput.getText().toString().trim());
        user.put("Contact Number", binding.signUpContactNumberInput.getText().toString().trim());

        documentReference.set(user)
                .addOnSuccessListener(unused -> Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show())

                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create user! Please try again", Toast.LENGTH_LONG).show());

    }

    private boolean validateRePassword() {
        if (binding.signUpRePasswordInput.getText().toString().trim().equals(binding.signUpPasswordInput.getText().toString().trim())) {
            binding.signUpRePasswordLayout.setErrorEnabled(false);
            return true;
        } else {
            binding.signUpRePasswordLayout.setError("Re-password must be identical to password");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        }
    }

    private boolean validateName() {
        if (binding.signUpFNameInput.getText().toString().trim().length() < 2) {
            binding.signUpFNameLayout.setError("First name can't be less than 2 characters");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        } else {
            binding.signUpFNameLayout.setErrorEnabled(false);
            return true;
        }
    }

}