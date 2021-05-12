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
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    private View view;
    private FragmentCalendarBinding binding;
    private String curDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        view = binding.getRoot();
        binding.calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            month++;
            curDate = dayOfMonth + "/" + month + "/" + year;

            ((TimeStampActivity) getActivity()).getDate(curDate);
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String selectedDate = sdf.format(new Date(binding.calendar.getDate()));
        ((TimeStampActivity) getActivity()).getDate(selectedDate);


        return view;
    }


}
