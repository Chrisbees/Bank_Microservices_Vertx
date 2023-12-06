package com.chrisbees.account.banktransactions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private double amount;
    private String transactionType;
    private String senderName;
    private String senderAccount;
    private String receiverName;
    private String receiverAccount;
    private String status;
    private LocalDateTime transactionDateTime;
    private Integer bankAccountId;

    // Many-to-one relationship with Account for associating transactions with accounts
//    @ManyToOne
//    @JoinColumn(name = "bank_account_id")
//    private BankAccount bankAccount;


    // Getters and setters
}