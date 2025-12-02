package com.example.ahorrapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewSharedExpensesFragment extends Fragment {

    private RecyclerView sharedExpensesRecyclerView;
    private SharedExpenseAdapter sharedExpenseAdapter;
    private List<SharedExpense> sharedExpenseList;
    private TextView emptyView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_shared_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        sharedExpensesRecyclerView = view.findViewById(R.id.sharedExpensesRecyclerView);
        emptyView = view.findViewById(R.id.emptyViewShared);
        view.findViewById(R.id.button_back_shared).setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        sharedExpensesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedExpenseList = new ArrayList<>();
        sharedExpenseAdapter = new SharedExpenseAdapter(sharedExpenseList);
        sharedExpensesRecyclerView.setAdapter(sharedExpenseAdapter);

        loadSharedExpenses();
    }

    private void loadSharedExpenses() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Query sharedExpensesQuery = mDatabase.child("shared_expenses");

            sharedExpensesQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sharedExpenseList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Firebase does not support querying for a value in a list directly.
                        // We have to filter on the client side.
                        SharedExpense expense = snapshot.getValue(SharedExpense.class);
                        if (expense != null && expense.getParticipants() != null && expense.getParticipants().contains(userId)) {
                            sharedExpenseList.add(expense);
                        }
                    }
                    Collections.reverse(sharedExpenseList); // Show newest first
                    sharedExpenseAdapter.notifyDataSetChanged();

                    // Show/Hide empty view
                    if (sharedExpenseList.isEmpty()) {
                        sharedExpensesRecyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        sharedExpensesRecyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    emptyView.setText("Error al cargar los gastos.");
                    emptyView.setVisibility(View.VISIBLE);
                    sharedExpensesRecyclerView.setVisibility(View.GONE);
                }
            });
        }
    }
}
