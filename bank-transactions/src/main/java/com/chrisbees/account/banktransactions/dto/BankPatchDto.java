package com.chrisbees.account.banktransactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankPatchDto {

    private Integer id;
    private String accountNumber;
    private String accountType;
    private String accountName;
    private double balance;
    private String status;
    private Integer userId;
}
