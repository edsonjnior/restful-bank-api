package com.github.edsonjnior.bankapi.services;

import com.github.edsonjnior.bankapi.shared.dto.AccountCreationDto;
import com.github.edsonjnior.bankapi.shared.dto.AccountDto;
import com.github.edsonjnior.bankapi.shared.dto.BranchDto;

import java.util.List;

public interface AccountsService {

    public AccountDto save(AccountDto accountDto);

    public AccountCreationDto save(AccountCreationDto accountCreationDto, Long banckId, Long branchId );

    public List<AccountDto> findAll();
}
