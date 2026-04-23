package com.baser_users.dto;

import java.util.List;
import java.util.UUID;

public record RoleResponseDTO(
        UUID id,
        String name,
        String description,
        List<UUID> permissionIds
) {
}
