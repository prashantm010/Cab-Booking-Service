package com.example.services;

import com.example.interfaces.Cab;

public class StandardCab implements Cab {
    @Override
    public String getType() {
        return "Standard";
    }

    @Override
    public double calculateFare(Double distanceInKm, Double waitingTimeInMinutes) {
        return Math.round(10.0 * distanceInKm + waitingTimeInMinutes) * 100.0 / 100.0;
    }
}
