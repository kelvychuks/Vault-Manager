package com.vault.model;
import java.util.HashMap;
import java.util.Map;

public class UserData {
    public String userId;
    public Map<String, String> properties;

        public UserData(String userId, Map<String, String> properties) {
            this.userId = userId;
           this.properties = properties;
        }
    }


