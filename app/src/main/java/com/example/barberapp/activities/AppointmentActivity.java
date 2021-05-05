package com.example.barberapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityAppointmentBinding;

import java.util.ArrayList;
import java.util.Collections;

public class AppointmentActivity extends AppCompatActivity {


    private ActivityAppointmentBinding binding;
    private boolean[] selectTreatment;
    private ArrayList<Integer> treatmentsList = new ArrayList<>();
    private String[] treatmentsArray = {"Blow Dry","Men's Hair Cut","Beard Trim",};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment);

        initViews();
    }

    private void initViews() {
        binding.appointmentSearchButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TimeStampActivity.class);
            startActivity(intent);
        });
        selectTreatment = new boolean[treatmentsArray.length];
        binding.appointmentSelectBarber.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    AppointmentActivity.this
            );
            builder.setTitle("Select Barber");
            builder.setCancelable(false);
            builder.setMultiChoiceItems(treatmentsArray, selectTreatment,
                    (dialog, which, isChecked) -> {
                        if (isChecked) {
                            treatmentsList.add(which);
                            Collections.sort(treatmentsList);

                        } else {
                            treatmentsList.remove(which);
                        }
                    });


            builder.setPositiveButton("OK", (dialog, which) -> {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < treatmentsList.size(); j++) {
                    stringBuilder.append(treatmentsArray[treatmentsList.get(j)]);
                    if (j != treatmentsList.size() - 1) {

                    }
                }
            });
        });
    }

    //todo: add count number for amout of time of treatment in design(TextView)

}