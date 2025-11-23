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

import com.example.ahorrapp.databinding.FragmentDebtsBinding;

public class DebtsFragment extends Fragment {

    private FragmentDebtsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDebtsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button14.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_debtsFragment_to_registerDebtsFragment);
        });

        binding.button15.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_debtsFragment_to_consultDebtsFragment);
        });

        binding.registerSharedDebtButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_debtsFragment_to_registerSharedDebtFragment);
        });

        binding.viewSharedDebtsButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_debtsFragment_to_sharedDebtListFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
