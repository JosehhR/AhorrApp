package com.example.ahorrapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ahorrapp.databinding.ActivityRecordSharedExpenseBinding;
import com.example.ahorrapp.lib.User;
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

public class RecordSharedExpenseActivity extends AppCompatActivity {

    private ActivityRecordSharedExpenseBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference usersRef;
    private List<User> userList = new ArrayList<>();
    private List<View> participantViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordSharedExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        setupViews();
        loadUsers();
    }

    private void setupViews() {
        binding.buttonBack.setOnClickListener(v -> finish());
        binding.saveSharedExpenseButton.setOnClickListener(v -> saveSharedExpense());
        binding.addParticipantButton.setOnClickListener(v -> addParticipantView());
        binding.addParticipantButton.setEnabled(false); // Disable until users are loaded
    }

    private void loadUsers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    if (currentUser != null && uid != null && !uid.equals(currentUser.getUid())) {
                        String name = userSnapshot.child("name").getValue(String.class);
                        if (name != null) {
                            userList.add(new User(uid, name));
                        }
                    }
                }
                binding.addParticipantButton.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecordSharedExpenseActivity.this, "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addParticipantView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View participantView = inflater.inflate(R.layout.item_debt_participant, binding.participantsContainer, false);

        Spinner userSpinner = participantView.findViewById(R.id.userSpinner);
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);

        participantView.findViewById(R.id.removeDebtParticipantButton).setOnClickListener(v -> {
            binding.participantsContainer.removeView(participantView);
            participantViews.remove(participantView);
        });

        participantViews.add(participantView);
        binding.participantsContainer.addView(participantView);
    }

    private void saveSharedExpense() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = binding.nameEditText.getText().toString().trim();
        String valueStr = binding.valueEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(valueStr)) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double value = Double.parseDouble(valueStr);
        String priority = ((RadioButton) findViewById(binding.radioGroupPrioridad.getCheckedRadioButtonId())).getText().toString();

        List<String> participantIds = new ArrayList<>();
        // Add creator to the list
        participantIds.add(currentUser.getUid());

        for (View participantView : participantViews) {
            Spinner userSpinner = participantView.findViewById(R.id.userSpinner);
            User selectedUser = (User) userSpinner.getSelectedItem();
            if (selectedUser != null && !participantIds.contains(selectedUser.getUid())) {
                participantIds.add(selectedUser.getUid());
            }
        }

        if (participantIds.size() <= 1 && !participantViews.isEmpty()) {
            Toast.makeText(this, "Añada al menos un participante válido.", Toast.LENGTH_SHORT).show();
            return;
        }
         if (participantViews.isEmpty()) {
            Toast.makeText(this, "Debe añadir al menos un participante.", Toast.LENGTH_SHORT).show();
            return;
        }

        String expenseId = mDatabase.child("shared_expenses").push().getKey();
        if (expenseId == null) {
            Toast.makeText(this, "Error al crear el gasto compartido", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> expense = new HashMap<>();
        expense.put("name", name);
        expense.put("value", value);
        expense.put("priority", priority);
        expense.put("ownerId", currentUser.getUid());
        expense.put("timestamp", System.currentTimeMillis());
        expense.put("participants", participantIds);

        mDatabase.child("shared_expenses").child(expenseId).setValue(expense).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Gasto compartido registrado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al registrar el gasto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
