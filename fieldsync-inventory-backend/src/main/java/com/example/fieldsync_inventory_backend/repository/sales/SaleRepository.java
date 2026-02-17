package com.example.fieldsync_inventory_backend.repository.sales;

import com.example.fieldsync_inventory_backend.entity.sales.SaleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
    // --- For Active Records ---

    @EntityGraph(attributePaths = {"stockTransaction", "customer"})
    @Query("SELECT r FROM SaleEntity r WHERE r.deletedAt IS NULL AND r.id = ?1")
    Optional<SaleEntity> findActiveById(Long id);

    @Query("SELECT r FROM SaleEntity r WHERE r.deletedAt IS NULL")
    List<SaleEntity> findAllActive();

    @Query("SELECT r FROM SaleEntity r WHERE r.stockTransaction.id = ?1 AND r.deletedAt IS NULL")
    Optional<SaleEntity> findActiveByStockTransactionId(Long stockTransactionId);

    // --- For Soft-Deleted Records ---

    @Query("SELECT r FROM SaleEntity r WHERE r.deletedAt IS NOT NULL")
    List<SaleEntity> findAllDeleted();

    @Query("SELECT r FROM SaleEntity r WHERE r.id = ?1 AND r.deletedAt IS NOT NULL")
    Optional<SaleEntity> findDeletedById(Long id);

    // --- For ALL Records (Active and Deleted) ---

    @Query("SELECT r FROM SaleEntity r")
    List<SaleEntity> findAllWithDeleted();
}