package com.baser_users.dto;

import java.util.UUID;

public record UserListResponseDTO (
        UUID id, Long code, String name, String email, Boolean mustChangePassword, String cpf,
        String phoneNumber, String birthDate, Boolean active, String createdAt, String updatedAt
){
}
