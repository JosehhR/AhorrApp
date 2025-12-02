package com.example.ahorrapp;

import android.content.Context;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahorrapp.lib.debt;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.DebtViewHolder> {

    private List<debt> debtList;
    private Context context;

    public DebtAdapter(Context context, List<debt> debtList) {
        this.context = context;
        this.debtList = debtList;
    }

    @NonNull
    @Override
    public DebtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_debt, parent, false);
        return new DebtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtViewHolder holder, int position) {
        debt currentDebt = debtList.get(position);
        holder.bind(currentDebt, context);
    }

    @Override
    public int getItemCount() {
        return debtList.size();
    }

    static class DebtViewHolder extends RecyclerView.ViewHolder {
        TextView debtNameTextView, debtValueTextView, startDateTextView, dueDateTextView, interestRateTextView, installmentsTextView, priorityTextView;
        Button editDebtButton, deleteDebtButton;
        LinearLayout expandableDetailsLayout;

        public DebtViewHolder(@NonNull View itemView) {
            super(itemView);
            debtNameTextView = itemView.findViewById(R.id.debtNameTextView);
            debtValueTextView = itemView.findViewById(R.id.debtValueTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            interestRateTextView = itemView.findViewById(R.id.interestRateTextView);
            installmentsTextView = itemView.findViewById(R.id.installmentsTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            editDebtButton = itemView.findViewById(R.id.editDebtButton);
            deleteDebtButton = itemView.findViewById(R.id.deleteDebtButton);
            expandableDetailsLayout = itemView.findViewById(R.id.expandable_details_layout);
        }

        public void bind(final debt currentDebt, final Context context) {
            // Set initial state for recycling views
            expandableDetailsLayout.setVisibility(View.GONE);

            // Bind summary data
            debtNameTextView.setText(currentDebt.getName());
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            format.setMaximumFractionDigits(0);
            debtValueTextView.setText(format.format(currentDebt.getDebtValue()));

            // Bind detailed data
            startDateTextView.setText("Fecha de inicio: " + currentDebt.getStartDate());
            dueDateTextView.setText("Fecha de vencimiento: " + currentDebt.getDueDate());
            interestRateTextView.setText("Tasa de interés: " + currentDebt.getInterestRate() + "%");
            installmentsTextView.setText("Cuotas: " + currentDebt.getInstallments());
            priorityTextView.setText("Prioridad: " + currentDebt.getPriority());

            // Click listener for expansion
            itemView.setOnClickListener(v -> {
                TransitionManager.beginDelayedTransition((ViewGroup) itemView, new AutoTransition());
                if (expandableDetailsLayout.getVisibility() == View.VISIBLE) {
                    expandableDetailsLayout.setVisibility(View.GONE);
                } else {
                    expandableDetailsLayout.setVisibility(View.VISIBLE);
                }
            });

            // Button listeners
            editDebtButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("DEBT_ID", currentDebt.getId());
                bundle.putString("USER_ID", currentDebt.getUserId());
                Navigation.findNavController(v).navigate(R.id.action_consultDebtsFragment_to_editDebtFragment, bundle);
            });

            deleteDebtButton.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmar Eliminación")
                        .setMessage("¿Estás seguro de que quieres eliminar esta deuda?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            currentDebt.deleteFromDatabase(currentDebt.getUserId(), currentDebt.getId())
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Deuda eliminada exitosamente", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Error al eliminar la deuda: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
        }
    }
}
