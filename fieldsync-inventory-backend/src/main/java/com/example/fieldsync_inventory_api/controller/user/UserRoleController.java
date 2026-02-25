package com.example.fieldsync_inventory_api.controller.user;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.user.user_role.UserRoleRequestDTO;
import com.example.fieldsync_inventory_api.dto.user.user_role.UserRoleResponseDTO;
import com.example.fieldsync_inventory_api.service.user.UserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<UserRoleResponseDTO> create(@RequestBody UserRoleRequestDTO dto) {
        return ResponseEntity.ok(userRoleService.create(dto));
    }

    @DeleteMapping("/{userId}/{roleId}")
    public ResponseEntity<Void> delete(@PathVariable Integer userId, @PathVariable Integer roleId) {
        userRoleService.delete(userId, roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserRoleResponseDTO>> getAll() {

        return ResponseEntity.ok(userRoleService.getAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRoleResponseDTO>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userRoleService.findByUserId(userId));
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<UserRoleResponseDTO>> getByRole(@PathVariable Integer roleId) {
        return ResponseEntity.ok(userRoleService.findByRoleId(roleId));
    }

    // No@PutMappting because there's only composite key (user and role, no extra field)
}
