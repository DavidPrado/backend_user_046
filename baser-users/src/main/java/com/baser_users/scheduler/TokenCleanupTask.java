package com.baser_users.scheduler;

import com.baser_users.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupTask {
    private final PasswordResetTokenRepository tokenRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanExpiredTokens() {
        tokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now())
                .doOnSuccess(v -> log.info("Limpeza de tokens expirados concluída com sucesso."))
                .doOnError(e -> log.error("Erro ao limpar tokens expirados: {}", e.getMessage()))
                .subscribe();
    }

}
