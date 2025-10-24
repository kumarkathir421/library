package com.collabera.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.collabera.library.dto.BorrowerDto;
import com.collabera.library.service.BorrowerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BorrowerController.class)
class BorrowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowerService borrowerService;

    @Autowired
    private ObjectMapper objectMapper;

    // POST /api/borrowers
    @Test
    void testRegisterBorrower_Success() throws Exception {
        BorrowerDto.BorrowerRequest request = new BorrowerDto.BorrowerRequest("John Doe", "john@example.com");
        BorrowerDto.BorrowerResponse response = new BorrowerDto.BorrowerResponse(1L, "BORR-00001", "John Doe", "john@example.com");

        when(borrowerService.registerBorrower(any())).thenReturn(response);

        mockMvc.perform(post("/api/borrowers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("BORR-00001"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(borrowerService).registerBorrower(any());
    }

    // GET /api/borrowers
    @Test
    void testGetAllBorrowers_Success() throws Exception {
        List<BorrowerDto.BorrowerResponse> borrowers = List.of(
                new BorrowerDto.BorrowerResponse(1L, "BORR-00001", "John Doe", "john@example.com")
        );

        when(borrowerService.getAllBorrowers()).thenReturn(borrowers);

        mockMvc.perform(get("/api/borrowers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("BORR-00001"))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));

        verify(borrowerService).getAllBorrowers();
    }
}
