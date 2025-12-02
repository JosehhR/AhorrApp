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

public class BannerExpensesFragment extends Fragment {

    CardView cardPresupuesto, cardRegistra, cardReduce, cardPrioriza;
    TextView detailsPresupuesto, detailsRegistra, detailsReduce, detailsPrioriza;
    ViewGroup containerLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_banner_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        containerLayout = view.findViewById(R.id.container_layout);
        Toolbar toolbar = view.findViewById(R.id.toolbar_expenses);

        NavController navController = NavHostFragment.findNavController(this);
        
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.nav_start);
        topLevelDestinations.add(R.id.nav_bills);
        topLevelDestinations.add(R.id.nav_reports);
        topLevelDestinations.add(R.id.nav_debts);
        topLevelDestinations.add(R.id.nav_profile);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        toolbar.setTitle("");

        cardPresupuesto = view.findViewById(R.id.card_presupuesto);
        detailsPresupuesto = view.findViewById(R.id.details_presupuesto);

        cardRegistra = view.findViewById(R.id.card_registra);
        detailsRegistra = view.findViewById(R.id.details_registra);

        cardReduce = view.findViewById(R.id.card_reduce);
        detailsReduce = view.findViewById(R.id.details_reduce);

        cardPrioriza = view.findViewById(R.id.card_prioriza);
        detailsPrioriza = view.findViewById(R.id.details_prioriza);

        cardPresupuesto.setOnClickListener(v -> toggleVisibility(detailsPresupuesto));
        cardRegistra.setOnClickListener(v -> toggleVisibility(detailsRegistra));
        cardReduce.setOnClickListener(v -> toggleVisibility(detailsReduce));
        cardPrioriza.setOnClickListener(v -> toggleVisibility(detailsPrioriza));
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
