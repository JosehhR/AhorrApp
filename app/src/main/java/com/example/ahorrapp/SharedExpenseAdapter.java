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

import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SharedExpenseAdapter extends RecyclerView.Adapter<SharedExpenseAdapter.SharedExpenseViewHolder> {

    private final List<SharedExpense> sharedExpenseList;

    public SharedExpenseAdapter(List<SharedExpense> sharedExpenseList) {
        this.sharedExpenseList = sharedExpenseList;
    }

    @NonNull
    @Override
    public SharedExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shared_expense, parent, false);
        return new SharedExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SharedExpenseViewHolder holder, int position) {
        SharedExpense expense = sharedExpenseList.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return sharedExpenseList.size();
    }

    static class SharedExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, valueTextView, ownerTextView, participantsTextView, dateTextView;
        LinearLayout expandableDetailsLayout;

        public SharedExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.sharedExpenseNameTextView);
            valueTextView = itemView.findViewById(R.id.sharedExpenseValueTextView);
            ownerTextView = itemView.findViewById(R.id.sharedExpenseOwnerTextView);
            participantsTextView = itemView.findViewById(R.id.sharedExpenseParticipantsTextView);
            dateTextView = itemView.findViewById(R.id.sharedExpenseDateTextView);
            expandableDetailsLayout = itemView.findViewById(R.id.expandable_details_layout);
        }

        public void bind(SharedExpense expense) {
            // Set initial state
            expandableDetailsLayout.setVisibility(View.GONE);

            nameTextView.setText(expense.getName());

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            format.setMaximumFractionDigits(0);
            valueTextView.setText(format.format(expense.getValue()));

            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (expense.getOwnerId().equals(currentUserId)) {
                ownerTextView.setText("Creado por: Tú");
            } else {
                // In a real app, you'd fetch the owner's name from their UID
                ownerTextView.setText("Creado por: Otro usuario");
            }

            int numParticipants = expense.getParticipants() != null ? expense.getParticipants().size() : 0;
            participantsTextView.setText("Participantes: " + numParticipants);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(new Date(expense.getTimestamp()));
            dateTextView.setText(formattedDate);

            // Set click listener
            itemView.setOnClickListener(v -> {
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
