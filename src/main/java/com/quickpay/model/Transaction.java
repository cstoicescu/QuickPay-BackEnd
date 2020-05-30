package com.quickpay.model;

import com.quickpay.model.dto.TransactionAccountResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;



@Entity
@Setter
@NoArgsConstructor
@Getter
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;
    //dateofnow
    @Min(value=0, message = "Amount must be greater than 0")
    private float amount;
    private String date;
    private String description;


    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="receiving_account")
    private Account receivingAccount;

    @ManyToOne
    @JoinColumn (name="sending_account")
    private Account sendingAccount;

    public Transaction(float amount, String description, Account receivingAccount, Account sendingAccount) {
        this.amount = amount;
        this.description = description;
        this.date = getCurrentTime();
        this.receivingAccount = receivingAccount;
        this.sendingAccount = sendingAccount;
    }

    // create new DTO response and return account
    public TransactionAccountResponseDTO getReceivingAccount() {
        return new TransactionAccountResponseDTO(receivingAccount.getFirstName(), receivingAccount.getLastName(),
                receivingAccount.getAccountNumber());
    }
    public TransactionAccountResponseDTO getSendingAccount() {
        return new TransactionAccountResponseDTO(sendingAccount.getFirstName(), sendingAccount.getLastName(),
                sendingAccount.getAccountNumber());
    }

    private String getCurrentTime () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")
                .withZone(ZoneId.of("Europe/Bucharest"));
        Instant instant = Instant.now();
         return formatter.format(instant)  ;
    }

}
