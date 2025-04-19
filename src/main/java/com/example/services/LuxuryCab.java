package com.example.services;

import com.example.interfaces.Cab;

public class LuxuryCab implements Cab {
    @Override
    public String getType() {
        return "Luxury";
    }

    @Override
    public double calculateFare(Double distanceInKm, Double waitingTimeInMinutes) {
        return Math.round(25.0 * distanceInKm + waitingTimeInMinutes) * 100.0 / 100.0;
    }
}