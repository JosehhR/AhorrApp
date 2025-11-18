package com.example.ahorrapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ahorrapp.lib.debt;

public class EditDebtActivity extends AppCompatActivity {

    private EditText nameEditText, startDateEditText, dueDateEditText, interestRateEditText, installmentsEditText, debtValueEditText, priorityEditText;
    private Button saveChangesButton;
    private String debtId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_debt);

        nameEditText = findViewById(R.id.nameEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        interestRateEditText = findViewById(R.id.interestRateEditText);
        installmentsEditText = findViewById(R.id.installmentsEditText);
        debtValueEditText = findViewById(R.id.debtValueEditText);
        priorityEditText = findViewById(R.id.priorityEditText);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        debtId = getIntent().getStringExtra("DEBT_ID");
        userId = getIntent().getStringExtra("USER_ID");

        if (debtId != null && userId != null) {
            loadDebtData();
        }

        saveChangesButton.setOnClickListener(v -> saveDebtChanges());
    }

    private void loadDebtData() {
        debt.downloadFromDatabase(userId, debtId, new debt.DebtCallback() {
            @Override
            public void onDebtReceived(debt debt) {
                if (debt != null) {
                    nameEditText.setText(debt.getName());
                    startDateEditText.setText(debt.getStartDate());
                    dueDateEditText.setText(debt.getDueDate());
                    interestRateEditText.setText(String.valueOf(debt.getInterestRate()));
                    installmentsEditText.setText(String.valueOf(debt.getInstallments()));
                    debtValueEditText.setText(String.valueOf(debt.getDebtValue()));
                    priorityEditText.setText(debt.getPriority());
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(EditDebtActivity.this, "Error al cargar la deuda: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveDebtChanges() {
        String name = nameEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String dueDate = dueDateEditText.getText().toString();
        double interestRate = Double.parseDouble(interestRateEditText.getText().toString());
        int installments = Integer.parseInt(installmentsEditText.getText().toString());
        double debtValue = Double.parseDouble(debtValueEditText.getText().toString());
        String priority = priorityEditText.getText().toString();

        debt updatedDebt = new debt(name, startDate, dueDate, interestRate, installments, debtValue, priority, userId);
        updatedDebt.setId(debtId); // Asegurarse de que la ID no cambie

        updatedDebt.uploadToDatabase()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditDebtActivity.this, "Deuda actualizada exitosamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad de edición
                })
                .addOnFailureListener(e -> Toast.makeText(EditDebtActivity.this, "Error al actualizar la deuda: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
