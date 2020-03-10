package com.github.edsonjnior.bankapi.repositories;

import com.github.edsonjnior.bankapi.entities.AccountEntity;
import com.github.edsonjnior.bankapi.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Page<TransactionEntity> findByAccount(AccountEntity account, Pageable pageable);
}
