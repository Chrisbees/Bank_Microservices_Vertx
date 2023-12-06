package com.chrisbees.account.bank.accounts.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountWithUserResponse {

    private Integer id;
    private String accountNumber;
    private String accountType;
    private String accountName;
    private double balance;
    private String status;
    private Integer userId;
    private UserDto user;
}








