package com.chrisbees.account.accountUsers.repository;


import com.chrisbees.account.accountUsers.model.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountUserRepository extends JpaRepository<AccountUser, Integer> {
    Optional<AccountUser> findByUsername(String username);
}

