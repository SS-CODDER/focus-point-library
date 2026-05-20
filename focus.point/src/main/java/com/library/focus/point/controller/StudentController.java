package com.library.focus.point.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.library.focus.point.model.Fees;
import com.library.focus.point.model.Student;
import com.library.focus.point.repo.FeesRepo;
import com.library.focus.point.repo.StudentRepository;

@Controller
public class StudentController {

	@Autowired
	StudentRepository repository;

	@Autowired
	FeesRepo feesRepo;

	@GetMapping("/login-page")
	public String loginPage() {

		return "login";
	}

	@GetMapping("/dashboard")
	public String dashboard(Principal principal, Model model) {

		String email = principal.getName();

		Student student = repository.findByEmail(email).orElse(null);

		model.addAttribute("student", student);

		List<Fees> feesList =

				feesRepo.findByStudent(student);

		model.addAttribute("feesList", feesList);

		return "dashboard";
	}

	@GetMapping("/membership")
	public String membershipPage(

			Model model, Principal principal) {

		String name = principal.getName();

		Student student = repository.findByEmail(name).orElse(null);
		System.out.println(student.getName());

		List<Fees> feesList =

				feesRepo.findByStudent(student);
		
		feesList.forEach(System.out::println);

		model.addAttribute("student", student);

		model.addAttribute("feesList", feesList);

		return "membership";
	}

}
