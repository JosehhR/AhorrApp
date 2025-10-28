package com.example.ahorrapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_list);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String userId = currentUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("debts").child(userId);

        debtsRecyclerView = findViewById(R.id.debtsRecyclerView);
        debtsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        debtList = new ArrayList<>();
        debtAdapter = new DebtAdapter(this, debtList);
        debtsRecyclerView.setAdapter(debtAdapter);

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
