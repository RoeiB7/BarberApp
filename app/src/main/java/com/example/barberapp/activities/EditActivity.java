package com.example.barberapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityEditBinding;
import com.example.barberapp.utils.AppManager;
import com.example.barberapp.utils.FBManager;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;
    private AppManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        manager = new AppManager(this);

        binding.treatmentAddButton.setOnClickListener(v -> {
            String treatment = binding.adminTreatmentInput.getText().toString().trim();
            String duration = binding.adminTreatmentTimeInput.getText().toString().trim();
            if (treatment.isEmpty() && duration.isEmpty()) {
                Toast.makeText(EditActivity.this, "Please fill both Treatment and Duration", Toast.LENGTH_LONG).show();

            } else if (duration.charAt(duration.length() - 1) != '0') {
                Toast.makeText(EditActivity.this, "Please make sure the duration is in increments of 10", Toast.LENGTH_LONG).show();

            } else {
                addTreatmentToFB(treatment, duration);
            }


        });

        binding.treatmentRemoveButton.setOnClickListener(v -> {
            removeFromFB(binding.adminTreatmentInput.getText().toString().trim(), "treatments");
        });

        binding.barberAddButton.setOnClickListener(v -> {
            addBarberToFB(binding.adminBarberInput.getText().toString().trim());
        });

        binding.barberRemoveButton.setOnClickListener(v -> {
            removeFromFB(binding.adminBarberInput.getText().toString().trim(), "barbers");
        });

    }


    private void addTreatmentToFB(String treatment, String duration) {

        DocumentReference documentReference = FBManager.getInstance()
                .getFirebaseFirestore()
                .collection(FBManager.TREATMENTS)
                .document(treatment);
        Map<String, Object> map = new HashMap<>();
        map.put("Treatment Name", treatment);
        map.put("Treatment Duration", duration);


        documentReference.set(map)
                .addOnSuccessListener(unused -> {
                    binding.adminTreatmentInput.setText("");
                    binding.adminTreatmentTimeInput.setText("");
                    manager.closeKeyboard(EditActivity.this);
                    binding.adminTreatmentInput.clearFocus();
                    binding.adminTreatmentTimeInput.clearFocus();
                    Toast.makeText(this, "Treatment Added successfully", Toast.LENGTH_SHORT).show();
                })

                .addOnFailureListener(e -> Toast.makeText(this, "Failed to Add treatment! Please try again", Toast.LENGTH_LONG).show());


    }

    private void addBarberToFB(String barberName) {
        DocumentReference documentReference = FBManager.getInstance()
                .getFirebaseFirestore()
                .collection(FBManager.BARBERS)
                .document(barberName);
        Map<String, Object> map = new HashMap<>();
        map.put("Barber Name", barberName);


        documentReference.set(map)
                .addOnSuccessListener(unused -> {
                    binding.adminBarberInput.setText("");
                    manager.closeKeyboard(EditActivity.this);
                    binding.adminBarberInput.clearFocus();
                    Toast.makeText(this, "Barber add successfully", Toast.LENGTH_SHORT).show();
                })

                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add barber! Please try again", Toast.LENGTH_LONG).show());


    }

    private void removeFromFB(String input, String collection) {

        DocumentReference documentReference = FBManager.getInstance()
                .getFirebaseFirestore()
                .collection(collection)
                .document(input);

        documentReference.delete()
                .addOnSuccessListener(unused -> {
                    if (collection.equals("treatments")) {
                        binding.adminTreatmentInput.setText("");
                        binding.adminTreatmentTimeInput.setText("");
                        manager.closeKeyboard(EditActivity.this);
                        binding.adminTreatmentInput.clearFocus();
                        binding.adminTreatmentTimeInput.clearFocus();
                        Toast.makeText(this, "Treatment removed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.adminBarberInput.setText("");
                        manager.closeKeyboard(EditActivity.this);
                        binding.adminBarberInput.clearFocus();
                        Toast.makeText(this, "Barber removed successfully", Toast.LENGTH_SHORT).show();
                    }

                })

                .addOnFailureListener(e -> Toast.makeText(this, "Failed to remove! Please try again", Toast.LENGTH_LONG).show());


    }
}