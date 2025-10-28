package com.example.ahorrapp.lib;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class debt {
    private String id;
    private String startDate;
    private String dueDate;
    private double interestRate;
    private int installments;
    private double debtValue;
    private String priority;
    private String userId;

    public debt() {
        // Constructor vacío requerido para Firebase
    }

    public debt(String startDate, String dueDate, double interestRate, int installments, double debtValue, String priority, String userId) {
        this.id = UUID.randomUUID().toString();
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.interestRate = interestRate;
        this.installments = installments;
        this.debtValue = debtValue;
        this.priority = priority;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public double getDebtValue() {
        return debtValue;
    }

    public void setDebtValue(double debtValue) {
        this.debtValue = debtValue;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Task<Void> uploadToDatabase() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        // Guardar la deuda bajo /debts/{userId}/{debtId}
        return mDatabase.child("debts").child(this.userId).child(this.id).setValue(this);
    }

    public static Task<Void> deleteFromDatabase(String userId, String debtId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        return mDatabase.child("debts").child(userId).child(debtId).removeValue();
    }

    public interface DebtCallback {
        void onDebtReceived(debt debt);
        void onError(String message);
    }

    public static void downloadFromDatabase(String userId, String debtId, final DebtCallback callback) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("debts").child(userId).child(debtId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    debt debt = snapshot.getValue(debt.class);
                    callback.onDebtReceived(debt);
                } else {
                    callback.onDebtReceived(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }
}
