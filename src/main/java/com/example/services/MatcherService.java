package com.example.services;

import com.example.models.DriverLocation;
import com.example.utils.HelperUtil;

import java.util.Comparator;
import java.util.List;

public class MatcherService {
    public static final Double RADIUS = 3.0;
    private final DriversLocationService driverLocationService = new DriversLocationService();

    public DriverLocation findNearestDriver(Double lat, Double lon) {
        List<DriverLocation> nearestDrivers = driverLocationService.getNearByDrivers(lat, lon, RADIUS);
        return nearestDrivers.stream()
                .min(Comparator.comparingDouble(
                        driver ->
                                HelperUtil.calculateDistance(driver.getLatitude(), driver.getLongitude(), lat, lon)))
                .orElse(null);
    }

    public List<DriverLocation> getAllNearByDrivers(Double lat, Double lon) {
        return driverLocationService.getNearByDrivers(lat, lon, RADIUS);
    }
}
