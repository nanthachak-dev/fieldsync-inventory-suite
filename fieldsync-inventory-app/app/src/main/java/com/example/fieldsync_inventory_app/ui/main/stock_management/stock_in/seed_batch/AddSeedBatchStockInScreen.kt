package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.seed_batch

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.CompoBoxCustom
import com.example.fieldsync_inventory_app.ui.common.components.RadioButtonGroupCustom
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputWithSuffix
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.IStockInViewModel
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.PreviewStockInViewModel
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.StockInViewModel
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import com.example.fieldsync_inventory_app.util.constans.StockInTask
import com.example.fieldsync_inventory_app.util.number.ThousandSeparatorTransformation
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun AddSeedBatchStockInScreen(
    viewModel: IStockInViewModel = hiltViewModel<StockInViewModel>(),
    navController: NavController? = null
) {
    // Define UI state variable
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val uiState by viewModel.seedBatchUiState.collectAsState()
    val stockOutUiState by viewModel.stockInUiState.collectAsState()

    val lazyListState = rememberLazyListState() // Define to auto scroll down when screen's heigh is expanded
    val coroutineScope = rememberCoroutineScope()

    val isEditing = uiState.isEditing

    // Observe uiState.error for showing error messages
    LaunchedEffect(resourceUiState.error) {
        resourceUiState.error?.let {
            coroutineScope.launch {
                val lastItemIndex = lazyListState.layoutInfo.totalItemsCount - 1
                if (lastItemIndex >= 0) {
                    lazyListState.animateScrollToItem(index = lastItemIndex)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSeedBatchForm()
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = if (isEditing) "Edit Seed Batch" else "Add Seed Batch",
                navController = navController
            )
        },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState, // Pass the state to auto scroll down when screen's heigh is expanded
            modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
        ) {
            val modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

            // Variety
            item {
                val varietyList = uiState.riceVarieties.map { it.name }
                CompoBoxCustom(
                    varietyList,
                    uiState.selectedVariety,
                    -1,
                    "Variety",
                    modifier = modifier,
                    onItemSelected = {viewModel.onVarietyChange(it)}
                )
            }

            // Year
            item {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val yearList = (currentYear - 5..currentYear + 5).map { it.toString() }
                CompoBoxCustom(
                    yearList,
                    uiState.selectedYear.toString(),
                    -1,
                    "Year",
                    modifier = modifier,
                    onItemSelected = { viewModel.onYearChange(it.toInt()) }
                )
            }

            // Season
            item {
                val seasonList = uiState.seasons.map { it.description }
                CompoBoxCustom(
                    seasonList,
                    uiState.selectedSeason,
                    -1,
                    "Season",
                    modifier = modifier,
                    onItemSelected = { viewModel.onSeasonChange(it) }
                )
            }

            // Generation
            item {
                val generationList = uiState.riceGenerations.map { it.name }
                CompoBoxCustom(generationList,
                    uiState.selectedGeneration,
                    -1,
                    "Generation",
                    modifier = modifier,
                    onItemSelected = { viewModel.onGenerationChange(it) }
                )
            }

            // Grading
            item {
                val radioItems = listOf("Yes","No")
                RadioButtonGroupCustom(
                    "Grading:",
                    radioItems,
                    uiState.selectedGrading,
                    onOptionSelected = { viewModel.onGradingChange(it) }
                )
            }

            // Germination
            item {
                val radioItems = listOf("Yes","No")
                RadioButtonGroupCustom(
                    "Germination:"
                    ,radioItems,
                    uiState.selectedGermination,
                    onOptionSelected = { viewModel.onGerminationChange(it) }
                )
            }

            // Quantity
            item {
                Spacer(modifier = Modifier.height(16.dp))
                TextInputWithSuffix(
                    "Quantity",
                    "Kg",
                    uiState.quantity?:"", // Support typing number more than 8 digits
                    onValueChange = { viewModel.onQuantityChange(it) },
                    modifier = modifier,
                    visualTransformation = ThousandSeparatorTransformation
                )
            }

            // Price
            if (stockOutUiState.selectedTask == StockInTask.PURCHASE) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextInputWithSuffix(
                        label = "Price per Kg",
                        suffix = "LAK",
                        value = uiState.price?:"", // Support typing number more than 8 digits
                        onValueChange = { viewModel.onPriceChange(it) },
                        modifier = modifier,
                        visualTransformation = ThousandSeparatorTransformation
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        viewModel.onAddOrEditSeedBatchClicked(
                            onSuccess = {
                                navController?.popBackStack()
                            },
                            onFailure = {}
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                ) {
                    Text(
                        text = if (isEditing) "Update Seed Batch" else "Add Seed Batch",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Display an error message if the form is invalid
            resourceUiState.error?.let{ errorMessage ->
                item {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

// -- Preview --
val previewAddSeedBatchStockInViewModel = PreviewStockInViewModel()

@Preview(showBackground = true)
@Composable
fun PreviewAddSeedBatchStockInScreen() {
    RCRCSeedManagerTheme {
        AddSeedBatchStockInScreen(previewAddSeedBatchStockInViewModel)
    }
}
