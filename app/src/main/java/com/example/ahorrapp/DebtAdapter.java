package com.example.ahorrapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahorrapp.lib.debt;

import java.util.List;

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

        holder.debtNameTextView.setText(currentDebt.getName());
        holder.debtValueTextView.setText("Valor de la Deuda: $" + String.format("%,.2f", currentDebt.getDebtValue()));
        holder.dueDateTextView.setText("Vence: " + currentDebt.getDueDate());
        holder.priorityTextView.setText("Prioridad: " + currentDebt.getPriority());

        holder.editDebtButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditDebtActivity.class);
            intent.putExtra("DEBT_ID", currentDebt.getId());
            intent.putExtra("USER_ID", currentDebt.getUserId());
            context.startActivity(intent);
        });

        holder.deleteDebtButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmar Eliminación")
                    .setMessage("¿Estás seguro de que quieres eliminar esta deuda?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        deleteDebt(currentDebt);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void deleteDebt(debt debtToDelete) {
        debt.deleteFromDatabase(debtToDelete.getUserId(), debtToDelete.getId())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Deuda eliminada exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al eliminar la deuda: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return debtList.size();
    }

    static class DebtViewHolder extends RecyclerView.ViewHolder {
        TextView debtNameTextView, debtValueTextView, dueDateTextView, priorityTextView;
        Button editDebtButton, deleteDebtButton;

        public DebtViewHolder(@NonNull View itemView) {
            super(itemView);
            debtNameTextView = itemView.findViewById(R.id.debtNameTextView);
            debtValueTextView = itemView.findViewById(R.id.debtValueTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            editDebtButton = itemView.findViewById(R.id.editDebtButton);
            deleteDebtButton = itemView.findViewById(R.id.deleteDebtButton);
        }
    }
}
