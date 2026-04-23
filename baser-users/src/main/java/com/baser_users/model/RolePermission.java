package com.baser_users.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@Table("role_permissions")
public class RolePermission {

    @Column("role_id")
    UUID roleId;
    @Column("permission_id")
    UUID permissionId;
}
