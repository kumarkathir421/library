package com.example.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.model.Book;
import com.example.library.model.Borrower;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowerRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	BorrowerRepository borrowerRepository;

	public Book registerBook(Book book) {
		return bookRepository.save(book);
	}

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public void borrowBook(Long bookId, Long borrowerId) {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

		if (book.getBorrowedBy() != null) {
			throw new RuntimeException("Book is already borrowed");
		}

		Borrower borrower = borrowerRepository.findById(borrowerId)
				.orElseThrow(() -> new RuntimeException("Borrower not found"));

		book.setBorrowedBy(borrower);
		bookRepository.save(book);
	}
	
	public void returnBook(Long bookId) {
	    Book book = bookRepository.findById(bookId)
	            .orElseThrow(() -> new RuntimeException("Book not found"));

	    if (book.getBorrowedBy() == null) {
	        throw new RuntimeException("Book is not currently borrowed");
	    }

	    book.setBorrowedBy(null);
	    bookRepository.save(book);
	}

}
