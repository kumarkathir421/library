package com.example.library.service;

import com.example.library.model.BorrowHistory;
import com.example.library.repository.BorrowHistoryRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowHistoryServiceImpl {

	private final BorrowHistoryRepository borrowHistoryRepository;

	/**
     * Get all book history with borrower name from db 
     *
     * @return List of book code, title, borrower code, name and borrowed/returned date
     */
	public List<BorrowHistory> getAllHistory() {
		return borrowHistoryRepository.findAll();
	}

	public List<BorrowHistory> getByBorrower(String borrowerCode) {
		return borrowHistoryRepository.findByBorrowerCode(borrowerCode);
	}

	public List<BorrowHistory> getByBook(String bookCode) {
		return borrowHistoryRepository.findByBookCode(bookCode);
	}
}
