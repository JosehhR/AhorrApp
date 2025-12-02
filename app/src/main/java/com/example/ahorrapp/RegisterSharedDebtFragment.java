package com.example.ahorrapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ahorrapp.databinding.FragmentRegisterSharedDebtBinding;
import com.example.ahorrapp.lib.DebtShared;
import com.example.ahorrapp.lib.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterSharedDebtFragment extends Fragment {

    private FragmentRegisterSharedDebtBinding binding;
    private List<View> participantViews = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private List<User> userList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterSharedDebtBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        binding.buttonBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        binding.addParticipantButton.setOnClickListener(v -> addParticipantView());
        binding.saveSharedDebtButton.setOnClickListener(v -> saveSharedDebt());

        loadUsers();
    }

    private void loadUsers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    // No añadir al usuario actual a la lista de selección
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
                Toast.makeText(getContext(), "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addParticipantView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View participantView = inflater.inflate(R.layout.item_debt_participant, binding.participantsContainer, false);

        Spinner userSpinner = participantView.findViewById(R.id.userSpinner);
        ArrayAdapter<User> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, userList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);

        participantView.findViewById(R.id.removeDebtParticipantButton).setOnClickListener(v -> {
            binding.participantsContainer.removeView(participantView);
            participantViews.remove(participantView);
        });

        participantViews.add(participantView);
        binding.participantsContainer.addView(participantView);
    }

    private void saveSharedDebt() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "No se pudo autenticar al usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recoger datos del formulario
        String name = binding.nameEditText.getText().toString().trim();
        String startDate = binding.startDateEditText.getText().toString().trim();
        String dueDate = binding.dueDateEditText.getText().toString().trim();
        String debtValueStr = binding.debtValueEditText.getText().toString().trim();
        String interestRateStr = binding.interestRateEditText.getText().toString().trim();
        String installmentsStr = binding.installmentsEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(dueDate) ||
            TextUtils.isEmpty(debtValueStr) || TextUtils.isEmpty(interestRateStr) || TextUtils.isEmpty(installmentsStr)) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double debtValue = Double.parseDouble(debtValueStr);
        double interestRate = Double.parseDouble(interestRateStr);
        int installments = Integer.parseInt(installmentsStr);

        String priority = ((RadioButton)getView().findViewById(binding.radioGroupPrioridad.getCheckedRadioButtonId())).getText().toString();

        // Recoger participantes
        List<String> sharedWithUserIds = new ArrayList<>();
        List<String> sharedWithUsers = new ArrayList<>();

        // Añadir al creador a la lista de participantes
        sharedWithUserIds.add(currentUser.getUid());
        sharedWithUsers.add(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Usuario sin nombre");

        for (View participantView : participantViews) {
            Spinner userSpinner = participantView.findViewById(R.id.userSpinner);
            User selectedUser = (User) userSpinner.getSelectedItem();
            if (selectedUser != null && !sharedWithUserIds.contains(selectedUser.getUid())) {
                sharedWithUserIds.add(selectedUser.getUid());
                sharedWithUsers.add(selectedUser.getName());
            }
        }

        if (sharedWithUserIds.size() <= 1) {
            Toast.makeText(getContext(), "Debe añadir al menos un participante más.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Por ahora, dividimos el porcentaje en partes iguales
        List<Double> sharedPercentages = new ArrayList<>();
        double percentage = 100.0 / sharedWithUserIds.size();
        for (int i = 0; i < sharedWithUserIds.size(); i++) {
            sharedPercentages.add(percentage);
        }
        
        // Crear el objeto DebtShared
        DatabaseReference debtRef = FirebaseDatabase.getInstance().getReference().child("shared_debts").push();
        String debtId = debtRef.getKey();

        DebtShared debt = new DebtShared(name, startDate, dueDate, interestRate, installments, debtValue, priority, currentUser.getUid(),
                                         sharedWithUsers, sharedPercentages, sharedWithUserIds);
        debt.setId(debtId);

        // Subir a la base de datos
        debt.uploadToDatabase().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Deuda compartida registrada con éxito", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                Toast.makeText(getContext(), "Error al registrar la deuda compartida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
