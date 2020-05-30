package com.quickpay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class TransactionRequestDTO {


    @NonNull
    @Min(value=0, message="Must be positive")
    private float amount;
    @NonNull
    private String description;
    @NonNull
    private String sendingUsername;
    @NonNull
    private String receivingAccountNumber;
    @NonNull
    private String accessToken;
}
