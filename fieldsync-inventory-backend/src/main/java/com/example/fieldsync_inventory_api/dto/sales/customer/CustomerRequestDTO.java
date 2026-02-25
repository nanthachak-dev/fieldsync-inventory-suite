package com.example.fieldsync_inventory_api.dto.sales.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDTO {
    private Integer customerTypeId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
}