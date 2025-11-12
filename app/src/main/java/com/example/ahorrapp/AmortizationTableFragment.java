package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ahorrapp.databinding.FragmentAmortizationTableBinding;

public class AmortizationTableFragment extends AppCompatActivity{
    private FragmentAmortizationTableBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = FragmentAmortizationTableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }




}
