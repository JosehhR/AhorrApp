package com.example.ahorrapp;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahorrapp.lib.DebtShared;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SharedDebtAdapter extends RecyclerView.Adapter<SharedDebtAdapter.SharedDebtViewHolder> {

    private List<DebtShared> debtList;
    private Context context;

    public SharedDebtAdapter(Context context, List<DebtShared> debtList) {
        this.context = context;
        this.debtList = debtList;
    }

    @NonNull
    @Override
    public SharedDebtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shared_debt, parent, false);
        return new SharedDebtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SharedDebtViewHolder holder, int position) {
        DebtShared currentDebt = debtList.get(position);
        holder.bind(currentDebt, context);
    }

    @Override
    public int getItemCount() {
        return debtList.size();
    }

    static class SharedDebtViewHolder extends RecyclerView.ViewHolder {
        TextView debtNameTextView, debtValueTextView;
        LinearLayout participantsListContainer, expandableDetailsLayout;
        CheckBox paidCheckBox;

        public SharedDebtViewHolder(@NonNull View itemView) {
            super(itemView);
            debtNameTextView = itemView.findViewById(R.id.debtNameTextView);
            debtValueTextView = itemView.findViewById(R.id.debtValueTextView);
            participantsListContainer = itemView.findViewById(R.id.participantsListContainer);
            paidCheckBox = itemView.findViewById(R.id.paidCheckBox);
            expandableDetailsLayout = itemView.findViewById(R.id.expandable_details_layout);
        }

        public void bind(final DebtShared currentDebt, final Context context) {
            // Set initial state
            expandableDetailsLayout.setVisibility(View.GONE);

            // Bind summary data
            debtNameTextView.setText(currentDebt.getName());

            // Bind detailed data
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            format.setMaximumFractionDigits(0);
            debtValueTextView.setText("Valor: " + format.format(currentDebt.getDebtValue()));

            // Dynamically add participants
            participantsListContainer.removeAllViews();
            if (currentDebt.getSharedWithUsers() != null) {
                for (int i = 0; i < currentDebt.getSharedWithUsers().size(); i++) {
                    TextView participantView = new TextView(context);
                    String participantName = currentDebt.getSharedWithUsers().get(i);
                    Double percentage = currentDebt.getSharedPercentages() != null && i < currentDebt.getSharedPercentages().size() ? currentDebt.getSharedPercentages().get(i) : 0.0;
                    String participantInfo = participantName + " (" + String.format(Locale.getDefault(), "%.2f", percentage) + "%)";
                    participantView.setText(participantInfo);
                    participantsListContainer.addView(participantView);
                }
            }

            // Checkbox logic
            paidCheckBox.setOnCheckedChangeListener(null); // Avoid recursive calls
            paidCheckBox.setChecked(currentDebt.isPaid());
            paidCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                currentDebt.setPaid(isChecked);
                currentDebt.updateInDatabase()
                        .addOnSuccessListener(aVoid -> Toast.makeText(context, "Estado de la deuda actualizado", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show());
            });

            // Click listener for expansion
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
