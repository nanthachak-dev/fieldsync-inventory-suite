package com.example.fieldsync_inventory_backend.mapper;

import com.example.fieldsync_inventory_backend.dto.stock.transaction_summary.TransactionSummaryResponseDTO;
import com.example.fieldsync_inventory_backend.entity.view.TransactionSummaryEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionSummaryMapper {

    public TransactionSummaryResponseDTO toResponseDTO(TransactionSummaryEntity entity) {
        if (entity == null) {
            return null;
        }

        return TransactionSummaryResponseDTO.builder()
                .transactionId(entity.getTransactionId())
                .transactionDate(entity.getTransactionDate())
                .transactionTypeId(entity.getTransactionTypeId())
                .transactionTypeName(entity.getTransactionTypeName())
                .transactionDescription(entity.getTransactionDescription())
                .username(entity.getUsername())
                .mainMovementType(entity.getMainMovementType())
                .itemCount(entity.getItemCount())
                .totalQuantity(entity.getTotalQuantity())
                .totalSalePrice(entity.getTotalSalePrice())
                .totalPurchasePrice(entity.getTotalPurchasePrice())
                .customerName(entity.getCustomerName())
                .supplierName(entity.getSupplierName())
                .fromBatchId(entity.getFromBatchId())
                .toBatchId(entity.getToBatchId())
                .build();
    }
}
