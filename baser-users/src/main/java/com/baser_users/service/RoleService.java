package com.baser_users.service;

import com.baser_users.dto.RoleRequestDTO;
import com.baser_users.dto.RoleResponseDTO;
import com.baser_users.model.Role;
import com.baser_users.model.RolePermission;
import com.baser_users.repository.RolePermissionRepository;
import com.baser_users.repository.RoleRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final R2dbcEntityTemplate entityTemplate;

    public RoleService(RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository, R2dbcEntityTemplate entityTemplate) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.entityTemplate = entityTemplate;
    }

    @Transactional
    public Mono<RoleResponseDTO> create(RoleRequestDTO dto) {

        Role role = new Role(null, dto.name().toUpperCase(), dto.description());

        return roleRepository.save(role).flatMap(savedRole -> attachPermissions(savedRole, dto.permissionIds()));

    }

    @Transactional
    public Mono<RoleResponseDTO> update(UUID roleId, RoleRequestDTO dto) {
        return roleRepository.findById(roleId)
                .flatMap(existingRole -> {
                    existingRole.setName(dto.name().toUpperCase());
                    existingRole.setDescription(dto.description());
                    return roleRepository.save(existingRole);
                })
                .flatMap(updatedRole ->
                        rolePermissionRepository.deleteAllByRoleId(roleId)
                                .then(attachPermissions(updatedRole, dto.permissionIds()))
                );
    }


    public Mono<RoleResponseDTO> findById(UUID roleId) {
        return roleRepository.findById(roleId)
                .flatMap(role ->
                        rolePermissionRepository.findAllByRoleId(roleId)
                                .map(RolePermission::getPermissionId)
                                .collectList()
                                .map(permissionIds -> new RoleResponseDTO(role.getId(), role.getName(), role.getDescription(), permissionIds))
                );
    }

    private Mono<RoleResponseDTO> attachPermissions(Role role, List<UUID> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return Mono.just(new RoleResponseDTO(role.getId(), role.getName(), role.getDescription(), List.of()));
        }

        var links = permissionIds.stream()
                .map(permissionId -> new RolePermission(role.getId(), permissionId))
                .toList();

        return rolePermissionRepository.saveAll(links).then(Mono.just(new RoleResponseDTO(role.getId(), role.getName(), role.getDescription(), permissionIds)));
    }

    @Transactional
    public Mono<Void> delete(UUID roleId) {
        return roleRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(role -> rolePermissionRepository.deleteAllByRoleId(roleId)
                        .then(Mono.defer(() -> roleRepository.delete(role)))
                );
    }


    public Flux<RoleResponseDTO> findAllPaginated(String name, String description, Pageable pageable) {

        Criteria criteria = Criteria.empty();

        if (name != null && !name.isBlank()) {
            criteria = criteria.and("name").like("%" + name.toUpperCase() + "%");
        }

        if (description != null && !description.isBlank()) {
            criteria = criteria.and("description").like("%" + description + "%");
        }

        return entityTemplate.select(Role.class)
                .from("roles")
                .matching(Query.query(criteria).with(pageable))
                .all()
                .flatMap(role ->
                        rolePermissionRepository.findAllByRoleId(role.getId())
                                .map(RolePermission::getPermissionId)
                                .collectList()
                                .map(permissionIds -> new RoleResponseDTO(role.getId(), role.getName(), role.getDescription(), permissionIds))
                );
    }

    @Transactional
    public Mono<Void> deleteRolesBatch(List<UUID> ids) {
        return roleRepository.findAllById(ids)
                .collectList()
                .flatMap(roles -> {
                    if (roles.size() != ids.size()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Um ou mais perfils não foram encontrados"));
                    }
                    return roleRepository.deleteAll(roles);
                });
    }
}
