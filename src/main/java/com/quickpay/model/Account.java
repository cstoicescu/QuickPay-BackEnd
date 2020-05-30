package com.quickpay.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue //auto is default
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String accountNumber;
    private String password;
    @Min(value=0, message = "Balance must be greater than 0")
    private float balance;
    private boolean isLogged;
    private String accessToken;

    @OneToMany (mappedBy = "sendingAccount", fetch = FetchType.LAZY)
    private Set<Transaction> transactionSent = new HashSet<>();

    @OneToMany (mappedBy = "receivingAccount", fetch= FetchType.LAZY)
    private Set<Transaction> transactionReceived = new HashSet<>();

    public Account(String firstName, String lastName, String username, String accountNumber, String password, float balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = balance;
        this.isLogged = false;
        this.accessToken = null;
    }

    public void addTransactionSent (Transaction transaction) {
        transactionSent.add(transaction);
    }

    public void addTransactionReceived (Transaction transaction) {
        transactionReceived.add(transaction);
    }
}
