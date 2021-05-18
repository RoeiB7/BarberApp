package com.example.barberapp.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivitySignUpBinding;
import com.example.barberapp.objects.User;
import com.example.barberapp.utils.AppManager;
import com.example.barberapp.utils.FBManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private boolean isPasswordValid = false;
    private boolean isNameValid = false;
    private boolean isEmailValid = false;
    private boolean isPhoneValid = false;
    private AppManager manager;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference fileReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        manager = new AppManager(this);
        initViews();
    }

    private void initViews() {
        binding.signUpButton.setOnClickListener(v -> {
            if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid) {
                User.getInstance().setFirstName(binding.signUpFNameInput.getText().toString().trim());
                User.getInstance().setLastName(binding.signUpLNameInput.getText().toString().trim());
                User.getInstance().setEmail(binding.signUpEmailInput.getText().toString().trim());
                User.getInstance().setContactNumber(binding.signUpContactNumberInput.getText().toString().trim());
                FBManager.getInstance().getFirebaseAuth().createUserWithEmailAndPassword(binding.signUpEmailInput.getText().toString().trim(), binding.signUpPasswordInput.getText().toString().trim())
                        .addOnSuccessListener(authResult -> {
                            addUserToFB();
                            uploadImage();
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

        binding.signUpBTNUploadImage.setOnClickListener(v -> openFileChooser());

    }

    private void addUserToFB() {
        DocumentReference documentReference = FBManager.getInstance().getFirebaseFirestore().collection("users").document(FBManager.getInstance().getUserID());
        Map<String, Object> user = new HashMap<>();
        user.put("First Name", binding.signUpFNameInput.getText().toString().trim());
        user.put("Last Name", binding.signUpLNameInput.getText().toString().trim());
        user.put("Email", binding.signUpEmailInput.getText().toString().trim());
        user.put("Contact Number", binding.signUpContactNumberInput.getText().toString().trim());

        documentReference.set(user)
                .addOnSuccessListener(unused -> {
                    manager.moveToUserActivity(this);
                    Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
                })

                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create user! Please try again", Toast.LENGTH_LONG).show());

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

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage() {

        if (mImageUri != null) {
            fileReference = FBManager.getInstance().getStorageReference().child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    DocumentReference documentReference = FBManager.getInstance().getFirebaseFirestore()
                            .collection("users").document(FBManager.getInstance().getUserID());

                    documentReference.update("Profile Pic", downloadUri.toString());
                    User.getInstance().setImageUri(downloadUri.toString());

                } else {
                    Toast.makeText(SignUpActivity.this, "upload failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            DocumentReference documentReference = FBManager.getInstance().getFirebaseFirestore()
                    .collection("users").document(FBManager.getInstance().getUserID());

            documentReference.update("Profile Pic", "N/A");
        }

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mImageUri != null) {
                Glide.with(this).load(mImageUri).apply(RequestOptions.circleCropTransform()).into(binding.signUpIMGAddProfilePic);
            }
        }

    }


}