package com.example.fieldsync_inventory_api.repository.stock;

import com.example.fieldsync_inventory_api.entity.stock.StockTransactionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransactionEntity, Long> {
// --- For Active Records ---

    @EntityGraph(attributePaths = {"transactionType", "performedBy"})
    @Query("SELECT r FROM StockTransactionEntity r WHERE r.deletedAt IS NULL AND r.id = ?1")
    Optional<StockTransactionEntity> findActiveById(Long id);

    @Query("SELECT r FROM StockTransactionEntity r WHERE r.deletedAt IS NULL")
    List<StockTransactionEntity> findAllActive();

    // --- For Soft-Deleted Records ---

    @Query("SELECT r FROM StockTransactionEntity r WHERE r.deletedAt IS NOT NULL")
    List<StockTransactionEntity> findAllDeleted();

    @Query("SELECT r FROM StockTransactionEntity r WHERE r.id = ?1 AND r.deletedAt IS NOT NULL")
    Optional<StockTransactionEntity> findDeletedById(Long id);

    // --- For ALL Records (Active and Deleted) ---

    @Query("SELECT r FROM StockTransactionEntity r")
    List<StockTransactionEntity> findAllWithDeleted();
}
