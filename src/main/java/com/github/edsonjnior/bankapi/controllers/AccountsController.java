package com.github.edsonjnior.bankapi.controllers;

import com.github.edsonjnior.bankapi.services.AccountsService;
import com.github.edsonjnior.bankapi.services.TransactionService;
import com.github.edsonjnior.bankapi.shared.dto.AccountCreationDto;
import com.github.edsonjnior.bankapi.shared.dto.AccountDto;
import com.github.edsonjnior.bankapi.shared.dto.TransactionDto;
import com.github.edsonjnior.bankapi.ui.model.request.AccountRequestModel;
import com.github.edsonjnior.bankapi.ui.model.request.TransactionRequestModel;
import com.github.edsonjnior.bankapi.ui.model.response.AccountCreationRest;
import com.github.edsonjnior.bankapi.ui.model.response.AccountSimpleRest;
import com.github.edsonjnior.bankapi.ui.model.response.TransactionRest;
import com.github.edsonjnior.bankapi.ui.model.response.TransactionSimpleRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountsController {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private TransactionService transactionService;

    @ResponseBody
    @PostMapping(path = "/banks/{bankId:[0-9]+}/branches/{branchId:[0-9]+}/accounts",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountCreationRest createAccount(@PathVariable Long bankId,
                                             @PathVariable Long branchId,
                                             @RequestBody AccountRequestModel requestModel) {
        var mapper = new ModelMapper();
        var accountCreationDto = mapper.map(requestModel, AccountCreationDto.class);
        var createdAccountDto = accountsService.save(accountCreationDto, bankId, branchId);

        return mapper.map(createdAccountDto, AccountCreationRest.class);
    }


    @ResponseBody
    @PostMapping(path = "/accounts/{accountId}/transactions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionRest createAccountTransaction(@PathVariable Long accountId,
                                                    @RequestBody TransactionRequestModel requestModel) {
        var mapper = new ModelMapper();
        var transactionDto = mapper.map(requestModel, TransactionDto.class);
        if (requestModel.getAccount() != null) {
            transactionDto.setAccount(new AccountDto(requestModel.getAccount()));
        }

        var createdTransactionDto = transactionService.save(transactionDto, accountId);
        return mapper.map(createdTransactionDto, TransactionRest.class);
    }

    @ResponseBody
    @GetMapping(path = "/accounts/{accountId}/transactions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransactionSimpleRest> getTransactions(@PathVariable Long accountId,
                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "limit", defaultValue = "25") int limit) {
        List<TransactionSimpleRest> response = new ArrayList<>();

        List<TransactionDto> listTransactionsDto = transactionService.findWithPagination(accountId, page, limit);
        if (listTransactionsDto != null && !listTransactionsDto.isEmpty()) {
            Type listType = new TypeToken<List<TransactionSimpleRest>>() {
            }.getType();
            response = new ModelMapper().map(listTransactionsDto, listType);
        }

        return response;
    }


    @ResponseBody
    @GetMapping(path = "/accounts",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountSimpleRest> getAccounts() {
        List<AccountSimpleRest> response = new ArrayList<>();

        List<AccountDto> listAccountsDto = accountsService.findAll();
        if (listAccountsDto != null && !listAccountsDto.isEmpty()) {
            Type listType = new TypeToken<List<AccountSimpleRest>>() {
            }.getType();
            response = new ModelMapper().map(listAccountsDto, listType);
        }

        return response;
    }


}
