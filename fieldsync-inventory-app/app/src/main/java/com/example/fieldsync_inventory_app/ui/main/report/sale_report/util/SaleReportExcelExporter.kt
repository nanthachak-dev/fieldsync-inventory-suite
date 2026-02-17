package com.example.fieldsync_inventory_app.ui.main.report.sale_report.util

import android.content.Context
import android.net.Uri
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.model.SaleReportSeedBatch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Row
import javax.inject.Inject

class SaleReportExcelExporter @Inject constructor() {
    suspend fun exportSaleReport(
        context: Context,
        uri: Uri,
        data: List<SaleReportSeedBatch>
    ) {
        withContext(Dispatchers.IO) {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                val workbook = XSSFWorkbook()
                val sheet = workbook.createSheet("Sale Report")

                // Create Header Row
                val headerRow = sheet.createRow(0)
                val headers = listOf(
                    "No", 
                    "Variety Name", 
                    "Year",
                    "Season", 
                    "Generation", 
                    "Good Seed Sale (Kg)", 
                    "Good Seed Sale (LAK)", 
                    "Bad Seed Sale (Kg)", 
                    "Bad Seed Sale (LAK)", 
                    "Total (Kg)", 
                    "Total (LAK)"
                )
                headers.forEachIndexed { index, title ->
                    headerRow.createCell(index).setCellValue(title)
                }

                // Fill Data
                var totalGoodKg = 0.0
                var totalGoodLak = 0.0
                var totalBadKg = 0.0
                var totalBadLak = 0.0
                var totalKg = 0.0
                var totalLak = 0.0

                // Helper function to set double value or leave empty if zero
                fun Row.createCellIfNotNull(index: Int, value: Double) {
                    if (value > 0) {
                        this.createCell(index).setCellValue(value)
                    }
                }

                data.forEachIndexed { index, item ->
                    val row = sheet.createRow(index + 1)
                    
                    totalGoodKg += item.germinated
                    totalGoodLak += item.germinatedSale
                    totalBadKg += item.ungerminated
                    totalBadLak += item.ungerminatedSale
                    totalKg += item.totalGermination
                    totalLak += item.totalGerminationSale

                    row.createCell(0).setCellValue((index + 1).toDouble())
                    row.createCell(1).setCellValue(item.varietyName)
                    row.createCell(2).setCellValue(item.year.toDouble())
                    row.createCell(3).setCellValue(item.seasonName)
                    row.createCell(4).setCellValue(item.generationName)
                    
                    row.createCellIfNotNull(5, item.germinated)
                    row.createCellIfNotNull(6, item.germinatedSale)
                    row.createCellIfNotNull(7, item.ungerminated)
                    row.createCellIfNotNull(8, item.ungerminatedSale)
                    row.createCellIfNotNull(9, item.totalGermination)
                    row.createCellIfNotNull(10, item.totalGerminationSale)
                }

                // Create Total Row
                val totalRow = sheet.createRow(data.size + 1)
                totalRow.createCell(0).setCellValue("Total")
                
                totalRow.createCellIfNotNull(5, totalGoodKg)
                totalRow.createCellIfNotNull(6, totalGoodLak)
                totalRow.createCellIfNotNull(7, totalBadKg)
                totalRow.createCellIfNotNull(8, totalBadLak)
                totalRow.createCellIfNotNull(9, totalKg)
                totalRow.createCellIfNotNull(10, totalLak)

                // Write to stream
                workbook.write(outputStream)
                workbook.close()
            }
        }
    }
}
