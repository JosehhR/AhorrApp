package com.example.ahorrapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.LoginScreenBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnTabRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailInputLayout.getEditText().getText().toString();
                String password = binding.passwordInputLayout.getEditText().getText().toString();

                String message = "Email: " + email + "\nContraseña: " + password;

                new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Datos de Inicio de Sesión")
                    .setMessage(message)
                    .setPositiveButton("Aceptar", null)
                    .show();
            }
        });
    }
}
