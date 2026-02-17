package com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.CompoBoxCustom
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple

@Composable
fun SupplierFormScreen(
    viewModel: ISupplierViewModel = hiltViewModel<SupplierViewModel>(),
    navController: NavController? = null
) {
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val uiState by viewModel.supplierFormUiState.collectAsState()

    LaunchedEffect(resourceUiState.isSuccess) {
        if (resourceUiState.isSuccess) {
            navController?.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = if (uiState.isEditing) "Update Supplier [Id: #${viewModel.selectedSupplier?.id}]" else "Add New Supplier",
                navController = navController
            )
        },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            val textInputModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp)

            // Full Name
            item {
                TextInputSimple(
                    label = "Full Name",
                    value = uiState.fullName,
                    onValueChange = { viewModel.onFullNameChange(it) },
                    textInputModifier,
                )
            }

            // Supplier Type Dropdown
            item {
                val supplierTypeNames = uiState.supplierTypes.map { it.name }
                val selectedIndex = uiState.supplierTypes.indexOf(uiState.selectedSupplierType)
                
                CompoBoxCustom(
                    list = supplierTypeNames,
                    selectedItem = uiState.selectedSupplierType?.name,
                    startIndex = selectedIndex,
                    label = "Supplier Type",
                    onItemSelected = { name ->
                        val selectedType = uiState.supplierTypes.find { it.name == name }
                        selectedType?.let { viewModel.onSupplierTypeChange(it) }
                    },
                    modifier = textInputModifier
                )
            }

            // Email
            item {
                TextInputSimple(
                    label = "Email",
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    textInputModifier,
                )
            }

            // Phone
            item {
                TextInputSimple(
                    label = "Phone",
                    value = uiState.phone,
                    onValueChange = { viewModel.onPhoneChange(it) },
                    textInputModifier,
                )
            }

            // Address
            item {
                TextInputSimple(
                    label = "Address",
                    value = uiState.address,
                    onValueChange = { viewModel.onAddressChange(it) },
                    textInputModifier,
                )
            }

            // Description
            item {
                TextInputSimple(
                    label = "Description",
                    value = uiState.description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    textInputModifier,
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Submit button
            item {
                Button(
                    onClick = { viewModel.onSubmitClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                ) {
                    Text(
                        text = if (uiState.isEditing) "Update Supplier" else "Add New Supplier",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Error Message
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
}

// -- Preview --
private val previewViewModel = PreviewSupplierFormViewModel()

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun PreviewSupplierFormScreen() {
    SupplierFormScreen(previewViewModel)
}