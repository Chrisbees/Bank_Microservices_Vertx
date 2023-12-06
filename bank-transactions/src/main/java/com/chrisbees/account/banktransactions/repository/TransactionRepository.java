package com.chrisbees.account.banktransactions.repository;

import com.chrisbees.account.banktransactions.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
