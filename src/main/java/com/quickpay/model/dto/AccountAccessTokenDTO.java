package com.quickpay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountAccessTokenDTO {

    private String username;
    private String accessToken;
}
