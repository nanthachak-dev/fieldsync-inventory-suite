package com.example.fieldsync_inventory_app.ui.main.history

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.BottomNavigationBar
import com.example.fieldsync_inventory_app.ui.common.components.ConfirmationDialog
import com.example.fieldsync_inventory_app.ui.common.components.FilterIconButton
import com.example.fieldsync_inventory_app.ui.main.history.components.HistoryFilterDialog
import com.example.fieldsync_inventory_app.ui.main.history.components.TransactionCard
import com.example.fieldsync_inventory_app.ui.main.history.components.TransactionDetailDialog
import com.example.fieldsync_inventory_app.ui.main.history.model.TransactionCardData
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import com.example.fieldsync_inventory_app.util.composables.PressBackToExit

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryScreen(viewModel: IHistoryViewModel,
                  topBar: @Composable () -> Unit = {},
                  navController: NavController? = null) {
    PressBackToExit()

    val uiState by viewModel.historyUiState.collectAsState()
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showTransactionDetailDialog by remember { mutableStateOf(false) }
    var selectedTransaction by remember { mutableStateOf<TransactionCardData?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Long?>(null) }

    val pullRefreshState = rememberPullRefreshState(
        uiState.isLoading,
        { viewModel.refreshScreen() })

    val listState = rememberLazyListState()

    // Pagination logic
    val shouldLoadMore = remember(uiState.isLastPage, uiState.isLoadingNextPage, uiState.hasError) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            !uiState.isLastPage && !uiState.isLoadingNextPage && !uiState.hasError && totalItemsNumber > 0 && lastVisibleItemIndex >= totalItemsNumber - 5
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            viewModel.loadNextPage()
        }
    }

    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.firstLaunch()
        }
    }

    // Show filter dialog
    if (showDialog) {
        HistoryFilterDialog(
            viewModel = viewModel,
            historyUiState = uiState,
            onDismiss = { showDialog = false }
        )
    }

    if (showTransactionDetailDialog && selectedTransaction != null) {
        TransactionDetailDialog(
            summary = selectedTransaction!!.summary,
            details = uiState.selectedTransactionDetails,
            isLoading = uiState.isDetailLoading,
            onDismiss = { showTransactionDetailDialog = false }
        )
    }

    if (showDeleteConfirmDialog) {
        ConfirmationDialog(
            showDialog = true,
            title = "Confirm Deletion",
            message = "Are you sure you want to delete this transaction [Transaction #${transactionToDelete}]?",
            onDismiss = {
                showDeleteConfirmDialog = false
                transactionToDelete = null
            },
            onConfirm = {
                transactionToDelete?.let { viewModel.onDeleteTransaction(it) }
            }
        )
    }

    // Success/Error/Loading handling from resourceUiState
    if (resourceUiState.isLoading) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Deleting...", color = Color.White)
                }
            }
        }
    }

    if (resourceUiState.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearResourceState() },
            title = { Text("Error") },
            text = { Text(resourceUiState.error ?: "Unknown error") },
            confirmButton = {
                TextButton(onClick = { viewModel.clearResourceState() }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            topBar()
        },
        bottomBar = {
            BottomNavigationBar(navController, 3)
        },
        containerColor = Color(0xFF3D3C31)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                Modifier.fillMaxSize()
            ) {
                // -- Info and filter --
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!uiState.isLoading) {
                        Text(
                            text = "Showed ${uiState.transactions.size} transactions of ${uiState.totalElements}",
                            color = Color.White.copy(alpha = 0.7f),
                            style = typography.bodySmall
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    FilterIconButton({ showDialog = true })
                }

                // -- Show loading indicator or list of transaction--
                if (uiState.isLoading && uiState.transactions.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState
                    ) {
                        items(uiState.transactions) { transaction ->
                            TransactionCard(
                                data = transaction,
                                onClick = {
                                    selectedTransaction = transaction
                                    viewModel.loadTransactionDetails(transaction.summary.transactionId)
                                    showTransactionDetailDialog = true
                                },
                                onDelete = {
                                    transactionToDelete = transaction.summary.transactionId
                                    showDeleteConfirmDialog = true
                                }
                            )
                        }

                        if (uiState.isLoadingNextPage) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        if (uiState.hasError) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Error loading data",
                                        color = Color.White,
                                        style = typography.bodyMedium
                                    )
                                    TextButton(onClick = { viewModel.loadNextPage() }) {
                                        Text("Retry", color = Color(0xFFE2B067))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = uiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

// =============================== Preview ==============================
val previewViewModel = PreviewHistoryViewModel()

@Preview(showBackground = true)
@Composable
fun PreviewHistoryScreen() {
    RCRCSeedManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF4A443A)
        ) {
            HistoryScreen(previewViewModel)
        }
    }
}
