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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.barberapp.R;
import com.example.barberapp.activities.TimeStampActivity;
import com.example.barberapp.databinding.FragmentHoursBinding;
import com.example.barberapp.adapters.AdapterHours;

import java.util.ArrayList;
import java.util.List;

public class HoursFragment extends Fragment {

    private View view;
    private FragmentHoursBinding binding;
    private AdapterHours adapter;
    private List<String> hours, records = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hours, container, false);
        view = binding.getRoot();
        createList();
        return view;

    }

    private void createList() {
        hours = new ArrayList<>();
        for (int i = 9; i < 20; i++) {
            for (int j = 0; j < 60; j = j + 10) {
                if (j == 0) {
                    hours.add(i + ":00");
                } else {
                    hours.add(i + ":" + j);
                }
            }
        }

        adapter = new AdapterHours(view.getContext(), hours);

        adapter.setClickListener((view, position) -> {
            ((TimeStampActivity) getActivity()).getHour(hours.get(position));
            ((TimeStampActivity) getActivity()).openBooking();
            Log.d("ptt", "Records from calendar" + records.toString());

        });


        binding.hoursList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.hoursList.setAdapter(adapter);


    }

    public void setRecords(ArrayList<String> arrayList) {
        records = arrayList;
        updateList();
        Log.d("ptt", "records in setRecords = " + records.toString());
    }

    private void updateList() {
        Log.d("ptt", "Records from calendar" + records.toString());
        if (!records.isEmpty()) {
            Log.d("ptt", "here");
            syncLists();
        } else {
            hours.clear();
            for (int i = 9; i < 20; i++) {
                for (int j = 0; j < 60; j = j + 10) {
                    if (j == 0) {
                        hours.add(i + ":00");
                    } else {
                        hours.add(i + ":" + j);
                    }
                }
            }
        }
        adapter = new AdapterHours(view.getContext(), hours);
        //todo: fix the IF statement to ensure customer cant book appointment that takes too long
        adapter.setClickListener((view, position) -> {
            if (hours.get(position)) {
                ((TimeStampActivity) getActivity()).getHour(hours.get(position));
                ((TimeStampActivity) getActivity()).openBooking();
                Log.d("ptt", "Records from calendar" + records.toString());
            } else {
                Toast.makeText(view.getContext(), "Appointment is too long for this hour", Toast.LENGTH_SHORT).show();
            }


        });


        binding.hoursList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.hoursList.setAdapter(adapter);
    }

    private void syncLists() {
        String time;
        int record;
        int place;
        for (int i = 0; i < records.size(); i = i + 2) {
            time = records.get(i);
            record = Integer.parseInt(records.get(i + 1));
            if (hours.contains(time)) {
                place = hours.indexOf(time);
                for (int j = 0; j < record; j++) {
                    hours.remove(place);
                    Log.d("ptt", "updated hours = " + hours.toString());
                }
            }
        }
    }
}



