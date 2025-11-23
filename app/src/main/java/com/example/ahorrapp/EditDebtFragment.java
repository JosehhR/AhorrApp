package com.example.ahorrapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ahorrapp.databinding.FragmentEditDebtBinding;
import com.example.ahorrapp.lib.debt;

public class EditDebtFragment extends Fragment {

    private FragmentEditDebtBinding binding;
    private String debtId, userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditDebtBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            debtId = getArguments().getString("DEBT_ID");
            userId = getArguments().getString("USER_ID");
        }

        if (debtId != null && userId != null) {
            loadDebtData();
        }

        binding.saveChangesButton.setOnClickListener(v -> saveDebtChanges());
    }

    private void loadDebtData() {
        debt.downloadFromDatabase(userId, debtId, new debt.DebtCallback() {
            @Override
            public void onDebtReceived(debt debt) {
                if (debt != null) {
                    binding.nameEditText.setText(debt.getName());
                    binding.startDateEditText.setText(debt.getStartDate());
                    binding.dueDateEditText.setText(debt.getDueDate());
                    binding.interestRateEditText.setText(String.valueOf(debt.getInterestRate()));
                    binding.installmentsEditText.setText(String.valueOf(debt.getInstallments()));
                    binding.debtValueEditText.setText(String.valueOf(debt.getDebtValue()));
                    binding.priorityEditText.setText(debt.getPriority());
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Error al cargar la deuda: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveDebtChanges() {
        String name = binding.nameEditText.getText().toString();
        String startDate = binding.startDateEditText.getText().toString();
        String dueDate = binding.dueDateEditText.getText().toString();
        double interestRate = Double.parseDouble(binding.interestRateEditText.getText().toString());
        int installments = Integer.parseInt(binding.installmentsEditText.getText().toString());
        double debtValue = Double.parseDouble(binding.debtValueEditText.getText().toString());
        String priority = binding.priorityEditText.getText().toString();

        debt updatedDebt = new debt(name, startDate, dueDate, interestRate, installments, debtValue, priority, userId);
        updatedDebt.setId(debtId);

        updatedDebt.updateInDatabase()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Deuda actualizada exitosamente", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).navigateUp();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al actualizar la deuda: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
