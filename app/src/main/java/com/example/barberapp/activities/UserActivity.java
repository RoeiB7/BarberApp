package com.example.barberapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityUserBinding;
import com.example.barberapp.objects.Appointment;
import com.example.barberapp.objects.User;
import com.example.barberapp.utils.FBManager;
import com.example.barberapp.utils.TimeComperator;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private ArrayList<Appointment> activeAppointments;
    private ArrayList<Appointment> pastAppointments;
    private Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        initViews();


    }

    private void initViews() {
        binding.userName.setText("Hi " + User.getInstance().getFirstName() + "!");
        if (User.getInstance().getImageUri().equals("N/A")) {
            Glide

                    .with(this)
                    .load(R.drawable.ic_user)
                    .apply(new RequestOptions().override(500, 500))
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.userImage);
        } else {
            Glide

                    .with(this)
                    .load(User.getInstance().getImageUri())
                    .apply(new RequestOptions().override(500, 500))
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.userImage);
        }
        binding.userNewAppointmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AppointmentActivity.class);
            startActivity(intent);
        });

        binding.userEditContactNumberButton.setOnClickListener(v -> {
            final EditText contactNumber = new EditText(v.getContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    this
            );
            builder.setView(contactNumber);
            builder.setTitle("Set New Contact number");
            builder.setMessage("\n" + "Please enter your number below:");
            builder.setCancelable(true);
            builder.setPositiveButton("Edit", (dialog, which) -> {
                String number = contactNumber.getText().toString().trim();
                User.getInstance().setContactNumber(number);
                DocumentReference userDoc = FBManager.getInstance().getFirebaseFirestore()
                        .collection(FBManager.USERS)
                        .document(FBManager.getInstance().getUserID());

                userDoc.update("Contact Number", number);

            });
            builder.setNegativeButton("Cancel", null);
            builder.show();

        });
        binding.userMyAppointmentsButton.setOnClickListener(v -> {
            createList();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_bar_logout) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Log out")
                    .setMessage("Do you wish to log out from your account?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FBManager.getInstance().getFirebaseAuth().signOut();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createList() {
        activeAppointments = new ArrayList<>();
        pastAppointments = new ArrayList<>();
        FBManager.getInstance().getFirebaseFirestore()
                .collection(FBManager.USERS)
                .document(FBManager.getInstance().getUserID())
                .collection(FBManager.APPOINTMENTS)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Intent intent = new Intent(UserActivity.this, AppointmentsSummaryActivity.class);
                        startActivity(intent);
                    } else {
                        for (DocumentSnapshot ds : documentSnapshots.getDocuments()) {
                            appointment = ds.toObject(Appointment.class);
                            if (System.currentTimeMillis() > appointment.getAppointmentTime()) {
                                pastAppointments.add(appointment);
                            } else {
                                activeAppointments.add(appointment);
                            }
                        }
                    }
                    Collections.sort(activeAppointments, new TimeComperator());
                    Collections.sort(pastAppointments, new TimeComperator());
                    User.getInstance().setActiveAppointments(activeAppointments);
                    User.getInstance().setPastAppointments(pastAppointments);
                    if (activeAppointments.isEmpty() && pastAppointments.isEmpty()) {
                        Toast.makeText(this, "No appointments found", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(UserActivity.this, AppointmentsSummaryActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
