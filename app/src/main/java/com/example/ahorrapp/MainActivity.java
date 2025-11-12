package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSigamos.setOnClickListener(new View.OnClickListener() { // 2. Usa el OnClickListener correcto de Android
            @Override
            public void onClick(View v) { // El parámetro también debe ser android.view.View
                Intent intent = new Intent(MainActivity.this, PresentScreenActivity.class);
                startActivity(intent);
            }
        });
    }
}
