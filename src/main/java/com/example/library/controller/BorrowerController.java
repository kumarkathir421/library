package com.example.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.model.Borrower;
import com.example.library.service.BorrowerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

	@Autowired
	private BorrowerService borrowerService;

	@PostMapping
	public ResponseEntity<Borrower> registerBorrower(@RequestBody @Valid Borrower borrower) {
		Borrower registeredBorrower = borrowerService.registerBorrower(borrower);
		return ResponseEntity.ok(registeredBorrower);
	}
}
