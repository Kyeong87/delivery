package com.project.delivery.model.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;

    public LoginResponse(){}
    public LoginResponse(String token) {
        this.token = token;
    }
}