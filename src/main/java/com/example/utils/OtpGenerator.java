package com.example.utils;

import java.util.Random;

public class OtpGenerator {

    public static String generateOtp() {
        return String.format("%04d", new Random().nextInt(9999));
    }
}
