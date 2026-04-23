package com.baser_users.repository;

import com.baser_users.model.UserRole;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, UUID> {

    @Query("SELECT * FROM user_roles WHERE user_id = :userId")
    Flux<UserRole> findAllByUserId(UUID userId);

    @Modifying
    @Query("DELETE FROM user_roles WHERE user_id = :userId")
    Mono<Void> deleteByUserId(UUID userId);
}
