package com.github.edsonjnior.bankapi.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "banks")
public class BankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private String code;
    @OneToMany(mappedBy = "bank")
    private List<BranchEntity> branches;

    public BankEntity() {
    }

    public BankEntity(String name, String code){
        this.name = name;
        this.code = code;
    }

    public BankEntity(Long id, String name, String code){
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
