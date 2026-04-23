package com.baser_users.service;

import com.baser_users.model.Permission;
import com.baser_users.repository.PermissionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Flux<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Flux<Permission> findAll(String name, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            permissionRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return permissionRepository.findAllBy(pageable);

    }

}
