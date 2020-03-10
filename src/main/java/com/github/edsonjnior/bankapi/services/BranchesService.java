package com.github.edsonjnior.bankapi.services;

import com.github.edsonjnior.bankapi.shared.dto.BranchDto;

import java.util.List;

public interface BranchesService {
    public BranchDto save(BranchDto branchDto);

    public BranchDto findByName(String name);

    public List<BranchDto> findAll();
}
