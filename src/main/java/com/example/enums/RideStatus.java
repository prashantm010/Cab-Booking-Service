package com.example.enums;

public enum RideStatus {
    IN_PROGRESS,
    BOOKED,
    COMPLETED,
    PAYMENT_PENDING,
    CANCELLED;

    public static RideStatus fromString(String status) {
        for (RideStatus rideStatus : RideStatus.values()) {
            if (rideStatus.name().equalsIgnoreCase(status)) {
                return rideStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
