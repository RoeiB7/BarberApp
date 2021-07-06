package com.example.barberapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityDaysOffBinding;
import com.example.barberapp.utils.FBManager;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DaysOffActivity extends AppCompatActivity {

    private ActivityDaysOffBinding binding;
    private boolean[] selectDays;
    private String[] days = new String[]{
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
    };
    private ArrayList<Integer> chosenList = new ArrayList<>();
    private ArrayList<String> chosenDays = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_days_off);
        binding.selectDaysOff.setOnClickListener(v -> buildMultiSelect());
        binding.applyDaysOffButton.setOnClickListener(v -> saveDays());

    }


    private void buildMultiSelect() {
        selectDays = new boolean[days.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(
                DaysOffActivity.this
        );
        builder.setTitle("Select Days Off ");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(days, selectDays, (dialog, which, isChecked) -> {
            if (isChecked) {
                if (!chosenList.contains(which)) {
                    chosenList.add(which);
                    selectDays[which] = true;
                }
            } else if (chosenList.contains(which)) {
                chosenList.remove(chosenList.indexOf(which));
                selectDays[which] = false;
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> writeDays());

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Clear All", (dialog, which) ->

        {
            for (int i = 0; i < selectDays.length; i++) {
                selectDays[i] = false;
                chosenList.clear();
                binding.selectDaysOff.setText("");
            }

        });
        builder.show();


    }

    private void writeDays() {
        chosenDays.clear();
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < chosenList.size(); j++) {
            stringBuilder.append(days[chosenList.get(j)]);
            chosenDays.add(days[chosenList.get(j)]);
            if (j != chosenList.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        binding.selectDaysOff.setText(stringBuilder.toString());
    }

    private void saveDays() {
        DocumentReference documentReference = FBManager.getInstance()
                .getFirebaseFirestore()
                .collection(FBManager.DAYS_OFF)
                .document();
        Map<String, Object> map = new HashMap<>();
        map.put("Days Off", chosenDays);

        documentReference.set(map)
                .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Days off set successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DaysOffActivity.this, UserActivity.class);
                            intent.putExtra("flag", 1);
                            startActivity(intent);
                            finish();
                        }


                )

                .addOnFailureListener(e -> Toast.makeText(this, "Failed to set days off! Please try again", Toast.LENGTH_LONG).show());


    }
}