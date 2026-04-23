package com.baser_users.service;

import com.baser_users.dto.*;
import com.baser_users.matchers.UserMatchers;
import com.baser_users.model.Role;
import com.baser_users.model.User;
import com.baser_users.model.UserRole;
import com.baser_users.repository.RoleRepository;
import com.baser_users.repository.UserRepository;
import com.baser_users.repository.UserRoleRepository;
import com.baser_users.util.Util;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    public Mono<User> registerUser(UserRegistrationDTO dto) {
        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .mustChangePassword(true)
                .cpf(dto.cpf())
                .phoneNumber(dto.phoneNumber())
                .birthDate(Util.convertStringToLocalDate(dto.birthDate()))
                .active(true)
                .password(passwordEncoder.encode(dto.password()))
                .build();

        return userRepository.save(user).flatMap(savedUser -> {
            return Flux.fromIterable(dto.roles())
                    .flatMap(roleRepository::findByName)
                    .flatMap(role -> userRepository.saveRoleRelation(savedUser.getId(), role.getId())
                            .thenReturn(role))
                    .collectList()
                    .map(roles -> {
                        savedUser.setRoles(new HashSet<>(roles));
                        return savedUser;
                    });
        });
    }

    @Transactional
    public Mono<Void> updatePasswordAndClearFlag(User user, UpdatePasswordDTO dto) {

        if (!dto.newPassword().equals(dto.confirmPassword())) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "A nova senha e a confirmação da senha não coincidem"));
        }

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha antiga incorreta"));
        }

        if (passwordEncoder.matches(dto.newPassword(), user.getPassword())) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "A nova senha deve ser diferente da senha antiga"));
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        user.setMustChangePassword(false);

        return userRepository.save(user).then();
    }

    public Flux<UserListResponseDTO> findAllByUsersFilter(UserListRequestDTO filter, Pageable pageable) {
        User filterUser = applyFilter(filter);
        ExampleMatcher matcher = UserMatchers.listUsersFilter();
        Example<User> example = Example.of(filterUser, matcher);

        return userRepository.findAll(example, pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .map(this::toUserListResponseDTO);
    }

    private UserListResponseDTO toUserListResponseDTO(User user) {
        return new UserListResponseDTO(
                user.getId(),
                user.getCode(),
                user.getName(),
                user.getEmail(),
                user.getMustChangePassword(),
                user.getCpf(),
                user.getPhoneNumber(),
                user.getBirthDate() != null ? user.getBirthDate().toString() : null,
                user.getActive(),
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null,
                user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null
        );
    }

    private User applyFilter(UserListRequestDTO filter) {
        User user = new User();
        user.setCode(filter.code());
        user.setMustChangePassword(filter.mustChangePassword());
        user.setPhoneNumber(filter.phoneNumber());
        user.setName(filter.name());
        user.setEmail(filter.email());
        user.setCpf(filter.cpf());
        user.setActive(filter.active());
        return user;
    }

    @Transactional
    public Mono<User> updateUser(UUID id, UserUpdateDTO dto) {

        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuário não encontrado")))
                .flatMap(user -> {

                    return Mono.when(validateEmailUniqueness(user, dto), validateCpfUniqueness(user, dto))
                            .then(Mono.defer(()-> updateAndSave(user, dto)));
                });
    }

    private Mono<Void> validateEmailUniqueness(User user, UserUpdateDTO dto) {
        if (!user.getEmail().equals(dto.email())) {
            return userRepository.existsByEmail(dto.email())
                    .flatMap(exists -> {
                        if (Boolean.TRUE.equals(exists)) {
                            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Email já está em uso por outro usuário"));
                        }
                        return Mono.empty();
                    });
        }
        return Mono.empty();
    }


    private Mono<Void> validateCpfUniqueness(User user, UserUpdateDTO dto) {
        if (!user.getCpf().equals(dto.cpf())) {
            return userRepository.existsByCpf(dto.cpf())
                    .flatMap(exists -> {
                        if (exists) {
                            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "CPF já está em uso por outro usuário"));
                        }
                        return Mono.empty();
                    });
        }
        return Mono.empty();

    }

    private Mono<User> updateAndSave(User user, UserUpdateDTO dto) {
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setBirthDate(Util.convertStringToLocalDate(dto.birthDate()));
        user.setPhoneNumber(dto.phoneNumber());
        user.setCpf(dto.cpf());
        user.setMustChangePassword(dto.mustChangePassword());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(dto.active());
        return userRepository.save(user);
    }

    public Mono<UserListResponseDTO> getUserById(UUID id) {
        return userRepository.findById(id)
                .flatMap(user -> roleRepository.findAllByUserId(id).collectList()
                        .map(roles -> {
                            user.setRoles(new HashSet<>(roles));
                            return toUserListResponseDTO(user);
                        })
                );
    }

    @Transactional
    public Mono<Void> deleteUser(UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .flatMap(user -> userRepository.deleteById(id));
    }

    @Transactional
    public Mono<Void> deleteUsersBatch(List<UUID> ids) {
        return userRepository.findAllById(ids)
                .collectList()
                .flatMap(users -> {
                    if (users.size() != ids.size()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Um ou mais usuários não foram encontrados"));
                    }
                    return userRepository.deleteAll(users);
                });
    }

    @Transactional
    public Mono<Void> updateUserRoles(UUID userId, List<UUID> roleIds) {
        return userRepository.existsById(userId)
                .flatMap(exists ->{
                    if(!exists){
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
                    }

                    return userRoleRepository.deleteByUserId(userId)
                            .thenMany(Flux.fromIterable(roleIds))
                            .flatMap(roleId -> userRoleRepository.save(
                                    UserRole.builder()
                                            .userId(userId)
                                            .roleId(roleId)
                                            .build()
                                    ))
                            .then();
                });

    }

    public Mono<Page<Role>> findRolesByUserId(UUID userId, Pageable pageable) {
        return roleRepository.countByUserId(userId)
                .flatMap(count -> roleRepository.findAllByUserId(userId, pageable.getPageSize(), pageable.getOffset())
                .collectList()
                .map(roles -> new PageImpl<>(roles, pageable, count))
                );
    }
}
