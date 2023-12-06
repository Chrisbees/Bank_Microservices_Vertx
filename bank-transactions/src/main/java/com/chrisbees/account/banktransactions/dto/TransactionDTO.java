package com.chrisbees.account.banktransactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Integer id;
    private String description;
    private double amount;
    private String transactionType;
    private String name;
    private String sender;
    private String receiver;
    private String noti;
    private LocalDateTime transactionDateTime;
    private Integer bankAccountId;
    private String senderAccountNumber;
    private String receiverAccountNumber;

}
