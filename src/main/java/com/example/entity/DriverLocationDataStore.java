package com.example.entity;

import com.example.models.DriverLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverLocationDataStore {
    private static DriverLocationDataStore instance;
    private Map<String, List<DriverLocation>> driversLocation = new HashMap<>();

    private DriverLocationDataStore() {
    }

    public static DriverLocationDataStore getInstance() {
        if (instance == null) {
            instance = new DriverLocationDataStore();
        }
        return instance;
    }

    public List<DriverLocation> getDriverLocations(String geoHash) {
        return driversLocation.getOrDefault(geoHash, new ArrayList<>());
    }

    public void updateDriverLocation(DriverLocation driverLocation) {
        driversLocation.computeIfAbsent(driverLocation.getGeoHash(), k -> new ArrayList<>()).add(driverLocation);
    }
}