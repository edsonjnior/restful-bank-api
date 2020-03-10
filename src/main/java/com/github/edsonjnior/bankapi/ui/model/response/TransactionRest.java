package com.github.edsonjnior.bankapi.ui.model.response;

import com.github.edsonjnior.bankapi.enums.EntryType;
import com.github.edsonjnior.bankapi.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionRest {
    private Long id;
    private LocalDateTime date;
    private BigDecimal value;
    private TransactionType type;
    private EntryType entry;
    private AccountSimpleRest account;
}
