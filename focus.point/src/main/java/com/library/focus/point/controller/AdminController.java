package com.library.focus.point.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.focus.point.model.Fees;
import com.library.focus.point.model.Seat;
import com.library.focus.point.model.Student;
import com.library.focus.point.repo.FeesRepo;
import com.library.focus.point.repo.SeatRepository;
import com.library.focus.point.repo.StudentRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	StudentRepository repository;

	@Autowired
	SeatRepository seatRepository;
	
	@Autowired
	FeesRepo feesRepo;

//	@GetMapping("/dashboard")
//	public String dashboard(Model model) {
//
//		model.addAttribute("students", repository.findAll());
//
//		return "admin-dashboard";
//	}

	
	@GetMapping("/dashboard")
	public String adminDashboard(
	        Model model){

	    List<Student> students =
	    		repository.findAll();

	    List<Fees> feesList =
	            feesRepo.findAll();

	    int totalStudents =
	            students.size();

	    double totalFees = 0;

	    double pendingFees = 0;

	    double monthlyCollection = 0;

	    String currentMonth =
	            LocalDateTime.now()
	            .getMonth()
	            .getDisplayName(
	                    TextStyle.FULL,
	                    Locale.ENGLISH
	            );

	    for(Fees fee : feesList){

	        if(fee.getStatus()
	                .equals("PAID")){

	            totalFees +=
	                    fee.getAmount();

	            if(fee.getMonth()
	                    .contains(currentMonth)){

	                monthlyCollection +=
	                        fee.getAmount();
	            }
	        }

	        if(fee.getStatus()
	                .equals("PENDING")){

	            pendingFees +=
	                    fee.getAmount();
	        }
	    }

	    model.addAttribute(
	            "students",
	            students
	    );

	    model.addAttribute(
	            "totalStudents",
	            totalStudents
	    );

	    model.addAttribute(
	            "totalFees",
	            totalFees
	    );

	    model.addAttribute(
	            "pendingFees",
	            pendingFees
	    );

	    model.addAttribute(
	            "monthlyCollection",
	            monthlyCollection
	    );

	    return "admin-dashboard";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteStudent(@PathVariable Long id) {

		repository.deleteById(id);

		return "redirect:/admin/dashboard";
	}

	@GetMapping("/make-admin/{id}")
	public String makeAdmin(@PathVariable Long id) {

		Student student = repository.findById(id).orElse(null);

		if (student != null) {

			student.setRole("ADMIN");

			repository.save(student);
		}

		return "redirect:/admin/dashboard";
	}

	@GetMapping("/seats")
	public String manageSeats(Model model) {

		model.addAttribute("seats", seatRepository.findAll());

		model.addAttribute("students", repository.findByRole("STUDENT"));

		return "admin-seats";
	}

	@PostMapping("/assign-seat")
	public String assignSeat(

	        @RequestParam Long seatId,

	        @RequestParam String email){

	    Seat oldSeat =
	            seatRepository.findByBookedBy(email);

	    if(oldSeat != null){

	        oldSeat.setBooked(false);

	        oldSeat.setBookedBy(null);

	        seatRepository.save(oldSeat);
	    }

	    Seat seat =
	            seatRepository.findById(seatId)
	            .orElse(null);

	    if(seat != null){

	        seat.setBooked(true);

	        seat.setBookedBy(email);

	        seatRepository.save(seat);
	    }

	    return "redirect:/admin/seats";
	}

	@GetMapping("/unassign-seat/{id}")
	public String unassignSeat(@PathVariable Long id) {

		Seat seat = seatRepository.findById(id).orElse(null);

		if (seat != null) {

			seat.setBooked(false);

			seat.setBookedBy(null);

			seatRepository.save(seat);
		}

		return "redirect:/admin/seats";
	}

	
	@GetMapping("/approve/{id}")
	public String approveStudent(

	        @PathVariable Long id){

	    Student student =
	    		repository.findById(id)
	            .orElse(null);

	    if(student != null){

	        student.setApproved(true);

	        repository.save(student);
	    }

	    return "redirect:/admin/dashboard";
	}
	
	@GetMapping("/fees")
	public String feesPage(
	        Model model){

	    model.addAttribute(

	            "students",

	            repository.findAll()
	    );

	    return "admin-fees";
	}
	
	@PostMapping("/save-fees")
	public String saveFees(

	        @RequestParam Long studentId,

	        @RequestParam String month,

	        @RequestParam Double amount,

	        @RequestParam String status,

	        @RequestParam String remarks,

	        Principal principal){

	    Student student =
	            repository.findById(studentId)
	            .orElse(null);

	    if(student != null){

	        Fees fees = new Fees();

	        fees.setStudent(student);

	        fees.setMonth(month);

	        fees.setAmount(amount);

	        fees.setStatus(status);

	        fees.setRemarks(remarks);

	        fees.setAddedBy(
	                principal.getName()
	        );

	        fees.setPaymentDate(
	                LocalDateTime.now()
	                .toString()
	        );

	        feesRepo.save(fees);
	    }

	    return "redirect:/admin/student-fees/"
	            + studentId;
	}
	
	@GetMapping("/student-fees/{id}")
	public String studentFeesPage(

	        @PathVariable Long id,

	        Model model){

	    Student student =
	            repository.findById(id)
	            .orElse(null);

	    List<Fees> feesList =
	            feesRepo.findByStudent(student);

	    model.addAttribute(
	            "student",
	            student
	    );

	    model.addAttribute(
	            "feesList",
	            feesList
	    );

	    return "student-fees";
	}
}
