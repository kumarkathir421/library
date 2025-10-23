package com.example.library.service;

import com.example.library.model.BorrowHistory;

import java.util.List;

public interface BorrowHistoryService {

    public List<BorrowHistory> getAllHistory();

    public List<BorrowHistory> getByBorrower(String borrowerCode);

    public List<BorrowHistory> getByBook(String bookCode);
    
}
