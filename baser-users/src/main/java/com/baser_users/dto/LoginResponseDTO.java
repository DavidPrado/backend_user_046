package com.baser_users.dto;

public record LoginResponseDTO(String token,
                               String type, // Bearer
                               Long expiresIn,
                               boolean mustChangePassword) {

}
