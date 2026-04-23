package com.baser_users.controller;

import com.baser_users.dto.*;
import com.baser_users.model.User;
import com.baser_users.service.AuthService;
import com.baser_users.service.PasswordResetService;
import com.baser_users.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(UserService userService, AuthService authService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public Mono<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/forgot-password")
    public Mono<ResponseEntity<Map<String, String>>> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        return passwordResetService.initiatePasswordReset(request.email())
                .thenReturn(ResponseEntity.ok(Map.of("message", "Processo de recuperação iniciado.")))
                .onErrorResume(e -> {
                    if (e instanceof ResponseStatusException) {
                        return Mono.error(e);
                    }
                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno"));
                });
    }

    @PostMapping("/reset-password")
    public Mono<ResponseEntity<String>> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        return passwordResetService.completePasswordReset(request.token(), request.newPassword())
                .then(Mono.just(ResponseEntity.ok().body("Senha alterada com sucesso!")))
                .onErrorResume(e -> {
                    HttpStatus status = (e instanceof ResponseStatusException) ?
                            (HttpStatus) ((ResponseStatusException) e).getStatusCode() :
                            HttpStatus.INTERNAL_SERVER_ERROR;

                    return Mono.just(ResponseEntity.status(status).body(e.getMessage()));
                });
    }

    @PostMapping("/change-password")
    public Mono<ResponseEntity<String>> changePassword(@Valid @RequestBody UpdatePasswordDTO dto) {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (User) ctx.getAuthentication().getPrincipal())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contexto vazio")))
                .flatMap(user -> userService.updatePasswordAndClearFlag(user, dto))
                .then(Mono.just(ResponseEntity.ok().body("Senha alterada com sucesso!")))
                .onErrorResume(e -> {
                    HttpStatus status = (e instanceof ResponseStatusException) ?
                            (HttpStatus) ((ResponseStatusException) e).getStatusCode() :
                            HttpStatus.INTERNAL_SERVER_ERROR;

                    return Mono.just(ResponseEntity.status(status).body(e.getMessage()));
                });
    }
}
