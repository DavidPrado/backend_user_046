package com.baser_users.service;

import com.baser_users.model.AuditLog;
import com.baser_users.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public Mono<Void> logChange(String entityName, UUID entityId, String action, Object oldState, Object newState) {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getName())
                .defaultIfEmpty("SYSTEM")
                .flatMap(user -> {
                    try {
                        AuditLog logEntry = AuditLog.builder()
                                .entityName(entityName)
                                .entityId(entityId)
                                .action(action)
                                .oldValue(oldState != null ? objectMapper.writeValueAsString(oldState) : null)
                                .newValue(newState != null ? objectMapper.writeValueAsString(newState) : null)
                                .changedBy(user)
                                .changedAt(LocalDateTime.now())
                                .build();

                        return auditLogRepository.save(logEntry).then();
                    } catch (Exception e) {
                        log.error("Erro ao gerar log de auditoria", e);
                        return Mono.empty();
                    }
                });
    }
}
