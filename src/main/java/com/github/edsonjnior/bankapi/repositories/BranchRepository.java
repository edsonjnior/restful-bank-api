package com.github.edsonjnior.bankapi.repositories;

import com.github.edsonjnior.bankapi.entities.BankEntity;
import com.github.edsonjnior.bankapi.entities.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<BranchEntity, Long> {

    public BranchEntity findByName(String name);

    public BranchEntity findByCode(String code);
}
