package com.example.library.controller;

import com.example.library.dto.BookDto;
import com.example.library.exception.ConflictException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    // Register Book
    @Test
    void testRegisterBook_Success() throws Exception {
        BookDto.BookRequest request = new BookDto.BookRequest("12345", "Java 101", "John Doe");
        BookDto.BookResponse response = new BookDto.BookResponse(1L, "BOOK-00001", "12345", "Java 101", "John Doe", null, null);

        when(bookService.registerBook(any())).thenReturn(response);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("BOOK-00001"))
                .andExpect(jsonPath("$.title").value("Java 101"))
                .andExpect(jsonPath("$.author").value("John Doe"));

        verify(bookService).registerBook(any());
    }

    // Get All Books
    @Test
    void testGetAllBooks_Success() throws Exception {
        List<BookDto.BookResponse> books = List.of(
                new BookDto.BookResponse(1L, "BOOK-00001", "12345", "Java 101", "John Doe", null, null)
        );

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("BOOK-00001"))
                .andExpect(jsonPath("$[0].title").value("Java 101"))
                .andExpect(jsonPath("$[0].author").value("John Doe"));

        verify(bookService).getAllBooks();
    }

    // Borrow Book by Code
    @Test
    void testBorrowBookByCode_Success() throws Exception {
        doNothing().when(bookService).borrowBookByCode("BOOK-00001", "BORR-00001");

        mockMvc.perform(post("/api/books/borrow/BOOK-00001")
                        .param("borrowerCode", "BORR-00001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book borrowed successfully"));

        verify(bookService).borrowBookByCode("BOOK-00001", "BORR-00001");
    }

    // Return Book by Code
    @Test
    void testReturnBookByCode_Success() throws Exception {
        doNothing().when(bookService).returnBookByCode("BOOK-00001");

        mockMvc.perform(post("/api/books/return/BOOK-00001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book returned successfully"));

        verify(bookService).returnBookByCode("BOOK-00001");
    }

    // Book Not Found (ResourceNotFoundException)
    @Test
    void testBorrowBookByCode_ResourceNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Book not found with code: BOOK-99999"))
                .when(bookService).borrowBookByCode("BOOK-99999", "BORR-00001");

        mockMvc.perform(post("/api/books/borrow/BOOK-99999")
                        .param("borrowerCode", "BORR-00001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Book not found with code: BOOK-99999"));
    }

    // Conflict (Book Already Borrowed)
    @Test
    void testBorrowBookByCode_Conflict() throws Exception {
        doThrow(new ConflictException("Book BOOK-00001 is already borrowed."))
                .when(bookService).borrowBookByCode("BOOK-00001", "BORR-00001");

        mockMvc.perform(post("/api/books/borrow/BOOK-00001")
                        .param("borrowerCode", "BORR-00001"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Book BOOK-00001 is already borrowed."));
    }

    // Generic Exception (Internal Server Error)
    @Test
    void testBorrowBookByCode_InternalServerError() throws Exception {
        doThrow(new RuntimeException("Unexpected error"))
                .when(bookService).borrowBookByCode("BOOK-00001", "BORR-00001");

        mockMvc.perform(post("/api/books/borrow/BOOK-00001")
                        .param("borrowerCode", "BORR-00001"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Internal server error"));
    }
}
