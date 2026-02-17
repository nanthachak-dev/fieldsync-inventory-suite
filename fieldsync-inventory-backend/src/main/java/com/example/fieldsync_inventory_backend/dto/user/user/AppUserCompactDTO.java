package com.example.fieldsync_inventory_backend.dto.user.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserCompactDTO {
    private Integer id;
    private String username;
}
