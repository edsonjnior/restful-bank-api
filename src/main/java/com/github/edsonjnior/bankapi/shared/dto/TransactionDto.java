package com.github.edsonjnior.bankapi.shared.dto;

import com.github.edsonjnior.bankapi.enums.EntryType;
import com.github.edsonjnior.bankapi.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto implements Serializable {
    private Long id;
    private LocalDateTime date;
    private BigDecimal value;
    private TransactionType type;
    private EntryType entry;
    private AccountDto account;
}
