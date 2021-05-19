package com.example.barberapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityLoginBinding;
import com.example.barberapp.objects.User;
import com.example.barberapp.utils.AppManager;
import com.example.barberapp.utils.FBManager;
import com.example.barberapp.utils.SPManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 420;
    private ActivityLoginBinding binding;
    private final String PASSWORD = "password";
    private final String EMAIL = "email";
    private boolean isPasswordValid = false;
    private boolean isEmailValid = false;
    private AppManager manager;
    private GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        manager = new AppManager(this);

        if (FBManager.getInstance().getFirebaseAuth().getCurrentUser() != null) {
            getUserData();
        }

        initGoogleClient();
        initViews();

        //todo: forgot password
        //todo: add splash screen with animation
        //todo: login with admin credentials open admin page with all admin options
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

        binding.signInBtn.setOnClickListener(v -> signIn());


    }


    private boolean checkCredentials() {
//        SPManager.getInstance().removeKey(EMAIL);
//        SPManager.getInstance().removeKey(PASSWORD);

        String email = SPManager.getInstance().getString(EMAIL, "NA");
        String password = SPManager.getInstance().getString(PASSWORD, "NA");

        if (!email.equals("NA") && !password.equals("NA")) {
            binding.loginEmailInput.setText(email);
            binding.loginPasswordInput.setText(password);
            isEmailValid = manager.validateEmail(this, binding.loginEmailInput, binding.loginEmailLayout);
            isPasswordValid = manager.validatePassword(this, binding.loginPasswordInput, binding.loginPasswordLayout);
            return true;

        } else {
            return false;
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

    private void initGoogleClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Exception exception = task.getException();
            if (task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("ptt", "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("ptt", "Google sign in failed", e);
                }
            } else {
                Log.w("ptt", exception.toString());
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FBManager.getInstance().getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("ptt", "signInWithCredential:success");
                        addUserToFB();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("ptt", "signInWithCredential:failure", task.getException());
                    }
                });
    }


    private void addUserToFB() {
        DocumentReference documentReference = FBManager.getInstance().getFirebaseFirestore().collection("users").document(FBManager.getInstance().getUserID());
        Map<String, Object> user = new HashMap<>();
        String fullName = FBManager.getInstance().getFirebaseAuth().getCurrentUser().getDisplayName();
        String firstName;
        String lastName = "";
        String email;
        String contactNumber;
        String profilePic;

        if (fullName.split("\\w+").length > 1) {
            firstName = fullName.substring(0, fullName.lastIndexOf(' '));
            lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
        } else {
            firstName = fullName;
        }
        email = FBManager.getInstance().getFirebaseAuth().getCurrentUser().getEmail();
        contactNumber = FBManager.getInstance().getFirebaseAuth().getCurrentUser().getPhoneNumber();
        profilePic = FBManager.getInstance().getFirebaseAuth().getCurrentUser().getPhotoUrl().toString();
        if (contactNumber == null) {
            contactNumber = "N/A";
        }

        user.put("First Name", firstName);
        user.put("Last Name", lastName);
        user.put("Email", email);
        user.put("Contact Number", contactNumber);
        user.put("Profile Pic", profilePic);

        User.getInstance().setFirstName(firstName);
        User.getInstance().setLastName(lastName);
        User.getInstance().setEmail(email);
        User.getInstance().setContactNumber(contactNumber);
        User.getInstance().setImageUri(profilePic);

        documentReference.set(user)
                .addOnSuccessListener(unused -> {
                    manager.moveToUserActivity(this);
                })

                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create user! Please try again", Toast.LENGTH_LONG).show());

    }
}