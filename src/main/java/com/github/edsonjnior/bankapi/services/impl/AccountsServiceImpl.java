package com.github.edsonjnior.bankapi.services.impl;

import com.github.edsonjnior.bankapi.domains.SecurityConstants;
import com.github.edsonjnior.bankapi.entities.*;
import com.github.edsonjnior.bankapi.enums.ErrorMessages;
import com.github.edsonjnior.bankapi.exceptions.EntitiesServiceException;
import com.github.edsonjnior.bankapi.repositories.*;
import com.github.edsonjnior.bankapi.services.AccountsService;
import com.github.edsonjnior.bankapi.shared.dto.AccountCreationDto;
import com.github.edsonjnior.bankapi.shared.dto.AccountDto;
import com.github.edsonjnior.bankapi.shared.dto.BranchDto;
import com.github.edsonjnior.bankapi.utils.Utils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AccountsServiceImpl implements AccountsService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Utils utils;


    @Override
    public AccountDto save(AccountDto accountDto) {
        return null;
    }

    @Override
    public AccountCreationDto save(AccountCreationDto accountCreationDto, Long bankId, Long branchId) {
        // Validating user
        UserEntity storedUser = userRepository.findUserByLogin(accountCreationDto.getLogin());
        if(storedUser != null) throw new EntitiesServiceException(
                "This login already exists"
        );

        // Validating branch and bank
        BranchEntity storedBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntitiesServiceException(
                        String.format(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(), "Branch", branchId)
                ));

        BankEntity storedBank = bankRepository.findById(bankId)
                .orElseThrow(() -> new EntitiesServiceException(
                        String.format(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(), "Bank", bankId)
                ));

        if(!storedBank.getId().equals(bankId)) throw new EntitiesServiceException(
                String.format("Branch with id %d was not found for bank with id %d", branchId, bankId)
        );

        var mapper = new ModelMapper();

        // Creating customer
        var encryptedPassword = passwordEncoder.encode(accountCreationDto.getPassword());
        var customerEntity = mapper.map(accountCreationDto, CustomerEntity.class);
        customerEntity.setPassword(encryptedPassword);
        var clientRoleEntity = roleRepository.findByName(SecurityConstants.ROLE_CLIENT);
        customerEntity.setRoles(Set.of(clientRoleEntity));
        customerEntity.setCreatedOn(LocalDateTime.now());

        var createdCustomer = customerRepository.save(customerEntity);

        // Creating Account
        if (createdCustomer == null) throw new EntitiesServiceException("Unable to create customer account");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCode(utils.generateNumberAccount());
        accountEntity.setCreatedOn(LocalDateTime.now());
        accountEntity.setBalance(BigDecimal.ZERO);
        accountEntity.setBranch(storedBranch);
        accountEntity.setCustomer(createdCustomer);

        var createdEntityAccount = accountRepository.save(accountEntity);

        // Mapping objects
        AccountCreationDto responseDto = mapper.map(createdEntityAccount, AccountCreationDto.class);
        responseDto.setName(createdCustomer.getName());
        var storedBranchDto = mapper.map(storedBranch, BranchDto.class);
        responseDto.setBranch(storedBranchDto);

        return responseDto;
    }

    @Override
    public List<AccountDto> findAll() {
        var mapper = new ModelMapper();
        var accountDto = new ArrayList<AccountDto>();

        var accountEntities = accountRepository.findAll();
        if (accountEntities != null && !accountEntities.isEmpty()) {
            var listType = new TypeToken<List<AccountDto>>() {
            }.getType();

            accountDto = mapper.map(accountEntities, listType);
        }

        return accountDto;
    }
}
