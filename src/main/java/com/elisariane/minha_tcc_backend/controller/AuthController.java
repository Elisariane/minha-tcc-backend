package com.elisariane.minha_tcc_backend.controller;

import com.elisariane.minha_tcc_backend.models.User;
import com.elisariane.minha_tcc_backend.models.dtos.LoginRequest;
import com.elisariane.minha_tcc_backend.models.dtos.RegisterRequest;
import com.elisariane.minha_tcc_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());
        user.setPassword(registerRequest.password());
        user.setRole(registerRequest.role());
        authService.registerUser(user);

        return ResponseEntity.ok("Usuário de e-mail " + user.getEmail() + " cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public  String login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest.email(), loginRequest.password());
    }

}
