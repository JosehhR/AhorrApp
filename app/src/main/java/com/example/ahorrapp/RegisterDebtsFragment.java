package com.example.ahorrapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.ahorrapp.databinding.FragmentRegisterDebtsBinding;
import com.example.ahorrapp.lib.debt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterDebtsFragment extends Fragment {

    private FragmentRegisterDebtsBinding binding;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterDebtsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding.buttonBack.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        binding.uploadDebtButton.setOnClickListener(v -> uploadDebt());
    }

    private void uploadDebt() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
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
        RadioButton selectedRadioButton = getView().findViewById(selectedPriorityId);
        String priority = selectedRadioButton.getText().toString();

        debt newDebt = new debt(name, startDate, dueDate, interestRate, installments, debtValue, priority, userId);

        newDebt.uploadToDatabase()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Deuda subida exitosamente", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).navigateUp(); // Volver atrás después de subir
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al subir la deuda: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
