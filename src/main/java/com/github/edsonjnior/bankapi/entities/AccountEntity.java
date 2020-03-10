package com.github.edsonjnior.bankapi.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(precision = 10, scale = 2)
    private BigDecimal balance;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @ManyToOne
    private CustomerEntity customer;
    @ManyToOne
    private BranchEntity branch;
    @OneToMany(mappedBy = "account")
    private List<TransactionEntity> transactions;

    public AccountEntity() {
    }

    public AccountEntity(Long id, String code, BigDecimal balance, LocalDateTime createdOn, CustomerEntity customer){
        this.id = id;
        this.code = code;
        this.balance = balance;
        this.createdOn = createdOn;
        this.customer = customer;
    }
}
