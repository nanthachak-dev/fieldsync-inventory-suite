package com.example.fieldsync_inventory_api.dto.user.user_role;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleRequestDTO {
    private Integer userId;
    private Integer roleId;
}
