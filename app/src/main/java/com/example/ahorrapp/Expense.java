package com.example.ahorrapp;

public class Expense {
    private String name;
    private double value;
    private String priority;
    private long timestamp;

    public Expense() {
        // Default constructor required for calls to DataSnapshot.getValue(Expense.class)
    }

    public Expense(String name, double value, String priority, long timestamp) {
        this.name = name;
        this.value = value;
        this.priority = priority;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
