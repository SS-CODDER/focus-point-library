package com.library.focus.point.controller;

import com.library.focus.point.model.Seat;

import com.library.focus.point.repo.SeatRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class SeatController {

    @Autowired
    SeatRepository repository;

    @GetMapping("/seats")
    public String seats(Model model){

        model.addAttribute(
                "seats",
                repository.findAll()
        );

        return "seats";
    }

    @GetMapping("/book-seat/{id}")
    public String bookSeat(
            @PathVariable Long id,
            Principal principal){

        String email =
                principal.getName();

        if(repository.existsByBookedBy(email)){

            return "redirect:/seats";
        }

        Seat seat =
                repository.findById(id)
                .orElse(null);

        if(seat != null &&
           !seat.isBooked()){

            seat.setBooked(true);

            seat.setBookedBy(email);

            repository.save(seat);
        }

        return "redirect:/seats";
    }
    
    
    
}