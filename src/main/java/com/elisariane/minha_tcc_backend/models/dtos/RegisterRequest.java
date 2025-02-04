package com.elisariane.minha_tcc_backend.models.dtos;

import com.elisariane.minha_tcc_backend.models.enums.Role;

public record RegisterRequest(
        String name,
        String email,
        String password,
        Role role
) {
}
