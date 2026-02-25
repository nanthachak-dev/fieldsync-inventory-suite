package com.example.fieldsync_inventory_api.entity.view;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import lombok.*;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@IdClass(StockMovementDetailsId.class)
@Subselect("SELECT * FROM stock_movement_details_view") // Use @Subselect instead of @Table because this class represents view
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementDetailsEntity {

    // The fields for the composite primary key
    @Id
    @Column(name = "transaction_id")
    private Long transactionId;

    @Id
    @Column(name = "stock_movement_id")
    private Long stockMovementId;

    // Transaction Details
    @Column(name = "transaction_type_id")
    private Integer transactionTypeId;

    @Column(name = "transaction_type_name")
    private String transactionTypeName;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username")
    private String username;

    @Column(name = "transaction_date")
    private Instant transactionDate;

    @Column(name = "transaction_description")
    private String transactionDescription;

    @Column(name = "created_at")
    private Instant createdAt; // A date and time without any time zone information.

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    // Stock Movement Details
    @Column(name = "movement_type_id")
    private Integer movementTypeId;

    @Column(name = "movement_type_name")
    private String movementTypeName;

    @Column(name = "movement_type_effect_on_stock")
    private String movementTypeEffectOnStock;

    @Column(name = "movement_type_description")
    private String movementTypeDescription;

    @Column(name = "stock_movement_quantity")
    private BigDecimal stockMovementQuantity;

    @Column(name = "stock_movement_description")
    private String stockMovementDescription;

    // Seed Batch Details
    @Column(name = "seed_batch_id")
    private Long seedBatchId;

    @Column(name = "seed_batch_year")
    private Integer seedBatchYear;

    @Column(name = "seed_batch_grading")
    private Boolean seedBatchGrading;

    @Column(name = "seed_batch_germination")
    private Boolean seedBatchGermination;

    @Column(name = "seed_batch_description")
    private String seedBatchDescription;

    // Rice Variety Details
    @Column(name = "rice_variety_id")
    private Integer riceVarietyId;

    @Column(name = "rice_variety_name")
    private String riceVarietyName;

    @Column(name = "rice_variety_description")
    private String riceVarietyDescription;

    @Column(name = "rice_variety_image_url")
    private String riceVarietyImageUrl;

    // Generation Details
    @Column(name = "generation_id")
    private Integer generationId;

    @Column(name = "generation_name")
    private String generationName;

    @Column(name = "generation_description")
    private String generationDescription;

    // Season Details
    @Column(name = "season_id")
    private Integer seasonId;

    @Column(name = "season_name")
    private String seasonName;

    @Column(name = "season_description")
    private String seasonDescription;

    // Sale & Customer Details (can be null)
    @Column(name = "sale_id")
    private Long saleId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_full_name")
    private String customerFullName;

    @Column(name = "sale_description")
    private String saleDescription;

    // Sale Item Details (can be null)
    @Column(name = "sale_item_price")
    private BigDecimal saleItemPrice;

    @Column(name = "sale_item_description")
    private String saleItemDescription;

    // Purchase & Supplier Details (can be null)
    @Column(name = "purchase_id")
    private Long purchaseId;

    @Column(name = "purchase_description")
    private String purchaseDescription;

    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "supplier_full_name")
    private String supplierFullName;

    // Purchase Item Details (can be null)
    @Column(name = "purchase_item_price")
    private BigDecimal purchaseItemPrice;

    @Column(name = "purchase_item_description")
    private String purchaseItemDescription;
}