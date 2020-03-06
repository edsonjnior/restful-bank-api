package com.github.edsonjnior.bankapi.repositories;

import com.github.edsonjnior.bankapi.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}
