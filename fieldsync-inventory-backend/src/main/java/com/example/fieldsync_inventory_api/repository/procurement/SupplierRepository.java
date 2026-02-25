package com.example.fieldsync_inventory_api.repository.procurement;

import com.example.fieldsync_inventory_api.entity.procurement.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Integer> {
}
