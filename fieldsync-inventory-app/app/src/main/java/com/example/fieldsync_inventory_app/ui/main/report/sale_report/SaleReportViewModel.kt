package com.example.fieldsync_inventory_app.ui.main.report.sale_report

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.fieldsync_inventory_app.domain.use_case.stock_movement_details.GetLocalStockMovementDetailsUseCase
import com.example.fieldsync_inventory_app.ui.common.state.ResourceUiState
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.model.SaleReportSeedBatch
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.state.SaleReportUiState
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.util.SaleReportExcelExporter
import com.example.fieldsync_inventory_app.util.view_model.endLoading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SaleReportViewModel @Inject constructor(
    private val getLocalStockTransactionDetailsUseCase: GetLocalStockMovementDetailsUseCase,
    private val saleReportExcelExporter: SaleReportExcelExporter
): ViewModel(), ISaleReportViewModel {
    private val _resourceUiState = MutableStateFlow(ResourceUiState())
    override val resourceUiState: StateFlow<ResourceUiState> = _resourceUiState

    private val _saleReportUiState = MutableStateFlow(SaleReportUiState())
    override val saleReportUiState: StateFlow<SaleReportUiState> = _saleReportUiState

    private val _isFirstLaunch = MutableStateFlow(true)
    override val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch

    override fun onFromDateChanged(date: String?) {
        _saleReportUiState.update { it.copy(selectedFromDate = date) }
    }

    override fun onToDateChanged(date: String?) {
        _saleReportUiState.update { it.copy(selectedToDate = date) }
    }

    override fun firstLaunch() {
        loadStockReportData()
        _isFirstLaunch.update { false }
    }

    private fun loadStockReportData() {
        val transactionsFlow = getLocalStockTransactionDetailsUseCase()
        val dateFilterFlow = _saleReportUiState.map { 
            it.selectedFromDate to it.selectedToDate 
        }.distinctUntilChanged()

        combine(transactionsFlow, dateFilterFlow) { transactions, (fromDate, toDate) ->
            _resourceUiState.update { it.copy(isLoading = true) }

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            
            // Group by Variety, Year, Season, Generation
            val reportBatches = transactions
                .filter { transaction ->
                    val dateMatches = if (fromDate != null || toDate != null) {
                        val transDate = Date(transaction.transactionDate)
                        val transDateStr = sdf.format(transDate)
                        
                        val afterFrom = fromDate?.let { transDateStr >= it } ?: true
                        val beforeTo = toDate?.let { transDateStr <= it } ?: true
                        afterFrom && beforeTo
                    } else true
                    
                    dateMatches
                }
                .groupBy { 
                    SaleReportGroupKey(
                        it.riceVarietyId,
                        it.seedBatchYear,
                        it.seasonId,
                        it.generationId
                    )
                }
                .map { (key, groupTransactions) ->
                    val rep = groupTransactions.first()
                    
                    val germinated = groupTransactions
                        .filter { it.seedBatchGermination && it.saleId != null }
                        .sumOf { it.stockMovementQuantity }
                    
                    val ungerminated = groupTransactions
                        .filter { !it.seedBatchGermination && it.saleId != null }
                        .sumOf { it.stockMovementQuantity }
                        
                    val germinatedSale = groupTransactions
                        .filter { it.seedBatchGermination && it.saleId != null }
                        .sumOf { (it.saleItemPrice ?: 0.0) * it.stockMovementQuantity }
                        
                    val ungerminatedSale = groupTransactions
                        .filter { !it.seedBatchGermination && it.saleId != null }
                        .sumOf { (it.saleItemPrice ?: 0.0) * it.stockMovementQuantity }

                    SaleReportSeedBatch(
                        varietyId = key.varietyId,
                        varietyName = rep.riceVarietyName,
                        year = key.year,
                        seasonId = key.seasonId,
                        seasonName = rep.seasonName,
                        seasonDescription = rep.seasonDescription ?: "",
                        generationId = key.generationId,
                        generationName = rep.generationName,
                        germinated = germinated,
                        ungerminated = ungerminated,
                        germinatedSale = germinatedSale,
                        ungerminatedSale = ungerminatedSale,
                        transactionDate = groupTransactions.maxOf { it.transactionDate }
                    )
                }
                .sortedWith(
                    compareBy(
                        { it.varietyName },
                        { -it.year },
                        { it.seasonId },
                        { it.generationId }
                    )
                )
            reportBatches
        }
        .onEach { reportBatches ->
            _saleReportUiState.update { it.copy(seedBatchList = reportBatches) }
            _resourceUiState.update { it.copy(isLoading = false) }
        }
        .catch { exception ->
            _resourceUiState.update { it.copy(isLoading = false, error = exception.message) }
        }
        .launchIn(viewModelScope)
    }

    override fun exportReport(context: Context, uri: Uri, data: List<SaleReportSeedBatch>) {
        viewModelScope.launch {
            _resourceUiState.update { it.copy(isLoading = true) }
            try {
                saleReportExcelExporter.exportSaleReport(context, uri, data)
                _resourceUiState.endLoading()
            } catch (e: Exception) {
                _resourceUiState.endLoading(e.message)
            }
        }
    }

    private data class SaleReportGroupKey(
        val varietyId: Int,
        val year: Int,
        val seasonId: Int,
        val generationId: Int
    )
}
