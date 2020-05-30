package com.quickpay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountRegisterResponseDTO {

    private String firstName;
    private String lastName;
    private String accountNumber;
    private float balance;
}
