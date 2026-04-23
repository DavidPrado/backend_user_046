package com.baser_users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("audit_log")
public class AuditLog {

    @Id
    private UUID id;
    private String entityName;
    private UUID entityId;
    private String action;
    private String oldValue;
    private String newValue;
    private String changedBy;
    private LocalDateTime changedAt;
}
