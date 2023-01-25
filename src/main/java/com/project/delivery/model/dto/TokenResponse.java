package com.project.delivery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TokenResponse {
    private String grantType;
    private String accessToken;
    private Long tokenExpirationTime;
}