package com.example.barberapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.barberapp.R;
import com.example.barberapp.activities.TimeStampActivity;
import com.example.barberapp.databinding.FragmentCalendarBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    private View view;
    private FragmentCalendarBinding binding;
    private String curDate;
    private long eventOccursOn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        view = binding.getRoot();

        binding.calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            int fixedMonth = month + 1;
            curDate = dayOfMonth + "/" + fixedMonth + "/" + year;
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            eventOccursOn = c.getTimeInMillis();
            ((TimeStampActivity) getActivity()).getDate(curDate, eventOccursOn);
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String selectedDate = sdf.format(new Date(binding.calendar.getDate()));
        ((TimeStampActivity) getActivity()).getDate(selectedDate, binding.calendar.getDate());
        binding.calendar.setMinDate(binding.calendar.getDate());

        return view;
    }


}
