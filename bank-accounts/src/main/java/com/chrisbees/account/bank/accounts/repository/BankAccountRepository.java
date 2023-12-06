package com.chrisbees.account.bank.accounts.repository;

import com.chrisbees.account.bank.accounts.dto.UserDto;
import com.chrisbees.account.bank.accounts.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

//   BankAccount findByUser(User user);
   Optional<BankAccount> findByAccountNumber(String accountNumber);
}
