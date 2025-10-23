package com.example.library.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.dto.BookDto;
import com.example.library.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing books.
 * Provides endpoints to register, list, borrow, and return books.
 */
@RestController
@RequestMapping("/api/books")
@Validated
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    /**
     * Register a new book in the library.
     */
    @PostMapping
    public ResponseEntity<BookDto.BookResponse> registerBook(@Valid @RequestBody BookDto.BookRequest request) {
        log.info("Registering new book: {} by {}", request.getTitle(), request.getAuthor());
        BookDto.BookResponse response = bookService.registerBook(request);
        log.debug("Book registered successfully with ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all books currently in the library.
     */
    @GetMapping
    public ResponseEntity<List<BookDto.BookResponse>> getAllBooks() {
        log.info("Fetching all books");
        List<BookDto.BookResponse> books = bookService.getAllBooks();
        log.debug("Total books fetched: {}", books.size());
        return ResponseEntity.ok(books);
    }

    /**
     * Borrow a specific book by code for a given borrower.
     */
    @PostMapping("/borrow/{bookCode}")
    public ResponseEntity<String> borrowBookByCode(@PathVariable("bookCode") String bookCode, @RequestParam String borrowerCode) {
        log.info("Borrow request - Book ID: {}, Borrower ID: {}", bookCode, borrowerCode);
        bookService.borrowBookByCode(bookCode, borrowerCode);
        log.info("Book borrowed successfully - Book ID: {}", bookCode);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    /**
     * Return a borrowed book, making it available again.
     */
    @PostMapping("/return/{bookCode}")
    public ResponseEntity<String> returnBookByCode(@PathVariable("bookCode") String bookCode) {
        log.info("Return request - Book Code: {}", bookCode);
        bookService.returnBookByCode(bookCode);
        log.info("Book returned successfully - Book Code: {}", bookCode);
        return ResponseEntity.ok("Book returned successfully");
    }
}
