package com.example.fieldsync_inventory_backend.repository.procurement;

import com.example.fieldsync_inventory_backend.entity.procurement.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Integer> {
}
