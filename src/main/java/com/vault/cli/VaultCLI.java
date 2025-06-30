package com.vault.cli;

import com.vault.auth.AuthManager;
import com.vault.data.DataManager;
import com.vault.model.User;

import java.util.Map;
import java.util.Scanner;

public class VaultCLI {
    final private Scanner scanner;
    final private AuthManager authManager;
    final private DataManager dataManager;
    private User currentUser;

    public VaultCLI() {
        this.scanner = new Scanner(System.in);
        this.dataManager = new DataManager();
        this.authManager = new AuthManager(dataManager);
    }

    public void start() {
        System.out.println("=== Welcome to Vault Application ===");

        while (currentUser == null) {
            displayMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    currentUser = handleLogin();
                    break;
                case 2:
                    currentUser = handleSignup();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        // Main vault operations
        vaultMenu();
    }

    private void displayMainMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Sign Up");
    }

    private User handleLogin() {
        System.out.println("\n=== Login ===");
        String email = getStringInput("Enter email: ");
        String password = getStringInput("Enter password: ");

        User user = authManager.loginUser(email, password);
        if (user != null) {
            System.out.println("Login successful! Welcome " + user.getFirstName());
            return user;
        } else {
            System.out.println("Invalid credentials. Please try again.");
            return null;
        }
    }

    private User handleSignup() {
        System.out.println("\n=== Sign Up ===");

        String firstName = getValidatedInput("Enter First Name: ", authManager::validateName, "Invalid name format");
        String lastName = getValidatedInput("Enter Last Name: ", authManager::validateName, "Invalid name format");
        String email = getValidatedInput("Enter Email: ", this::validateUniqueEmail, "Invalid or duplicate email");
        String password = getValidatedInput("Enter Password: ", authManager::validatePassword, "Password must be 5+ chars with uppercase, lowercase, number,special character");
        String confirmPassword = getValidatedInput("Confirm Password: ", pwd -> pwd.equals(password), "Passwords don't match");

        try {
            User user = authManager.registerUser(firstName, lastName, email, password);
            System.out.println("Registration successful! Welcome " + firstName);
            return user;
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return null;
        }
    }

    private void vaultMenu() {
        while (true) {
            System.out.println("\n=== Vault Menu ===");
            System.out.println("1. Store Property");
            System.out.println("2. View Properties");
            System.out.println("3. Update Property");
            System.out.println("4. Delete Property");
            System.out.println("0. Exit");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    handleStoreProperty();
                    break;
                case 2:
                    handleViewProperties();
                    break;
                    case 3:
                handleUpdateProperties();
                    break;
                case 4:
                    handleDeleteProperty();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }


    private void handleStoreProperty() {
        String key = getValidatedInput("Enter Key: ", this::validateKey, "Key must contain only letters, numbers, and underscores");
        String value = getValidatedInput("Enter Value: ", val -> !val.trim().isEmpty(), "Value cannot be blank");

        Map<String, String> properties = dataManager.loadUserProperties(currentUser.getId());

        if (properties.containsKey(key)) {
            System.out.println("Key already exists. Use update option instead.");
            return;
        }
        properties.put(key, value);
        if (dataManager.saveUserProperties(currentUser.getId(), properties)) {
            System.out.println("Property stored successfully!");
        } else {
            System.out.println("Failed to store property.");
        }
    }

    private void handleViewProperties() {
        Map<String, String> properties = dataManager.loadUserProperties(currentUser.getId());

        if (properties.isEmpty()) {
            System.out.println("No properties stored.");
            return;
        }
        System.out.println("\n=== Your Properties ===");
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    private void handleUpdateProperties(){
        String key = getValidatedInput("Enter the key you want to update: ",val-> !val.trim().isEmpty(),"Keycannot be empty");

        Map<String, String> properties = dataManager.loadUserProperties(currentUser.getId());
        if(!properties.containsKey(key)) {
            System.out.println("key cannot be found.");
            return;
        }
        System.out.println("Current value: "+ properties.get(key));

        String newValue = getValidatedInput("Enter the new value",val -> !val.trim().isEmpty(),"Value cannot be empty");
        properties.put(key,newValue);

        if(dataManager.saveUserProperties(currentUser.getId(), properties)){
            System.out.println("Property updated successfully");
        } else {
            System.out.println("failed to update property.");
        }
    }

    private void handleDeleteProperty() {
        // Step 1: Ask the user for the key they want to delete
        String key = getValidatedInput(
                "Enter the key to delete: ",
                val -> !val.trim().isEmpty(),
                "Key cannot be empty"
        );

        Map<String, String> properties = dataManager.loadUserProperties(currentUser.getId());

        // Step 3: Check if the key exists
        if (!properties.containsKey(key)) {
            System.out.println("Key does not exist.");
            return;
        }

        properties.remove(key);

        if (dataManager.saveUserProperties(currentUser.getId(), properties)) {
            System.out.println("Property deleted successfully.");
        } else {
            System.out.println("Failed to delete property.");
        }
    }

    // Helper methods
    private boolean validateUniqueEmail(String email) {
        return authManager.validateEmail(email) && authManager.isEmailUnique(email);
    }

    private boolean validateKey(String key) {
        return key != null && key.matches("^[A-Za-z0-9_]+$");
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput() {
        while (true) {
            try {
                System.out.print("Enter your choice: ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String getValidatedInput(String prompt, ValidationFunction validator, String errorMessage) {
        while (true) {
            String input = getStringInput(prompt);
            if (validator.validate(input)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }

    @FunctionalInterface
    private interface ValidationFunction {
        boolean validate(String input);
    }
}