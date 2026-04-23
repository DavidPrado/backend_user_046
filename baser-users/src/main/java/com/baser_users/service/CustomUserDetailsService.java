package com.baser_users.service;

import com.baser_users.model.User;
import com.baser_users.repository.PermissionRepository;
import com.baser_users.repository.RoleRepository;
import com.baser_users.repository.UserRepository;
import com.baser_users.security.SystemUserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return userRepository.findByEmail(email)
                .flatMap(this::loadRolesAndPermissions);
    }

    private Mono<UserDetails> loadRolesAndPermissions(User user) {
        return roleRepository.findAllByUserId(user.getId())
                .flatMap(role ->
                        permissionRepository.findByRoleId(role.getId())
                                .collectList()
                                .map(permissions -> {
                                    role.setPermissions(permissions);
                                    return role;
                                })
                )
                .collectList()
                .map(roles -> new SystemUserDetails(user, roles));
    }
}
