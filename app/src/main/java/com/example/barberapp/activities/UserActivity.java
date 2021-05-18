package com.example.barberapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.barberapp.R;
import com.example.barberapp.databinding.ActivityUserBinding;
import com.example.barberapp.objects.User;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        initViews();

        //todo: complete my appointments
        //todo: add menu bar with edit profile, exit(logout)
    }

    private void initViews() {
        binding.userName.setText("Hi " + User.getInstance().getFirstName() + "!");
        Glide
                .with(this)
                .load(User.getInstance().getImageUri())
                .apply(new RequestOptions().override(500, 500))
                .apply(RequestOptions.circleCropTransform())
                .into(binding.userImage);
        binding.userNewAppointmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AppointmentActivity.class);
            startActivity(intent);
        });
    }
}