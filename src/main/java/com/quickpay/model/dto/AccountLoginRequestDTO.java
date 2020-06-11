package com.quickpay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class AccountLoginRequestDTO {

    @NonNull
    private String username;
    @NonNull
    private String password;

}
