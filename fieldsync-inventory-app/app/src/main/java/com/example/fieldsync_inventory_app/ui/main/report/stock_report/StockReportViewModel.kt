package com.example.fieldsync_inventory_app.ui.main.report.stock_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.GetLocalStockMovementDetailsUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.model.StockReportSeedBatch
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.state.StockReportUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

import android.content.Context
import android.net.Uri
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.util.StockReportExcelExporter
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@HiltViewModel
class StockReportViewModel @Inject constructor(
    private val getLocalStockTransactionDetailsUseCase: GetLocalStockMovementDetailsUseCase,
    private val stockReportExcelExporter: StockReportExcelExporter
) : ViewModel(), IStockReportViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState.asStateFlow()

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch = _isFirstLaunch.asStateFlow()

    private val _stockReportUiState = MutableStateFlow(StockReportUiState())
    override val stockReportUiState: StateFlow<StockReportUiState> =
        _stockReportUiState.asStateFlow()

    override fun firstLaunch() {
        loadStockReportData()
        _isFirstLaunch.update { false }
    }

    override fun onLastDateChanged(date: String?) {
        _stockReportUiState.update { it.copy(selectedLastDate = date) }
        loadStockReportData()
    }

    private var loadDataJob: Job? = null

    private fun loadStockReportData() {
        loadDataJob?.cancel()
        loadDataJob = getLocalStockTransactionDetailsUseCase()
            .onEach { transactions ->
                _resourceUiState.update { it.copy(isLoading = true) }

                val lastDateStr = _stockReportUiState.value.selectedLastDate
                val lastDateMillis = if (lastDateStr != null) {
                    try {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date = sdf.parse(lastDateStr)
                        // Add 23 hours, 59 minutes, 59 seconds to include the whole day
                        date?.let { it.time + 86399999L } ?: Long.MAX_VALUE
                    } catch (e: Exception) {
                        Long.MAX_VALUE
                    }
                } else {
                    Long.MAX_VALUE
                }

                val filteredTransactions = transactions.filter { it.transactionDate <= lastDateMillis }

                // 1. Calculate stock for each physical batch (seedBatchId)
                val batchStocks = filteredTransactions
                    .groupBy { it.seedBatchId }
                    .map { (batchId, batchTransactions) ->
                        val stock = batchTransactions.sumOf { detail ->
                            if (detail.movementTypeEffectOnStock == "IN")
                                detail.stockMovementQuantity
                            else
                                -detail.stockMovementQuantity
                        }
                        // We need a representative transaction to get batch metadata
                        val rep = batchTransactions.first()

                        BatchStockData(
                            varietyId = rep.riceVarietyId,
                            varietyName = rep.riceVarietyName,
                            year = rep.seedBatchYear,
                            seasonId = rep.seasonId,
                            seasonName = rep.seasonName,
                            seasonDescription = rep.seasonDescription ?: "",
                            generationId = rep.generationId,
                            generationName = rep.generationName,
                            grading = rep.seedBatchGrading,
                            germination = rep.seedBatchGermination,
                            stock = stock
                        )
                    }

                // 2. Group by Variety, Year, Season, Generation, Germination
                val reportBatches = batchStocks
                    .groupBy {
                        ReportGroupKey(
                            it.varietyId,
                            it.year,
                            it.seasonId,
                            it.generationId,
                            it.germination
                        )
                    }
                    .map { (key, batches) ->
                        val varietyName = batches.first().varietyName
                        val seasonName = batches.first().seasonName
                        val seasonDescription = batches.first().seasonDescription
                        val generationName = batches.first().generationName

                        val gradedStock = batches.filter { it.grading }.sumOf { it.stock }
                        val ungradedStock = batches.filter { !it.grading }.sumOf { it.stock }

                        StockReportSeedBatch(
                            varietyId = key.varietyId,
                            varietyName = varietyName,
                            year = key.year,
                            seasonId = key.seasonId,
                            seasonName = seasonName,
                            seasonDescription = seasonDescription,
                            generationId = key.generationId,
                            generationName = generationName,
                            graded = gradedStock,
                            ungraded = ungradedStock,
                            germination = key.germination
                        )
                    }
                    .sortedWith(
                        compareBy(
                            { it.varietyName },
                            { -it.year }, // Descending year
                            { it.seasonId },
                            { it.generationId }
                        )
                    )

                _stockReportUiState.update { it.copy(seedBatchList = reportBatches) }
                _resourceUiState.update { it.copy(isLoading = false) }
            }
            .catch { exception ->
                _resourceUiState.update { it.copy(isLoading = false, error = exception.message) }
            }
            .launchIn(viewModelScope)
    }

    override fun exportReport(context: Context, uri: Uri, data: List<StockReportSeedBatch>) {
        viewModelScope.launch {
            _resourceUiState.update { it.copy(isLoading = true) }
            try {
                stockReportExcelExporter.exportStockReport(context, uri, data)
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading(e.message)
            }
        }
    }

    // Helper data classes for internal processing
    private data class BatchStockData(
        val varietyId: Int,
        val varietyName: String,
        val year: Int,
        val seasonId: Int,
        val seasonName: String,
        val seasonDescription: String,
        val generationId: Int,
        val generationName: String,
        val grading: Boolean,
        val germination: Boolean,
        val stock: Double
    )

    private data class ReportGroupKey(
        val varietyId: Int,
        val year: Int,
        val seasonId: Int,
        val generationId: Int,
        val germination: Boolean
    )
}