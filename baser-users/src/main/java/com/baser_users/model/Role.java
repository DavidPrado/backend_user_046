package com.baser_users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("roles")
public class Role {

    @Id
    private UUID id;
    private String name;
    private String description;
    @Transient
    @Builder.Default
    private List<Permission> permissions = new ArrayList<>();

    public Role(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;

    }
}
