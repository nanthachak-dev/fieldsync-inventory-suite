package com.example.fieldsync_inventory_app.ui.main.report.sale_report

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.components.SaleReportSeedBatchCard
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.components.VarietyHeader
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SaleReportScreen(
    viewModel: ISaleReportViewModel = hiltViewModel<SaleReportViewModel>(),
    navController: NavController? = null
) {
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val saleReportUiState by viewModel.saleReportUiState.collectAsState()
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.firstLaunch()
        }
    }

    val filteredList = remember(saleReportUiState.seedBatchList, searchQuery) {
        saleReportUiState.seedBatchList.filter { batch ->
            // Filter out items with all zero values
            val hasData = (batch.totalGermination > 0) || (batch.totalGerminationSale > 0)
            
            val searchCondition = if (searchQuery.isBlank()) {
                true
            } else {
                batch.varietyName.contains(searchQuery, ignoreCase = true)
            }
            
            hasData && searchCondition
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

    Scaffold(
        topBar = {
            SubTopBar(
                title = "Sale Report",
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
                // -- Export --
                Row(
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    // Date input (From)
                    val dateInputModifier:Modifier = Modifier.width(160.dp)
                    DateInput(
                        label = "From",
                        selectedDate = saleReportUiState.selectedFromDate?:"Select Date",
                        modifier = dateInputModifier,
                        launchDialogs = { showFromDatePicker = true }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // Date input (To)
                    DateInput(
                        label = "To",
                        selectedDate = saleReportUiState.selectedToDate?:"Select Date",
                        modifier = dateInputModifier,
                        launchDialogs = { showToDatePicker = true }
                    )
                }

                // -- Search --
                Row(
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 16.dp).padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // -- Search Variety --
                    val textInputModifier = Modifier
                        .fillMaxWidth()
                    TextInputSimple(
                        label = "Search Variety",
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                        },
                        textInputModifier,
                    )
                }

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
                                    SaleReportSeedBatchCard(
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
                Button(
                    onClick = {
                        exportLauncher.launch("SaleReport.xlsx")
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

    if (showFromDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showFromDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onFromDateChanged(SimpleDateFormat("yyyy-MM-dd",
                            Locale.getDefault()).format(Date(it)))
                    }
                    showFromDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                // Reset button
                TextButton(onClick = {
                    datePickerState.selectedDateMillis = null
                    viewModel.onFromDateChanged(null)
                    showFromDatePicker = false
                }) {
                    Text("Reset")
                }

                // Cancel button
                TextButton(onClick = { showFromDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showToDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showToDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onToDateChanged(SimpleDateFormat("yyyy-MM-dd",
                            Locale.getDefault()).format(Date(it)))
                    }
                    showToDatePicker = false
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
                    viewModel.onToDateChanged(null)
                    showToDatePicker = false
                }) {
                    Text("Reset")
                }

                // Cancel button
                TextButton(onClick = { showToDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


// -- Preview --
val previewViewModel = PreviewSaleReportViewModel()

@Preview(showBackground = true)
@Composable
private fun PreviewSaleReportScreen() {
    RCRCSeedManagerTheme {
        SaleReportScreen(previewViewModel)
    }
}