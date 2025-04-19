package com.example.services;

import com.example.interfaces.Cab;

public class SharedCab implements Cab {
    @Override
    public String getType() {
        return "Shared";
    }

    @Override
    public double calculateFare(Double distanceInKm, Double waitingTimeInMinutes) {
        return Math.round(6.0 * distanceInKm + waitingTimeInMinutes) * 100.0 / 100.0;
    }
}