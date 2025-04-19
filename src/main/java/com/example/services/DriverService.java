package com.example.services;

import com.example.entity.DriverDataStore;
import com.example.enums.CabType;
import com.example.models.DriverProfile;

public class DriverService {
    private final DriverDataStore driverDataStore = DriverDataStore.getInstance();

    public DriverProfile registerDriver(String name, String email, String phone, String vehicleNumber, CabType cabType) {
        DriverProfile driver = new DriverProfile(name, email, phone, vehicleNumber, cabType);
        driverDataStore.addDriver(driver);
        return driver;
    }

    public DriverProfile getDriverProfile(String driverId) {
        return driverDataStore.getDriver(driverId);
    }
}