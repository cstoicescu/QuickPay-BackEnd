package com.quickpay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionAccountResponseDTO {

    private String firstName;
    private String lastName;
    private String accountNumber;
}
