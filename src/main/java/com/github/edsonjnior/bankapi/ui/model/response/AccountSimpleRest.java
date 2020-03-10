package com.github.edsonjnior.bankapi.ui.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AccountSimpleRest {
    private Long id;
    private String code;
    private BigDecimal balance;
    private CustomerRest customer;
    private BranchRest branch;
}
