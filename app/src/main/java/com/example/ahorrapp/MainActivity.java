package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View; // Asegúrate de importar la clase View correcta
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // 1. Declara la variable 'binding' aquí
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ahora la asignación funciona porque la variable ya existe
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Asumiendo que tu botón tiene el id "next_button"
        // Si tiene otro id, cámbialo aquí.
        binding.btnSigamos.setOnClickListener(new View.OnClickListener() { // 2. Usa el OnClickListener correcto de Android
            @Override
            public void onClick(View v) { // El parámetro también debe ser android.view.View
                Intent intent = new Intent(MainActivity.this, PresentScreenActivity.class);
                startActivity(intent);
            }
        });
    }
}
