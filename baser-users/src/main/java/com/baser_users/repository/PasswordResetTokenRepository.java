package com.baser_users.repository;

import com.baser_users.model.PasswordResetToken;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PasswordResetTokenRepository extends ReactiveCrudRepository<PasswordResetToken, UUID> {

    Mono<PasswordResetToken> findByTokenAndUsedFalse(String token);

    Mono<Void> deleteAllByUserId(UUID userId);

    Mono<PasswordResetToken> findByToken(String token);

    Mono<Void> deleteAllByExpiryDateBefore(LocalDateTime dateTime);
}
