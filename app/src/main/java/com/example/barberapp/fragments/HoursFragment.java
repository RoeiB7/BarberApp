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
        //todo:get dateData from Calendar fragment and sync hours list with dateData
        if (!records.isEmpty()) {
            Log.d("ptt", records.toString());
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
    }
}
