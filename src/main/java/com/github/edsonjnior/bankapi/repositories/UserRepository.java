package com.github.edsonjnior.bankapi.repositories;

import com.github.edsonjnior.bankapi.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByLogin(String login);
}
