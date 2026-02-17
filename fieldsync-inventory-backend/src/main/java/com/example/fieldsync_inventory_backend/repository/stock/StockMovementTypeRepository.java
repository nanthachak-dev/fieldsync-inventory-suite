package com.example.fieldsync_inventory_backend.repository.stock;

import com.example.fieldsync_inventory_backend.entity.stock.StockMovementTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockMovementTypeRepository extends JpaRepository<StockMovementTypeEntity, Integer> {
    Optional<StockMovementTypeEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
