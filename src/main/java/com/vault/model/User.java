package com.vault.model;

public class User {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

    public User(String firstName, String lastName, String email, String password) {
        this.id = java.util.UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    // Getters and setters for the fields

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
