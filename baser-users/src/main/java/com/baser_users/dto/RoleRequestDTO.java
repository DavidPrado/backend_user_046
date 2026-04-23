package com.baser_users.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record RoleRequestDTO(
        @NotBlank(message = "O nome do perfil de acesso é obrigatório")
        String name,

        @NotBlank(message = "A descrição do perfil de acesso é obrigatória")
        String description,

        List<UUID> permissionIds
) {
}
