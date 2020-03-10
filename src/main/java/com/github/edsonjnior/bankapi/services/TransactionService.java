package com.github.edsonjnior.bankapi.services;

import com.github.edsonjnior.bankapi.shared.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    public TransactionDto save(TransactionDto transactionDto, Long accountId);

    public List<TransactionDto> findAll();

    public List<TransactionDto> findWithPagination(long accountId, int page, int limit);
}
