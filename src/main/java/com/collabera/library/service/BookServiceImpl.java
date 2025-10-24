package com.collabera.library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.collabera.library.dto.BookDto;
import com.collabera.library.exception.ConflictException;
import com.collabera.library.exception.ResourceNotFoundException;
import com.collabera.library.model.Book;
import com.collabera.library.model.BorrowHistory;
import com.collabera.library.model.Borrower;
import com.collabera.library.repository.BookRepository;
import com.collabera.library.repository.BorrowHistoryRepository;
import com.collabera.library.repository.BorrowerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for managing Books.
 * Handles registration, listing, borrowing, and returning books.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private final BorrowHistoryRepository borrowHistoryRepository;

    /**
     * Register a new book in the library.
     * Multiple copies with the same ISBN are allowed.
     */
    @Transactional
    public BookDto.BookResponse registerBook(BookDto.BookRequest request) {
        log.info("Registering new book: [{} - {}]", request.getIsbn(), request.getTitle());
        Book book = new Book();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        
        // First save to get the numeric ID
        Book savedBook = bookRepository.save(book);

        // Generate readable code using ID
        String code = String.format("BOOK-%05d", savedBook.getId());
        savedBook.setCode(code);

        // Save again to persist the generated code
        Book updated = bookRepository.save(savedBook);
        
        log.debug("Book saved with ID: {}", updated.getId());
        return mapToResponse(updated);
    }

    /**
     * List all books with borrower.
     */
    @Transactional
    public List<BookDto.BookResponse> getAllBooks() {
    	log.debug("Fetching all books from repository");
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Borrow a book by Code.
     */
    @Transactional
    public void borrowBookByCode(String bookCode, String borrowerCode) {
        log.info("Borrowing book with code: {} for borrower: {}", bookCode, borrowerCode);

        // Find the book
        Book book = bookRepository.findByCode(bookCode)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with code: " + bookCode));

        // Find the borrower
        Borrower borrower = borrowerRepository.findByCode(borrowerCode)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with code: " + borrowerCode));

        // Check if already borrowed
        if (book.getBorrowedBy() != null) {
            throw new ConflictException("Book " + bookCode + " is already borrowed.");
        }

        // Assign borrower to the book
        book.setBorrowedBy(borrower);
        bookRepository.save(book);

        // Save borrow history record
        BorrowHistory history = BorrowHistory.builder()
                .bookCode(book.getCode())
                .bookTitle(book.getTitle())
                .borrowerCode(borrower.getCode())
                .borrowerName(borrower.getName())
                .borrowDate(LocalDateTime.now())
                .status(BorrowHistory.Status.BORROWED)
                .build();
        borrowHistoryRepository.save(history);

        log.info("Book {} successfully borrowed by {}", bookCode, borrowerCode);
    }

    
    /**
     * Return a book by Code.
     */
    @Transactional
    public void returnBookByCode(String bookCode) {
        Book book = bookRepository.findByCode(bookCode)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with code: " + bookCode));

        if (book.getBorrowedBy() == null) {
            throw new ConflictException("Book " + bookCode + " is not currently borrowed.");
        }

        book.setBorrowedBy(null);
        bookRepository.save(book);

        List<BorrowHistory> historyList = borrowHistoryRepository.findByBookCode(bookCode);
        BorrowHistory latest = historyList.stream()
                .filter(h -> h.getStatus() == BorrowHistory.Status.BORROWED)
                .reduce((first, second) -> second)
                .orElse(null);

        if (latest != null) {
            latest.setReturnDate(LocalDateTime.now());
            latest.setStatus(BorrowHistory.Status.RETURNED);
            borrowHistoryRepository.save(latest);
        }
    }


    /**
     * Helper method to map entity -> response DTO.
     */
    private BookDto.BookResponse mapToResponse(Book book) {
        BookDto.BookResponse response = new BookDto.BookResponse();
        response.setId(book.getId());
        response.setCode(book.getCode());
        response.setIsbn(book.getIsbn());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        if (book.getBorrowedBy() != null) {
            response.setBorrowedById(book.getBorrowedBy().getId());
            response.setBorrowedByCode(book.getBorrowedBy().getCode());
        }
        return response;
    }
}
