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
    private List<Double> hoursDoubleEdit, hoursDoubleOG;
    private int recordsToRemove;

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
        hoursDoubleEdit = new ArrayList<>();
        hoursDoubleOG = new ArrayList<>();
        for (int i = 9; i < 20; i++) {
            for (int j = 0; j < 60; j = j + 10) {
                if (j == 0) {
                    hours.add(i + ":00");
                    hoursDoubleEdit.add((double) i);
                    hoursDoubleOG.add((double) i);
                } else {
                    hours.add(i + ":" + j);
                    hoursDoubleEdit.add((double) i + (j * Math.pow(10, -2)));
                    hoursDoubleOG.add((double) i + (j * Math.pow(10, -2)));
                }
            }
        }


        adapter = new AdapterHours(view.getContext(), hours);

        adapter.setClickListener((view, position) -> {
            ((TimeStampActivity) getActivity()).getHour(hours.get(position));
            ((TimeStampActivity) getActivity()).openBooking();

        });

        binding.hoursList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.hoursList.setAdapter(adapter);

    }

    public void setRecords(ArrayList<String> arrayList) {
        records = arrayList;
        updateList();
    }

    private void updateList() {
        recordsToRemove = getArguments().getInt("records");
        if (!records.isEmpty()) {
            syncLists();
        } else {
            hours.clear();
            hoursDoubleEdit.clear();
            hoursDoubleOG.clear();
            for (int i = 9; i < 20; i++) {
                for (int j = 0; j < 60; j = j + 10) {
                    if (j == 0) {
                        hours.add(i + ":00");
                        hoursDoubleEdit.add((double) i);
                        hoursDoubleOG.add((double) i);
                    } else {
                        hours.add(i + ":" + j);
                        hoursDoubleEdit.add((double) i + (j * Math.pow(10, -2)));
                        hoursDoubleOG.add((double) i + (j * Math.pow(10, -2)));
                    }
                }
            }
            Log.d("ptt", hours.toString());
            Log.d("ptt", hoursDoubleEdit.toString());
            Log.d("ptt", hoursDoubleOG.toString());

        }
        adapter = new AdapterHours(view.getContext(), hours);

        adapter.setClickListener((view, position) -> {
//todo: check why IF statement not working!
            if (hours.subList(position, hours.size()).size() >= recordsToRemove
                    &&
                    hoursDoubleEdit.get((position + recordsToRemove - 1)) - hoursDoubleEdit.get(position) !=
                            hoursDoubleOG.get((hoursDoubleOG.indexOf(hoursDoubleEdit.get(position)) + recordsToRemove - 1)) - hoursDoubleEdit.get(position)) {
                Log.d("ptt", "here");
                ((TimeStampActivity) getActivity()).getHour(hours.get(position));
                ((TimeStampActivity) getActivity()).openBooking();

            } else {
                Log.d("ptt", "here2");

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
                    hoursDoubleEdit.remove(place);

                }
            }
        }
    }
}



