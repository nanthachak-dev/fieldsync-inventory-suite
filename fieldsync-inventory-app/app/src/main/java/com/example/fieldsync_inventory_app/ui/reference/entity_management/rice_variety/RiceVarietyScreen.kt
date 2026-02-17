package com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.domain.model.RiceVariety
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.components.RiceVarietyCard
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@Composable
fun RiceVarietyScreen(
    viewModel: IRiceVarietyViewModel = hiltViewModel<RiceVarietyViewModel>(),
    navController: NavController? = null
) {
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val varietyUiState by viewModel.varietyUiState.collectAsState()

    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()
    val varieties = varietyUiState.varieties

    var searchQuery by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedVariety by remember { mutableStateOf<RiceVariety?>(null) }

    val filteredList = remember(varieties, searchQuery) {
        varieties.filter { variety ->
            variety.name.contains(searchQuery, ignoreCase = true)
        }
    }

    // Observe first launch state
    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.firstLaunch()
        }
    }

    Scaffold(
        topBar = {
            SubTopBar("Rice Variety", navController)
        },
        containerColor = Color(0xFF3D3C31),
        // Add new seed batch button
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onAddNewVarietyClick()
                    navController?.navigate(Screen.RiceVarietyForm.route)
                },
                // Change the FAB background color
                containerColor = Color(0xFF7C5B40),
                // Change the default color for all content (Icons/Text) inside the FAB
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Seed Batch")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // -- Search Variety --
            val textInputModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp)
            TextInputSimple(
                label = "Search Variety",
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                textInputModifier,
            )
            Spacer(modifier = Modifier.height(24.dp))
            // List of rice varieties
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                // Show list of variety cards
                items(filteredList, itemContent = {
                    RiceVarietyCard(
                        it,
                        onCardClicked = {
                            viewModel.onEditVarietyItemClick(it)
                            navController?.navigate(Screen.RiceVarietyForm.route)
                        },
                        onDeleteClicked = {
                            selectedVariety = it
                            showDeleteDialog = true
                        }
                    )
                })

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                if (resourceUiState.loadingMessage != null) {
                    Text(
                        text = resourceUiState.loadingMessage.toString(),
                        Modifier.padding(start = 20.dp, end = 20.dp),
                        color = Color.White
                    )
                }
            }
        }
    }

    // Delete dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this variety?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedVariety?.let { viewModel.onDeleteVarietyItemClick(it) }
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
}

// -- Preview --
private val previewViewModel = PreviewRiceVarietyViewModel()

@Preview(showBackground = true)
@Composable
private fun PreviewRiceVarietyScreen() {
    RCRCSeedManagerTheme {
        RiceVarietyScreen(previewViewModel)
    }
}