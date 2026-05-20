package com.library.focus.point.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.library.focus.point.model.Student;
import com.library.focus.point.repo.StudentRepository;

import jakarta.validation.Valid;

@Controller
public class RegistrationController {

	@Autowired
	StudentRepository repository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/register-page")
	public String registerPage(Model model) {

	    model.addAttribute("student",
	                       new Student());

	    return "register";
	}

	@PostMapping("/register")
	public String register(
	        @Valid @ModelAttribute Student student,
	        BindingResult result,
	        Model model) {

	    if(repository.existsByEmail(student.getEmail())) {

	        result.rejectValue(
	                "email",
	                "error.student",
	                "Email already registered"
	        );
	    }

	    if(repository.existsByMobile(student.getMobile())) {

	        result.rejectValue(
	                "mobile",
	                "error.student",
	                "Mobile number already registered"
	        );
	    }

	    if(result.hasErrors()) {

	        model.addAttribute("student",
	                           student);

	        return "register";
	    }

	    student.setPassword(
	    	    passwordEncoder.encode(
	    	        student.getPassword()
	    	    )
	    	);
	    student.setRole("STUDENT");

	    	repository.save(student);

	    return "redirect:/";
	}

}
