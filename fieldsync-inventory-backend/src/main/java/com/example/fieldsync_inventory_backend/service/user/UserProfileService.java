package com.example.fieldsync_inventory_backend.service.user;

//=============================================================
// Service
// This class contains the business logic for UserProfile.
// It uses the repository to interact with the database.
//=============================================================

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.user.user.AppUserCompactDTO;
import com.example.fieldsync_inventory_backend.dto.user.profile.UserProfileRequestDTO;
import com.example.fieldsync_inventory_backend.dto.user.profile.UserProfileResponseDTO;
import com.example.fieldsync_inventory_backend.entity.user.AppUserEntity;
import com.example.fieldsync_inventory_backend.entity.user.UserProfileEntity;
import com.example.fieldsync_inventory_backend.repository.user.AppUserRepository;
import com.example.fieldsync_inventory_backend.repository.user.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository repository;
    private final AppUserRepository appUserRepository;

    // Helper method to map Entity to DTO
    private UserProfileResponseDTO mapToResponseDTO(UserProfileEntity entity) {
        // Create UserDTO
        AppUserCompactDTO userDTO = null;
        userDTO = AppUserCompactDTO.builder()
                .id(entity.getAppUser().getId())
                .username(entity.getAppUser().getUsername())
                .build();

        return UserProfileResponseDTO.builder()
                .user(userDTO)
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    // Helper method to map DTO to Entity
    private UserProfileEntity mapToEntity(UserProfileRequestDTO dto) {
        // Find the AppUser to link the UserProfile to
        AppUserEntity appUser = appUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("No record found for UserProfile " +
                        "creation with user_id: " + dto.getUserId()));

        return UserProfileEntity.builder()
                .appUser(appUser) // Let @MapsId handle the userId
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }

    public UserProfileResponseDTO createOrUpdateUserProfile(UserProfileRequestDTO dto) {

        // This is the key change: Find the existing entity or create a new one.
        Optional<UserProfileEntity> existingProfile = repository.findById(dto.getUserId());

        UserProfileEntity entity;

        if (existingProfile.isPresent()) {
            // We're working with a managed entity from the database.
            entity = existingProfile.get();
            entity.setFullName(dto.getFullName());
            entity.setEmail(dto.getEmail());
            entity.setPhone(dto.getPhone());
            entity.setAddress(dto.getAddress());
            entity.setDeletedAt(null);
        } else {
            // We're creating a new entity because it doesn't exist yet.
            AppUserEntity appUser = appUserRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("No corresponding user " +
                            "record found for user_id: " + dto.getUserId()));

            entity = UserProfileEntity.builder()
                    .appUser(appUser)
                    .fullName(dto.getFullName())
                    .email(dto.getEmail())
                    .phone(dto.getPhone())
                    .address(dto.getAddress())
                    .build();
        }

        // Now, `save()` will either INSERT the new entity or UPDATE the managed one.
        UserProfileEntity savedEntity = repository.save(entity);

        return mapToResponseDTO(savedEntity);
    }

    public void deleteUserProfile(Integer id) {
        repository.deleteById(id);
    }

    public UserProfileResponseDTO getUserProfileByUserId(Integer userId) {
        UserProfileEntity entity = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + userId));
        return mapToResponseDTO(entity);
    }

    public List<UserProfileResponseDTO> getAllUserProfiles() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
