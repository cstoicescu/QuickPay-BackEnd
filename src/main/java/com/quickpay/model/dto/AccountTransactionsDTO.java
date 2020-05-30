package com.quickpay.model.dto;

import com.quickpay.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class AccountTransactionsDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Set<Transaction> transactionSent = new HashSet<>();
    private Set<Transaction> transactionReceived = new HashSet<>();
}
