package com.example.fieldsync_inventory_api.controller.user;

//=============================================================
// Controller
// This class handles incoming HTTP requests and responses.
// It exposes REST endpoints for the UserProfile resource.
//=============================================================

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.user.profile.UserProfileRequestDTO;
import com.example.fieldsync_inventory_api.dto.user.profile.UserProfileResponseDTO;
import com.example.fieldsync_inventory_api.service.user.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;

    @PostMapping
    public ResponseEntity<UserProfileResponseDTO> createOrUpdate(@RequestBody UserProfileRequestDTO dto) {
        // This endpoint will handle both creation and updates
        return ResponseEntity.ok(service.createOrUpdateUserProfile(dto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Integer userId) {
        service.deleteUserProfile(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDTO> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getUserProfileByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserProfileResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllUserProfiles());
    }
}
