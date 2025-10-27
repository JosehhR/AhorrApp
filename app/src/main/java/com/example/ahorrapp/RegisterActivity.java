package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.RegisterScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private RegisterScreenBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.btnTabLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.loginButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = binding.nameInputLayout.getEditText().getText().toString().trim();
        String frequency = "Mensual"; // Valor predeterminado
        String incomeStr = binding.incomeInputLayout.getEditText().getText().toString().trim();
        String email = binding.emailInputLayout.getEditText().getText().toString().trim();
        String password = binding.passwordInputLayout.getEditText().getText().toString().trim();
        String confirmPassword = binding.confirmPasswordInputLayout.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(frequency) || TextUtils.isEmpty(incomeStr) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showErrorDialog("Todos los campos son obligatorios.");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showErrorDialog("El formato del correo no es válido.");
            return;
        }

        if (password.length() < 6) {
            showErrorDialog("La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showErrorDialog("Las contraseñas no coinciden.");
            return;
        }

        double income;
        try {
            income = Double.parseDouble(incomeStr);
        } catch (NumberFormatException e) {
            showErrorDialog("El formato de los ingresos no es válido.");
            return;
        }

        // Crear usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Guardar datos adicionales en Realtime Database
                            writeNewUser(user.getUid(), name, email, frequency, income);
                        }
                    } else {
                        // Si el registro falla, mostrar un mensaje.
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "La autenticación falló.";
                        showErrorDialog("Error de Registro: " + errorMessage);
                    }
                });
    }

    private void writeNewUser(String userId, String name, String email, String frequency, double income) {
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("paymentFrequency", frequency);
        userData.put("income", income);

        mDatabase.child("users").child(userId).setValue(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Registro Exitoso")
                            .setMessage("¡Tu cuenta ha sido creada!")
                            .setPositiveButton("Ir a Iniciar Sesión", (dialog, which) -> {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            })
                            .setCancelable(false)
                            .show();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "No se pudieron guardar los datos.";
                        showErrorDialog("Error de Base de Datos: " + errorMessage);
                    }
                });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(RegisterActivity.this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .show();
    }
}
