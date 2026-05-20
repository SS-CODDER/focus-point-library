package com.library.focus.point.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.HashMap;

import java.util.Map;

import java.util.Random;

@Service
public class OtpService {

    private Map<String, String> otpStorage =
            new HashMap<>();

    private Map<String, LocalDateTime>
            otpTimeStorage =
            new HashMap<>();

    public String generateOtp(
            String email){

        Random random =
                new Random();

        String otp =
                String.valueOf(

                        100000 +

                        random.nextInt(900000)
                );

        otpStorage.put(
                email,
                otp
        );

        otpTimeStorage.put(

                email,

                LocalDateTime.now()
        );

        return otp;
    }

    public boolean verifyOtp(

            String email,

            String otp){

        if(!otpStorage.containsKey(email)){

            return false;
        }

        LocalDateTime sentTime =

                otpTimeStorage.get(email);

        LocalDateTime now =
                LocalDateTime.now();

        long minutes =

                java.time.Duration
                        .between(
                                sentTime,
                                now
                        )

                        .toMinutes();

        if(minutes >= 1){

            otpStorage.remove(email);

            otpTimeStorage.remove(email);

            return false;
        }

        return otp.equals(
                otpStorage.get(email)
        );
    }
}