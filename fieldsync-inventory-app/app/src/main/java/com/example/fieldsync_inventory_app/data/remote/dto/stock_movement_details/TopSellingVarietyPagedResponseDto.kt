package com.example.fieldsync_inventory_app.data.remote.dto.stock_movement_details

data class TopSellingVarietyPagedResponseDto(
    val content: List<TopSellingVarietyResponseDto>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)
