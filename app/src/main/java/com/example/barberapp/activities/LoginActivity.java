package com.example.barberapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityLoginBinding;
import com.example.barberapp.objects.User;
import com.example.barberapp.utils.AppManager;
import com.example.barberapp.utils.FBManager;
import com.example.barberapp.utils.SPManager;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private final String PASSWORD = "password";
    private final String EMAIL = "email";
    private boolean isPasswordValid = false;
    private boolean isEmailValid = false;
    private AppManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        manager = new AppManager(this);
        checkCredentials();
        initViews();

        //todo: login with google
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        binding.loginButton.setOnClickListener(v -> {

            if (isEmailValid && isPasswordValid) {
                FBManager.getInstance().getFirebaseAuth().signInWithEmailAndPassword(binding.loginEmailInput.getText().toString().trim(), binding.loginPasswordInput.getText().toString().trim())

                        .addOnSuccessListener(authResult -> getUserData())

                        .addOnFailureListener(e -> Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_LONG).show());

            } else {
                Toast.makeText(this, "Error! please check validation of email & password", Toast.LENGTH_LONG).show();
            }

        });

        binding.loginSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
        binding.loginCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String mail = binding.loginEmailInput.getText().toString().trim();
            String pass = binding.loginPasswordInput.getText().toString().trim();

            if (isChecked
                    && manager.validateEmail(this, binding.loginEmailInput, binding.loginEmailLayout)
                    && manager.validatePassword(this, binding.loginPasswordInput, binding.loginPasswordLayout)) {

                SPManager.getInstance().putString(EMAIL, mail);
                SPManager.getInstance().putString(PASSWORD, pass);
            }
        });

        binding.loginEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmailValid = manager.validateEmail(LoginActivity.this, binding.loginEmailInput, binding.loginEmailLayout);
            }
        });

        binding.loginPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isPasswordValid = manager.validatePassword(LoginActivity.this, binding.loginPasswordInput, binding.loginPasswordLayout);
            }
        });


    }


    private void checkCredentials() {
//        SPManager.getInstance().removeKey(EMAIL);
//        SPManager.getInstance().removeKey(PASSWORD);

        String email = SPManager.getInstance().getString(EMAIL, "NA");
        String password = SPManager.getInstance().getString(PASSWORD, "NA");

        if (!email.equals("NA") && !password.equals("NA")) {
            binding.loginEmailInput.setText(email);
            binding.loginPasswordInput.setText(password);
            isEmailValid = manager.validateEmail(this, binding.loginEmailInput, binding.loginEmailLayout);
            isPasswordValid = manager.validatePassword(this, binding.loginPasswordInput, binding.loginPasswordLayout);

        }

    }

    private void getUserData() {
        final DocumentReference docRef = FBManager.getInstance().getFirebaseFirestore()
                .collection("users")
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

                manager.moveToUserActivity(this);
            } else {
                Log.d("ptt", "user data: null");
            }
        });

    }


}