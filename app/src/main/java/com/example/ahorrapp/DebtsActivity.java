package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.ActivityDebtsBinding;


public class DebtsActivity extends AppCompatActivity {

    private ActivityDebtsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDebtsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button14.setOnClickListener(v -> {
            Intent intent = new Intent(DebtsActivity.this, RegisterDebtsActivity.class);
            startActivity(intent);
        });
        binding.button15.setOnClickListener(v -> {
            Intent intent = new Intent(DebtsActivity.this, ConsultDebtsActivity.class);
            startActivity(intent);
        });


        binding.bottomMenu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_bills) {
                startActivity(new Intent(this, ExpensesActivity.class));
                return true;
            } else if (id == R.id.nav_reports) {
                startActivity(new Intent(this, ReportsActivity.class));
                return true;
            } else if (id == R.id.nav_start) {
                startActivity(new Intent(this, InformationActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, PerfilFragment.class));
                return true;
            }

            return false;
        });

    }
}