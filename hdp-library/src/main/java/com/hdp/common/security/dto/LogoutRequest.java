package com.hdp.common.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LogoutRequest(@JsonProperty("refreshToken") String refreshToken) {
}