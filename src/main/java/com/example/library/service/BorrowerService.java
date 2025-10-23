package com.example.library.service;

import java.util.List;

import com.example.library.dto.BorrowerDto;

public interface BorrowerService {

    public BorrowerDto.BorrowerResponse registerBorrower(BorrowerDto.BorrowerRequest request);
    
    public List<BorrowerDto.BorrowerResponse> getAllBorrowers();

}
