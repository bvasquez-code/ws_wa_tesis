package com.ccadmin.app.security.model.dto;

import java.util.Date;

public class AuthenticationResponseDto {

    public String token;
    public long expiresIn;
    public Date expirationDate;

    public AuthenticationResponseDto() {}

    public AuthenticationResponseDto(String token, long expiresIn, Date expirationDate) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.expirationDate = expirationDate;
    }
}
