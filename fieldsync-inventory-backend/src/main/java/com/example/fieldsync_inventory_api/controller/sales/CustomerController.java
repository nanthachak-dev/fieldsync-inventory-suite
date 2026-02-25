package com.example.fieldsync_inventory_api.controller.sales;

import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_api.dto.sales.customer.CustomerRequestDTO;
import com.example.fieldsync_inventory_api.dto.sales.customer.CustomerResponseDTO;
import com.example.fieldsync_inventory_api.service.sales.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@RequestBody CustomerRequestDTO dto) {
        return ResponseEntity.ok(service.createCustomer(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getCustomerById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {

        return ResponseEntity.ok(service.getAllCustomers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable Integer id,
                                                      @RequestBody CustomerRequestDTO dto) {
        return ResponseEntity.ok(service.updateCustomer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}