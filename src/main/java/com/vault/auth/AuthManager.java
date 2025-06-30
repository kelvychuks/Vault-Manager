package com.vault.auth;

import com.vault.data.DataManager;
import com.vault.model.User;

import java.util.regex.Pattern;

public class AuthManager {
    final private DataManager dataManager;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-z]+$");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$");

    public AuthManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public User registerUser(String firstName, String lastName, String email, String password) {
        // Validate all inputs
        if (!validateName(firstName) || !validateName(lastName)) {
            throw new IllegalArgumentException("Invalid name format");
        }

        if (!validateEmail(email) || !isEmailUnique(email)) {
            throw new IllegalArgumentException("Invalid or duplicate email");
        }

        if (!validatePassword(password)) {
            throw new IllegalArgumentException("Password doesn't meet requirements");
        }

        // Create and save user
        User user = new User(firstName, lastName, email, password);

        if (dataManager.addUser(user)) {
            return user;
        }
        return null;
    }

    public User loginUser(String email, String password) {
        User user = dataManager.getUserByEmail(email);
        if (user != null && verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean validateEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean validatePassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public boolean validateName(String name) {
        return name != null && !name.trim().isEmpty() && NAME_PATTERN.matcher(name).matches();
    }

    public boolean isEmailUnique(String email) {
        return dataManager.getUserByEmail(email) == null;
    }

    private boolean verifyPassword(String password, String dbPassword) {
        return password.equals(dbPassword);

    }
}
