package com.github.edsonjnior.bankapi.services;

import com.github.edsonjnior.bankapi.shared.dto.BankDto;

import java.util.List;

public interface BanksService {
    public BankDto save(BankDto bankDto);

    public BankDto findByName(String name);

    public List<BankDto> findAll();
}
