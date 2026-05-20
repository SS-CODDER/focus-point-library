package com.library.focus.point.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    @Autowired
    JavaMailSender mailSender;

    @PostMapping("/send-message")
    public String sendMessage(

            @RequestParam String name,

            @RequestParam String email,

            @RequestParam String message,

            RedirectAttributes redirectAttributes){

        String emailRegex =
                "^[A-Za-z0-9+_.-]+@(.+)$";

        boolean validEmail =
                Pattern.matches(
                        emailRegex,
                        email
                );

        if(!validEmail){

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Please Enter Valid Email"
            );

            return "redirect:/#contact";
        }

        SimpleMailMessage mail =
                new SimpleMailMessage();

        mail.setTo(
                "focuspoint.library991@gmail.com"
        );

        mail.setSubject(
                "New Contact Form Message"
        );

        mail.setText(

                "Name: " + name +

                "\nEmail: " + email +

                "\n\nMessage:\n" + message
        );

        mailSender.send(mail);

        redirectAttributes.addFlashAttribute(
                "success",
                "Message Sent Successfully"
        );

        return "redirect:/#contact";
    }
}
