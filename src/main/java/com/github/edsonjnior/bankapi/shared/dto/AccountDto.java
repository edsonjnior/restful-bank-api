package com.github.edsonjnior.bankapi.shared.dto;

import com.github.edsonjnior.bankapi.entities.CustomerEntity;
import com.github.edsonjnior.bankapi.entities.TransactionEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AccountDto implements Serializable {
    private Long id;
    private String code;
    private BigDecimal balance;
    private CustomerDto customer;
    private BranchDto branch;
    private List<TransactionDto> transactions;
    private LocalDateTime createdOn;

    public AccountDto(){}

    public AccountDto(Long id) {
        this.id = id;
    }
}
