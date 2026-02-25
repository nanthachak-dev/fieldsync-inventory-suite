package com.example.fieldsync_inventory_api.repository.stock;

import com.example.fieldsync_inventory_api.entity.view.TransactionSummaryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;

@Repository
public interface TransactionSummaryRepository extends JpaRepository<TransactionSummaryEntity, Long> {

    Page<TransactionSummaryEntity> findAll(Pageable pageable);

    Page<TransactionSummaryEntity> findAllByTransactionDateBetween(Instant startDate, Instant endDate,
            Pageable pageable);

    long countByTransactionDateBetween(Instant startDate, Instant endDate);

    @Query("SELECT SUM(t.totalSalePrice) FROM TransactionSummaryEntity t " +
            "WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "AND t.mainMovementType = :movementType")
    BigDecimal sumTotalSalePriceByDateRangeAndMovementType(
            Instant startDate, Instant endDate, String movementType);
}
