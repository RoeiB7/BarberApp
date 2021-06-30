package com.example.barberapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private ArrayList<String> dateData;
    private Callback_timeStamp callback_timeStamp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        view = binding.getRoot();

        binding.calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            int fixedMonth = month + 1;
            getRecordsFromFB(dayOfMonth + "", fixedMonth + "", year + "");
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            eventOccursOn = c.getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String selectedDate = sdf.format(new Date(eventOccursOn));
            Log.d("ptt", "date from calandar fragment after click " + selectedDate);
            callback_timeStamp.getDate(selectedDate, eventOccursOn);


        });


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String selectedDate = sdf.format(new Date(binding.calendar.getDate()));
        Log.d("ptt", "date from calendar fragment:" + selectedDate);
        callback_timeStamp.getDate(selectedDate, binding.calendar.getDate());
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
