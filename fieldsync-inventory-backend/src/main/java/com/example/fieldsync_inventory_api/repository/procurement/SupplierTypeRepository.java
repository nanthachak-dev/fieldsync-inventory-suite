package com.example.fieldsync_inventory_api.repository.procurement;

import com.example.fieldsync_inventory_api.entity.procurement.SupplierTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierTypeRepository extends JpaRepository<SupplierTypeEntity, Integer> {
}