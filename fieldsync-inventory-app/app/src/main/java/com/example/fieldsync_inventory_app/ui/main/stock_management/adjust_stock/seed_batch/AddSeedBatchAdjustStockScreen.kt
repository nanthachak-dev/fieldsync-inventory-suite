package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch

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
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun SelectSeedBatchScreen(
    viewModel: IAddSeedBatchAdjustStockViewModel = hiltViewModel<AddSeedBatchAdjustStockViewModel>(),
    navController: NavController? = null
) {
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val lazyListState =
        rememberLazyListState() // Define to auto scroll down when screen's heigh is expanded
    val coroutineScope = rememberCoroutineScope()

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

    LaunchedEffect(Unit) {
        viewModel.launchScreen()
    }

    Scaffold(
        topBar = {
            SubTopBar("Add Seed Batch", navController)
        },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState, // Pass the state to auto scroll down when screen's heigh is expanded
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            val comboBoxModifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            item {
                val varietyList = uiState.riceVarieties.map { it.name }
                CompoBoxCustom(
                    varietyList,
                    uiState.selectedVariety,
                    -1,
                    "Variety",
                    modifier = comboBoxModifier,
                    onItemSelected = { viewModel.onVarietyChange(it) }
                )
            }
            item {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val yearList = (currentYear - 5..currentYear + 5).map { it.toString() }
                CompoBoxCustom(
                    yearList,
                    uiState.selectedYear,
                    -1,
                    "Year",
                    modifier = comboBoxModifier,
                    onItemSelected = { viewModel.onYearChange(it) }
                )
            }
            item {
                val seasonList = uiState.seasons.map { it.description }
                CompoBoxCustom(
                    seasonList,
                    uiState.selectedSeason,
                    -1,
                    "Season",
                    modifier = comboBoxModifier,
                    onItemSelected = { viewModel.onSeasonChange(it) }
                )
            }
            item {
                val generationList = uiState.riceGenerations.map { it.name }
                CompoBoxCustom(
                    generationList,
                    uiState.selectedGeneration,
                    -1,
                    "Generation",
                    modifier = comboBoxModifier,
                    onItemSelected = { viewModel.onGenerationChange(it) }
                )
            }
            item {
                val radioItems = listOf("Yes", "No")
                RadioButtonGroupCustom(
                    "Grading:",
                    radioItems,
                    uiState.selectedGrading,
                    onOptionSelected = { viewModel.onGradingChange(it) }
                )
            }
            item {
                val radioItems = listOf("Yes", "No")
                RadioButtonGroupCustom(
                    "Germination:", radioItems,
                    uiState.selectedGermination,
                    onOptionSelected = { viewModel.onGerminationChange(it) }
                )
            }

            item {
                Button(
                    onClick = {
                        viewModel.onOkClicked(navController)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                ) {
                    Text(
                        text = "OK",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Display an error message if the form is invalid
            resourceUiState.error?.let { errorMessage ->
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
val previewViewModel = PreviewSelectSeedBatchViewModel()

@Preview(showBackground = true)
@Composable
fun PreviewSelectSeedBatchScreen() {
    RCRCSeedManagerTheme {
        SelectSeedBatchScreen(previewViewModel)
    }
}
