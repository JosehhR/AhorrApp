package com.example.ahorrapp.lib;

public class User {
    private String uid;
    private String name;

    public User(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    // Esto es lo que se mostrará en el Spinner
    @Override
    public String toString() {
        return name;
    }
}
