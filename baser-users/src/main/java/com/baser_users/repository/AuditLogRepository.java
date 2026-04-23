package com.baser_users.repository;

import com.baser_users.model.AuditLog;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface AuditLogRepository extends ReactiveCrudRepository<AuditLog, UUID> {
}
