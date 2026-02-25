package com.example.fieldsync_inventory_api.dto.user.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleCompactDTO {
    private Integer id;
    private String name;
    private String description;
}