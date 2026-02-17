package com.example.fieldsync_inventory_backend.entity.stock;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_backend.entity.Auditable;
import com.example.fieldsync_inventory_backend.entity.inventory.SeedBatchEntity;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "stock_movement")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE stock_movement SET deleted_at = NOW() WHERE id=?")
public class StockMovementEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with StockMovementType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movement_type_id", nullable = false)
    private StockMovementTypeEntity movementType;

    // Many-to-one relationship with SeedBatch
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private SeedBatchEntity seedBatch;

    // Many-to-one relationship with StockTransaction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private StockTransactionEntity stockTransaction;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
