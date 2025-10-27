package com.example.ahorrapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.ForgotPasswordScreenBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ForgotPasswordScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ForgotPasswordScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.correoInputLayout.getEditText().getText().toString();

                String message = "Email: " + email;

                new AlertDialog.Builder(ForgotPasswordActivity.this)
                    .setTitle("Datos de Recuperación")
                    .setMessage(message)
                    .setPositiveButton("Aceptar", null)
                    .show();
            }
        });
    }
}
