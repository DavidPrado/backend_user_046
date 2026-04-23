package com.baser_users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Set;

public record UserRegistrationDTO(
        @NotBlank(message = "O nome é obrigatório") String name,
        @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") String email,
        @NotBlank(message = "A senha é obrigatória") @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres") String password,
        Set<String> roles,
        @NotBlank(message = "O CPF é obrigatório") @CPF(message = "CPF em formato inválido") String cpf,
        @NotBlank(message = "O telefone é obrigatório") String phoneNumber,
        @NotBlank(message = "A data de nascimento é obrigatória") String birthDate,
        Boolean mustChangePassword
) {}