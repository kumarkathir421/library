package com.example.library.repository;

import com.example.library.model.BorrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowHistoryRepository extends JpaRepository<BorrowHistory, Long> {
    List<BorrowHistory> findByBookCode(String bookCode);
    List<BorrowHistory> findByBorrowerCode(String borrowerCode);
}
