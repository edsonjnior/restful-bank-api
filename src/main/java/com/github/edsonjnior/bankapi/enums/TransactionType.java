package com.github.edsonjnior.bankapi.enums;

import lombok.Getter;

import java.util.Map;

@Getter
public enum TransactionType {
    DEPOSIT("Deposit", Map.of("CREDIT", EntryType.CREDIT)),
    TRANSFER("Transfer", Map.of("DEBIT", EntryType.DEBIT, "CREDIT", EntryType.CREDIT)),
    WITHDRAWL("Withdrawl", Map.of("DEBIT", EntryType.DEBIT));

    private String description;
    private Map<String, EntryType> entries;

    TransactionType(String description, Map<String, EntryType> entries) {
        this.description = description;
        this.entries = entries;
    }

}
