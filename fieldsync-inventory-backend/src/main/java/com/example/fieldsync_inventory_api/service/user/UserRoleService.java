package com.example.fieldsync_inventory_api.service.user;


import com.example.fieldsync_inventory_api.dto.user.user_role.UserRoleRequestDTO;
import com.example.fieldsync_inventory_api.dto.user.user_role.UserRoleResponseDTO;

import java.util.List;

public interface UserRoleService {
    UserRoleResponseDTO create(UserRoleRequestDTO dto);
    List<UserRoleResponseDTO> getAll();
    void delete(Integer userId, Integer roleId);
    List<UserRoleResponseDTO> findByUserId(Integer userId);
    List<UserRoleResponseDTO> findByRoleId(Integer roleId);
}
