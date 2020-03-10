package com.github.edsonjnior.bankapi.entities;

import com.github.edsonjnior.bankapi.enums.EntryType;
import com.github.edsonjnior.bankapi.enums.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private BigDecimal value;
    @Enumerated
    private TransactionType type;
    @Enumerated
    private EntryType entry;
    @ManyToOne
    private AccountEntity account;

    public TransactionEntity() {
    }

    public TransactionEntity(Long id, BigDecimal value, TransactionType type, EntryType entry, AccountEntity account){
        this.id = id;
        this.value = value;
        this.type = type;
        this.entry = entry;
        this.account = account;
    }
}
