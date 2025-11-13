package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ahorrapp.databinding.FragmentPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

public class PerfilFragment extends AppCompatActivity {

    private FragmentPerfilBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = FragmentPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            loadUserProfile();
        } else {
            redirectToLogin();
        }

        binding.CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                redirectToLogin();
            }
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
            } else if (id == R.id.nav_debts) {
                startActivity(new Intent(this, DebtsActivity.class));
                return true;
            }

            return false;
        });
    }

    private void loadUserProfile() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String email = dataSnapshot.child("email").getValue(String.class);
                    Double income = dataSnapshot.child("income").getValue(Double.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String frequency = dataSnapshot.child("paymentFrequency").getValue(String.class);



                    binding.Email.setText(email);

                    name = "@" + name;
                    binding.Name.setText(name);

                    binding.Frequency.setText(frequency);



                    if (income != null) {
                        // Formatear el número como moneda
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
                        format.setMaximumFractionDigits(0);
                        binding.Income.setText(format.format(income));
                    } else {
                        binding.Income.setText("$ 0");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PerfilFragment.this, "Error al cargar los datos del perfil.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(PerfilFragment.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
