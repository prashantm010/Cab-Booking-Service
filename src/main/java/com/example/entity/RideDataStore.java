package com.example.entity;

import com.example.models.Ride;

import java.util.HashMap;
import java.util.Map;

public class RideDataStore {
    private static RideDataStore instance;
    private Map<String, Ride> rides = new HashMap<>();

    private RideDataStore() {
    }

    public static RideDataStore getInstance() {
        if (instance == null) {
            instance = new RideDataStore();
        }
        return instance;
    }

    public void addRide(Ride ride) {
        rides.put(ride.getRideId(), ride);
    }

    public Ride getRide(String rideId) {
        return rides.get(rideId);
    }
}