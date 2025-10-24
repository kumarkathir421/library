package com.collabera.library.service;

import java.util.List;

import com.collabera.library.dto.BorrowerDto;

public interface BorrowerService {

    public BorrowerDto.BorrowerResponse registerBorrower(BorrowerDto.BorrowerRequest request);
    
    public List<BorrowerDto.BorrowerResponse> getAllBorrowers();

}
