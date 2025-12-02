package com.example.ahorrapp;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void updateExpenses(List<Expense> newExpenses) {
        this.expenseList.clear();
        this.expenseList.addAll(newExpenses);
        notifyDataSetChanged();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseNameTextView;
        TextView expenseValueTextView;
        TextView expensePriorityTextView;
        TextView expenseDateTextView;
        LinearLayout expandableDetailsLayout;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseNameTextView = itemView.findViewById(R.id.expenseNameTextView);
            expenseValueTextView = itemView.findViewById(R.id.expenseValueTextView);
            expensePriorityTextView = itemView.findViewById(R.id.expensePriorityTextView);
            expenseDateTextView = itemView.findViewById(R.id.expenseDateTextView);
            expandableDetailsLayout = itemView.findViewById(R.id.expandable_details_layout);
        }

        public void bind(Expense expense) {
            // Set initial state for recycling views
            expandableDetailsLayout.setVisibility(View.GONE);

            // Bind data to views
            expenseNameTextView.setText(expense.getName());

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            format.setMaximumFractionDigits(0);
            expenseValueTextView.setText(format.format(expense.getValue()));

            expensePriorityTextView.setText(expense.getPriority());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(new Date(expense.getTimestamp()));
            expenseDateTextView.setText(formattedDate);

            // Set click listener to expand/collapse
            itemView.setOnClickListener(v -> {
                // Use a transition for smooth animation
                TransitionManager.beginDelayedTransition((ViewGroup) itemView, new AutoTransition());
                if (expandableDetailsLayout.getVisibility() == View.VISIBLE) {
                    expandableDetailsLayout.setVisibility(View.GONE);
                } else {
                    expandableDetailsLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
