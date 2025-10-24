package com.collabera.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collabera.library.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	List<Book> findByIsbn(String isbn);
	Optional<Book> findByCode(String bookCode);
	
}
