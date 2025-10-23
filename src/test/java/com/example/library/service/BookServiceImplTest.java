package com.example.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.library.dto.BookDto;
import com.example.library.exception.ConflictException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Book;
import com.example.library.model.BorrowHistory;
import com.example.library.model.Borrower;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowHistoryRepository;
import com.example.library.repository.BorrowerRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private BorrowHistoryRepository borrowHistoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private Borrower borrower;

    @BeforeEach
    void setUp() {
        borrower = new Borrower(1L, "BORR-00001", "John", "john@example.com");

        book = new Book();
        book.setId(1L);
        book.setCode("BOOK-00001");
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setIsbn("1111");
    }
    
    @SuppressWarnings("unchecked")
	@AfterEach
    void tearDown() {
        clearInvocations(bookRepository, borrowHistoryRepository);
    }

    @Test
    void testRegisterBook_Success() {
        // Arrange
        BookDto.BookRequest request = new BookDto.BookRequest("1111", "Title", "Author");
        when(bookRepository.save(any(Book.class)))
        .thenAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            b.setId(1L);
            return b;
        })
        .thenAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            b.setCode("BOOK-00001");
            return b;
        });

        BookDto.BookResponse response = bookService.registerBook(request);

        // Assert
        assertNotNull(response);
        assertEquals("BOOK-00001", response.getCode());
        verify(bookRepository, times(2)).save(any(Book.class));
    }
    
    @Test
    void testGetAllBooks_Success() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        List<BookDto.BookResponse> result = bookService.getAllBooks();
        assertEquals(1, result.size());
        assertEquals("BOOK-00001", result.get(0).getCode());
    }
    
    @Test
    void testBorrowBookByCode_Success() {
        // Arrange
        when(bookRepository.findByCode("BOOK-00001")).thenReturn(Optional.of(book));
        when(borrowerRepository.findByCode("BORR-00001")).thenReturn(Optional.of(borrower));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(borrowHistoryRepository.save(any(BorrowHistory.class))).thenReturn(new BorrowHistory());

        // Act
        bookService.borrowBookByCode("BOOK-00001", "BORR-00001");

        // Assert
        assertEquals(borrower, book.getBorrowedBy());
        verify(bookRepository).save(book);
        verify(borrowHistoryRepository).save(any(BorrowHistory.class));
    }

    @Test
    void testBorrowBookByCode_BookNotFound() {
        when(bookRepository.findByCode("BOOK-00002")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> bookService.borrowBookByCode("BOOK-00002", "BORR-00001"));
    }

    @Test
    void testBorrowBookByCode_BorrowerNotFound() {
        when(bookRepository.findByCode("BOOK-00001")).thenReturn(Optional.of(book));
        when(borrowerRepository.findByCode("BORR-00001")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> bookService.borrowBookByCode("BOOK-00001", "BORR-00001"));
    }

    @Test
    void testBorrowBookByCode_AlreadyBorrowed() {
        // Given
        book.setBorrowedBy(borrower); // already borrowed

        when(bookRepository.findByCode("BOOK-00001")).thenReturn(Optional.of(book));
        when(borrowerRepository.findByCode("BORR-00001")).thenReturn(Optional.of(borrower));

        // When + Then
        assertThrows(ConflictException.class,
                () -> bookService.borrowBookByCode("BOOK-00001", "BORR-00001"));
    }

    @Test
    void testReturnBookByCode_Success() {
        // Set book as borrowed
        book.setBorrowedBy(borrower);

        BorrowHistory history = BorrowHistory.builder()
                .bookCode("BOOK-00001")
                .status(BorrowHistory.Status.BORROWED)
                .borrowDate(LocalDateTime.now())
                .build();

        // Correctly mock repository methods
        when(bookRepository.findByCode("BOOK-00001")).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(borrowHistoryRepository.findByBookCode("BOOK-00001"))
                .thenReturn(new ArrayList<>(List.of(history)));
        when(borrowHistoryRepository.save(any(BorrowHistory.class))).thenReturn(history);

        // Execute
        bookService.returnBookByCode("BOOK-00001");

        // Verify outcomes
        assertNull(book.getBorrowedBy()); // borrower cleared
        assertEquals(BorrowHistory.Status.RETURNED, history.getStatus());
    }

    @Test
    void testReturnBookByCode_BookNotFound() {
        when(bookRepository.findByCode("BOOK-00099")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> bookService.returnBookByCode("BOOK-00099"));
    }

    @Test
    void testReturnBookByCode_NotBorrowed() {
        // given book exists but not borrowed
        when(bookRepository.findByCode("BOOK-00001")).thenReturn(Optional.of(book));

        // when + then
        assertThrows(ConflictException.class,
                () -> bookService.returnBookByCode("BOOK-00001"));
    }
}
