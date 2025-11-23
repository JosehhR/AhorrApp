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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ahorrapp.databinding.FragmentConsultDebtsBinding;
import com.example.ahorrapp.lib.debt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConsultDebtsFragment extends Fragment {

    private FragmentConsultDebtsBinding binding;
    private DebtAdapter debtAdapter;
    private List<debt> debtList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConsultDebtsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("debts").child(userId);

        binding.debtssRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        debtList = new ArrayList<>();
        debtAdapter = new DebtAdapter(getContext(), debtList);
        binding.debtssRecyclerView.setAdapter(debtAdapter);

        binding.buttonBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });

        loadDebts();
    }

    private void loadDebts() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                debtList.clear();
                for (DataSnapshot debtSnapshot : snapshot.getChildren()) {
                    debt debt = debtSnapshot.getValue(debt.class);
                    if (debt != null) {
                        debtList.add(debt);
                    }
                }
                debtAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar las deudas: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
