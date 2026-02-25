package com.example.fieldsync_inventory_api.dto.user.profile;

//=============================================================
// DTO (Data Transfer Object)
// This class is used to transfer data between layers.
// It exposes only the necessary fields to the client.
//=============================================================

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRequestDTO {
    private Integer userId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
}
