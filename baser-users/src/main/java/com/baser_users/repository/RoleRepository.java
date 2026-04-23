package com.baser_users.repository;

import com.baser_users.model.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RoleRepository extends ReactiveCrudRepository<Role, UUID> {

    Mono<Role> findByName(String name);

    @Query("SELECT r.* FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = :userId LIMIT :limit OFFSET :offset")
    Flux<Role> findAllByUserId(UUID userId, int limit, long offset);

    @Query("SELECT r.* FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = :userId")
    Flux<Role> findAllByUserId(UUID userId);

    @Query("SELECT COUNT(r.id) FROM roles r " +
            "INNER JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = :userId")
    Mono<Long> countByUserId(UUID userId);

}
