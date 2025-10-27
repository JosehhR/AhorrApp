package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.RegisterScreenBinding;

public class RegisterActivity extends AppCompatActivity {

    private RegisterScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Navegación a la pantalla de inicio de sesión
        binding.btnTabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
