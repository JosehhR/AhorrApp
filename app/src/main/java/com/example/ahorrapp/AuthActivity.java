package com.example.ahorrapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.ActivityAuthBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar el ViewPager y el Adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(viewPagerAdapter);

        // Conectar el TabLayout con el ViewPager
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 1) {
                tab.setText("Registrarse");
            } else {
                tab.setText("Iniciar Sesión");
            }
        }).attach();
    }
}
