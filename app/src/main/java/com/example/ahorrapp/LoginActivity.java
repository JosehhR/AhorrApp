package com.example.ahorrapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity; // 1. Cambia la importación a AppCompatActivity
import com.example.ahorrapp.databinding.LoginScreenBinding; // Importa la clase de ViewBinding

public class LoginActivity extends AppCompatActivity { // También cambia la herencia aquí

    // Declara la variable para el ViewBinding
    private LoginScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 2. Infla el layout usando ViewBinding y lo establece como la vista de la actividad
        binding = LoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // A partir de aquí, podrías añadir la lógica para los botones, por ejemplo:
        // binding.loginButton.setOnClickListener(new View.OnClickListener() { ... });
    }
}
