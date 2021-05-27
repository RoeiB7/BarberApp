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
import com.example.barberapp.adapters.AdapterActive;
import com.example.barberapp.databinding.FragementActiveAppointmentsBinding;
import com.example.barberapp.objects.Appointment;
import com.example.barberapp.utils.FBManager;
import com.example.barberapp.utils.TimeComperator;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ActiveAppointmentsFragment extends Fragment {
    private View view;
    private FragementActiveAppointmentsBinding binding;
    private ArrayList<Appointment> appointments;
    private Appointment appointment;
    private AdapterActive activeAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragement_active_appointments, container, false);
        view = binding.getRoot();
        createList();
        return view;

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
                        showActive();
                    }
                });


    }

    private void showActive() {
        Collections.sort(appointments, new TimeComperator());
        activeAdapter = new AdapterActive(view.getContext(), appointments);
        initAdapter();
        binding.activeList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.activeList.setAdapter(activeAdapter);
    }

    private void initAdapter() {

    }


}
