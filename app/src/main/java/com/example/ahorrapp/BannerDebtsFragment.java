package com.example.ahorrapp;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.HashSet;
import java.util.Set;

public class BannerDebtsFragment extends Fragment {

    CardView cardNoDeudas, cardPriorizaGastos, cardEstructuraDeudas;
    TextView detailsNoDeudas, detailsPriorizaGastos, detailsEstructuraDeudas;
    ViewGroup containerLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_banner_debts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        containerLayout = view.findViewById(R.id.container_layout_debts);
        Toolbar toolbar = view.findViewById(R.id.toolbar_debts);

        NavController navController = NavHostFragment.findNavController(this);

        // Define los destinos de nivel superior (los de la barra de navegación inferior)
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.nav_start);
        topLevelDestinations.add(R.id.nav_bills);
        topLevelDestinations.add(R.id.nav_reports);
        topLevelDestinations.add(R.id.nav_debts);
        topLevelDestinations.add(R.id.nav_profile);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        cardNoDeudas = view.findViewById(R.id.card_no_deudas);
        detailsNoDeudas = view.findViewById(R.id.details_no_deudas);

        cardPriorizaGastos = view.findViewById(R.id.card_prioriza_gastos);
        detailsPriorizaGastos = view.findViewById(R.id.details_prioriza_gastos);

        cardEstructuraDeudas = view.findViewById(R.id.card_estructura_deudas);
        detailsEstructuraDeudas = view.findViewById(R.id.details_estructura_deudas);

        cardNoDeudas.setOnClickListener(v -> toggleVisibility(detailsNoDeudas));
        cardPriorizaGastos.setOnClickListener(v -> toggleVisibility(detailsPriorizaGastos));
        cardEstructuraDeudas.setOnClickListener(v -> toggleVisibility(detailsEstructuraDeudas));
    }

    private void toggleVisibility(View viewToToggle) {
        TransitionManager.beginDelayedTransition(containerLayout, new AutoTransition());

        if (viewToToggle.getVisibility() == View.VISIBLE) {
            viewToToggle.setVisibility(View.GONE);
        } else {
            viewToToggle.setVisibility(View.VISIBLE);
        }
    }
}
