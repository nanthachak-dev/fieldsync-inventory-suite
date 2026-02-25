package com.example.fieldsync_inventory_api.dto.user.user_role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.dto.user.role.RoleCompactDTO;
import com.example.fieldsync_inventory_api.dto.user.user.AppUserCompactDTO;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleResponseDTO {
    private AppUserCompactDTO appUser;
    private RoleCompactDTO role;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
