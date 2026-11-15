package com.hdp.common.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
        @JsonProperty("accessToken") String accessToken,
        @JsonProperty("refreshToken") String refreshToken,
        @JsonProperty("tokenType") String tokenType,
        @JsonProperty("expiresIn") long expiresIn) {
}