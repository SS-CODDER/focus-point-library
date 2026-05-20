package com.library.focus.point.config;

import com.library.focus.point.model.Seat;

import com.library.focus.point.repo.SeatRepository;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SeatLoader {

    @Bean
    CommandLineRunner loadSeats(
            SeatRepository repository){

        return args -> {

            if(repository.count() == 0){

                for(int i=1; i<=36; i++){

                    Seat seat = new Seat();

                    seat.setSeatNumber(
                            "S-" + i
                    );

                    seat.setBooked(false);

                    repository.save(seat);
                }
            }
        };
    }
}