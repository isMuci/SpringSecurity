package com.example.entity;

import lombok.Data;

@Data
public class OAuth {

    private String accessToken;

    private String token_type;

    private String refreshToken;
}
