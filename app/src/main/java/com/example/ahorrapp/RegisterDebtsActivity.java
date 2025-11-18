package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.ActivityRegisterDebtsBinding;
import com.example.ahorrapp.lib.debt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterDebtsActivity extends AppCompatActivity {

    private ActivityRegisterDebtsBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterDebtsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.button.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterDebtsActivity.this, DebtsActivity.class);
            startActivity(intent);
        });

        binding.uploadDebtButton.setOnClickListener(v -> uploadDebt());

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

    private void uploadDebt() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        String name = binding.nameEditText.getText().toString();
        String startDate = binding.startDateEditText.getText().toString();
        String dueDate = binding.dueDateEditText.getText().toString();
        double interestRate = Double.parseDouble(binding.interestRateEditText.getText().toString());
        int installments = Integer.parseInt(binding.installmentsEditText.getText().toString());
        double debtValue = Double.parseDouble(binding.debtValueEditText.getText().toString());

        int selectedPriorityId = binding.radioGroupPrioridad.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedPriorityId);
        String priority = selectedRadioButton.getText().toString();

        debt newDebt = new debt(name, startDate, dueDate, interestRate, installments, debtValue, priority, userId);

        newDebt.uploadToDatabase()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterDebtsActivity.this, "Deuda subida exitosamente", Toast.LENGTH_SHORT).show();
                    // Limpiar campos
                    binding.nameEditText.setText("");
                    binding.startDateEditText.setText("");
                    binding.dueDateEditText.setText("");
                    binding.interestRateEditText.setText("");
                    binding.installmentsEditText.setText("");
                    binding.debtValueEditText.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterDebtsActivity.this, "Error al subir la deuda: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
