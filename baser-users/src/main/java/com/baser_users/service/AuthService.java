package com.baser_users.service;

import com.baser_users.dto.LoginRequestDTO;
import com.baser_users.dto.LoginResponseDTO;
import com.baser_users.model.User;
import com.baser_users.security.SystemUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomUserDetailsService userDetailsService; // Usar o seu service customizado
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Mono<LoginResponseDTO> login(LoginRequestDTO dto) {
        return userDetailsService.findByUsername(dto.email())
                .cast(SystemUserDetails.class)
                .flatMap(userDetails -> {
                    if (passwordEncoder.matches(dto.password(), userDetails.getPassword())) {
                        String token = jwtService.generateToken(userDetails);
                        User user = userDetails.getUser();
                        boolean mustChange = user.getMustChangePassword() != null && user.getMustChangePassword();
                        return Mono.just(new LoginResponseDTO(
                                token,
                                "Bearer",
                                32400000L,
                                mustChange
                        ));
                    }
                    return Mono.error(new BadCredentialsException("Senha inválida"));
                })
                .switchIfEmpty(Mono.error(new BadCredentialsException("Usuário não encontrado")));
    }
}
