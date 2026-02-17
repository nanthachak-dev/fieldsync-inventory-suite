package com.example.fieldsync_inventory_backend.repository.stock;

import com.example.fieldsync_inventory_backend.entity.stock.StockTransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockTransactionTypeRepository extends JpaRepository<StockTransactionTypeEntity, Integer> {
    Optional<StockTransactionTypeEntity> findByNameIgnoreCase(String name);
}