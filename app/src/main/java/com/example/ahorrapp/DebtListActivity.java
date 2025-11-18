package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahorrapp.databinding.ActivityConsultDebtsBinding;
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

public class DebtListActivity extends AppCompatActivity {

    private RecyclerView debtsRecyclerView;
    private DebtAdapter debtAdapter;
    private List<debt> debtList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    
    private ActivityConsultDebtsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = ActivityConsultDebtsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String userId = currentUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("debts").child(userId);
        
        debtsRecyclerView = binding.debtssRecyclerView;
        debtsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        debtList = new ArrayList<>();
        debtAdapter = new DebtAdapter(this, debtList);
        debtsRecyclerView.setAdapter(debtAdapter);

        // The back button in activity_consult_debts.xml has the id 'button'
        binding.buttonback.setOnClickListener(v -> {
            Intent intent = new Intent(DebtListActivity.this, DebtsActivity.class);
            startActivity(intent);
        });

        binding.bottomMenu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_bills) {
                startActivity(new Intent(this, ExpensesActivity.class));
                return true;
            } else if (id == R.id.nav_reports) {
                startActivity(new Intent(this, ReportsActivity.class));
                return true;
            } else if (id == R.id.nav_start) {
                startActivity(new Intent(this, InformationActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, PerfilFragment.class));
                return true;
            }

            return false;
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
                Toast.makeText(DebtListActivity.this, "Error al cargar las deudas: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
