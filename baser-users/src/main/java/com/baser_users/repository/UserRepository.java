package com.baser_users.repository;

import com.baser_users.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveSortingRepository<User, UUID>, ReactiveCrudRepository<User, UUID>, ReactiveQueryByExampleExecutor<User> {

    Mono<User> findByEmail(String email);

    @Query("INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)")
    Mono<Void> saveRoleRelation(UUID userId, UUID roleId);

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsByCpf(String cpf);
}
