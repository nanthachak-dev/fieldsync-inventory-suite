package com.example.fieldsync_inventory_app.data.mapper

import com.example.fieldsync_inventory_app.data.remote.dto.stock.StockSummaryResponseDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock.VarietySummaryItemDto
import com.example.fieldsync_inventory_app.data.remote.dto.stock.VarietySummaryResponseDto
import com.example.fieldsync_inventory_app.domain.model.stock.StockSummary
import com.example.fieldsync_inventory_app.domain.model.stock.VarietySummary
import com.example.fieldsync_inventory_app.domain.model.stock.VarietySummaryPaged

fun StockSummaryResponseDto.toDomain(): StockSummary {
    return StockSummary(
        totalStock = this.totalStock,
        totalR1Stock = this.totalR1Stock,
        totalR2Stock = this.totalR2Stock,
        totalR3Stock = this.totalR3Stock,
        totalGraded = this.totalGraded,
        totalR1Graded = this.totalR1Graded,
        totalR2Graded = this.totalR2Graded,
        totalR3Graded = this.totalR3Graded,
        totalUngraded = this.totalUngraded,
        totalR1Ungraded = this.totalR1Ungraded,
        totalR2Ungraded = this.totalR2Ungraded,
        totalR3Ungraded = this.totalR3Ungraded,
        totalGerminated = this.totalGerminated,
        totalR1Germinated = this.totalR1Germinated,
        totalR2Germinated = this.totalR2Germinated,
        totalR3Germinated = this.totalR3Germinated,
        totalUngerminated = this.totalUngerminated,
        totalR1Ungerminated = this.totalR1Ungerminated,
        totalR2Ungerminated = this.totalR2Ungerminated,
        totalR3Ungerminated = this.totalR3Ungerminated
    )
}

fun VarietySummaryResponseDto.toDomain(): VarietySummaryPaged {
    return VarietySummaryPaged(
        content = this.content.map { it.toDomain() },
        pageNumber = this.pageNumber,
        pageSize = this.pageSize,
        totalElements = this.totalElements,
        totalPages = this.totalPages,
        last = this.last
    )
}

fun VarietySummaryItemDto.toDomain(): VarietySummary {
    return VarietySummary(
        varietyId = this.varietyId,
        varietyName = this.varietyName,
        stock = this.stock,
        r1Stock = this.r1Stock,
        r2Stock = this.r2Stock,
        r3Stock = this.r3Stock,
        graded = this.graded,
        r1Graded = this.r1Graded,
        r2Graded = this.r2Graded,
        r3Graded = this.r3Graded,
        ungraded = this.ungraded,
        r1Ungraded = this.r1Ungraded,
        r2Ungraded = this.r2Ungraded,
        r3Ungraded = this.r3Ungraded,
        germinated = this.germinated,
        r1Germinated = this.r1Germinated,
        r2Germinated = this.r2Germinated,
        r3Germinated = this.r3Germinated,
        ungerminated = this.ungerminated,
        r1Ungerminated = this.r1Ungerminated,
        r2Ungerminated = this.r2Ungerminated,
        r3Ungerminated = this.r3Ungerminated
    )
}
