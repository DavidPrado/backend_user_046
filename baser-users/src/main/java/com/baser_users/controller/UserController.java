package com.baser_users.controller;

import com.baser_users.dto.UserListRequestDTO;
import com.baser_users.dto.UserListResponseDTO;
import com.baser_users.dto.UserRegistrationDTO;
import com.baser_users.dto.UserUpdateDTO;
import com.baser_users.model.Role;
import com.baser_users.model.User;
import com.baser_users.security.SystemUserDetails;
import com.baser_users.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> register(@Valid @RequestBody UserRegistrationDTO dto) {
        return userService.registerUser(dto);
    }

    @GetMapping("/me")
    public Mono<User> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> {
                    SystemUserDetails details = (SystemUserDetails) ctx.getAuthentication().getPrincipal();
                    return details.getUser();
                })
                .cast(User.class)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado")));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDTO dto) {
        return userService.updateUser(id, dto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public Flux<UserListResponseDTO> findAllUsersFilter(
            UserListRequestDTO filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100, 300);
        int finalSize = allowedSizes.contains(size) ? size : 10;

        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return userService.findAllByUsersFilter(filter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public Mono<ResponseEntity<UserListResponseDTO>> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUsersBatch(@RequestBody List<UUID> ids) {
        return userService.deleteUsersBatch(ids);
    }

    @GetMapping("{id}/roles")
    @PreAuthorize("hasAuthority('USER_READ')")
    public Mono<ResponseEntity<Page<Role>>> getUserRoles(@PathVariable UUID id,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.findRolesByUserId(id, pageable)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")));
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public Mono<ResponseEntity<String>> updateUserRoles(@PathVariable UUID id, @RequestBody List<UUID> roleIds) {
        return userService.updateUserRoles(id, roleIds)
                .then(Mono.just(ResponseEntity.ok("Roles atualizadas com sucesso.")));
    }

}
