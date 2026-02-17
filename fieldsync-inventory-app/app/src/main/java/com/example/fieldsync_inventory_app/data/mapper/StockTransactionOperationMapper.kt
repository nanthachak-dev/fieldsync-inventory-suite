package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response.TOCustomerResDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response.TOSaleItemResDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response.TOSaleResDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response.TOStockMovementResDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.response.StockTransactionOperationResDto
import com.example.fieldsync_inventory_app.domain.model.transaction_operation.IdNamePair
import com.example.fieldsync_inventory_app.domain.model.transaction_operation.TOCustomer
import com.example.fieldsync_inventory_app.domain.model.transaction_operation.TOSale
import com.example.fieldsync_inventory_app.domain.model.transaction_operation.TOSaleItem
import com.example.fieldsync_inventory_app.domain.model.transaction_operation.TOStockMovement
import com.example.fieldsync_inventory_app.domain.model.transaction_operation.TOUser
import com.example.fieldsync_inventory_app.domain.model.transaction_operation.TransactionOperation


// DTO to Model
fun StockTransactionOperationResDto.toDomain(): TransactionOperation {
    return TransactionOperation(
        id = transactionId,
        type = IdNamePair(transactionType.id, transactionType.name),
        performedBy = TOUser(performedBy.id, performedBy.username),
        stockMovements = stockMovements.map { it.toDomain() },
        sale = sale?.toDomain(),
        date = transactionDate,
        description = description,
    )
}

// DTO to Model for StockMovement
fun TOStockMovementResDto.toDomain(): TOStockMovement {
    return TOStockMovement(
        id = id,
        movementType = IdNamePair(movementType.id, movementType.name),
        seedBatchId = seedBatch.id,
        saleItem = saleItem?.toDomain(),
        quantity = quantity,
        description = description
    )
}

// DTO to Model for SaleItem
fun TOSaleItemResDto.toDomain(): TOSaleItem{
    return TOSaleItem(
        price = price,
        description = description
    )
}

// DTO to Model for Sale
fun TOSaleResDto.toDomain(): TOSale{
    return TOSale(
        customer = customer?.toDomain(),
        totalSale = totalSale
    )
}

// DTO to Model for Customer
fun TOCustomerResDto.toDomain(): TOCustomer{
    return TOCustomer(
        id = id,
        name = fullName
    )
}

// NOTE: There's no mapper for model to TransactionOperationReqDto because most data got from user inputs