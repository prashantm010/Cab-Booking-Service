package com.example.models;

import com.example.utils.IdGenerator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserProfile {
    String userId;
    String name;
    String email;
    String phone;
    List<Ride> rideHistory;

    public UserProfile(String name, String email, String phone) {
        this.userId = IdGenerator.getId();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.rideHistory = new ArrayList<>();
    }

    public void addRide(Ride ride) {
        this.rideHistory.add(ride);
    }
}
