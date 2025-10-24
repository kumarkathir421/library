package com.collabera.library.service;

import com.collabera.library.model.BorrowHistory;

import java.util.List;

public interface BorrowHistoryService {

    public List<BorrowHistory> getAllHistory();

    public List<BorrowHistory> getByBorrower(String borrowerCode);

    public List<BorrowHistory> getByBook(String bookCode);
    
}
