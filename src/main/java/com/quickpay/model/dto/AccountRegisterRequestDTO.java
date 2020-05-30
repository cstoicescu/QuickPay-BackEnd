package com.quickpay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;


@Data
@AllArgsConstructor
public class AccountRegisterRequestDTO {

    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String username;
    @NonNull
    private String accountNumber;
    @NonNull
    private String password;
    @NonNull
    private float balance;
}
