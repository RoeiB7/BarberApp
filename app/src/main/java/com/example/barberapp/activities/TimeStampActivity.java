package com.example.barberapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityTimeStampBinding;
import com.example.barberapp.fragments.CalendarFragment;
import com.example.barberapp.fragments.HoursFragment;
import com.example.barberapp.interfaces.Callback_timeStamp;
import com.example.barberapp.objects.Appointment;
import com.example.barberapp.objects.User;
import com.example.barberapp.utils.FBManager;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TimeStampActivity extends AppCompatActivity {

    private CalendarFragment calendarFragment;
    private HoursFragment hoursFragment;
    private ActivityTimeStampBinding binding;
    private String chosenHour, chosenDate, barberName, contactNumber, day, month, year;
    private ArrayList<String> treatments;
    private long miliDate;
    private int recordsToRemove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_stamp);
        recordsToRemove = (int) (getIntent().getLongExtra("time", -1) / 10);

        calendarFragment = new CalendarFragment();
        calendarFragment.setCallback_timeStamp(callback_timeStamp);
        getSupportFragmentManager().beginTransaction().add(R.id.container_upper_fragment, calendarFragment).commit();

        hoursFragment = new HoursFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("records", recordsToRemove);
        Log.d("ptt", "date send to bundle" + chosenDate);
        bundle.putString("current_date", chosenDate);
        hoursFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container_lower_fragment, hoursFragment).commit();
    }

    public void openBooking() {
        contactNumber = User.getInstance().getContactNumber();
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
        Log.d("ptt", "date from getDate:" + chosenDate);
        String[] dmy = chosenDate.split("/");
        day = dmy[0];
        month = dmy[1];
        year = dmy[2];
        miliDate = _miliDate;
    }

    public void getHour(String hour) {
        chosenHour = hour;
    }


    private void saveAppointmentToFB() {
        DocumentReference appointmentDoc = FBManager.getInstance().getFirebaseFirestore()
                .collection(FBManager.USERS)
                .document(FBManager.getInstance().getUserID())
                .collection(FBManager.APPOINTMENTS)
                .document(chosenDate.replace('/', '.') + " " + chosenHour);


        Appointment appointment = new Appointment(
                barberName, chosenDate, chosenHour, miliDate, treatments

        );

        appointmentDoc.set(appointment).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Toast.makeText(this, "Appointment booked!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to book!", Toast.LENGTH_SHORT).show();
            }
        });

        DocumentReference userDoc = FBManager.getInstance().getFirebaseFirestore()
                .collection(FBManager.USERS)
                .document(FBManager.getInstance().getUserID());

        userDoc.update("Contact Number", contactNumber);


        DocumentReference dateDoc = FBManager.getInstance().getFirebaseFirestore()
                .collection(FBManager.CALENDAR)
                .document(year)
                .collection(month)
                .document(day)
                .collection(FBManager.APPOINTMENTS)
                .document();


        Map<String, Object> map = new HashMap<>();
        map.put("record", recordsToRemove);
        map.put("time", chosenHour);

        dateDoc.set(map).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Log.d("ptt", "appointment saved to calendar");
            } else {
                Log.d("ptt", "failed to save appointment ot calendar");
            }
        });

    }

    private final Callback_timeStamp callback_timeStamp = new Callback_timeStamp() {

        @Override
        public void getRecords(ArrayList<String> arrayList) {
            hoursFragment.setRecords(arrayList);
        }
    };
}