package com.elisariane.minha_tcc_backend.service;

import com.elisariane.minha_tcc_backend.models.User;
import com.elisariane.minha_tcc_backend.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private static final byte[] SECRET_KEY_BYTES = new byte[64];
    static {
        new java.security.SecureRandom().nextBytes(SECRET_KEY_BYTES); // Gera bytes aleatórios seguros
    }
    private final Key jwtSecretKey = Keys.hmacShaKeyFor(SECRET_KEY_BYTES);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UsernameNotFoundException("Usuário já cadastrado!");
        }
        User userToSave = new User();
        userToSave.setEmail(user.getEmail());
        userToSave.setName(user.getName());
        userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
        userToSave.setRole(user.getRole());
        userRepository.save(userToSave);
    }

    public String authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails);
    }

    private String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 dia
                .signWith(jwtSecretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) jwtSecretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
