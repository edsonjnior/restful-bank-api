package com.github.edsonjnior.bankapi.repositories;

import com.github.edsonjnior.bankapi.entities.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<BankEntity, Long> {

    public BankEntity findByName(String name);
}
