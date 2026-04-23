package com.baser_users.repository;

import com.baser_users.model.Permission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends ReactiveCrudRepository<Permission, UUID> {

    @Query("SELECT p.* FROM permissions p " + "JOIN role_permissions rp ON p.id = rp.permission_id " + "WHERE rp.role_id = :roleId ")
    Flux<Permission> findByRoleId(UUID roleId);

    Flux<Permission> findAllBy(Pageable pageable);

    Flux<Permission> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
