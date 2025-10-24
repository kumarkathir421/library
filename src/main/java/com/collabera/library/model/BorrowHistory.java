package com.collabera.library.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // book being borrowed
    private String bookCode;
    private String bookTitle;

    // borrower
    private String borrowerCode;
    private String borrowerName;

    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        BORROWED,
        RETURNED
    }
}
