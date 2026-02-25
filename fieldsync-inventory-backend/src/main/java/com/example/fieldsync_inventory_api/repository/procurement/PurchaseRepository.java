package com.example.fieldsync_inventory_api.repository.procurement;

import com.example.fieldsync_inventory_api.entity.procurement.PurchaseEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {

    @EntityGraph(attributePaths = {"stockTransaction", "supplier"})
    @Query("SELECT r FROM PurchaseEntity r WHERE r.deletedAt IS NULL AND r.stockTransactionId = ?1")
    Optional<PurchaseEntity> findActiveById(Long id);

    @Query("SELECT r FROM PurchaseEntity r WHERE r.deletedAt IS NULL")
    List<PurchaseEntity> findAllActive();

    @Query("SELECT r FROM PurchaseEntity r WHERE r.stockTransaction.id = ?1 AND r.deletedAt IS NULL")
    Optional<PurchaseEntity> findActiveByStockTransactionId(Long stockTransactionId);

    @Query("SELECT r FROM PurchaseEntity r WHERE r.deletedAt IS NOT NULL")
    List<PurchaseEntity> findAllDeleted();

    @Query("SELECT r FROM PurchaseEntity r WHERE r.stockTransactionId = ?1 AND r.deletedAt IS NOT NULL")
    Optional<PurchaseEntity> findDeletedById(Long id);

    @Query("SELECT r FROM PurchaseEntity r")
    List<PurchaseEntity> findAllWithDeleted();
}
