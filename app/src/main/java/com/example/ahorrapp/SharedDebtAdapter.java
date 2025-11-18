package com.example.ahorrapp;

import android.content.Context;
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

import java.util.List;

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

        // Asignar datos básicos
        holder.debtNameTextView.setText(currentDebt.getName());
        holder.debtValueTextView.setText("Valor: $" + String.format("%,.2f", currentDebt.getDebtValue()));

        // Limpiar y añadir participantes dinámicamente
        holder.participantsListContainer.removeAllViews();
        if (currentDebt.getSharedWithUsers() != null) {
            for (int i = 0; i < currentDebt.getSharedWithUsers().size(); i++) {
                TextView participantView = new TextView(context);
                String participantInfo = currentDebt.getSharedWithUsers().get(i) + " (" + currentDebt.getSharedPercentages().get(i) + "%)";
                participantView.setText(participantInfo);
                holder.participantsListContainer.addView(participantView);
            }
        }

        // Controlar el CheckBox
        holder.paidCheckBox.setOnCheckedChangeListener(null); // Evitar llamadas recursivas
        holder.paidCheckBox.setChecked(currentDebt.isPaid());

        holder.paidCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentDebt.setPaid(isChecked);
            currentDebt.updateInDatabase()
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Estado de la deuda actualizado", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return debtList.size();
    }

    static class SharedDebtViewHolder extends RecyclerView.ViewHolder {
        TextView debtNameTextView, debtValueTextView;
        LinearLayout participantsListContainer;
        CheckBox paidCheckBox;

        public SharedDebtViewHolder(@NonNull View itemView) {
            super(itemView);
            debtNameTextView = itemView.findViewById(R.id.debtNameTextView);
            debtValueTextView = itemView.findViewById(R.id.debtValueTextView);
            participantsListContainer = itemView.findViewById(R.id.participantsListContainer);
            paidCheckBox = itemView.findViewById(R.id.paidCheckBox);
        }
    }
}
