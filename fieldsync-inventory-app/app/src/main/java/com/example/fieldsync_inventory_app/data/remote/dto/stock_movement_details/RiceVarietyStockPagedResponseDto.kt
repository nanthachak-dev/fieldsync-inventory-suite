package com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details

data class RiceVarietyStockPagedResponseDto(
    val content: List<RiceVarietyStockResponseDto>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)
