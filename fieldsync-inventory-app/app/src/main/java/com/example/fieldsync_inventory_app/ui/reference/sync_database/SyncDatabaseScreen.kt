package com.example.fieldsync_inventory_app.ui.reference.sync_database

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.SyncButton
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import kotlinx.coroutines.launch

@Composable
fun SyncDatabaseScreen(
    viewModel: ISyncDatabaseViewModel = hiltViewModel<SyncDatabaseViewModel>(),
    navController: NavController? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val resourceUiState by viewModel.resourceUiState.collectAsState()

    // Observe for success state to show toast and navigate
    LaunchedEffect(resourceUiState.isSuccess) {
        if (resourceUiState.isSuccess) {
            snackbarHostState.showSnackbar(
                message = "Sync data completed!",
                duration = SnackbarDuration.Short // Or SnackbarDuration.Long
            )
        }
    }

    // Observe uiState.error for showing error messages
    LaunchedEffect(resourceUiState.error) {
        resourceUiState.error?.let { errorMessage ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Short // Or SnackbarDuration.Long
                )
            }
        }
    }

    Scaffold(
        topBar = {
            SubTopBar("Sync Database", navController)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SyncButton(
                mainText = "Sync Reference",
                subText = "Last synced: 22/9/2025",
                onClick = {
                    viewModel.onSyncReferenceDataClicked()
                }
            )

            // Sync Supplier, Customer and User (Staff)
            SyncButton(
                mainText = "Sync Person",
                subText = "Last synced: 22/9/2025",
                onClick = {
                    viewModel.onSyncPersonDataClicked()
                }
            )

            SyncButton(
                mainText = "Sync Seed Batch",
                subText = "Last synced: 22/9/2025",
                onClick = {
                    viewModel.onSyncSeedBatchDataClicked()
                }
            )

            SyncButton(
                mainText = "Sync Transaction",
                subText = "Last synced: 22/9/2025",
                onClick = {
                    viewModel.onSyncTransactionClicked()
                }
            )
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


// -- Preview --
val previewViewModel = PreviewSyncDatabaseViewModel()

@Preview(showBackground = true)
@Composable
fun PreviewSyncDatabaseScreen() {
    RCRCSeedManagerTheme {
        SyncDatabaseScreen(viewModel = previewViewModel)
    }
}