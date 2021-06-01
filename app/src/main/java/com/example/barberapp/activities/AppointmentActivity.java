package com.example.barberapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityAppointmentBinding;
import com.example.barberapp.objects.User;
import com.example.barberapp.utils.AppManager;

import java.util.ArrayList;
import java.util.Locale;

public class AppointmentActivity extends AppCompatActivity {


    private ActivityAppointmentBinding binding;
    private boolean[] selectTreatment;
    private ArrayList<Integer> chosenList = new ArrayList<>();
    private String[] treatmentsArray = {
            "Blow Dry",
            "Men's Hair Cut",
            "Beard Trim",
            "Keratin Straightening",
            "Extensions",
            "Kids Cuts",
            "Ladies Dry Trim",
            "Full Highlights",
            "Partial Highlights",
            "Ombre hair Dying"
    };
    private Integer[] treatmentsTime = {30, 30, 10, 120, 60, 20, 60, 180, 120, 180};
    private long time = 0;
    private String[] barbersNames = {"Aviv", "Soli", "Oren"};
    private String chosenBarber;
    private AppManager manager;
    private ArrayList<String> chosenTreatments = new ArrayList<>();
    private boolean isPhoneValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment);
        manager = new AppManager(this);
        initViews();
    }

    private void initViews() {
        checkContactNumber();
        binding.appointmentSearchButton.setOnClickListener(v -> {

            if (isPhoneValid && chosenBarber != null) {
                User.getInstance().setContactNumber(binding.appointmentContactNumberInput.getText().toString().trim());
                Intent intent = new Intent(this, TimeStampActivity.class);
                intent.putExtra("barber", chosenBarber);
                intent.putExtra("time", time);
                intent.putStringArrayListExtra("treatments", chosenTreatments);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error! one or more fields are incomplete", Toast.LENGTH_SHORT).show();
            }

        });
        binding.appointmentContactNumberInput.addTextChangedListener(new TextWatcher() {
            int keyDel;


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.appointmentContactNumberInput.setOnKeyListener((v, keyCode, event) -> {

                    if (keyCode == KeyEvent.KEYCODE_DEL)
                        keyDel = 1;
                    return false;
                });

                if (keyDel == 0) {
                    int len = binding.appointmentContactNumberInput.getText().length();
                    if (len == 3) {
                        binding.appointmentContactNumberInput.setText(binding.appointmentContactNumberInput.getText().toString() + " - ");
                        binding.appointmentContactNumberInput.setSelection(binding.appointmentContactNumberInput.getText().length());
                    }
                } else {
                    keyDel = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                isPhoneValid = manager.validatePhone(AppointmentActivity.this,
                        binding.appointmentContactNumberInput, binding.appointmentContactNumberLayout);
            }
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
        builder.setSingleChoiceItems(barbersNames, -1, (dialog, which) -> {

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

            long timeSec = time * 60;
            int hours = (int) timeSec / 3600;
            int temp = (int) timeSec - hours * 3600;
            int mins = temp / 60;

            String hm = String.format(Locale.ENGLISH, "%02dh : %02dm", hours, mins);
            binding.appointmentTimeCounter.setText(hm + " total");
            writeTreatment();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            if (time == 0) {
                binding.appointmentTimeCounter.setVisibility(View.GONE);

            } else {
                binding.appointmentTimeCounter.setVisibility(View.VISIBLE);
            }
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

    private void checkContactNumber() {
        if (User.getInstance().getContactNumber().equals("N/A")) {
            binding.appointmentContactNumberLayout.setVisibility(View.VISIBLE);
        } else {
            binding.appointmentContactNumberInput.setText(User.getInstance().getContactNumber());
            isPhoneValid = true;
        }
    }


}