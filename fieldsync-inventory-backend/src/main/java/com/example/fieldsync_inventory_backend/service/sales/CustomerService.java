package com.example.fieldsync_inventory_backend.service.sales;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import com.example.fieldsync_inventory_backend.dto.sales.customer.CustomerRequestDTO;
import com.example.fieldsync_inventory_backend.dto.sales.customer.CustomerResponseDTO;
import com.example.fieldsync_inventory_backend.entity.sales.CustomerEntity;
import com.example.fieldsync_inventory_backend.entity.sales.CustomerTypeEntity;
import com.example.fieldsync_inventory_backend.exception.ResourceNotFoundException;
import com.example.fieldsync_inventory_backend.mapper.CustomerMapper;
import com.example.fieldsync_inventory_backend.repository.sales.CustomerRepository;
import com.example.fieldsync_inventory_backend.repository.sales.CustomerTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerTypeRepository customerTypeRepository;

    private final CustomerMapper customerMapper;
    @PersistenceContext
    private EntityManager entityManager;

    // Helper method to map DTO to Entity
    private CustomerEntity mapToEntity(CustomerRequestDTO dto) {
        CustomerTypeEntity customerType = customerTypeRepository.findById(dto.getCustomerTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer type not found with id: " +
                        dto.getCustomerTypeId()));

        return CustomerEntity.builder()
                .customerType(customerType)
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        CustomerEntity validatedEntity = mapToEntity(dto);

        // Save new entity
        CustomerEntity savedEntity = repository.save(validatedEntity);

        // Retrieve the fully populated entity (if your save doesn't return it)
        CustomerEntity fullyPopulatedEntity = repository.findById(savedEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve newly created record"));

        return customerMapper.toResponseDTO(fullyPopulatedEntity);
    }

    public CustomerResponseDTO getCustomerById(Integer id) {
        CustomerEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return customerMapper.toResponseDTO(entity);
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return repository.findAll()
                .stream()
                .map(customerMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CustomerResponseDTO updateCustomer(Integer id, CustomerRequestDTO dto) {
        CustomerEntity existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        CustomerEntity validatedEntity = mapToEntity(dto);

        existing.setCustomerType(validatedEntity.getCustomerType());
        existing.setFullName(validatedEntity.getFullName());
        existing.setEmail(validatedEntity.getEmail());
        existing.setPhone(validatedEntity.getPhone());
        existing.setAddress(validatedEntity.getAddress());

        // Save the updated entity
        CustomerEntity savedEntity = repository.save(existing);

        // !!! CRITICAL STEP !!!
        // Clear the persistence context to force a fresh database read
        entityManager.clear();

        // return stockMovementMapper.toResponseDTO(savedEntity);
        // Retrieve the fully populated entity (if your save doesn't return it)
        CustomerEntity fullyPopulatedEntity = repository.findById(savedEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to retrieve updated record"));

        return customerMapper.toResponseDTO(fullyPopulatedEntity);
    }

    public void deleteCustomer(Integer id) {
        repository.deleteById(id);
    }
}