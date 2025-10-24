package com.collabera.library;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.collabera.library.dto.BookDto;
import com.collabera.library.dto.BorrowerDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LibraryApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterAndBorrowBookFlow_Success() throws Exception {
        // Register Borrower
        BorrowerDto.BorrowerRequest borrowerReq = new BorrowerDto.BorrowerRequest("John", "john@example.com");
        mockMvc.perform(post("/api/borrowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(borrowerReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists());

        // Register Book
        BookDto.BookRequest bookReq = new BookDto.BookRequest("12345", "Java 101", "John Doe");
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists());

        // Borrow Book
        mockMvc.perform(post("/api/books/borrow/BOOK-00001")
                        .param("borrowerCode", "BORR-00001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book borrowed successfully"));

        // Return Book
        mockMvc.perform(post("/api/books/return/BOOK-00001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book returned successfully"));
    }
}
