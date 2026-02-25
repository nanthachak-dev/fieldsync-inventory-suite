package com.example.fieldsync_inventory_api.service.user;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.user.user.AppUserRequestDTO;
import com.example.fieldsync_inventory_api.exception.BadRequestException;
import com.example.fieldsync_inventory_api.dto.user.user.AppUserResponseDTO;
import com.example.fieldsync_inventory_api.entity.user.AppUserEntity;
import com.example.fieldsync_inventory_api.entity.user.RoleEntity;
import com.example.fieldsync_inventory_api.entity.user.UserRoleEntity;
import com.example.fieldsync_inventory_api.repository.user.AppUserRepository;
import com.example.fieldsync_inventory_api.repository.user.RoleRepository;
import com.example.fieldsync_inventory_api.repository.user.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository repository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // Entity to DTO
    private AppUserResponseDTO mapToResponseDTO(AppUserEntity entity, AppUserRequestDTO dto) {
        List<String> roles;
        if (dto != null && dto.getRoles() != null) {
            roles = dto.getRoles();
        } else {
            roles = entity.getUserRoles() != null
                    ? entity.getUserRoles().stream()
                    .map(userRole -> userRole.getRole().getName())
                    .collect(Collectors.toList())
                    : Collections.emptyList();
        }

        return AppUserResponseDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .roles(roles)
                .build();
    }

    // DTO to Entity
    private AppUserEntity mapToEntity(AppUserRequestDTO dto) {
        return AppUserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .isActive(dto.getIsActive())
                .build();
    }

    // POST: /api/app-users
    @Transactional
    public AppUserResponseDTO createAppUser(AppUserRequestDTO dto) {
        if (repository.findByUsername(dto.getUsername()).isPresent())
            throw new BadRequestException("Username: "+dto.getUsername()+" is already taken");

        AppUserEntity entity = mapToEntity(dto);
        AppUserEntity savedUser = repository.save(entity);

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            for (String roleName : dto.getRoles()) {
                RoleEntity role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

                UserRoleEntity userRole = UserRoleEntity.builder()
                        .user(savedUser)
                        .role(role)
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        // Refresh to get roles
        return mapToResponseDTO(savedUser, dto);
    }

    // PUT: /api/app-users/:id
    @Transactional
    public AppUserResponseDTO updateAppUser(Integer id, AppUserRequestDTO dto) {
        AppUserEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found: " + id));

        // Username Uniqueness Check
        if (dto.getUsername() != null && !dto.getUsername().equals(existing.getUsername())) {
            if (repository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
                throw new BadRequestException("Username '" + dto.getUsername() + "' is already taken");
            }
            existing.setUsername(dto.getUsername());
        }

        // Update basic fields concisely
        Optional.ofNullable(dto.getUsername()).ifPresent(existing::setUsername);
        Optional.ofNullable(dto.getPassword()).ifPresent(p -> existing.setPassword(passwordEncoder.encode(p)));
        Optional.ofNullable(dto.getIsActive()).ifPresent(existing::setIsActive);

        // Role Updates
        if (dto.getRoles() == null) throw new BadRequestException("Role can't be null");
        if (dto.getRoles().size() > 5) throw new BadRequestException("Max 5 roles allowed");

        // Clear and FORCE DELETE now
        existing.getUserRoles().clear();
        repository.saveAndFlush(existing); // <--- This is the key fix

        // Add new roles
        dto.getRoles().forEach(roleName -> {
            RoleEntity role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

            existing.getUserRoles().add(UserRoleEntity.builder()
                    .user(existing)
                    .role(role)
                    .build());
        });

        // In a @Transactional method, the entity is auto-saved.
        // We pass 'existing' directly to the mapper.
        return mapToResponseDTO(existing, dto);
    }

    // DELETE: /api/app-users/:id
    public void deleteAppUser(Integer id) {
        repository.deleteById(id);
    }

    // GET: /api/app-users/:id
    public AppUserResponseDTO getAppUserById(Integer id) {
        AppUserEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
        return mapToResponseDTO(entity, null);
    }

    // GET: /api/app-users
    public List<AppUserResponseDTO> getAllAppUsers() {
        return repository.findAll()
                .stream()
                .map(entity -> mapToResponseDTO(entity, null))
                .collect(Collectors.toList());
    }
}