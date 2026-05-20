package com.library.focus.point.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.focus.point.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	
	boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    Optional<Student> findByEmail(String email);
    List<Student> findByRole(String role);

}
