package com.baser_users.dto;

import java.time.LocalDate;

public record UserListRequestDTO(Long code, String name, String email, Boolean mustChangePassword, String cpf,
                                 String phoneNumber, LocalDate birthDate, Boolean active) {
}
