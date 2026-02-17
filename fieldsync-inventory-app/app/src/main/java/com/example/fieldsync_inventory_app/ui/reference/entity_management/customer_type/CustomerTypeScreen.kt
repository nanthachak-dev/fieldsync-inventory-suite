package com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.domain.model.CustomerType
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.components.CustomerTypeCard

@Composable
fun CustomerTypeScreen(
    viewModel: ICustomerTypeViewModel = hiltViewModel<CustomerTypeViewModel>(),
    navController: NavController? = null
) {
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val customerTypeUiState by viewModel.customerTypeUiState.collectAsState()

    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()
    val customerTypes = customerTypeUiState.customerTypes

    var searchQuery by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCustomerType by remember { mutableStateOf<CustomerType?>(null) }

    val filteredList = remember(customerTypes, searchQuery) {
        customerTypes.filter { customerType ->
            customerType.name.contains(searchQuery, ignoreCase = true)
        }
    }

    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.firstLaunch()
        }
    }

    Scaffold(
        topBar = {
            SubTopBar("Customer Type", navController)
        },
        containerColor = Color(0xFF3D3C31),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onAddNewCustomerTypeClick()
                    navController?.navigate(Screen.CustomerTypeForm.route)
                },
                containerColor = Color(0xFF7C5B40),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Customer Type")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val textInputModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp)
            TextInputSimple(
                label = "Search Customer Type",
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                textInputModifier,
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList, itemContent = {
                    CustomerTypeCard(
                        it,
                        onCardClicked = {
                            viewModel.onEditCustomerTypeItemClick(it)
                            navController?.navigate(Screen.CustomerTypeForm.route)
                        },
                        onDeleteClicked = {
                            selectedCustomerType = it
                            showDeleteDialog = true
                        }
                    )
                })

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

    if (resourceUiState.isLoading) {
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

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this customer type?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedCustomerType?.let { viewModel.onDeleteCustomerTypeItemClick(it) }
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
private val previewViewModel = PreviewCustomerTypeViewModel()

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun PreviewCustomerTypeScreen() {
    CustomerTypeScreen(previewViewModel)
}