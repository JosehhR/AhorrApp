package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ahorrapp.lib.debt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TestDebtActivity extends AppCompatActivity {

    private EditText startDateEditText, dueDateEditText, interestRateEditText, installmentsEditText, debtValueEditText, priorityEditText;
    private Button uploadDebtButton, seeDebtsButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_debt);

        mAuth = FirebaseAuth.getInstance();

        startDateEditText = findViewById(R.id.startDateEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        interestRateEditText = findViewById(R.id.interestRateEditText);
        installmentsEditText = findViewById(R.id.installmentsEditText);
        debtValueEditText = findViewById(R.id.debtValueEditText);
        priorityEditText = findViewById(R.id.priorityEditText);
        uploadDebtButton = findViewById(R.id.uploadDebtButton);
        seeDebtsButton = findViewById(R.id.seeDebtsButton);

        uploadDebtButton.setOnClickListener(v -> uploadDebt());

        seeDebtsButton.setOnClickListener(v -> {
            Intent intent = new Intent(TestDebtActivity.this, DebtListActivity.class);
            startActivity(intent);
        });
    }

    private void uploadDebt() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        String startDate = startDateEditText.getText().toString();
        String dueDate = dueDateEditText.getText().toString();
        double interestRate = Double.parseDouble(interestRateEditText.getText().toString());
        int installments = Integer.parseInt(installmentsEditText.getText().toString());
        double debtValue = Double.parseDouble(debtValueEditText.getText().toString());
        String priority = priorityEditText.getText().toString();

        debt newDebt = new debt(startDate, dueDate, interestRate, installments, debtValue, priority, userId);

        newDebt.uploadToDatabase()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(TestDebtActivity.this, "Deuda subida exitosamente", Toast.LENGTH_SHORT).show();
                    // Limpiar campos
                    startDateEditText.setText("");
                    dueDateEditText.setText("");
                    interestRateEditText.setText("");
                    installmentsEditText.setText("");
                    debtValueEditText.setText("");
                    priorityEditText.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(TestDebtActivity.this, "Error al subir la deuda: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
