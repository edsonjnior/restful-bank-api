package com.github.edsonjnior.bankapi.repositories;

import com.github.edsonjnior.bankapi.entities.AccountEntity;
import com.github.edsonjnior.bankapi.entities.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {


}
