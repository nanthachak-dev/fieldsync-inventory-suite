package com.example.fieldsync_inventory_api.entity.procurement;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_api.entity.Auditable;
import com.example.fieldsync_inventory_api.entity.stock.StockMovementEntity;
import org.hibernate.annotations.SQLDelete;

import java.time.Instant;
import java.math.BigDecimal;

@Entity
@Table(name = "purchase_item")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE purchase_item SET deleted_at = NOW() WHERE stock_movement_id=?")
public class PurchaseItemEntity extends Auditable {
    @Id
    @Column(name = "stock_movement_id")
    private Long stockMovementId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "stock_movement_id")
    private StockMovementEntity stockMovement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_id", nullable = false)
    private PurchaseEntity purchase;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
