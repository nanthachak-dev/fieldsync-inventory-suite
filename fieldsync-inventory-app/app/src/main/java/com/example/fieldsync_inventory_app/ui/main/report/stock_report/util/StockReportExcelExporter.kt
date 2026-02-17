package com.example.fieldsync_inventory_app.ui.main.report.stock_report.util

import android.content.Context
import android.net.Uri
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.model.StockReportSeedBatch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StockReportExcelExporter @Inject constructor() {
    suspend fun exportStockReport(
        context: Context,
        uri: Uri,
        data: List<StockReportSeedBatch>
    ) {
        withContext(Dispatchers.IO) {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                val workbook = XSSFWorkbook()
                val sheet = workbook.createSheet("Stock Report")

                // Create Header Row
                val headerRow = sheet.createRow(0)
                // Updated headers: Added "Year" and "Season"
                val headers = listOf("No", "Variety Name", "Year",
                    "Season", "Generation", "Graded", "Ungraded", "Total (Graded+Ungraded)")
                headers.forEachIndexed { index, title ->
                    headerRow.createCell(index).setCellValue(title)
                }

                // Fill Data
                var totalGraded = 0.0
                var totalUngraded = 0.0
                var grandTotal = 0.0

                data.forEachIndexed { index, item ->
                    val row = sheet.createRow(index + 1)
                    val total = item.graded + item.ungraded
                    
                    totalGraded += item.graded
                    totalUngraded += item.ungraded
                    grandTotal += total

                    row.createCell(0).setCellValue((index + 1).toDouble())
                    row.createCell(1).setCellValue(item.varietyName)
                    row.createCell(2).setCellValue(item.year.toDouble())
                    row.createCell(3).setCellValue(item.seasonName)
                    row.createCell(4).setCellValue(item.generationName)
                    row.createCell(5).setCellValue(item.graded)
                    row.createCell(6).setCellValue(item.ungraded)
                    row.createCell(7).setCellValue(total)
                }

                // Create Total Row
                val totalRow = sheet.createRow(data.size + 1)
                totalRow.createCell(0).setCellValue("Total")
                
                // Adjusted indices for total values
                totalRow.createCell(5).setCellValue(totalGraded)
                totalRow.createCell(6).setCellValue(totalUngraded)
                totalRow.createCell(7).setCellValue(grandTotal)

                // Write to stream
                workbook.write(outputStream)
                workbook.close()
            }
        }
    }
}
