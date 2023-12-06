package com.chrisbees.account.bank.accounts.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@RequiredArgsConstructor
@Table(name = "bank_account")
@Data
@Getter
@Setter
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String accountNumber;
    private String accountType;
    private String accountName;
    private double balance;
    private String status;
    private Integer userId;
//    @JsonIgnore
//    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
//    private List<Transaction> transactions = new ArrayList<>();
}
