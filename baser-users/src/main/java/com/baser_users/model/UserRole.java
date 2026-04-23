package com.baser_users.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table("user_roles")
public class UserRole {
    private UUID userId;
    private UUID roleId;
}
