package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.ActivityRecordExpensesBinding;


public class RecordExpensesActivity extends AppCompatActivity {

    private ActivityRecordExpensesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecordExpensesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(v -> {
            Intent intent = new Intent(RecordExpensesActivity.this, ExpensesActivity.class);
            startActivity(intent);
        });

        binding.bottomMenu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_start) {
                startActivity(new Intent(this, InformationActivity.class));
                return true;
            } else if (id == R.id.nav_reports) {
                startActivity(new Intent(this, ReportsActivity.class));
                return true;
            } else if (id == R.id.nav_debts) {
                startActivity(new Intent(this, DebtsActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, PerfilFragment.class));
                return true;
            }

            return false;
        });
    }
}
