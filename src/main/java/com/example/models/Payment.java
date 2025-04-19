package com.example.models;

import com.example.enums.PaymentStatus;
import com.example.utils.IdGenerator;
import lombok.Data;

@Data
public class Payment {
    String paymentId;
    String rideId;
    double amount;
    PaymentStatus status;

    public Payment(String rideId, double amount, PaymentStatus status) {
        this.paymentId = IdGenerator.getId();
        this.rideId = rideId;
        this.amount = amount;
        this.status = status;
    }
}
