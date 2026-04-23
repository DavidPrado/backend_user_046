package com.baser_users.controller;


import com.baser_users.dto.RoleRequestDTO;
import com.baser_users.dto.RoleResponseDTO;
import com.baser_users.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RoleResponseDTO> create(@Valid @RequestBody RoleRequestDTO dto) {
        return roleService.create(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    public Mono<ResponseEntity<RoleResponseDTO>> getById(@PathVariable UUID id) {
        return roleService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public Mono<ResponseEntity<RoleResponseDTO>> update(@PathVariable UUID id, @Valid @RequestBody RoleRequestDTO dto) {
        return roleService.update(id, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return roleService.delete(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    public Flux<RoleResponseDTO> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        List<Integer> allowedSizes = List.of(5, 10, 20, 50, 100);
        int finalSize = allowedSizes.contains(size) ? size : 10;
        Pageable pageable = PageRequest.of(page, finalSize, Sort.by(direction, sortBy));
        return roleService.findAllPaginated(name, description, pageable);
    }


    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteRolesBatch(@RequestBody List<UUID> ids) {
        return roleService.deleteRolesBatch(ids);
    }
}
