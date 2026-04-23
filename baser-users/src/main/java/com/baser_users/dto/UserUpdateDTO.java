package com.baser_users.dto;

public record UserUpdateDTO(String name,
                            String email,
                            Boolean active,
                            Boolean mustChangePassword,
                            String phoneNumber,
                            String cpf,
                            String birthDate) {
}
