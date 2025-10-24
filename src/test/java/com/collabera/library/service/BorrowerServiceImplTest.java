package com.collabera.library.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.collabera.library.dto.BorrowerDto;
import com.collabera.library.exception.ConflictException;
import com.collabera.library.model.Borrower;
import com.collabera.library.repository.BorrowerRepository;

@ExtendWith(MockitoExtension.class)
class BorrowerServiceImplTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;

    private Borrower borrower;
    private BorrowerDto.BorrowerRequest request;

    @BeforeEach
    void setUp() {
        borrower = new Borrower(1L, "BORR-00001", "John Doe", "john@example.com");
        request = new BorrowerDto.BorrowerRequest("John Doe", "john@example.com");
    }

    // Successful Registration
    @Test
    void testRegisterBorrower_Success() {
        when(borrowerRepository.findAll()).thenReturn(List.of()); // no duplicates
        when(borrowerRepository.save(any(Borrower.class))).thenAnswer(invocation -> {
            Borrower b = invocation.getArgument(0);
            b.setId(1L);
            return b;
        });

        BorrowerDto.BorrowerResponse response = borrowerService.registerBorrower(request);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("BORR-00001", response.getCode()); // generated code

        verify(borrowerRepository, times(2)).save(any(Borrower.class)); // saved twice (before + after code generation)
    }

    // Duplicate Email -> ConflictException
    @Test
    void testRegisterBorrower_EmailAlreadyExists() {
        when(borrowerRepository.findAll()).thenReturn(List.of(borrower));

        ConflictException ex = assertThrows(ConflictException.class,
                () -> borrowerService.registerBorrower(request));

        assertEquals("Email already registered: john@example.com", ex.getMessage());
        verify(borrowerRepository, never()).save(any());
    }

    // Get All Borrowers
    @Test
    void testGetAllBorrowers() {
        when(borrowerRepository.findAll()).thenReturn(List.of(borrower));

        List<BorrowerDto.BorrowerResponse> result = borrowerService.getAllBorrowers();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("BORR-00001", result.get(0).getCode());
        verify(borrowerRepository).findAll();
    }
}
