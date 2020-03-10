package com.github.edsonjnior.bankapi.entities;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "branches")
public class BranchEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private BankEntity bank;

    public BranchEntity() {
    }

    public BranchEntity(Long id, String name, String code, BankEntity bank) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.bank = bank;
    }

    public BranchEntity(String name, String code, BankEntity bank) {
        this.name = name;
        this.code = code;
        this.bank = bank;
    }
}
