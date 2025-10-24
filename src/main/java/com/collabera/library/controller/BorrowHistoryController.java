package com.collabera.library.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collabera.library.model.BorrowHistory;
import com.collabera.library.service.BorrowHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class BorrowHistoryController {

    private final BorrowHistoryService borrowHistoryService;

	/** Get all borrow history records 
	 * 
	 * */
    @GetMapping
    public ResponseEntity<List<BorrowHistory>> getAllHistory() {
        return ResponseEntity.ok(borrowHistoryService.getAllHistory());
    }

	/** Get borrow history by borrower code 
	 * 
	 * */
    @GetMapping("/borrower/{code}")
    public ResponseEntity<List<BorrowHistory>> getByBorrower(@PathVariable String code) {
        return ResponseEntity.ok(borrowHistoryService.getByBorrower(code));
    }

	/** Get borrow history by book code 
	 * 
	 * */
    @GetMapping("/book/{code}")
    public ResponseEntity<List<BorrowHistory>> getByBook(@PathVariable String code) {
        return ResponseEntity.ok(borrowHistoryService.getByBook(code));
    }
}
