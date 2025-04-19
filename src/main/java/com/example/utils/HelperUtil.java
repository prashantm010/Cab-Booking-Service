package com.example.utils;

public class HelperUtil {
    public static final Double BASE_FARE = 15.0;

    public static Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round((R * c) * 100.0) / 100.0; // Convert to km and round to 2 decimal places
    }

    public static Double calculateRideTime(Double lat1, Double lon1, Double lat2, Double lon2) {
        // Assuming an average speed of 30 km/h
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        return Math.round((distance / 30) * 60 * 100.0) / 100.0; // In Minutes
    }
}
