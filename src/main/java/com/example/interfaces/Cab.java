package com.example.interfaces;

public interface Cab {
    String getType();

    double calculateFare(Double distanceInKm, Double waitingTimeInMinutes);
}