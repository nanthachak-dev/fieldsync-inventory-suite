package com.example.fieldsync_inventory_api.dto.stock.movement_details;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementDetailsResponseDTO {

    // Composite ID
    private Long transactionId;
    private Long stockMovementId;

    // Transaction Details
    private Integer transactionTypeId;
    private String transactionTypeName;
    private Integer userId;
    private String username;
    private Instant transactionDate;
    private String transactionDescription;

    // Stock Movement Details
    private Integer movementTypeId;
    private String movementTypeName;
    private String movementTypeEffectOnStock;
    private String movementTypeDescription;
    private BigDecimal stockMovementQuantity;
    private String stockMovementDescription;

    // Seed Batch Details
    private Long seedBatchId;
    private Integer seedBatchYear;
    private Boolean seedBatchGrading;
    private Boolean seedBatchGermination;
    private String seedBatchDescription;

    // Rice Variety Details
    private Integer riceVarietyId;
    private String riceVarietyName;
    private String riceVarietyDescription;
    private String riceVarietyImageUrl;

    // Generation Details
    private Integer generationId;
    private String generationName;
    private String generationDescription;

    // Season Details
    private Integer seasonId;
    private String seasonName;
    private String seasonDescription;

    // Sale & Customer Details (can be null)
    private Long saleId;
    private Integer customerId;
    private String customerFullName;
    private String saleDescription;

    // Sale Item Details (can be null)
    private BigDecimal saleItemPrice;
    private String saleItemDescription;

    // Purchase & Supplier Details (can be null)
    private Long purchaseId;
    private String purchaseDescription;
    private Integer supplierId;
    private String supplierFullName;

    // Purchase Item Details (can be null)
    private BigDecimal purchaseItemPrice;
    private String purchaseItemDescription;

    // Audit/Timestamps
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
