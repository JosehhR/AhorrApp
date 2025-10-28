package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.LoginScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private LoginScreenBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        binding.btnTabRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        binding.loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = binding.emailInputLayout.getEditText().getText().toString().trim();
        String password = binding.passwordInputLayout.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showErrorDialog("El correo y la contraseña son obligatorios.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso, redirigir a PerfilActivity
                        Intent intent = new Intent(LoginActivity.this, TestDebtActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish(); // Cierra LoginActivity
                    } else {
                        // Si el inicio de sesión falla, mostrar un mensaje de error.
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "La autenticación falló.";
                        showErrorDialog("Error de Inicio de Sesión: " + errorMessage);
                    }
                });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(LoginActivity.this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .show();
    }
}
