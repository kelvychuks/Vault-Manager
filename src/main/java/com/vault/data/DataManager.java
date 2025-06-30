package com.vault.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vault.model.User;
import com.vault.model.UserData;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static final String USERS_FILE = "C:\\Users\\User\\IdeaProjects\\Vault\\src\\main\\resources\\data\\users.json";
    private static final String PROPERTIES_FILE = "C:\\Users\\User\\IdeaProjects\\Vault\\src\\main\\resources\\data\\userproperties.json";
    final private Gson gson;

    public DataManager() {
        this.gson = new Gson();
    }

    public List<User> loadUsers() {
        // Load the file from the 'data' folder inside resources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/users.json");

        if (inputStream == null) {
            System.out.println("data/users.json not found in resources.");
            return new ArrayList<>(); // Return an empty list if a file isn't found
        }

        InputStreamReader reader = new InputStreamReader(inputStream);
        Type listType = new TypeToken<List<User>>() {}.getType();
        return gson.fromJson(reader, listType);
    }

    public boolean saveUser(User user) {
        try {
            List<User> users = loadUsers();
            users.add(user);

            try (FileWriter writer = new FileWriter(USERS_FILE)) {
                gson.toJson(users, writer);
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error saving users.json: " + e.getMessage());
            return false;
        }
    }

    public boolean addUser(User user) {
        List<User> users = loadUsers();
        users.add(user);
        return saveUser(user);
    }

    public Map<String, String> loadUserProperties(String userId) {
        File file = new File(PROPERTIES_FILE);
        if (!file.exists()) return new HashMap<>();

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<List<UserData>>() {}.getType();
            List<UserData> userList = gson.fromJson(reader, type);
            if (userList != null) {
                for (UserData user : userList) {
                    if (user.userId.equals(userId)) {
                        return user.properties;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return new HashMap<>();
    }

    public boolean saveUserProperties(String userId, Map<String, String> properties) {
        File file = new File(PROPERTIES_FILE);
        List<UserData> userList = new ArrayList<>();

        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<List<UserData>>() {}.getType();
                userList = gson.fromJson(reader, type);
                if (userList == null) userList = new ArrayList<>();
            } catch (IOException e) {
                System.out.println("Error reading files: " + e.getMessage());
            }
        }

        boolean found = false;
        for (UserData user : userList) {
            if (user.userId.equals(userId)) {
                user.properties = new HashMap<>(properties);
                found = true;
                break;
            }
        }

        if (!found) {
            userList.add(new UserData(userId, properties));
        }
        try (Writer writer = new FileWriter(file,false)) {
            gson.toJson(userList, writer);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }

    public User getUserByEmail(String email) {
        List<User> users = loadUsers();
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

}