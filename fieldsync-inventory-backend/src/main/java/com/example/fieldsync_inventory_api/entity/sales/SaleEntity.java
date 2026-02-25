package com.example.fieldsync_inventory_api.entity.sales;

import jakarta.persistence.*;
import lombok.*;
import com.example.fieldsync_inventory_api.entity.Auditable;
import com.example.fieldsync_inventory_api.entity.stock.StockTransactionEntity;
import org.hibernate.annotations.SQLDelete;

import java.time.Instant;

@Entity
@Table(name = "sale")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE sale SET deleted_at = NOW() WHERE stock_transaction_id=?")
public class SaleEntity extends Auditable {
    // The primary key is the same as the StockTransaction's primary key
    @Id
    @Column(name = "stock_transaction_id")
    private Long stockTransactionId;

    // This creates a one-to-one relationship with StockTransaction
    // and maps the primary key of this entity to the primary key of the StockTransaction.
    @OneToOne
    @MapsId
    @JoinColumn(name = "stock_transaction_id")
    private StockTransactionEntity stockTransaction;

    // Many-to-one relationship with Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}