package com.hdp.common.security.dto;

public record LoginRequest(String email, String phone, String password) {
}