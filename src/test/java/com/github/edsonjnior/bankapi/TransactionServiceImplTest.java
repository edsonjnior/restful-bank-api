package com.github.edsonjnior.bankapi;

import com.github.edsonjnior.bankapi.entities.*;
import com.github.edsonjnior.bankapi.enums.EntryType;
import com.github.edsonjnior.bankapi.enums.TransactionType;
import com.github.edsonjnior.bankapi.exceptions.EntitiesServiceException;
import com.github.edsonjnior.bankapi.repositories.*;
import com.github.edsonjnior.bankapi.services.impl.AccountsServiceImpl;
import com.github.edsonjnior.bankapi.services.impl.TransactionServiceImpl;
import com.github.edsonjnior.bankapi.shared.dto.AccountCreationDto;
import com.github.edsonjnior.bankapi.shared.dto.AccountDto;
import com.github.edsonjnior.bankapi.shared.dto.BranchDto;
import com.github.edsonjnior.bankapi.shared.dto.TransactionDto;
import com.github.edsonjnior.bankapi.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {

    @InjectMocks
    TransactionServiceImpl transactionsService;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    AccountRepository accountRepository;

    private Long id = 1L;
    private String genericCode = "99999-0";
    private LocalDateTime dateTime = LocalDateTime.now();
    private BankEntity bankEntity;
    private RoleEntity roleEntity;
    private CustomerEntity customerEntity;
    private CustomerEntity customer2Entity;
    private TransactionEntity transactionEntity;
    private AccountEntity accountIncomingEntity;
    private AccountEntity accountOutcomingEntity;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


    }

    @Test
    final void saveTransaction_Transfer() {
        // Initial balance: 250.00
        // Transfer value: 250.00
        // Final balance: 0.00
        initEntities();

        accountOutcomingEntity.setBalance(BigDecimal.valueOf(250L));
        transactionEntity.setValue(BigDecimal.valueOf(250L));

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(accountOutcomingEntity))
                .thenReturn(Optional.of(accountIncomingEntity));
        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenReturn(transactionEntity);

        var transactionDto = new TransactionDto();
        transactionDto.setType(TransactionType.TRANSFER);
        transactionDto.setEntry(EntryType.DEBIT);
        transactionDto.setValue(BigDecimal.valueOf(250L));
        transactionDto.setAccount(new AccountDto(2L));

        TransactionDto createdTransaction = transactionsService.save(transactionDto, id);

        assertNotNull(createdTransaction);
        assertEquals(250.0, createdTransaction.getValue().doubleValue());
        assertEquals(0.0, createdTransaction.getAccount().getBalance().doubleValue());
    }

    @Test
    final void saveTransaction_Withdrawl() {
        // Initial acc balance: 500.00
        // Request value: 250.00
        // Final acc balance: 250.00
        initEntities();

        accountOutcomingEntity.setBalance(BigDecimal.valueOf(500L));
        transactionEntity.setValue(BigDecimal.valueOf(250L));
        transactionEntity.setEntry(EntryType.DEBIT);
        transactionEntity.setType(TransactionType.WITHDRAWL);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(accountOutcomingEntity));
        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenReturn(transactionEntity);

        var transactionDto = new TransactionDto();
        transactionDto.setType(TransactionType.WITHDRAWL);
        transactionDto.setEntry(EntryType.DEBIT);
        transactionDto.setValue(BigDecimal.valueOf(250L));
        transactionDto.setAccount(new AccountDto(id));

        TransactionDto createdTransaction = transactionsService.save(transactionDto, id);

        assertNotNull(createdTransaction);
        assertEquals(250.0, createdTransaction.getValue().doubleValue());
        assertEquals(TransactionType.WITHDRAWL, createdTransaction.getType());
        assertEquals(EntryType.DEBIT, createdTransaction.getEntry());
        assertEquals(250.0, createdTransaction.getAccount().getBalance().doubleValue());
    }

    @Test
    final void saveTransaction_Deposit() {
        // Initial acc balance: 0.00
        // Request value: 250.00
        // Final acc balance: 250.00
        initEntities();

        transactionEntity.setValue(BigDecimal.valueOf(250L));
        transactionEntity.setEntry(EntryType.CREDIT);
        transactionEntity.setType(TransactionType.DEPOSIT);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(accountOutcomingEntity));
        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenReturn(transactionEntity);

        var transactionDto = new TransactionDto();
        transactionDto.setType(TransactionType.DEPOSIT);
        transactionDto.setEntry(EntryType.CREDIT);
        transactionDto.setValue(BigDecimal.valueOf(250L));
        transactionDto.setAccount(new AccountDto(id));

        TransactionDto createdTransaction = transactionsService.save(transactionDto, id);

        assertNotNull(createdTransaction);
        assertEquals(250.0, createdTransaction.getValue().doubleValue());
        assertEquals(TransactionType.DEPOSIT, createdTransaction.getType());
        assertEquals(EntryType.CREDIT, createdTransaction.getEntry());
        assertEquals(250.0, createdTransaction.getAccount().getBalance().doubleValue());
    }


    @Test
    final void saveTransaction_AccountNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(null);

        var transactionDto = new TransactionDto();

        assertThrows(EntitiesServiceException.class, () -> {
            transactionsService.save(transactionDto, null);
        });
    }

    @Test
    final void saveTransaction_SameOutcomingIncomingTransfer() {
        initEntities();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(accountIncomingEntity));

        var transactionDto = new TransactionDto();
        transactionDto.setType(TransactionType.TRANSFER);
        transactionDto.setAccount(new AccountDto(id));

        assertThrows(EntitiesServiceException.class, () -> {
            transactionsService.save(transactionDto, id);
        });
    }

    @Test
    final void saveTransaction_InsufficientFundsForTransfer() {
        initEntities();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(accountIncomingEntity));

        var transactionDto = new TransactionDto();
        transactionDto.setAccount(new AccountDto(2L));
        transactionDto.setType(TransactionType.TRANSFER);
        transactionDto.setEntry(EntryType.DEBIT);
        transactionDto.setValue(BigDecimal.valueOf(100L));

        assertThrows(EntitiesServiceException.class, () -> {
            transactionsService.save(transactionDto, id);
        });
    }

    @Test
    final void saveTransaction_InsufficientFundsForWithdrawl() {
        initEntities();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(accountIncomingEntity));

        var transactionDto = new TransactionDto();
        transactionDto.setAccount(new AccountDto(id));
        transactionDto.setType(TransactionType.WITHDRAWL);
        transactionDto.setEntry(EntryType.DEBIT);
        transactionDto.setValue(BigDecimal.valueOf(100L));

        assertThrows(EntitiesServiceException.class, () -> {
            transactionsService.save(transactionDto, id);
        });
    }

    private void initEntities() {
        bankEntity = new BankEntity(id, "001", "Banco do Brasil SA");
        roleEntity = new RoleEntity(id, "ROLE_CLIENT");
        customerEntity = new CustomerEntity(id, "John Doe Fonseca", "johndoe", "1a5d8e1f88a1", Set.of(roleEntity));
        customer2Entity = new CustomerEntity(id, "Mary Moe Stonks", "marymoe", "1a5d8e1f88a1", Set.of(roleEntity));
        accountIncomingEntity = new AccountEntity(2L, genericCode, BigDecimal.ZERO, dateTime, customer2Entity);
        accountOutcomingEntity = new AccountEntity(id, genericCode, BigDecimal.ZERO, dateTime, customerEntity);
        transactionEntity = new TransactionEntity(id, BigDecimal.ZERO, null, null, accountOutcomingEntity);
    }
}
