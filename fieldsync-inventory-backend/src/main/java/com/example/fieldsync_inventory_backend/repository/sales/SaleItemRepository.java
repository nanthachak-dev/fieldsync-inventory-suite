package com.example.fieldsync_inventory_backend.repository.sales;

import com.example.fieldsync_inventory_backend.entity.sales.SaleItemEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItemEntity, Long> {
    // --- For Active Records ---

    @EntityGraph(attributePaths = {"stockMovement", "sale"})
    @Query("SELECT r FROM SaleItemEntity r WHERE r.deletedAt IS NULL AND r.id = ?1")
    Optional<SaleItemEntity> findActiveById(Long id);

    @EntityGraph(attributePaths = {"stockMovement", "sale"})
    @Query("SELECT r FROM SaleItemEntity r WHERE r.deletedAt IS NULL")
    List<SaleItemEntity> findAllActive();

    @Query("SELECT r FROM SaleItemEntity r WHERE r.sale.id = ?1")
    List<SaleItemEntity> findBySaleId(Long saleId);

    @Query("SELECT r FROM SaleItemEntity r WHERE r.stockMovement.id = ?1 AND r.deletedAt IS NULL")
    Optional<SaleItemEntity> findActiveByStockMovementId(Long stockMovementId);

    // --- For Soft-Deleted Records ---

    @Query("SELECT r FROM SaleItemEntity r WHERE r.deletedAt IS NOT NULL")
    List<SaleItemEntity> findAllDeleted();

    @Query("SELECT r FROM SaleItemEntity r WHERE r.id = ?1 AND r.deletedAt IS NOT NULL")
    Optional<SaleItemEntity> findDeletedById(Long id);

    // --- For ALL Records (Active and Deleted) ---

    @Query("SELECT r FROM SaleItemEntity r")
    List<SaleItemEntity> findAllWithDeleted();
}