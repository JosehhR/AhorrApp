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

import com.example.ahorrapp.databinding.FragmentSharedDebtListBinding;
import com.example.ahorrapp.lib.DebtShared;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SharedDebtListFragment extends Fragment {

    private FragmentSharedDebtListBinding binding;
    private SharedDebtAdapter sharedDebtAdapter;
    private List<DebtShared> sharedDebtList;
    private FirebaseAuth mAuth;
    private DatabaseReference userSharedDebtsRef;
    private DatabaseReference sharedDebtsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSharedDebtListBinding.inflate(inflater, container, false);
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
        userSharedDebtsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("shared_debts");
        sharedDebtsRef = FirebaseDatabase.getInstance().getReference("shared_debts");

        binding.sharedDebtsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedDebtList = new ArrayList<>();
        sharedDebtAdapter = new SharedDebtAdapter(getContext(), sharedDebtList);
        binding.sharedDebtsRecyclerView.setAdapter(sharedDebtAdapter);

        binding.buttonBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });

        loadSharedDebts();
    }

    private void loadSharedDebts() {
        userSharedDebtsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sharedDebtList.clear();
                if (!snapshot.exists()) {
                    sharedDebtAdapter.notifyDataSetChanged();
                    return;
                }
                
                for (DataSnapshot debtIdSnapshot : snapshot.getChildren()) {
                    String debtId = debtIdSnapshot.getKey();
                    if (debtId != null) {
                        sharedDebtsRef.child(debtId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot debtSnapshot) {
                                if (debtSnapshot.exists()) {
                                    DebtShared sharedDebt = debtSnapshot.getValue(DebtShared.class);
                                    if (sharedDebt != null) {
                                        sharedDebtList.add(sharedDebt);
                                        sharedDebtAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Error al cargar una deuda compartida", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar la lista de deudas compartidas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
