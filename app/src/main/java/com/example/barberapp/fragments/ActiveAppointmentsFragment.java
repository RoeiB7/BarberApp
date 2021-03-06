package com.example.barberapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.barberapp.R;
import com.example.barberapp.adapters.AdapterAppointment;
import com.example.barberapp.databinding.FragementActiveAppointmentsBinding;
import com.example.barberapp.objects.Appointment;
import com.example.barberapp.objects.User;

import java.util.Arrays;

public class ActiveAppointmentsFragment extends Fragment {
    private View view;
    private FragementActiveAppointmentsBinding binding;
    private AdapterAppointment activeAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragement_active_appointments, container, false);
        view = binding.getRoot();
        showActive();
        return view;

    }


    private void showActive() {
        activeAdapter = new AdapterAppointment(view.getContext(), User.getInstance().getActiveAppointments());
        initAdapter();
        binding.activeList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.activeList.setAdapter(activeAdapter);
    }

    private void initAdapter() {
        activeAdapter.setClickListener((view, position) -> openSummary(position));
    }

    public void openSummary(int position) {
        Appointment appointment = User.getInstance().getActiveAppointments().get(position);
        String chosenDate = appointment.getDate();
        String chosenHour = appointment.getHour();
        String contactNumber = User.getInstance().getContactNumber();
        String barberName = appointment.getBarberName();
        String treatmentsList = Arrays.toString(appointment.getTreatments().toArray()).replace("[", "").replace("]", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(
                view.getContext()
        );
        builder.setTitle("Appointment Summary");
        builder.setMessage("\n" + "Treatments: " + treatmentsList + "\n\n"
                + "Barber: " + barberName + "\n\n"
                + "Contact Number: " + contactNumber + "\n\n"
                + "Date: " + chosenDate + "\n\n"
                + "Time: " + chosenHour);
        builder.setCancelable(true);
        builder.show();
    }


}
