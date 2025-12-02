package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.ahorrapp.databinding.FragmentExpensesBinding;

public class ExpensesFragment extends Fragment {

    private FragmentExpensesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExpensesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRecordExpense.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecordExpensesActivity.class);
            startActivity(intent);
        });

        binding.buttonViewExpenses.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_expensesFragment_to_viewExpensesFragment);
        });

        binding.buttonRecordSharedExpense.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecordSharedExpenseActivity.class);
            startActivity(intent);
        });

        binding.buttonViewSharedExpenses.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_expensesFragment_to_viewSharedExpensesFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
