package com.collabera.library.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collabera.library.dto.BorrowerDto;
import com.collabera.library.service.BorrowerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing borrowers (library members).
 * Handles borrower registration and listing if extended in future.
 */
@RestController
@RequestMapping("/api/borrowers")
@Validated
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    /**
     * Register a new borrower in the library.
     */
    @PostMapping
    public ResponseEntity<BorrowerDto.BorrowerResponse> registerBorrower(
            @Valid @RequestBody BorrowerDto.BorrowerRequest request) {
    	BorrowerDto.BorrowerResponse response = borrowerService.registerBorrower(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Retrieves all registered borrowers in the system.
     *
     * @return List of BorrowerResponse DTO
     */
    @GetMapping
    public ResponseEntity<List<BorrowerDto.BorrowerResponse>> getAllBorrowers() {
        List<BorrowerDto.BorrowerResponse> borrowers = borrowerService.getAllBorrowers();
        return ResponseEntity.ok(borrowers);
    }

}
