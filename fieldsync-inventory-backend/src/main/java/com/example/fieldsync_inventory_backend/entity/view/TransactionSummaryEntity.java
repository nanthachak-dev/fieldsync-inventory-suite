package com.example.fieldsync_inventory_backend.entity.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Immutable
@Subselect("SELECT * FROM transaction_summary_view")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionSummaryEntity {

    @Id
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "transaction_date")
    private Instant transactionDate;

    @Column(name = "transaction_type_id")
    private Integer transactionTypeId;

    @Column(name = "transaction_type_name")
    private String transactionTypeName;

    @Column(name = "transaction_description")
    private String transactionDescription;

    @Column(name = "username")
    private String username;

    @Column(name = "main_movement_type")
    private String mainMovementType;

    @Column(name = "item_count")
    private Long itemCount;

    @Column(name = "total_quantity")
    private BigDecimal totalQuantity;

    @Column(name = "total_sale_price")
    private BigDecimal totalSalePrice;

    @Column(name = "total_purchase_price")
    private BigDecimal totalPurchasePrice;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "from_batch_id")
    private Long fromBatchId;

    @Column(name = "to_batch_id")
    private Long toBatchId;
}
