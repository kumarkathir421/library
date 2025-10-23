package com.example.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.model.Book;
import com.example.library.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@PostMapping
	public ResponseEntity<Book> registerBook(@RequestBody @Valid Book book) {
		Book registered = bookService.registerBook(book);
		return ResponseEntity.ok(registered);
	}

	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> bookList = bookService.getAllBooks();
		return ResponseEntity.ok(bookList);
	}

	@GetMapping("/borrow/{bookId}")
	public ResponseEntity<String> borrowBook(@PathVariable("bookId") Long bookId, @RequestParam Long borrowerId) {
		bookService.borrowBook(bookId, borrowerId);
		return ResponseEntity.ok("Book borrowed successfully");
	}
	
	@PostMapping("/return/{bookId}")
	public ResponseEntity<String> returnBook(@PathVariable Long bookId) {
	    bookService.returnBook(bookId);
	    return ResponseEntity.ok("Book returned successfully");
	}
	
}
