package com.example.barberapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.barberapp.R;
import com.example.barberapp.activities.TimeStampActivity;
import com.example.barberapp.databinding.FragmentCalendarBinding;
import com.example.barberapp.interfaces.Callback_timeStamp;
import com.example.barberapp.utils.FBManager;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    private View view;
    private FragmentCalendarBinding binding;
    private long eventOccursOn;
    private ArrayList<String> dateData, daysOff;
    private Callback_timeStamp callback_timeStamp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        view = binding.getRoot();
        daysOff = getArguments().getStringArrayList("days");


        binding.calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {

            int fixedMonth = month + 1;
            getRecordsFromFB(dayOfMonth + "", fixedMonth + "", year + "");
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            String day = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
            eventOccursOn = c.getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String selectedDate = sdf.format(new Date(eventOccursOn));
            if (daysOff.contains(day)) {
                Toast.makeText(view.getContext(), "This is an day off, Please pick another day", Toast.LENGTH_LONG).show();
                callback_timeStamp.getFlag(1);
            } else {
                callback_timeStamp.getDate(selectedDate, eventOccursOn);
                callback_timeStamp.getFlag(0);
            }

        });


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String selectedDate = sdf.format(new Date(binding.calendar.getDate()));
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(binding.calendar.getDate());
        String day = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if (daysOff.contains(day)) {
            Toast.makeText(view.getContext(), "This is an day off, Please pick another day", Toast.LENGTH_LONG).show();
            callback_timeStamp.getFlag(1);
        } else {
            callback_timeStamp.getDate(selectedDate, binding.calendar.getDate());
            callback_timeStamp.getFlag(0);
        }
        binding.calendar.setMinDate(binding.calendar.getDate());
        return view;
    }

    private void getRecordsFromFB(String day, String month, String year) {
        dateData = new ArrayList<>();
        FBManager.getInstance().getFirebaseFirestore()
                .collection(FBManager.CALENDAR)
                .document(year)
                .collection(month)
                .document(day)
                .collection(FBManager.APPOINTMENTS)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (documentSnapshots.isEmpty()) {
                    } else {
                        for (DocumentSnapshot ds : documentSnapshots.getDocuments()) {
                            dateData.add(ds.getString("time"));
                            dateData.add(String.valueOf(ds.getLong("record")));
                        }
                    }
                    callback_timeStamp.getRecords(dateData);


                });
    }


    public void setCallback_timeStamp(Callback_timeStamp callback_timeStamp) {
        this.callback_timeStamp = callback_timeStamp;
    }
}
