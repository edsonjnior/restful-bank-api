package com.github.edsonjnior.bankapi.services.impl;

import com.github.edsonjnior.bankapi.entities.AccountEntity;
import com.github.edsonjnior.bankapi.entities.TransactionEntity;
import com.github.edsonjnior.bankapi.enums.EntryType;
import com.github.edsonjnior.bankapi.enums.ErrorMessages;
import com.github.edsonjnior.bankapi.enums.TransactionType;
import com.github.edsonjnior.bankapi.exceptions.EntitiesServiceException;
import com.github.edsonjnior.bankapi.repositories.AccountRepository;
import com.github.edsonjnior.bankapi.repositories.TransactionRepository;
import com.github.edsonjnior.bankapi.services.TransactionService;
import com.github.edsonjnior.bankapi.shared.dto.TransactionDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public TransactionDto save(TransactionDto transactionDto, Long accountId) {
        // Checking if bank account exists
        AccountEntity storedAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntitiesServiceException(
                        String.format(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(), "Account", accountId)
                ));

        // Throw error if try to transfer to the same account
        if(transactionDto.getType().equals(TransactionType.TRANSFER) && transactionDto.getAccount().getId().equals(accountId)){
            throw new EntitiesServiceException("Outcoming account cannot be equals to the incoming account");
        }

        BigDecimal balance = storedAccount.getBalance();
        BigDecimal transactionValue = transactionDto.getValue();
        EntryType entryType = transactionDto.getEntry();

        // Checking if account has sufficient funds
        if (entryType.equals(EntryType.DEBIT) &&
                balance.subtract(transactionValue).doubleValue() < 0) {
            throw new EntitiesServiceException(String.format(ErrorMessages.NO_FUNDS.getErrorMessage(), storedAccount.getCode()));
        }

        var mapper = new ModelMapper();
        TransactionEntity transactionEntity = mapper.map(transactionDto, TransactionEntity.class);
        transactionEntity.setDate(LocalDateTime.now());

        updateBalance(storedAccount, transactionEntity);

        transactionEntity.setAccount(storedAccount);

        if(transactionEntity.getType().equals(TransactionType.TRANSFER)){
            createIncomingTransaction(transactionDto);
        }

        TransactionEntity createdTransaction = transactionRepository.save(transactionEntity);

        TransactionDto reponseDto = mapper.map(createdTransaction, TransactionDto.class);

        return reponseDto;
    }

    private void createIncomingTransaction(TransactionDto transactionDto) {
        var transactionEntity = new TransactionEntity();
        var incomingAccountEntity = accountRepository.findById(transactionDto.getAccount().getId()).orElseThrow(() ->
                new EntitiesServiceException(
                        String.format(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(), "Destination account", transactionDto.getAccount().getId())
                ));

        transactionEntity.setDate(LocalDateTime.now());
        transactionEntity.setValue(transactionDto.getValue());
        transactionEntity.setType(TransactionType.TRANSFER);
        transactionEntity.setEntry(EntryType.CREDIT);

        updateBalance(incomingAccountEntity, transactionEntity);

        transactionEntity.setAccount(incomingAccountEntity);

        transactionRepository.save(transactionEntity);
    }

    private void updateBalance(AccountEntity account, TransactionEntity transactionEntity) {
        var transactionValue = transactionEntity.getValue();
        var oldBalance = account.getBalance();
        var entryType = transactionEntity.getEntry();

        account.setBalance(entryType.equals(EntryType.DEBIT) ? oldBalance.subtract(transactionValue) : oldBalance.add(transactionValue));
    }

    @Override
    public List<TransactionDto> findAll() {
        return null;
    }

    @Override
    public List<TransactionDto> findWithPagination(long accountId, int page, int limit) {
        // Checking if bank account exists
        AccountEntity storedAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntitiesServiceException(
                        String.format(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(), "Bank", accountId)
                ));

        if (page > 0) page -= 1;

        Stream<TransactionEntity> transactionsStream = transactionRepository.findByAccount(storedAccount, PageRequest.of(page, limit, Sort.by("date").descending())).get();
        List<TransactionEntity> transactionsEntity = transactionsStream.collect(Collectors.toList());
        List<TransactionDto> resultList = new ArrayList<>();
        if (transactionsEntity != null && !transactionsEntity.isEmpty()) {
            Type listType = new TypeToken<List<TransactionDto>>() {
            }.getType();
            resultList = new ModelMapper().map(transactionsEntity, listType);
        }

        return resultList;
    }
}
