package com.hdp.common.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshResponse(
        @JsonProperty("accessToken") String accessToken,
        @JsonProperty("expiresIn") long expiresIn) {
}