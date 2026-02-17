package com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.fieldsync_inventory_app.ui.common.components.SeedBatchStockOutCard
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple
import com.example.fieldsync_inventory_app.ui.common.components.TextInputWithSuffix
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.model.SeedBatchStockOutData
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import com.example.fieldsync_inventory_app.util.constans.StockOutTask
import kotlinx.coroutines.launch

@Composable
fun StockOutScreen(
    viewModel: IStockOutViewModel = hiltViewModel<StockOutViewModel>(),
    navController: NavController? = null
) {
    val context = LocalContext.current // For toast
    val lazyListState = rememberLazyListState() // Define to auto scroll down when screen's heigh is expanded
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.stockOutUiState.collectAsState()
    val resourceUiState by viewModel.resourceUiState.collectAsState()

    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()

    val taskList = uiState.taskList.map { it.displayName }
    val customerList = uiState.customerList

    // Data
    val seedBatches = viewModel.stockOutCartItems

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showClearScreenDialog by remember { mutableStateOf(false) }
    var selectedSeedBatch by remember { mutableStateOf<SeedBatchStockOutData?>(null) }
    var pendingTaskSelection by remember { mutableStateOf<String?>(null) }

    // Observe first launch state
    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.firstLaunch()
        }
    }

    // Check if cart has items and set task to SALE if needed
    LaunchedEffect(key1 = Unit) {
        if (seedBatches.isNotEmpty()) {
            // Set task to SALE if cart has items
            viewModel.onTaskChange(StockOutTask.SALE)
        }
        viewModel.screenLaunch()
    }

    // Observe for success state to show toast and navigate
    LaunchedEffect(resourceUiState.isSuccess) {
        if (resourceUiState.isSuccess) {
            Toast.makeText(context, "Stock out saved successfully!", Toast.LENGTH_SHORT).show()
            navController?.popBackStack()
            viewModel.clearStockOutScreen()
        }
    }

    // Observe uiState.error for scrolling screen down to show error messages
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

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this seed batch?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedSeedBatch?.let { viewModel.deleteSeedBatch(it) }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Confirm Edit") },
            text = { Text("Are you sure you want to edit this seed batch?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedSeedBatch?.let { viewModel.onEditClicked(it) }
                        showEditDialog = false
                        navController?.navigate("add_seed_batch_stock_out_screen")
                    }
                ) {
                    Text("Edit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showClearScreenDialog) {
        AlertDialog(
            onDismissRequest = { showClearScreenDialog = false },
            title = { Text("Confirm Action") },
            text = { Text("Changing the task will clear the screen. Do you want to continue?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearStockOutScreen()
                        pendingTaskSelection?.let {
                            val selectedTask = StockOutTask.fromDisplayName(it)
                            viewModel.onTaskChange(selectedTask)
                        }
                        showClearScreenDialog = false
                    }
                ) {
                    Text("Continue")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearScreenDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            SubTopBar("Stock Out", navController)
        },
        containerColor = Color(0xFF3D3C31),
        // Add new seed batch button
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navigate to the screen where user collects info
                    if (uiState.selectedTask == null)
                        Toast.makeText(context, "Select task first!", Toast.LENGTH_SHORT).show()
                    else
                        navController?.navigate("add_seed_batch_stock_out_screen")
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Card")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState, // Pass the state to auto scroll down when screen's heigh is expanded
            modifier = Modifier.padding(innerPadding)
        ) {
            val modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

            // Select Task
            item {
                CompoBoxCustom(
                    taskList,
                    uiState.selectedTask?.displayName,
                    -1,
                    "Select Task",
                    modifier = modifier,
                    onItemSelected = { selectedTaskString ->
                        if (seedBatches.isNotEmpty() && selectedTaskString != uiState.selectedTask?.displayName) {
                            pendingTaskSelection = selectedTaskString
                            showClearScreenDialog = true
                        } else {
                            val selectedTask = StockOutTask.fromDisplayName(selectedTaskString)
                            viewModel.onTaskChange(selectedTask)
                        }
                    }
                )
            }

            // Seed Batch card
            items(seedBatches, itemContent = {
                SeedBatchStockOutCard(
                    it, {
                        // Edit the seed batch
                        selectedSeedBatch = it
                        showEditDialog = true
                    },
                    {
                        // Delete the seed batch
                        selectedSeedBatch = it
                        showDeleteDialog = true
                    },
                    isSell = uiState.selectedTask == StockOutTask.SALE
                )
                Spacer(modifier = Modifier.height(16.dp))
            })

            if (!seedBatches.isEmpty()) {
                // Total Quantity (read only)
                item {
                    TextInputWithSuffix(
                        "Total Quantity",
                        "Kg",
                        uiState.totalQuantityDisplay,
                        onValueChange = {},
                        modifier = modifier,
                        readOnly = true
                    )
                }

                // Total Price (read only)
                if (uiState.selectedTask == StockOutTask.SALE) {
                    item {
                        TextInputWithSuffix(
                            "Total Price",
                            "LAK",
                            uiState.totalPriceDisplay,
                            onValueChange = {},
                            modifier = modifier,
                            readOnly = true
                        )
                    }
                }

                // Customer
                item {
                    CompoBoxCustom(
                        customerList.map { it.fullName },
                        uiState.selectedCustomer,
                        -1,
                        "Customer:",
                        modifier = modifier,
                        onItemSelected = viewModel::onCustomerChange // Delegate event
                    )
                }
            }

            // Time
            item {
                DateTimeInput(
                    label = "Time",
                    selectedDateTime = uiState.transactionTime, // Pass the LocalDateTime state
                    onDateTimeSelected = viewModel::onDateTimeSelected, // Delegate event to ViewModel
                    modifier = modifier
                )
            }

            // Note
            item {
                TextInputSimple(
                    "Note",
                    value = uiState.note ?: "",
                    onValueChange = { viewModel.onNoteChange(it) },
                    modifier = modifier
                )
            }

            // Submit button
            item {
                Button(
                    onClick = {
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
    } // End scaffold

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

val previewViewModel = PreviewStockOutViewModel()

@Preview(showBackground = true)
@Composable
fun PreviewStockOutScreen() {
    RCRCSeedManagerTheme {
        StockOutScreen(previewViewModel)
    }
}
