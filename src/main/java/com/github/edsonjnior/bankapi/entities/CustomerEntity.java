package com.github.edsonjnior.bankapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor()
@Entity
@Table(name = "customers")
public class CustomerEntity extends UserEntity{

    @OneToMany(mappedBy = "customer")
    private List<AccountEntity> accounts;

    public CustomerEntity() {
    }

    public CustomerEntity(Long id, String name, String login, String password, Set<RoleEntity> roles) {
        super(id, name, login, password, roles);
    }
}
