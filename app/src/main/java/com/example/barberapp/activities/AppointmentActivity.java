package com.example.barberapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityAppointmentBinding;
import com.example.barberapp.utils.AppManager;

import java.util.ArrayList;

public class AppointmentActivity extends AppCompatActivity {


    private ActivityAppointmentBinding binding;
    private boolean[] selectTreatment;
    private ArrayList<Integer> chosenList = new ArrayList<>();
    private String[] treatmentsArray = {"Blow Dry", "Men's Hair Cut", "Beard Trim", "another one"};
    private Integer[] treatmentsTime = {20, 30, 10, 40};
    private int time = 0;
    private String[] barbersNames = {"Aviv", "Soli", "Benjamin", "Dudu", "Oren", "Stav"};
    private String chosenBarber;
    private AppManager manager;
    private ArrayList<String> chosenTreatments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment);
        manager = new AppManager(this);
        initViews();
    }

    private void initViews() {
        binding.appointmentSearchButton.setOnClickListener(v -> {

            Intent intent = new Intent(this, TimeStampActivity.class);
            intent.putExtra("barber", chosenBarber);
            intent.putStringArrayListExtra("treatments", chosenTreatments);
            startActivity(intent);

        });
        selectTreatment = new boolean[treatmentsArray.length];
        binding.appointmentSelectTreatment.setOnClickListener(v -> buildMultiSelect());
        binding.appointmentSelectBarber.setOnClickListener(v -> buildSingleSelect());


    }

    private void buildSingleSelect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                AppointmentActivity.this
        );
        builder.setTitle("Select Barber");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(barbersNames, 0, (dialog, which) -> {
            chosenBarber = barbersNames[which];

        });
        builder.setPositiveButton("OK", (dialog, which) -> {
            binding.appointmentSelectBarber.setText(chosenBarber);
        });
        builder.setNegativeButton("CANCEL", null);
        builder.show();
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
                    if (!chosenList.contains(which))
                        if (chosenList.size() < 3) {
                            chosenList.add(which);
                            time = time + treatmentsTime[which];
                            selectTreatment[which] = true;
                        } else {
                            count--;
                            ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                            selectTreatment[which] = false;
                            Toast.makeText(getApplicationContext(), "you can't add more", Toast.LENGTH_SHORT).show();
                        }
                } else if (chosenList.contains(which)) {
                    chosenList.remove(chosenList.indexOf(which));
                    time = time - treatmentsTime[which];
                    selectTreatment[which] = false;
                }
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (time == 0) {
                binding.appointmentTimeCounter.setVisibility(View.GONE);

            } else {
                binding.appointmentTimeCounter.setVisibility(View.VISIBLE);
            }
            binding.appointmentTimeCounter.setText(time + " minutes total");
            writeTreatment();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            binding.appointmentTimeCounter.setVisibility(View.GONE);
            dialog.dismiss();
        });

        builder.setNeutralButton("Clear All", (dialog, which) ->

        {
            for (int i = 0; i < selectTreatment.length; i++) {
                selectTreatment[i] = false;
                chosenList.clear();
                binding.appointmentSelectTreatment.setText("");
            }
            time = 0;
            binding.appointmentTimeCounter.setVisibility(View.GONE);
        });
        builder.show();
    }

    private void writeTreatment() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < chosenList.size(); j++) {
            stringBuilder.append(treatmentsArray[chosenList.get(j)]);
            chosenTreatments.add(treatmentsArray[chosenList.get(j)]);
            if (j != chosenList.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        binding.appointmentSelectTreatment.setText(stringBuilder.toString());
    }


}