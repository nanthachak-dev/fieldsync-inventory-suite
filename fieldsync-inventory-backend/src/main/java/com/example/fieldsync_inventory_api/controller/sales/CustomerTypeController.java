package com.example.fieldsync_inventory_api.controller.sales;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.sales.customer_type.CustomerTypeRequestDTO;
import com.example.fieldsync_inventory_api.dto.sales.customer_type.CustomerTypeResponseDTO;
import com.example.fieldsync_inventory_api.service.sales.CustomerTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-types")
@RequiredArgsConstructor
public class CustomerTypeController {
    private final CustomerTypeService service;

    @PostMapping
    public ResponseEntity<CustomerTypeResponseDTO> create(@RequestBody CustomerTypeRequestDTO dto) {
        return ResponseEntity.ok(service.createCustomerType(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerTypeResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getCustomerTypeById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerTypeResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllCustomerTypes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerTypeResponseDTO> update(@PathVariable Integer id,
                                                         @RequestBody CustomerTypeRequestDTO dto) {
        return ResponseEntity.ok(service.updateCustomerType(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteCustomerType(id);
        return ResponseEntity.noContent().build();
    }
}