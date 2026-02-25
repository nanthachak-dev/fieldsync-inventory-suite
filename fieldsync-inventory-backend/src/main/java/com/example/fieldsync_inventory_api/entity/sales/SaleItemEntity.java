package com.example.fieldsync_inventory_api.entity.sales;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

import lombok.*;
import com.example.fieldsync_inventory_api.entity.Auditable;
import com.example.fieldsync_inventory_api.entity.stock.StockMovementEntity;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "sale_item")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE sale_item SET deleted_at = NOW() WHERE stock_movement_id=?") // Query for soft-delete
public class SaleItemEntity extends Auditable {
    // The primary key is the same as the StockMovement's primary key
    @Id
    @Column(name = "stock_movement_id")
    private Long stockMovementId;

    // This creates a one-to-one relationship with StockMovement
    // and maps the primary key of this entity to the primary key of the StockMovement.
    @OneToOne
    @MapsId
    @JoinColumn(name = "stock_movement_id")
    private StockMovementEntity stockMovement;

    // Many-to-one relationship with Sale
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private SaleEntity sale;

    @Column(precision = 19, scale = 4, nullable = false) // for standard financial
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}