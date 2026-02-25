package com.example.fieldsync_inventory_api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDto {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
