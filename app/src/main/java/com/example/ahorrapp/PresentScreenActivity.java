package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.PresentScreenBinding; // Importa el binding de este layout

public class PresentScreenActivity extends AppCompatActivity {

    // 1. Declara la variable de binding para este layout
    private PresentScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 2. Infla el layout "present_screen.xml" usando su binding
        binding = PresentScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 3. Busca el botón con id "btn_next_square" y asigna el listener
        binding.btnNextSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 4. Al hacer clic, crea un Intent para ir a la siguiente pantalla
                Intent intent = new Intent(PresentScreenActivity.this, PresentScreen2Activity.class);
                startActivity(intent);
            }
        });
    }
}
