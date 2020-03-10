package com.github.edsonjnior.bankapi.repositories;

import com.github.edsonjnior.bankapi.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    public CustomerEntity findByLogin(String login);
}
