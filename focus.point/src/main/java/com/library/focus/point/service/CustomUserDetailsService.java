package com.library.focus.point.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.library.focus.point.model.Student;
import com.library.focus.point.repo.StudentRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    @Autowired
    StudentRepository repository;

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        Student student =
                repository
                .findByEmail(email)

                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        ));
        
        if(!student.isApproved()){

            throw new RuntimeException(
                    "Account Not Approved"
            );
        };

        return new User(

                student.getEmail(),

                student.getPassword(),
                
              

                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + student.getRole()
                        )
                )
        );
    }
}