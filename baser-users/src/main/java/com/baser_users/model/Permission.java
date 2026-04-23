package com.baser_users.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("permissions")
public class Permission {

    @Id
    private UUID id;
    private String name;
    private String description;

}
