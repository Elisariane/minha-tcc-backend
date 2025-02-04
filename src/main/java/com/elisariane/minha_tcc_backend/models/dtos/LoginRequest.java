package com.elisariane.minha_tcc_backend.models.dtos;

public record LoginRequest(
        String email,
        String password
) {
}
