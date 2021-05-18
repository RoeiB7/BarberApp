package com.example.barberapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityTimeStampBinding;
import com.example.barberapp.fragments.CalendarFragment;
import com.example.barberapp.fragments.HoursFragment;
import com.example.barberapp.objects.Appointment;
import com.example.barberapp.objects.User;
import com.example.barberapp.utils.FBManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class TimeStampActivity extends AppCompatActivity {

    private CalendarFragment calendarFragment;
    private HoursFragment hoursFragment;
    private ActivityTimeStampBinding binding;
    private String chosenHour, chosenDate, barberName;
    private ArrayList<String> treatments;
    private long miliDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_stamp);
        calendarFragment = new CalendarFragment();
        hoursFragment = new HoursFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_upper_fragment, calendarFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_lower_fragment, hoursFragment).commit();
    }

    public void openBooking() {
        String contactNumber = User.getInstance().getContactNumber();
        barberName = getIntent().getStringExtra("barber");
        treatments = getIntent().getStringArrayListExtra("treatments");
        String treatmentsList = Arrays.toString(treatments.toArray()).replace("[", "").replace("]", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );
        builder.setTitle("Appointment Summary");
        builder.setMessage("\n" + "Treatments: " + treatmentsList + "\n\n"
                + "Barber: " + barberName + "\n\n"
                + "Contact Number: " + contactNumber + "\n\n"
                + "Date: " + chosenDate + "\n\n"
                + "Time: " + chosenHour);
        builder.setCancelable(false);
        builder.setPositiveButton("Book", (dialog, which) -> {

            saveAppointmentToFB();


        });
        builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(this, "Appointment canceled!", Toast.LENGTH_SHORT).show());
        builder.show();
    }

    public void getDate(String date, long _miliDate) {
        chosenDate = date;
        miliDate = _miliDate;

    }

    public void getHour(String hour) {
        chosenHour = hour;
    }


    private void saveAppointmentToFB() {
        DocumentReference documentReference = FBManager.getInstance().getFirebaseFirestore()
                .collection("users")
                .document(FBManager.getInstance().getUserID())
                .collection("appointments")
                .document(chosenDate.replace('/', '.') + " " + chosenHour);


        Appointment appointment = new Appointment(
                barberName, chosenDate, chosenHour, miliDate, treatments

        );

        documentReference.set(appointment).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Toast.makeText(this, "Appointment booked!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to book!", Toast.LENGTH_SHORT).show();
            }
        });


    }

}