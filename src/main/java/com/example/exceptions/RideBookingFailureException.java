package com.example.exceptions;

public class RideBookingFailureException extends RuntimeException {
    public RideBookingFailureException(String s) {
        super(s);
    }
}
