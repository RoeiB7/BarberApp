package com.example.barberapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.barberapp.R;
import com.example.barberapp.adapters.AdapterAppointment;
import com.example.barberapp.databinding.ActivityAdminAppointmentsBinding;
import com.example.barberapp.objects.Appointment;
import com.example.barberapp.utils.FBManager;
import com.example.barberapp.utils.TimeComperator;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminAppointmentsActivity extends AppCompatActivity {

    private ActivityAdminAppointmentsBinding binding;
    private ArrayList<Appointment> allAppointments, filterList;
    private AdapterAppointment adapter;
    private String month = "", year = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_appointments);
        getAllAppointments();
        setFilters();
    }

    private void setFilters() {
        filterList = new ArrayList<>();
        String[] months = getResources().getStringArray(R.array.months);
        String[] years = getResources().getStringArray(R.array.years);
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, months);
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, years);

        binding.adminMonthFilter.setOnItemClickListener((parent, view, position, id) -> {
            filterList.clear();

            if ((position + 1) > 9) {
                month = String.valueOf(position + 1);
            } else {
                month = "0" + (position + 1);
            }

        });

        binding.adminYearFilter.setOnItemClickListener((parent, view, position, id) -> {
            year = String.valueOf(position + 2021);

        });


        binding.adminApplyButton.setOnClickListener(v -> {
            if (month.equals("") || year.equals("")) {
                Toast.makeText(AdminAppointmentsActivity.this, "Please select both Year and Month", Toast.LENGTH_SHORT).show();
            } else if (allAppointments.isEmpty()) {
                Toast.makeText(AdminAppointmentsActivity.this, "No appointments found", Toast.LENGTH_SHORT).show();
            } else {
                filterList = multipleFilter(month, year, allAppointments);
                if (filterList.isEmpty()) {
                    Toast.makeText(AdminAppointmentsActivity.this, "No appointments found", Toast.LENGTH_SHORT).show();
                }
                setList(filterList);
            }
        });

        binding.adminResetButton.setOnClickListener(v -> {
            binding.adminMonthFilter.setText("");
            binding.adminYearFilter.setText("");
            binding.adminMonthFilter.clearFocus();
            binding.adminYearFilter.clearFocus();
            filterList.clear();
            setList(allAppointments);

        });

        binding.adminMonthFilter.setAdapter(monthsAdapter);
        binding.adminYearFilter.setAdapter(yearsAdapter);


    }

    private void getAllAppointments() {
        allAppointments = new ArrayList<>();
        FBManager.getInstance().getFirebaseFirestore()
                .collection(FBManager.USERS)
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        for (QueryDocumentSnapshot document1 : task1.getResult()) {
                            FBManager.getInstance().getFirebaseFirestore()
                                    .collection(FBManager.USERS)
                                    .document(document1.getId())
                                    .collection(FBManager.APPOINTMENTS)
                                    .get()
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                                Appointment appointment = document2.toObject(Appointment.class);
                                                appointment.setContactNumber(document1.getString("Contact Number"));
                                                appointment.setClientName(document1.getString("First Name") + " " + document1.getString("Last Name"));
                                                allAppointments.add(appointment);
                                                setList(allAppointments);
                                            }

                                        } else {
                                            Log.d("ptt", "Error getting documents from task2: ", task2.getException());
                                        }
                                    });
                        }
                    } else {
                        Log.d("ptt", "Error getting documents from task1: ", task1.getException());
                    }

                });

    }

    private void setList(ArrayList<Appointment> list) {
        list.sort(new TimeComperator());
        adapter = new AdapterAppointment(this, list);
        initAdapter();
        binding.adminAppointmentsList.setLayoutManager(new LinearLayoutManager(this));
        binding.adminAppointmentsList.setAdapter(adapter);
    }

    private void initAdapter() {
        adapter.setClickListener((view, position) -> openSummary(position));

    }

    public void openSummary(int position) {
        Appointment appointment = allAppointments.get(position);
        String chosenDate = appointment.getDate();
        String chosenHour = appointment.getHour();
        String contactNumber = appointment.getContactNumber();
        String barberName = appointment.getBarberName();
        String clientName = appointment.getClientName();
        String treatmentsList = Arrays.toString(appointment.getTreatments().toArray()).replace("[", "").replace("]", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this
        );
        builder.setTitle("Appointment Summary");
        builder.setMessage("\n" + "Client: " + clientName + "\n\n"
                + "Treatments: " + treatmentsList + "\n\n"
                + "Barber: " + barberName + "\n\n"
                + "Contact Number: " + contactNumber + "\n\n"
                + "Date: " + chosenDate + "\n\n"
                + "Time: " + chosenHour);
        builder.setCancelable(true);
        builder.show();
    }


    public ArrayList<Appointment> multipleFilter(String monthFilter, String yearFilter, ArrayList<Appointment> listAllAppointment) {
        ArrayList<Appointment> listAppointmentAfterFiltering = new ArrayList<>();
        for (Appointment appointment : listAllAppointment) {
            String year = appointment.getDate().substring(6, 10);
            String month = appointment.getDate().substring(3, 5);

            if (monthFilter.equals(month) || monthFilter.isEmpty())
                if (yearFilter.equals(year) || yearFilter.isEmpty())
                    if (!monthFilter.isEmpty() || !yearFilter.isEmpty()) {
                        listAppointmentAfterFiltering.add(appointment);
                    }
        }

        return listAppointmentAfterFiltering;

    }
}

