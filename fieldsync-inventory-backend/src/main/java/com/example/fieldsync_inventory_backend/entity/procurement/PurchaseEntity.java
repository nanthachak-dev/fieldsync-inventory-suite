package com.example.fieldsync_inventory_backend.entity.procurement;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_backend.entity.Auditable;
import com.example.fieldsync_inventory_backend.entity.stock.StockTransactionEntity;
import org.hibernate.annotations.SQLDelete;

import java.time.Instant;

@Entity
@Table(name = "purchase")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE purchase SET deleted_at = NOW() WHERE stock_transaction_id=?")
public class PurchaseEntity extends Auditable {
    @Id
    @Column(name = "stock_transaction_id")
    private Long stockTransactionId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "stock_transaction_id")
    private StockTransactionEntity stockTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
