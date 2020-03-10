package com.github.edsonjnior.bankapi.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AccountCreationDto {
    private Long id;
    private String name;
    private String login;
    private String password;
    private String code;
    private BranchDto branch;
    private BigDecimal balance;
    private LocalDateTime createdOn;
}
