package org.alpermelkeli.repository;

import org.alpermelkeli.model.RefreshTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshTokenEntity, String> {
    RefreshTokenEntity findByRefreshToken(String refreshToken);
    boolean existsByRefreshToken(String refreshToken);
}
