package com.example.fieldsync_inventory_backend.dto.user.profile;

//=============================================================
// DTO (Data Transfer Object)
// This class is used to transfer data between layers.
// It exposes only the necessary fields to the client.
//=============================================================

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.user.user.AppUserCompactDTO;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDTO {
    private AppUserCompactDTO user;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
