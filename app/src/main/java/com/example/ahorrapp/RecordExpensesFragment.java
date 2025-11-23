package com.example.ahorrapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.ahorrapp.databinding.FragmentRecordExpensesBinding;

public class RecordExpensesFragment extends Fragment {

    private FragmentRecordExpensesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecordExpensesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lógica para el botón de volver
        binding.buttonBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });

        // Aquí iría la lógica para registrar el gasto
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
