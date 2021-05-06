package com.example.barberapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityAppointmentBinding;

import java.util.ArrayList;

public class AppointmentActivity extends AppCompatActivity {


    private ActivityAppointmentBinding binding;
    private boolean[] selectTreatment;
    private ArrayList<Integer> treatmentsList = new ArrayList<>();
    private String[] treatmentsArray = {"Blow Dry", "Men's Hair Cut", "Beard Trim", "another one"};


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
        binding.appointmentSelectTreatment.setOnClickListener(v -> {
            buildMultiSelect();
        });


    }

    private void buildMultiSelect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                AppointmentActivity.this
        );
        builder.setTitle("Select Treatment - Up to 3 ");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(treatmentsArray, selectTreatment, new DialogInterface.OnMultiChoiceClickListener() {
            int count = 0;

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    if (!treatmentsList.contains(which))
                        if (treatmentsList.size() < 3) {
                            treatmentsList.add(which);
                            selectTreatment[which] = true;
                        } else {
                            count--;
                            ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                            selectTreatment[which] = false;
                            Toast.makeText(getApplicationContext(), "you can't add more", Toast.LENGTH_SHORT).show();
                        }
                } else if (treatmentsList.contains(which)) {
                    treatmentsList.remove(which);
                    selectTreatment[which] = false;
                }
            }
        });

        builder.setPositiveButton("OK", (dialog, which) ->
                writeTreatment());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Clear All", (dialog, which) ->

        {
            for (int i = 0; i < selectTreatment.length; i++) {
                selectTreatment[i] = false;
                treatmentsList.clear();
                binding.appointmentSelectTreatment.setText("");
            }
        });
        builder.show();
    }

    private void writeTreatment() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < treatmentsList.size(); j++) {
            stringBuilder.append(treatmentsArray[treatmentsList.get(j)]);
            if (j != treatmentsList.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        binding.appointmentSelectTreatment.setText(stringBuilder.toString());
    }


}