package com.quickpay.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class AccountLoginRequestDTO {

    @NonNull
    private String username;
    @NonNull
    private String password;

}
