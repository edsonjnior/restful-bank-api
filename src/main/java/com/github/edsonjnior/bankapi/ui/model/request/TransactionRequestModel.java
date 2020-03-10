package com.github.edsonjnior.bankapi.ui.model.request;

import com.github.edsonjnior.bankapi.enums.EntryType;
import com.github.edsonjnior.bankapi.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequestModel {
    private Long account;
    private BigDecimal value;
    private TransactionType type;
    private EntryType entry;
}
