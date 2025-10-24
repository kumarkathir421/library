package com.collabera.library.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.collabera.library.dto.BorrowerDto;
import com.collabera.library.exception.ConflictException;
import com.collabera.library.model.Borrower;
import com.collabera.library.repository.BorrowerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Service layer
 * borrower registration and related operations.
 */
@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;

    /**
     * Register a new borrower.
     * email unique
     */
    @Transactional
    public BorrowerDto.BorrowerResponse registerBorrower(BorrowerDto.BorrowerRequest request) {
        // Check for duplicate email
        borrowerRepository.findAll().stream()
                .filter(b -> b.getEmail().equalsIgnoreCase(request.getEmail()))
                .findFirst()
                .ifPresent(existing -> {
                    throw new ConflictException("Email already registered: " + request.getEmail());
                });

        // Create new borrower entity
        Borrower borrower = new Borrower();
        borrower.setName(request.getName());
        borrower.setEmail(request.getEmail());

     // Save first to generate numeric ID
        Borrower saved = borrowerRepository.save(borrower);

        // Generate code like BORR-00001
        String code = String.format("BORR-%05d", saved.getId());
        saved.setCode(code);

        Borrower updated = borrowerRepository.save(saved);

        // Map to response DTO
        BorrowerDto.BorrowerResponse response = new BorrowerDto.BorrowerResponse(updated.getId(), updated.getCode(), updated.getName(), updated.getEmail());

        return response;
    }
    
    /**
     * Fetches all borrowers from the db and maps them to BorrowerResponse DTOs.
     *
     * @return List of borrowers with ID, code, name, and email.
     */
    public List<BorrowerDto.BorrowerResponse> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(b -> new BorrowerDto.BorrowerResponse(b.getId(), b.getCode(), b.getName(), b.getEmail()))
                .toList();
    }

}
