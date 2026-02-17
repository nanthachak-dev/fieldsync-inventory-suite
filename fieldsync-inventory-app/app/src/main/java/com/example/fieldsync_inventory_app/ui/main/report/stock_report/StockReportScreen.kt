package com.example.fieldsync_inventory_app.ui.main.report.stock_report

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.DateInput
import com.example.fieldsync_inventory_app.ui.common.components.FilterIconButton
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.components.SimpleStockReportSeedBatchCard
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.components.StockReportSeedBatchFilterMenu
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.components.VarietyHeader
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StockReportScreen(
    viewModel: IStockReportViewModel = hiltViewModel<StockReportViewModel>(),
    navController: NavController? = null,
) {

    val stockReportUiState by viewModel.stockReportUiState.collectAsState()
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()

    var hideZeroStock by remember { mutableStateOf(true) }
    var filterExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showLastDatePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val filteredList = remember(stockReportUiState.seedBatchList, hideZeroStock, selectedFilter, searchQuery) {
        stockReportUiState.seedBatchList.filter { batch ->
            // 1. Zero Stock Filter
            val stockCondition = if (hideZeroStock) (batch.graded + batch.ungraded) > 0 else true
            
            // 2. Seed Type Filter
            val typeCondition = when (selectedFilter) {
                "Good Seed" -> batch.germination
                "Bad Seed" -> !batch.germination
                else -> true // "All"
            }

            // 3. Search Query Filter
            val searchCondition = if (searchQuery.isBlank()) {
                true 
            } else {
                batch.varietyName.contains(searchQuery, ignoreCase = true)
            }

            stockCondition && typeCondition && searchCondition
        }
    }

    val groupedByVariety = remember(filteredList) {
        filteredList.groupBy { it.varietyName }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ),
        onResult = { uri ->
            uri?.let {
                viewModel.exportReport(context, it, filteredList)
            }
        }
    )

    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.firstLaunch()
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = "Stock Report",
                navController = navController
            )
        },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                Modifier.fillMaxSize()
            ) {
                // -- Search --
                Row(
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Date input (Last Date)
                    val dateInputModifier:Modifier = Modifier.width(160.dp)
                    DateInput(
                        label = "Last Date",
                        selectedDate = stockReportUiState.selectedLastDate?:"Today",
                        modifier = dateInputModifier,
                        launchDialogs = { showLastDatePicker = true }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // -- Filter --
                    Text(
                        text = "Filter:",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Box {
                        FilterIconButton(onClick = { filterExpanded = true })
                        StockReportSeedBatchFilterMenu(
                            expanded = filterExpanded,
                            onDismissRequest = { filterExpanded = false },
                            onFilterChange = { filter ->
                                selectedFilter = filter
                                filterExpanded = false
                            }
                        )
                    }
                } // End filter bar
                // -- Search Variety --
                val textInputModifier = Modifier
                    .fillMaxWidth().padding(horizontal = 16.dp).padding(vertical = 8.dp)
                TextInputSimple(
                    label = "Search Variety",
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    textInputModifier,
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    groupedByVariety.forEach { (varietyName, varietyBatches) ->
                        // Variety Header
                        stickyHeader {
                            VarietyHeader(varietyName)
                        }

                        val groupedByYear = varietyBatches.groupBy { it.year }
                        groupedByYear.forEach { (year, yearBatches) ->
                            // Year Header
                            item {
                                Text(
                                    text = year.toString(),
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }

                            val groupedBySeason = yearBatches.groupBy { it.seasonName }
                            groupedBySeason.forEach { (seasonName, seasonBatches) ->
                                // Season Header
                                item {
                                    Text(
                                        text = seasonName,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }

                                itemsIndexed(seasonBatches) { index, batch ->
                                    SimpleStockReportSeedBatchCard(
                                        item = batch,
                                        odd = index % 2 != 0,
                                        onClick = {}
                                    )
                                }
                            }
                            // Spacer after each year
                            item {
                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }
                    }
                }
            }

            // Floating export button
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Excel Export button
                Button(
                    onClick = {
                        exportLauncher.launch("StockReport.xlsx")
                    },
                    modifier = Modifier
                        .height(80.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                ) {
                    Text(
                        text = "Export Excel",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // Last Date Picker dialog
    if (showLastDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showLastDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onLastDateChanged(SimpleDateFormat("yyyy-MM-dd",
                            Locale.getDefault()).format(Date(it)))
                    }
                    showLastDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                // Reset button
                TextButton(onClick = {
                    // Action 1: Clear the selection in the UI
                    datePickerState.selectedDateMillis = null
                    // Action 2: Update your ViewModel to reflect the cleared state
                    viewModel.onLastDateChanged(null)
                    showLastDatePicker = false
                }) {
                    Text("Reset")
                }

                // Cancel button
                TextButton(onClick = { showLastDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// -- Preview --
val previewViewModel = PreviewStockReportViewModel()

@Preview(showBackground = true)
@Composable
private fun PreviewStockReportScreen() {
    RCRCSeedManagerTheme {
        StockReportScreen(previewViewModel)
    }
}