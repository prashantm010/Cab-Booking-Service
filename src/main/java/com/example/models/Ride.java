package com.example.models;

import com.example.enums.CabType;
import com.example.enums.RideStatus;
import com.example.utils.IdGenerator;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Ride {
    String rideId;
    String userId;
    String driverId;
    CabType cabType;
    Double originLat;
    Double originLon;
    Double destinationLat;
    Double destinationLon;
    Double fare;
    RideStatus status;
    String rideStartOtp;
    String rideEndOtp;
    LocalDateTime rideStartTime;
    LocalDateTime rideEndTime;

    public Ride(String userId, CabType cabType, String driverId, Double originLat, Double originLon, Double destinationLat, Double destinationLon, Double fare, String rideStartOtp, String rideEndOtp) {
        this.rideId = IdGenerator.getId();
        this.userId = userId;
        this.cabType = cabType;
        this.driverId = driverId;
        this.originLat = originLat;
        this.originLon = originLon;
        this.destinationLat = destinationLat;
        this.destinationLon = destinationLon;
        this.fare = fare;
        this.status = RideStatus.BOOKED;
        this.rideStartOtp = rideStartOtp;
        this.rideEndOtp = rideEndOtp;
    }
}
