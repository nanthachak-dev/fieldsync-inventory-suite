package com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.CompoBoxCustom
import com.example.fieldsync_inventory_app.ui.common.components.DateTimeInput
import com.example.fieldsync_inventory_app.ui.common.components.SeedBatchCard
import com.example.fieldsync_inventory_app.ui.common.components.SimpleCardButton
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple
import com.example.fieldsync_inventory_app.ui.common.components.TextInputWithSuffix
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import kotlinx.coroutines.launch

@Composable
fun AdjustStockScreen(
    viewModel: IAdjustStockViewModel = hiltViewModel<AdjustStockViewModel>(),
    navController: NavController? = null
) {
    // Collect the UI state
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val uiState by viewModel.state.collectAsState()

    val context = LocalContext.current
    val lazyListState = rememberLazyListState() // Define to auto scroll down when screen's heigh is expanded
    val coroutineScope = rememberCoroutineScope()

    // Initial screen
    LaunchedEffect(Unit) {
        viewModel.screenLaunch()
    }

    // Observe for success state to show toast and navigate
    LaunchedEffect(resourceUiState.isSuccess) {
        if (resourceUiState.isSuccess) {
            Toast.makeText(context, "Adjustment saved successfully!", Toast.LENGTH_SHORT).show()
            navController?.popBackStack()
            viewModel.onSuccessShown() // Reset the flag
            viewModel.clearForm()
        }
    }

    // Observe for the error state and scroll to bottom
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

    Scaffold(
        topBar = {
            SubTopBar("Adjust Stock", navController)
        },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState, // Pass the state to auto scroll down when screen's heigh is expanded
            modifier = Modifier.padding(innerPadding)
        ) {
            val modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            // Use the taskList from the UiState
            val taskList = uiState.taskList
            item {
                CompoBoxCustom(
                    taskList,
                    uiState.selectedTask, // Observe state
                    -1,
                    "Select Task",
                    modifier = modifier,
                    onItemSelected = viewModel::onTaskSelected // Delegate event
                )
            }

            // Show Seed Batch Card if data is available
            if (uiState.seedBatchDataFrom == null) {
                item {
                    SimpleCardButton(
                        "Add Seed Batch",
                        modifier = modifier,
                        onClick = {
                            navController?.navigate("select_seed_batch_screen")
                        }
                    )
                }
            } else {
                // -- Seed Batch Source
                item {
                    Text(
                        text = "From:",
                        modifier = Modifier.padding(start = 16.dp),
                        color = Color.White
                    )
                }
                item {
                    SeedBatchCard(
                        // Use the data from the UiState
                        seedBatchData = uiState.seedBatchDataFrom,
                        onClick = {
                            // Navigation logic remains here
                            navController?.navigate("select_seed_batch_screen")
                        },
                        highlightTask = uiState.selectedTask
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // -- Generate Seed Batch Card Destination --
                if (uiState.selectedTask != null && uiState.seedBatchDataTo != null) {
                    item {
                        Text(
                            text = "To:",
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.White
                        )
                    }
                    item {
                        // Pass the calculated 'To' data.
                        // Since this card's data is calculated, it should not be clickable to select a batch.
                        SeedBatchCard(
                            seedBatchData = uiState.seedBatchDataTo, // <-- NEW: Use the 'To' data
                            onClick = {}, // <-- Disable click for 'To' card
                            highlightTask = uiState.selectedTask
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            // Inputs
            // Quantity
            item {
                // Use state value and delegate change event
                TextInputWithSuffix(
                    "Quantity",
                    "Kg",
                    uiState.quantity,
                    onValueChange = viewModel::onQuantityChanged, // Assuming you add this in the VM
                    modifier = modifier
                )
            }
            // Loss
            item {
                TextInputWithSuffix(
                    "Loss",
                    "Kg",
                    uiState.loss,
                    viewModel::onLossChanged,
                    modifier = modifier
                )
            }
            item {
                TextInputSimple(
                    "Reason",
                    uiState.reason,
                    viewModel::onReasonChanged,
                    modifier = modifier
                )
            }
            item {
                DateTimeInput(
                    label = "Time",
                    selectedDateTime = uiState.dateTime, // Pass the LocalDateTime state
                    onDateTimeSelected = viewModel::onDateTimeSelected, // Delegate event to ViewModel
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            item {
                Button(
                    onClick = {
                        // Delegate submit logic to the ViewModel
                        viewModel.onSubmitClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                ) {
                    Text(
                        text = "Submit",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Show error message
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

    // Loading indicator and overlay
    if (resourceUiState.isLoading) {
        // A Box with a background and clickable modifier will consume touch events,
        // effectively disabling the UI below.
        // The semi-transparent background also provides visual feedback that the UI is busy.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

val previewViewModel = PreviewAdjustStockViewModel()

@Preview(showBackground = true)
@Composable
fun PreviewAdjustStockScreen() {
    RCRCSeedManagerTheme {
        AdjustStockScreen(previewViewModel)
    }
}
