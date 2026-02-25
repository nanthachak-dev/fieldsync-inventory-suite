package com.example.fieldsync_inventory_api.controller.user;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.user.role.RoleRequestDTO;
import com.example.fieldsync_inventory_api.dto.user.role.RoleResponseDTO;
import com.example.fieldsync_inventory_api.service.user.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService service;

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(@RequestBody RoleRequestDTO dto) {
        return ResponseEntity.ok(service.createRole(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getRoleById(id));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllRoles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> update(@PathVariable Integer id,
                                                    @RequestBody RoleRequestDTO dto) {
        return ResponseEntity.ok(service.updateRole(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}