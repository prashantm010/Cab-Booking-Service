package com.example.entity;

import com.example.models.UserProfile;

import java.util.HashMap;
import java.util.Map;

public class UserDataStore {
    private static UserDataStore instance;
    private Map<String, UserProfile> users = new HashMap<>();

    private UserDataStore() {
    }

    public static UserDataStore getInstance() {
        if (instance == null) {
            instance = new UserDataStore();
        }
        return instance;
    }

    public void addUser(UserProfile user) {
        users.put(user.getUserId(), user);
    }

    public UserProfile getUser(String userId) {
        return users.get(userId);
    }
}