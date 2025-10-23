package com.example.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.model.Borrower;
import com.example.library.repository.BorrowerRepository;

@Service
public class BorrowerService {
	
	@Autowired
	private BorrowerRepository borrowerRepository;
	
	public Borrower registerBorrower(Borrower borrower) {
	    return borrowerRepository.save(borrower);
	}
	

}
