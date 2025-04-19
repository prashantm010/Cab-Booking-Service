package com.example.models;


import com.example.enums.CabType;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;

public class DriverProfile extends UserProfile {
    @Getter
    String vehicleNumber;
    @Getter
    private final AtomicBoolean isAvailable = new AtomicBoolean(true);

    CabType cabType;

    public DriverProfile(String name, String email, String phone, String vehicleNumber, CabType cabType) {
        super(name, email, phone);
        this.name = name;
        this.vehicleNumber = vehicleNumber;
        this.cabType = cabType;
    }

    public String getDriverId() {
        return this.getUserId();
    }

    public Boolean isAvailable() {
        return isAvailable.get();
    }

    public boolean assignToRide() {
        return isAvailable.compareAndSet(true, false);
    }

    public void release() {
        isAvailable.set(false);
    }

    public CabType getCabType() {
        return cabType;
    }
}
