package com.example.fieldsync_inventory_api.dto.sales.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.fieldsync_inventory_api.dto.sales.customer_type.CustomerTypeResponseDTO;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {
    private Integer id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private CustomerTypeResponseDTO customerType;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}