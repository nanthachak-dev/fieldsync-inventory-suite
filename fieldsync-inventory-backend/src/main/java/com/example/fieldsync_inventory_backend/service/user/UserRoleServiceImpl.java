package com.example.fieldsync_inventory_backend.service.user;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.user.role.RoleCompactDTO;
import com.example.fieldsync_inventory_backend.dto.user.user.AppUserCompactDTO;
import com.example.fieldsync_inventory_backend.dto.user.user_role.UserRoleRequestDTO;
import com.example.fieldsync_inventory_backend.dto.user.user_role.UserRoleResponseDTO;
import com.example.fieldsync_inventory_backend.entity.user.AppUserEntity;
import com.example.fieldsync_inventory_backend.entity.user.RoleEntity;
import com.example.fieldsync_inventory_backend.entity.user.UserRoleEntity;
import com.example.fieldsync_inventory_backend.entity.user.UserRoleId;
import com.example.fieldsync_inventory_backend.repository.user.AppUserRepository;
import com.example.fieldsync_inventory_backend.repository.user.RoleRepository;
import com.example.fieldsync_inventory_backend.repository.user.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    private UserRoleResponseDTO mapToResponseDTO(UserRoleEntity entity) {
        AppUserCompactDTO appUserDTO = null;
        if (entity.getUser() != null) {
            appUserDTO = AppUserCompactDTO.builder()
                    .id(entity.getUser().getId())
                    .username(entity.getUser().getUsername())
                    .build();
        }

        RoleCompactDTO roleDTO = null;
        if (entity.getRole()!=null){
            roleDTO = RoleCompactDTO.builder()
                    .id(entity.getRole().getId())
                    .name(entity.getRole().getName())
                    .description(entity.getRole().getDescription())
                    .build();
        }

        return UserRoleResponseDTO.builder()
                .appUser(appUserDTO)
                .role(roleDTO)
                .createdAt(entity.getRole().getCreatedAt())
                .updatedAt(entity.getRole().getUpdatedAt())
                .deletedAt(entity.getRole().getDeletedAt())
                .build();
    }

    private UserRoleEntity mapToEntity(UserRoleRequestDTO dto) {
        AppUserEntity user = appUserRepository.findById(dto.getUserId()).orElseThrow();
        RoleEntity role = roleRepository.findById(dto.getRoleId()).orElseThrow();

        return UserRoleEntity.builder()
                .user(user)
                .role(role)
                .build();
    }

    @Override
    public UserRoleResponseDTO create(UserRoleRequestDTO dto) {
        UserRoleEntity entity = mapToEntity(dto);
        // JPA Auditing will automatically set created_at and updated_at
        return mapToResponseDTO(userRoleRepository.save(entity));
    }

    @Override
    public void delete(Integer userId, Integer roleId) {
        userRoleRepository.deleteById(new UserRoleId(userId, roleId));
    }

    @Override
    public List<UserRoleResponseDTO> getAll() {
        return userRoleRepository.findAll().stream().map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRoleResponseDTO> findByUserId(Integer userId) {
        return userRoleRepository.findByUserId(userId).stream().map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRoleResponseDTO> findByRoleId(Integer roleId) {
        return userRoleRepository.findByRoleId(roleId).stream().map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
