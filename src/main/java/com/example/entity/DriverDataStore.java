package com.example.entity;

import com.example.models.DriverProfile;

import java.util.HashMap;
import java.util.Map;

public class DriverDataStore {
    private static DriverDataStore instance;
    private Map<String, DriverProfile> drivers = new HashMap<>();

    private DriverDataStore() {
    }

    public static DriverDataStore getInstance() {
        if (instance == null) {
            instance = new DriverDataStore();
        }
        return instance;
    }

    public void addDriver(DriverProfile driver) {
        drivers.put(driver.getDriverId(), driver);
    }

    public DriverProfile getDriver(String driverId) {
        return drivers.getOrDefault(driverId, null);
    }
}