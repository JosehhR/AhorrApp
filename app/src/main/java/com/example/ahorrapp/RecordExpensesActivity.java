package com.example.ahorrapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ahorrapp.databinding.ActivityRecordExpensesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RecordExpensesActivity extends AppCompatActivity {

    private ActivityRecordExpensesBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordExpensesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.buttonBack.setOnClickListener(v -> finish());
        binding.saveExpenseButton.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = binding.nameEditText.getText().toString().trim();
        String valueStr = binding.valueEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(valueStr)) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double value;
        try {
            value = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, ingrese un valor numérico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPriorityId = binding.radioGroupPrioridad.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedPriorityId);
        String priority = selectedRadioButton.getText().toString();

        String userId = currentUser.getUid();
        DatabaseReference expensesRef = mDatabase.child("users").child(userId).child("expenses").push();

        Map<String, Object> expense = new HashMap<>();
        expense.put("name", name);
        expense.put("value", value);
        expense.put("priority", priority);
        expense.put("timestamp", System.currentTimeMillis());

        expensesRef.setValue(expense).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RecordExpensesActivity.this, "Gasto registrado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RecordExpensesActivity.this, "Error al registrar el gasto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
