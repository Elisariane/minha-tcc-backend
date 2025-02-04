package com.elisariane.minha_tcc_backend.repository;

import com.elisariane.minha_tcc_backend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
}
