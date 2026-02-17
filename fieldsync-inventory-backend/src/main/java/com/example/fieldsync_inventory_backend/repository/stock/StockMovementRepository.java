package com.example.fieldsync_inventory_backend.repository.stock;

import com.example.fieldsync_inventory_backend.entity.stock.StockMovementEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovementEntity, Long> {
    // --- For Active Records ---

    Optional<ArrayList<StockMovementEntity>> findByStockTransactionId(Long transactionId);

    @EntityGraph(attributePaths = {"movementType", "seedBatch", "stockTransaction"})
    @Query("SELECT r FROM StockMovementEntity r WHERE r.deletedAt IS NULL AND r.id = ?1")
    Optional<StockMovementEntity> findActiveById(Long id);

    @EntityGraph(attributePaths = {"movementType", "seedBatch", "stockTransaction"})
    @Query("SELECT r FROM StockMovementEntity r WHERE r.deletedAt IS NULL")
    List<StockMovementEntity> findAllActive();

    @Query("SELECT r FROM StockMovementEntity r WHERE r.stockTransaction.id = ?1 AND r.deletedAt IS NULL")
    List<StockMovementEntity> findActiveByStockTransactionId(Long stockTransactionId);

    // --- For Soft-Deleted Records ---

    @Query("SELECT r FROM StockMovementEntity r WHERE r.deletedAt IS NOT NULL")
    List<StockMovementEntity> findAllDeleted();

    @Query("SELECT r FROM StockMovementEntity r WHERE r.id = ?1 AND r.deletedAt IS NOT NULL")
    Optional<StockMovementEntity> findDeletedById(Long id);

    // --- For ALL Records (Active and Deleted) ---

    @Query("SELECT r FROM StockMovementEntity r")
    List<StockMovementEntity> findAllWithDeleted();
}
