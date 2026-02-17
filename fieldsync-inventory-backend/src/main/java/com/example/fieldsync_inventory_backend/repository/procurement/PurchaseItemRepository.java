package com.example.fieldsync_inventory_backend.repository.procurement;

import com.example.fieldsync_inventory_backend.entity.procurement.PurchaseItemEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItemEntity, Long> {
    @EntityGraph(attributePaths = {"stockMovement", "purchase"})
    @Query("SELECT r FROM PurchaseItemEntity r WHERE r.deletedAt IS NULL AND r.stockMovementId = ?1")
    Optional<PurchaseItemEntity> findActiveById(Long id);

    @EntityGraph(attributePaths = {"stockMovement", "purchase"})
    @Query("SELECT r FROM PurchaseItemEntity r WHERE r.deletedAt IS NULL")
    List<PurchaseItemEntity> findAllActive();

    @Query("SELECT r FROM PurchaseItemEntity r WHERE r.purchase.stockTransactionId = ?1")
    List<PurchaseItemEntity> findByPurchaseId(Long purchaseId);

    @Query("SELECT r FROM PurchaseItemEntity r WHERE r.stockMovement.id = ?1 AND r.deletedAt IS NULL")
    Optional<PurchaseItemEntity> findActiveByStockMovementId(Long stockMovementId);

    @Query("SELECT r FROM PurchaseItemEntity r WHERE r.deletedAt IS NOT NULL")
    List<PurchaseItemEntity> findAllDeleted();

    @Query("SELECT r FROM PurchaseItemEntity r WHERE r.stockMovementId = ?1 AND r.deletedAt IS NOT NULL")
    Optional<PurchaseItemEntity> findDeletedById(Long id);

    @Query("SELECT r FROM PurchaseItemEntity r")
    List<PurchaseItemEntity> findAllWithDeleted();
}
