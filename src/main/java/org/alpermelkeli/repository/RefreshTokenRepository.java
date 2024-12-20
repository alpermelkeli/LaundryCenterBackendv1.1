package org.alpermelkeli.repository;

import org.alpermelkeli.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    RefreshToken findByRefreshToken(String refreshToken);
    boolean existsByRefreshToken(String refreshToken);
}
