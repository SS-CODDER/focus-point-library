package com.library.focus.point.repo;

import com.library.focus.point.model.Fees;

import com.library.focus.point.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeesRepo
extends JpaRepository<Fees,Long> {

    List<Fees> findByStudent(
            Student student
    );
}