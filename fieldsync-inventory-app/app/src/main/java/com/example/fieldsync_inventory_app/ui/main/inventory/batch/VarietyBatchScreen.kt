package com.example.fieldsync_inventory_app.ui.main.inventory.batch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.FilterIconButton
import com.example.fieldsync_inventory_app.ui.common.components.SortIconButton
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.components.BatchCard
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.model.BatchCardData
import com.example.fieldsync_inventory_app.ui.main.inventory.components.AddToSaleDialog
import com.example.fieldsync_inventory_app.ui.main.inventory.components.BatchContextMenu
import com.example.fieldsync_inventory_app.ui.main.inventory.components.BatchFilterMenu
import com.example.fieldsync_inventory_app.ui.main.inventory.components.BatchSortMenu
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun VarietyBatchScreen(
    viewModel: IVarietyBatchViewModel,
    navController: NavController? = null,
    varietyId: Int = 0 // Receives from InventoryScreen
) {
    val uiState by viewModel.batchUiState.collectAsState()
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val groupedBatches = uiState.batchList.groupBy { it.year }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = resourceUiState.isLoading,
        onRefresh = { viewModel.refreshScreen() }
    )

    var sortExpanded by remember { mutableStateOf(false) }
    var filterExpanded by remember { mutableStateOf(false) }
    var hideZeroStock by remember { mutableStateOf(true) }

    // State for context menu
    var selectedBatchCard by remember { mutableStateOf<BatchCardData?>(null) }
    var batchMenuExpanded by remember { mutableStateOf(false) }

    // State for Add to Sale dialog
    var showSaleDialog by remember { mutableStateOf(false) }
    val saleCount = viewModel.saleCartItems.size

    LaunchedEffect(varietyId) {
        if (varietyId != 0) {
            viewModel.firstLaunch(varietyId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.clearSortAndFilter()
        viewModel.onHideZeroStockChange(true) // Always apply filter when opening screen
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = uiState.selectedVarietyName,
                navController = navController
            )
        },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        Box( // Using box for alignment of floating buttons and dialogs
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // -- Hide Zero Stock Switch --
                    Surface(
                        color = Color.White.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Hide Zero Stock",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Switch(
                                checked = hideZeroStock,
                                onCheckedChange = {
                                    hideZeroStock = it
                                    viewModel.onHideZeroStockChange(it)
                                },
                                modifier = Modifier.scale(0.7f),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFFFBC02D),
                                    checkedTrackColor = Color(0xFFFBC02D).copy(alpha = 0.3f),
                                    uncheckedThumbColor = Color.White.copy(alpha = 0.4f),
                                    uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
                                )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // -- Filter --
                    Box {
                        FilterIconButton(
                            onClick = { filterExpanded = true },
                            tint = Color(0xFFFBC02D).copy(alpha = 0.8f),
                            containerColor = Color.White.copy(alpha = 0.08f),
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                        )
                        BatchFilterMenu(
                            expanded = filterExpanded,
                            onDismissRequest = { filterExpanded = false },
                            activeFilter = uiState.currentFilter,
                            onFilterChange = { filter ->
                                viewModel.onFilterChange(filter)
                                filterExpanded = false
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    // -- Sort --
                    Box {
                        SortIconButton(
                            onClick = { sortExpanded = true },
                            tint = Color(0xFFFBC02D).copy(alpha = 0.8f),
                            containerColor = Color.White.copy(alpha = 0.08f),
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                        )
                        BatchSortMenu(
                            expanded = sortExpanded,
                            onDismissRequest = { sortExpanded = false },
                            activeSort = uiState.currentSort,
                            onSortChange = { sort ->
                                viewModel.onSortChange(sort)
                                sortExpanded = false
                            }
                        )
                    }
                } // End filter bar
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    groupedBatches.forEach { (year, batches) ->
                        stickyHeader {
                            Surface(
                                color = Color(0xFF3D3C31),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFBC02D))
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Year $year",
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    HorizontalDivider(
                                        modifier = Modifier.weight(1f),
                                        color = Color.White.copy(alpha = 0.1f)
                                    )
                                }
                            }
                        }

                        val batchesBySeason = batches.groupBy { it.seasonName }
                        batchesBySeason.forEach { (season, seasonBatches) ->
                            item {
                                Surface(
                                    color = Color(0xFFFBC02D).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                                    modifier = Modifier.padding(vertical = 4.dp).padding(end = 32.dp)
                                ) {
                                    Text(
                                        text = season.uppercase(),
                                        color = Color(0xFFFBC02D),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                        letterSpacing = 1.sp
                                    )
                                }
                            }

                            itemsIndexed(seasonBatches) { index, batch ->
                                Box { // Box for pop-up context menu
                                    BatchCard(
                                        item = batch,
                                        odd = index % 2 != 0,
                                        onClick = {
                                            selectedBatchCard = batch
                                            batchMenuExpanded = true
                                        }
                                    )

                                    // Context menu anchored to this specific batch item
                                    BatchContextMenu(
                                        expanded = batchMenuExpanded,
                                        selectedBatchCard = selectedBatchCard,
                                        currentBatchCard = batch,
                                        onDismissRequest = {
                                            batchMenuExpanded = false
                                            selectedBatchCard = null
                                        },
                                        onAddToAdjustStock = {
                                            batchMenuExpanded = false
                                            selectedBatchCard = null
                                            // Add data to StockAdjustmentBatch
                                            viewModel.onAddToAdjustStockClick(batch)
                                            navController?.navigate(Screen.AdjustStock.route)
                                        },
                                        onAddToSale = {
                                            // Show dialog for adding to sale
                                            showSaleDialog = true
                                            batchMenuExpanded = false
                                            // Keep selectedBatchId for the dialog
                                        }
                                    )
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }

            // Floating buttons on the right edge
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 60.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Sale button with notification badge
                BadgedBox(
                    badge = {
                        if (saleCount > 0) {
                            Badge(
                                containerColor = Color(0xFFE57373),
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = "$saleCount",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    SmallFloatingActionButton(
                        onClick = {
                            navController?.navigate(Screen.StockOut.route)
                        },
                        containerColor = Color(0xFFFBC02D),
                        contentColor = Color(0xFF3D3C31),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Sale",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Add to Sale Dialog
            AddToSaleDialog(
                showDialog = showSaleDialog,
                batchCard = selectedBatchCard,
                onDismiss = {
                    showSaleDialog = false
                    selectedBatchCard = null
                },
                onConfirm = { quantity, price ->
                    // Call ViewModel to add to sale cart
                    selectedBatchCard?.let { batch ->
                        val quantityDouble = quantity.toDoubleOrNull() ?: 0.0
                        val priceDouble = price.toDoubleOrNull() ?: 0.0
                        viewModel.onAddToSaleCartClick(batch,
                            quantityDouble, priceDouble)
                    }
                    showSaleDialog = false
                    selectedBatchCard = null
                }
            )
            
            PullRefreshIndicator(
                refreshing = resourceUiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

// -- Preview --
val previewViewModel = PreviewVarietyBatchViewModel()

@Preview(showBackground = true)
@Composable
fun PreviewBatchScreen() {
    RCRCSeedManagerTheme {
        VarietyBatchScreen(viewModel = previewViewModel, varietyId = 1)
    }
}
