package com.quickpay.model.dto;

import com.quickpay.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class AccountGetUserResponseDTO {

    private String firstName;
    private String lastName;
    private String accountNumber;
    private float balance;
    private Set<Transaction> transactionSent = new HashSet<>();
    private Set<Transaction> transactionReceived = new HashSet<>();


}
