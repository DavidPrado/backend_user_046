package com.baser_users.repository;

import com.baser_users.model.RolePermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RolePermissionRepository extends ReactiveCrudRepository<RolePermission, UUID> {

    Flux<RolePermission> findAllByRoleId(UUID roleId);
    Mono<Void> deleteAllByRoleId(UUID roleId);

}
