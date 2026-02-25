package com.example.fieldsync_inventory_api.controller.user;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.user.user.AppUserRequestDTO;
import com.example.fieldsync_inventory_api.dto.user.user.AppUserResponseDTO;
import com.example.fieldsync_inventory_api.service.auth.AuthService;
import com.example.fieldsync_inventory_api.service.user.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app-users")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService service;
    private final AuthService authService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUserResponseDTO> create(@RequestBody AppUserRequestDTO dto) {
        return ResponseEntity.ok(service.createAppUser(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<AppUserResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getAppUserById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppUserResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllAppUsers());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<AppUserResponseDTO> update(@PathVariable Integer id,
            @RequestBody AppUserRequestDTO dto) {
        return ResponseEntity.ok(service.updateAppUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteAppUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/kick")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> kickUser(@PathVariable Integer id) {
        authService.revokeTokensByUserId(id);
        return ResponseEntity.ok().build();
    }
}