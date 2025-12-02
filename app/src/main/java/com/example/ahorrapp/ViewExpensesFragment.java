package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewExpensesFragment extends Fragment {

    private RecyclerView expensesRecyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;
    private FloatingActionButton addExpenseFab;
    private TextView emptyView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Toolbar and Navigation setup
        Toolbar toolbar = view.findViewById(R.id.toolbar_view_expenses);
        NavController navController = NavHostFragment.findNavController(this);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        toolbar.setTitle("");

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize Views
        expensesRecyclerView = view.findViewById(R.id.expensesRecyclerView);
        addExpenseFab = view.findViewById(R.id.addExpenseFab);
        emptyView = view.findViewById(R.id.emptyView);

        // Setup RecyclerView
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(expenseList);
        expensesRecyclerView.setAdapter(expenseAdapter);

        // FAB OnClickListener
        addExpenseFab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecordExpensesActivity.class);
            startActivity(intent);
        });

        loadExpenses();
    }

    private void loadExpenses() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userExpensesRef = mDatabase.child("users").child(userId).child("expenses");

            userExpensesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    expenseList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Expense expense = snapshot.getValue(Expense.class);
                        if (expense != null) {
                            expenseList.add(expense);
                        }
                    }
                    Collections.reverse(expenseList);
                    expenseAdapter.notifyDataSetChanged();

                    if (expenseList.isEmpty()) {
                        expensesRecyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        expensesRecyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    expensesRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("Error al cargar los gastos.");
                }
            });
        }
    }
}
