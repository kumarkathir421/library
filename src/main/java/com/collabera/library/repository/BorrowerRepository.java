package com.collabera.library.repository;

import com.collabera.library.model.Borrower;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

	Optional<Borrower> findByCode(String borrowerCode);

}
