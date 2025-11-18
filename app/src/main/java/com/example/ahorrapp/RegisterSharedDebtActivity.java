package com.example.ahorrapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ahorrapp.lib.DebtShared;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterSharedDebtActivity extends AppCompatActivity {

    private EditText nameEditText, startDateEditText, dueDateEditText, debtValueEditText, interestRateEditText, installmentsEditText;
    private RadioGroup radioGroupPrioridad;
    private Button addParticipantButton, saveSharedDebtButton;
    private LinearLayout participantsContainer;
    private List<View> participantViews = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shared_debt);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        nameEditText = findViewById(R.id.nameEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        debtValueEditText = findViewById(R.id.debtValueEditText);
        interestRateEditText = findViewById(R.id.interestRateEditText);
        installmentsEditText = findViewById(R.id.installmentsEditText);
        radioGroupPrioridad = findViewById(R.id.radioGroupPrioridad);
        addParticipantButton = findViewById(R.id.addParticipantButton);
        saveSharedDebtButton = findViewById(R.id.saveSharedDebtButton);
        participantsContainer = findViewById(R.id.participantsContainer);

        addParticipantButton.setOnClickListener(v -> addParticipantView());
        saveSharedDebtButton.setOnClickListener(v -> saveSharedDebt());
    }

    private void addParticipantView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View participantView = inflater.inflate(R.layout.item_participant, participantsContainer, false);
        participantViews.add(participantView);
        participantsContainer.addView(participantView);
    }

    private void saveSharedDebt() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(nameEditText.getText().toString()) || participantViews.isEmpty()) {
            Toast.makeText(this, "El nombre y al menos un participante son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        String creatorId = currentUser.getUid();
        String name = nameEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String dueDate = dueDateEditText.getText().toString();
        double debtValue = Double.parseDouble(debtValueEditText.getText().toString());
        double interestRate = Double.parseDouble(interestRateEditText.getText().toString());
        int installments = Integer.parseInt(installmentsEditText.getText().toString());

        int selectedPriorityId = radioGroupPrioridad.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedPriorityId);
        String priority = selectedRadioButton.getText().toString();

        List<String> participantNames = new ArrayList<>();
        List<Double> participantPercentages = new ArrayList<>();
        double totalPercentage = 0;

        for (View participantView : participantViews) {
            EditText userNameEditText = participantView.findViewById(R.id.userNameEditText);
            EditText percentageEditText = participantView.findViewById(R.id.percentageEditText);

            if (TextUtils.isEmpty(userNameEditText.getText().toString()) || TextUtils.isEmpty(percentageEditText.getText().toString())) {
                Toast.makeText(this, "Asegúrate de rellenar el nombre y el porcentaje de todos los participantes.", Toast.LENGTH_LONG).show();
                return;
            }
            participantNames.add(userNameEditText.getText().toString());
            double percentage = Double.parseDouble(percentageEditText.getText().toString());
            participantPercentages.add(percentage);
            totalPercentage += percentage;
        }

        if (Math.abs(totalPercentage - 100.0) > 0.001) {
            Toast.makeText(this, "La suma de los porcentajes debe ser 100%.", Toast.LENGTH_SHORT).show();
            return;
        }

        checkUsersAndSave(creatorId, name, startDate, dueDate, debtValue, interestRate, installments, priority, participantNames, participantPercentages);
    }

    private void checkUsersAndSave(String creatorId, String name, String startDate, String dueDate, double debtValue, double interestRate, int installments, String priority, List<String> participantNames, List<Double> percentages) {
        usersRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> allUsersMap = new HashMap<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String dbName = userSnapshot.child("name").getValue(String.class);
                    String dbId = userSnapshot.getKey();
                    if (dbName != null && !allUsersMap.containsKey(dbName)) {
                        allUsersMap.put(dbName, dbId);
                    }
                }

                List<String> invalidUserNames = new ArrayList<>();
                List<String> participantIds = new ArrayList<>();
                for (String participantName : participantNames) {
                    if (allUsersMap.containsKey(participantName)) {
                        participantIds.add(allUsersMap.get(participantName));
                    } else {
                        invalidUserNames.add(participantName);
                    }
                }
                
                if (!participantIds.contains(creatorId)) {
                    participantIds.add(creatorId);
                }

                if (invalidUserNames.isEmpty()) {
                    DebtShared newDebt = new DebtShared(name, startDate, dueDate, interestRate, installments, debtValue, priority, creatorId, participantNames, percentages, participantIds);
                    newDebt.uploadToDatabase()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegisterSharedDebtActivity.this, "Deuda compartida guardada", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(RegisterSharedDebtActivity.this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    showInvalidUsersAlert(invalidUserNames);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterSharedDebtActivity.this, "Error al verificar usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showInvalidUsersAlert(List<String> invalidUsers) {
        new AlertDialog.Builder(this)
                .setTitle("Usuarios no Válidos")
                .setMessage("Los siguientes usuarios no existen: " + String.join(", ", invalidUsers) + ". Por favor, corrígelos.")
                .setPositiveButton("Aceptar", null)
                .show();
    }
}
