package com.example.ahorrapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.ahorrapp.databinding.FragmentRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String selectedAvatar = "imagen1";
    private ImageView selectedAvatarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.registerButton.setOnClickListener(v -> registerUser());

        // Default selection
        selectedAvatarView = binding.avatar1;
        selectAvatar(selectedAvatarView, "imagen1");

        binding.avatar1.setOnClickListener(v -> selectAvatar(binding.avatar1, "imagen1"));
        binding.avatar2.setOnClickListener(v -> selectAvatar(binding.avatar2, "imagen2"));
        binding.avatar3.setOnClickListener(v -> selectAvatar(binding.avatar3, "imagen3"));
        binding.avatar4.setOnClickListener(v -> selectAvatar(binding.avatar4, "imagen4"));
    }

    private void selectAvatar(ImageView imageView, String avatarName) {
        // Reset previous selection
        if (selectedAvatarView != null) {
            selectedAvatarView.setBackgroundColor(Color.TRANSPARENT);
        }

        // Set new selection
        imageView.setBackgroundColor(Color.LTGRAY);
        selectedAvatar = avatarName;
        selectedAvatarView = imageView;
    }

    private void registerUser() {
        String name = binding.nameInputLayout.getEditText().getText().toString().trim();
        String email = binding.emailInputLayout.getEditText().getText().toString().trim();
        String password = binding.passwordInputLayout.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            new AlertDialog.Builder(getContext()).setMessage("Todos los campos son obligatorios.").setPositiveButton("Aceptar", null).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);
                            userData.put("avatar", selectedAvatar);

                            mDatabase.child("users").child(user.getUid()).setValue(userData);

                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    } else {
                        new AlertDialog.Builder(getContext()).setMessage("Error de registro: " + task.getException().getMessage()).setPositiveButton("Aceptar", null).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
