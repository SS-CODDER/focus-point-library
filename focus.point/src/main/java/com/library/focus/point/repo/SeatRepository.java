package com.library.focus.point.repo;

import com.library.focus.point.model.Seat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository
        extends JpaRepository<Seat, Long> {

	 boolean existsByBookedBy(String bookedBy);

	    Seat findByBookedBy(String bookedBy);
}