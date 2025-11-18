package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahorrapp.databinding.ActivitySharedDebtListBinding;
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

public class SharedDebtListActivity extends AppCompatActivity {

    private ActivitySharedDebtListBinding binding;
    private RecyclerView sharedDebtsRecyclerView;
    private SharedDebtAdapter sharedDebtAdapter;
    private List<DebtShared> sharedDebtList;
    private FirebaseAuth mAuth;
    private DatabaseReference userSharedDebtsRef;
    private DatabaseReference sharedDebtsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySharedDebtListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();
        userSharedDebtsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("shared_debts");
        sharedDebtsRef = FirebaseDatabase.getInstance().getReference("shared_debts");

        sharedDebtsRecyclerView = binding.sharedDebtsRecyclerView;
        sharedDebtsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedDebtList = new ArrayList<>();
        sharedDebtAdapter = new SharedDebtAdapter(this, sharedDebtList);
        sharedDebtsRecyclerView.setAdapter(sharedDebtAdapter);

        setupBottomNavigation();

        loadSharedDebts();
    }

    private void setupBottomNavigation() {
        binding.bottomMenu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_bills) {
                startActivity(new Intent(this, ExpensesActivity.class));
            } else if (id == R.id.nav_reports) {
                startActivity(new Intent(this, ReportsActivity.class));
            } else if (id == R.id.nav_start) {
                startActivity(new Intent(this, InformationActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, PerfilFragment.class));
            }
            return true;
        });
    }

    private void loadSharedDebts() {
        userSharedDebtsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sharedDebtList.clear();
                if (!snapshot.exists()) {
                    sharedDebtAdapter.notifyDataSetChanged();
                    return; // No hay deudas compartidas para este usuario
                }
                
                for (DataSnapshot debtIdSnapshot : snapshot.getChildren()) {
                    String debtId = debtIdSnapshot.getKey();
                    if (debtId != null) {
                        // Ahora, buscamos la deuda completa en el nodo /shared_debts/
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
                                Toast.makeText(SharedDebtListActivity.this, "Error al cargar una deuda compartida", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SharedDebtListActivity.this, "Error al cargar la lista de deudas compartidas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
