package com.example.services;

import com.example.entity.UserDataStore;
import com.example.models.UserProfile;

public class UserService {
    private final UserDataStore userDataStore = UserDataStore.getInstance();

    public UserProfile createUser(String name, String email, String phone) {
        UserProfile user = new UserProfile(name, email, phone);
        userDataStore.addUser(user);
        return user;
    }

    public UserProfile getUser(String userId) {
        return userDataStore.getUser(userId);
    }
}