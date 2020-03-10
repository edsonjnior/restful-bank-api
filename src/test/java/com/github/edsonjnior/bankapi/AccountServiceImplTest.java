package com.github.edsonjnior.bankapi;

import com.github.edsonjnior.bankapi.entities.*;
import com.github.edsonjnior.bankapi.exceptions.EntitiesServiceException;
import com.github.edsonjnior.bankapi.repositories.*;
import com.github.edsonjnior.bankapi.services.impl.AccountsServiceImpl;
import com.github.edsonjnior.bankapi.shared.dto.AccountCreationDto;
import com.github.edsonjnior.bankapi.shared.dto.BankDto;
import com.github.edsonjnior.bankapi.shared.dto.BranchDto;
import com.github.edsonjnior.bankapi.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class AccountServiceImplTest {

    @InjectMocks
    AccountsServiceImpl accountsService;
    @Mock
    BranchRepository branchRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    Utils utils;
    @Mock
    RoleRepository roleRepository;
    @Mock
    BankRepository bankRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    AccountRepository accountRepository;

    private Long id = 1L;
    private String passwordEncoded = "d8fe51f89w1d6f8e";
    private String genericCode = "99999-0";
    private LocalDateTime dateTime = LocalDateTime.now();
    private BankEntity bankEntity;
    private BranchEntity branchEntity;
    private RoleEntity roleEntity;
    private CustomerEntity customerEntity;
    private AccountEntity accountEntity;
    private UserEntity userEntity;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


    }

    @Test
    final void saveClientWithAccountTest() {
        initEntities();

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branchEntity));
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bankEntity));
        when(passwordEncoder.encode(anyString())).thenReturn(passwordEncoded);
        when(roleRepository.findByName(anyString())).thenReturn(roleEntity);
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);
        when(utils.generateNumberAccount()).thenReturn(genericCode);
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        var accCreationDto = new AccountCreationDto();
        accCreationDto.setBranch(new BranchDto());
        AccountCreationDto createdAccountDto = accountsService.save(accCreationDto, id, id);

        assertNotNull(createdAccountDto);
        assertEquals(BigDecimal.ZERO, createdAccountDto.getBalance());
    }


    @Test
    final void saveClientWithAccount_BranchNotFound() {
        when(branchRepository.findById(anyLong())).thenReturn(null);

        var accCreationDto = new AccountCreationDto();

        assertThrows(EntitiesServiceException.class, () -> {
            accountsService.save(accCreationDto, id, null);
        });
    }

    @Test
    final void saveClientWithAccount_BranchNotFoundForBank() {
        initEntities();

        bankEntity.setId(2L);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branchEntity));
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bankEntity));

        var accCreationDto = new AccountCreationDto();

        assertThrows(EntitiesServiceException.class, () -> {
            accountsService.save(accCreationDto, id, id);
        });
    }

    @Test
    final void saveClientWithAccount_LoginAlreadyExists() {
        initEntities();

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branchEntity));
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bankEntity));
        when(userRepository.findUserByLogin(anyString())).thenReturn(userEntity);

        var accCreationDto = new AccountCreationDto();
        accCreationDto.setLogin("johndoe");

        assertThrows(EntitiesServiceException.class, () -> {
            accountsService.save(accCreationDto, id, id);
        });
    }


    @Test
    final void saveClientWithAccount_AccountNotCreated() {
        initEntities();

        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(null);

        var accCreationDto = new AccountCreationDto();

        assertThrows(EntitiesServiceException.class, () -> {
            accountsService.save(accCreationDto, id, id);
        });
    }

    private void initEntities() {
        bankEntity = new BankEntity(id, "001", "Banco do Brasil SA");
        branchEntity = new BranchEntity(id, "Ag Jockey Club", genericCode, bankEntity);
        roleEntity = new RoleEntity(id, "ROLE_CLIENT");
        customerEntity = new CustomerEntity(id, "John Doe Fonseca", "johndoe", "1a5d8e1f88a1", Set.of(roleEntity));
        userEntity = new UserEntity(id, "John Doe Fonseca", "johndoe", "1a5d8e1f88a1", Set.of(roleEntity));
        accountEntity = new AccountEntity(id, genericCode, BigDecimal.ZERO, dateTime, customerEntity);
    }
}
