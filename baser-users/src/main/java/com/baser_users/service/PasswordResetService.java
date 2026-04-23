package com.baser_users.service;

import com.baser_users.model.PasswordResetToken;
import com.baser_users.repository.PasswordResetTokenRepository;
import com.baser_users.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository; // Seu repositório de usuários
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final MeterRegistry registry;

    @Transactional
    public Mono<Void> initiatePasswordReset(String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "E-mail não cadastrado")))
                .flatMap(user -> {
                    String token = UUID.randomUUID().toString();
                    PasswordResetToken resetToken = PasswordResetToken.builder()
                            .userId(user.getId())
                            .token(token)
                            .expiryDate(LocalDateTime.now().plusMinutes(15))
                            .used(false)
                            .build();

                    return tokenRepository.save(resetToken)
                            .doOnSuccess(savedToken -> {
                                emailService.sendPasswordResetEmail(user.getEmail(), token)
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .subscribe();
                            });
                })
                .then();
    }

    @Transactional
    public Mono<Void> completePasswordReset(String token, String newPassword) {
        return tokenRepository.findByToken(token)

                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Token inválido")))
                .flatMap(resetToken -> {

                    if (resetToken.getUsed()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este token já foi utilizado"));
                    }

                    if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "O token expirou"));
                    }

                    return userRepository.findById(resetToken.getUserId())
                            .flatMap(user -> {
                                user.setPassword(passwordEncoder.encode(newPassword));
                                user.setMustChangePassword(true);
                                resetToken.setUsed(true);

                                return userRepository.save(user)
                                        .then(tokenRepository.save(resetToken));
                            });
                })
                .then()
                .doOnSuccess(v -> log.info("Senha redefinida com sucesso para o token: {}", token));
    }
}
