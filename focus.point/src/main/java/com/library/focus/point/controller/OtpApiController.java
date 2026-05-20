package com.library.focus.point.controller;

import com.library.focus.point.service.OtpService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OtpApiController {

    @Autowired
    OtpService otpService;

    @Autowired
    JavaMailSender mailSender;

    @PostMapping("/api/send-otp")
    public String sendOtp(

            @RequestBody
            Map<String,String> body){

        String email =
                body.get("email");

        String otp =
                otpService.generateOtp(
                        email
                );

        SimpleMailMessage mail =
                new SimpleMailMessage();

        mail.setTo(email);

        mail.setSubject(
                "Focus Point Library OTP"
        );

        mail.setText(
                "Your OTP is: " + otp
        );

        mailSender.send(mail);

        return "OTP SENT";
    }

    @PostMapping("/api/verify-otp")
    public String verifyOtp(

            @RequestBody
            Map<String,String> body){

        boolean verified =

                otpService.verifyOtp(

                        body.get("email"),

                        body.get("otp")
                );

        if(verified){

            return "OTP VERIFIED";
        }

        return "INVALID OTP";
    }
    
    
    @PostMapping("/api/resend-otp")
    public String resendOtp(

            @RequestBody
            Map<String,String> body){

        String email =
                body.get("email");

        String otp =
                otpService.generateOtp(
                        email
                );

        SimpleMailMessage mail =
                new SimpleMailMessage();

        mail.setTo(email);

        mail.setSubject(
                "Focus Point Library New OTP"
        );

        mail.setText(

                "Your New OTP is: "
                        + otp
        );

        mailSender.send(mail);

        return "NEW OTP SENT";
    }
}