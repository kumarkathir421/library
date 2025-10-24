package com.collabera.library.service;

import java.util.List;

import com.collabera.library.dto.BookDto;

public interface BookService {

    public BookDto.BookResponse registerBook(BookDto.BookRequest request);

    public List<BookDto.BookResponse> getAllBooks();

    public void borrowBookByCode(String bookCode, String borrowerCode);
    
    public void returnBookByCode(String bookCode);

}
