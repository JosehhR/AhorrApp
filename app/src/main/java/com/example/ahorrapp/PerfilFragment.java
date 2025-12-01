package com.example.ahorrapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            loadUserProfile();
        } else {
            redirectToLogin();
        }

        binding.logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            redirectToLogin();
        });
    }

    private void loadUserProfile() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && getContext() != null) {
                    String email = dataSnapshot.child("email").getValue(String.class);
                    Double income = dataSnapshot.child("income").getValue(Double.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String frequency = dataSnapshot.child("paymentFrequency").getValue(String.class);
                    String avatar = dataSnapshot.child("avatar").getValue(String.class);

                    binding.Email.setText(email);
                    binding.Name.setText("@" + name);
                    binding.Frequency.setText(frequency);

                    if (income != null) {
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
                        format.setMaximumFractionDigits(0);
                        binding.Income.setText(format.format(income));
                    } else {
                        binding.Income.setText("$ 0");
                    }

                    if (avatar != null) {
                        Resources res = getResources();
                        int resID = res.getIdentifier(avatar, "drawable", getContext().getPackageName());
                        if (resID != 0) {
                            Drawable drawable = ContextCompat.getDrawable(getContext(), resID);
                            binding.imageView.setImageDrawable(drawable);
                        } else {
                            binding.imageView.setImageResource(R.drawable.default_icon);
                        }
                    } else {
                        binding.imageView.setImageResource(R.drawable.default_icon);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error al cargar los datos del perfil.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void redirectToLogin() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
