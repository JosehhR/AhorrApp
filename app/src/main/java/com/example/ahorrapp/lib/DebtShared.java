package com.example.ahorrapp.lib;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebtShared extends debt {
    private List<String> sharedWithUsers; // Nombres de usuario
    private List<Double> sharedPercentages; // Porcentajes de cada usuario
    private List<String> sharedWithUserIds; // UIDs de los participantes
    private int numberOfDebtors;

    public DebtShared() {
        // Constructor vacío requerido para Firebase
        super();
    }

    public DebtShared(String name, String startDate, String dueDate, double interestRate, int installments, double debtValue, String priority, String creatorId,
                        List<String> sharedWithUsers, List<Double> sharedPercentages, List<String> sharedWithUserIds) {
        super(name, startDate, dueDate, interestRate, installments, debtValue, priority, creatorId);
        this.sharedWithUsers = sharedWithUsers;
        this.sharedPercentages = sharedPercentages;
        this.sharedWithUserIds = sharedWithUserIds;
        if (sharedWithUsers != null) {
            this.numberOfDebtors = sharedWithUsers.size();
        } else {
            this.numberOfDebtors = 0;
        }
    }

    // Sobrescribimos el método para guardar en una estructura diferente
    @Override
    public Task<Void> uploadToDatabase() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        String debtId = getId(); // Usamos el ID de la deuda base

        // Usamos un Map para realizar todas las escrituras a la vez (atómico)
        Map<String, Object> updates = new HashMap<>();

        // 1. Guardamos el objeto completo en el nuevo nodo /shared_debts/
        updates.put("/shared_debts/" + debtId, this);

        // 2. Creamos una referencia a esta deuda para cada participante
        if (sharedWithUserIds != null) {
            for (String participantId : sharedWithUserIds) {
                updates.put("/users/" + participantId + "/shared_debts/" + debtId, true);
            }
        }

        return rootRef.updateChildren(updates);
    }

    // Getters y Setters

    public List<String> getSharedWithUsers() {
        return sharedWithUsers;
    }

    public void setSharedWithUsers(List<String> sharedWithUsers) {
        this.sharedWithUsers = sharedWithUsers;
    }

    public List<Double> getSharedPercentages() {
        return sharedPercentages;
    }

    public void setSharedPercentages(List<Double> sharedPercentages) {
        this.sharedPercentages = sharedPercentages;
    }

    public List<String> getSharedWithUserIds() {
        return sharedWithUserIds;
    }

    public void setSharedWithUserIds(List<String> sharedWithUserIds) {
        this.sharedWithUserIds = sharedWithUserIds;
    }

    public int getNumberOfDebtors() {
        return numberOfDebtors;
    }

    public void setNumberOfDebtors(int numberOfDebtors) {
        this.numberOfDebtors = numberOfDebtors;
    }
}
