package com.example;

import com.example.enums.CabType;
import com.example.models.*;
import com.example.services.*;
import com.example.utils.HelperUtil;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        UserService userService = new UserService();
        DriverService driverService = new DriverService();
        DriversLocationService driversLocationService = new DriversLocationService();
        RideService rideService = new RideService();
        PaymentService paymentService = new PaymentService();

        UserProfile user = userService.createUser("Alice", "alice@mail.com", "9917381111");
        DriverProfile driverProfile1 = driverService.registerDriver("Bob1", "bob1@mail.com", "9917381131", "KA01AB1234", CabType.STANDARD);
        DriverProfile driverProfile2 = driverService.registerDriver("Bob2", "bob2@mail.com", "9917381132", "KA01AB1235", CabType.STANDARD);
        DriverProfile driverProfile3 = driverService.registerDriver("Bob3", "bob3@mail.com", "9917381133", "KA01AB1236", CabType.STANDARD);

        System.out.println("User registered with Name: " + user.getName());
        System.out.println("Driver registered with Name: " + driverProfile1.getName());
        System.out.println("Driver registered with Name: " + driverProfile2.getName());
        System.out.println("Driver registered with Name: " + driverProfile3.getName());

        driversLocationService.updateDriverLocation(driverProfile1.getUserId(), 12.91, 77.61);
        driversLocationService.updateDriverLocation(driverProfile2.getUserId(), 12.90, 77.62);
        driversLocationService.updateDriverLocation(driverProfile3.getUserId(), 12.92, 77.61);

        List<DriverLocation> driverLocations = rideService.showNearByDrivers(12.91, 77.60);
        System.out.println("Nearby drivers:");
        for (DriverLocation location : driverLocations) {
            System.out.println("Driver ID: " + location.getDriverId() + ", Distance: " + HelperUtil.calculateDistance(12.91, 77.60, location.getLatitude(), location.getLongitude()));
        }
        Double distance = rideService.showDistance(12.91, 77.60, 12.95, 77.65);
        System.out.println("Distance: " + distance + " km");
        for (CabType cabType : CabType.values()) {
            Double fare = rideService.showFare(cabType, 12.91, 77.60, 12.95, 77.65);
            System.out.println("Estimated fare: " + fare + " , for cabType: " + cabType);
        }

        Ride ride = rideService.bookRide(user.getUserId(), CabType.STANDARD, 12.91, 77.60, 12.95, 77.65);
        System.out.println("Ride booked with ID: " + ride.getRideId());
        System.out.println("Please share OTP for starting ride " + ride.getRideStartOtp());

        rideService.startRide(ride.getRideId(), ride.getRideStartOtp());
        System.out.println("Ride started successfully");

        Thread.sleep(10000); // Simulate ride duration

        System.out.println("Please share OTP for ending ride " + ride.getRideEndOtp());
        Double rideFare = rideService.completeRide(ride.getRideId(), ride.getRideEndOtp());
        System.out.println("Please pay amount: " + rideFare);

        Payment payment = paymentService.makePayment(ride.getRideId(), ride.getFare());
        System.out.println("Payment successful: " + payment.getPaymentId());
        System.out.println("Ride completed successfully");

        List<Ride> history = rideService.getUserRides(user.getUserId());
        System.out.println("Ride history for User: " + history + " ride(s)");
    }
}