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

import java.util.ArrayList;
import java.util.Collections;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private ArrayList<Appointment> appointments;
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
        appointments = new ArrayList<>();
        FBManager.getInstance().getFirebaseFirestore()
                .collection(FBManager.USERS)
                .document(FBManager.getInstance().getUserID())
                .collection(FBManager.APPOINTMENTS)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                        Log.d("empty", "onSuccess: LIST EMPTY");
                        return;
                    } else {
                        for (DocumentSnapshot ds : documentSnapshots.getDocuments()) {
                            appointment = ds.toObject(Appointment.class);
                            appointments.add(appointment);
                        }

                    }

                });
        Log.d("ptt", appointments.toString());
        Collections.sort(appointments, new TimeComperator());
        User.getInstance().setAppointments(appointments);
        if (User.getInstance().getAppointments() == null) {
            Toast.makeText(this, "No appointments found", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("ptt", User.getInstance().getAppointments().toString());
            Intent intent = new Intent(UserActivity.this, AppointmentsSummaryActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
