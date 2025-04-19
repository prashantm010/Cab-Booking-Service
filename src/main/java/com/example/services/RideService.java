package com.example.services;

import com.example.entity.RideDataStore;
import com.example.entity.UserDataStore;
import com.example.enums.CabType;
import com.example.enums.RideStatus;
import com.example.exceptions.FareMismatchException;
import com.example.exceptions.NoDriverFoundException;
import com.example.exceptions.RideBookingFailureException;
import com.example.exceptions.RideNotFoundException;
import com.example.factories.CabFactory;
import com.example.interfaces.Cab;
import com.example.models.DriverLocation;
import com.example.models.DriverProfile;
import com.example.models.Ride;
import com.example.managers.DriverLockManager;
import com.example.utils.HelperUtil;
import com.example.utils.OtpGenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class RideService {
    private final RideDataStore rideDataStore = RideDataStore.getInstance();
    private final UserDataStore userDataStore = UserDataStore.getInstance();
    private final DriverService driverService = new DriverService();
    private final MatcherService matchingService = new MatcherService();
    private static final long LOCK_TIMEOUT_MS = 100;

    public Ride bookRide(String userId, CabType cabType, double originLat, double originLon, double destLat, double destLon) {
        DriverLocation driverLocation = matchingService.findNearestDriver(originLat, originLon);
        if (driverLocation == null) throw new NoDriverFoundException("No drivers available nearby");

        String driverId = driverLocation.getDriverId();
        DriverProfile driverProfile = driverService.getDriverProfile(driverId);
        if (!driverProfile.getCabType().equals(cabType)) {
            throw new RideBookingFailureException("No drivers available for the selected cab type");
        }

        ReentrantLock lock = DriverLockManager.getInstance().getDriverLock(driverId);
        Boolean acquired = Boolean.FALSE;
        try {
            acquired = lock.tryLock(LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (acquired && driverProfile.isAvailable()) {
                driverProfile.assignToRide();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RideBookingFailureException("Thread interrupted while trying to acquire lock " + e.getMessage());
        } finally {
            if (Boolean.TRUE.equals(acquired)) {
                lock.unlock();
            }
        }

        String rideStartOtp = OtpGenerator.generateOtp();
        String rideEndOtp = OtpGenerator.generateOtp();
        double fare = getRideFare(cabType, originLat, originLon, destLat, destLon, null, null);
        Ride ride = new Ride(userId, cabType, driverLocation.getDriverId(), originLat, originLon, destLat, destLon, fare, rideStartOtp, rideEndOtp);
        rideDataStore.addRide(ride);
        userDataStore.getUser(userId).addRide(ride);
        return ride;
    }

    public Double showFare(CabType cabType, double originLat, double originLon, double destLat, double destLon) {
        Cab cab = CabFactory.getCab(cabType);
        Double rideDistance = HelperUtil.calculateDistance(originLat, originLon, destLat, destLon);
        Double rideTime = HelperUtil.calculateRideTime(originLat, originLon, destLat, destLon);
        return cab.calculateFare(rideDistance, rideTime);
    }

    public Double showDistance(double originLat, double originLon, double destLat, double destLon) {
        return HelperUtil.calculateDistance(originLat, originLon, destLat, destLon);
    }

    public List<DriverLocation> showNearByDrivers(double originLat, double originLon) {
        return matchingService.getAllNearByDrivers(originLat, originLon);
    }

    public void startRide(String rideId, String startRideOtp) {
        Ride ride = rideDataStore.getRide(rideId);
        if (Objects.isNull(ride)) {
            throw new RideNotFoundException("Ride not found");
        }
        if (!RideStatus.BOOKED.equals(ride.getStatus())) {
            throw new RideBookingFailureException("Ride cannot be started, as ride status is not Booked");
        }
        if (!ride.getRideStartOtp().equals(startRideOtp)) {
            throw new RideBookingFailureException("Invalid OTP for ride start, Please provide correct OTP!");
        }
        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setRideStartTime(LocalDateTime.now());
    }

    public Double completeRide(String rideId, String endRideOtp) {
        Ride ride = rideDataStore.getRide(rideId);
        if (Objects.isNull(ride)) {
            throw new RideNotFoundException("Ride not found");
        }
        if (!ride.getRideEndOtp().equals(endRideOtp)) {
            throw new RideBookingFailureException("Invalid OTP for ride completion");
        }
        if (!RideStatus.IN_PROGRESS.equals(ride.getStatus())) {
            throw new RideBookingFailureException("Ride cannot be completed, as ride status is not In Progress");
        }
        ride.setRideEndTime(LocalDateTime.now());
        double fare = getRideFare(ride.getCabType(), ride.getOriginLat(), ride.getOriginLon(), ride.getDestinationLat(), ride.getDestinationLon(), ride.getRideStartTime(), ride.getRideEndTime());
        if (ride.getFare() < fare) {
            System.out.println("Ride Fare has been revised due to traffic conditions");
            ride.setFare(fare);
        }
        ride.setStatus(RideStatus.PAYMENT_PENDING);
        DriverProfile driverProfile = driverService.getDriverProfile(ride.getDriverId());
        ReentrantLock lock = DriverLockManager.getInstance().getDriverLock(driverProfile.getDriverId());
        lock.lock();
        try {
            driverProfile.release();
        } finally {
            lock.unlock();
        }
        return ride.getFare();
    }

    public void cancelRide(String rideId) {
        Ride ride = rideDataStore.getRide(rideId);
        if (Objects.isNull(ride)) {
            throw new RideNotFoundException("Ride not found");
        }
        if (!RideStatus.BOOKED.equals(ride.getStatus())) {
            throw new RideBookingFailureException("Ride cannot be cancelled, as ride status is not Booked");
        }
        ride.setStatus(RideStatus.CANCELLED);
        DriverProfile driverProfile = driverService.getDriverProfile(ride.getRideId());
        ReentrantLock lock = DriverLockManager.getInstance().getDriverLock(driverProfile.getDriverId());
        lock.lock();
        try {
            driverProfile.release();
        } finally {
            lock.unlock();
        }
    }

    public List<Ride> getUserRides(String userId) {
        return userDataStore.getUser(userId).getRideHistory();
    }

    public void markRideCompleted(String rideId) {
        Ride ride = rideDataStore.getRide(rideId);
        if (Objects.isNull(ride)) {
            throw new RideNotFoundException("Ride not found");
        }
        if (!RideStatus.PAYMENT_PENDING.equals(ride.getStatus())) {
            throw new RideBookingFailureException("Ride cannot be marked completed, as ride status is not Payment Pending");
        }
        ride.setStatus(RideStatus.COMPLETED);
        rideDataStore.addRide(ride);
    }

    private Double getRideFare(CabType cabType, Double originLat, Double originLon, Double destLat, Double destLon, LocalDateTime rideStartTime, LocalDateTime rideEndTime) {
        Cab cab = CabFactory.getCab(cabType);
        Double rideDistance = HelperUtil.calculateDistance(originLat, originLon, destLat, destLon);
        Double rideTime = HelperUtil.calculateRideTime(originLat, originLon, destLat, destLon);
        if (Objects.nonNull(rideStartTime) && Objects.nonNull(rideEndTime)) {
            rideTime = Duration.between(rideStartTime, rideEndTime).toMinutes() * 100.0 / 100.0;
            if (rideTime < 0) {
                rideTime = 1.0;
            }
        }
        return cab.calculateFare(rideDistance, rideTime);
    }
}
