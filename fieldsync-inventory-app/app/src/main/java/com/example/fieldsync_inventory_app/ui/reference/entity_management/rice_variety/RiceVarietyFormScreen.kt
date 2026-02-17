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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@Composable
fun RiceVarietyFormScreen(
    viewModel: IRiceVarietyViewModel = hiltViewModel<RiceVarietyViewModel>(),
    navController: NavController? = null
){
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val uiState by viewModel.varietyFormUiState.collectAsState()

    LaunchedEffect(resourceUiState.isSuccess) {
        if (resourceUiState.isSuccess) {
            navController?.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = if (uiState.isEditing) "Update Rice Variety [Id: #${viewModel.selectedVariety?.id}]" else "Add New Rice Variety",
                navController = navController
            )
        },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        LazyColumn(
            //state = lazyListState, // Pass the state to auto scroll down when screen's heigh is expanded
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            // Variety Name
            val textInputModifier = Modifier
                .fillMaxWidth().padding(horizontal = 16.dp).padding(vertical = 8.dp)
            item {
                TextInputSimple(
                    label = "Variety Name",
                    value = uiState.name,
                    onValueChange = {
                        viewModel.onNameChange(it)
                    },
                    textInputModifier,
                )
            }
            // Variety Description
            item {
                TextInputSimple(
                    label = "Variety Description",
                    value = uiState.description,
                    onValueChange = {
                        viewModel.onDescriptionChange(it)
                    },
                    textInputModifier,
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Submit button
            item {
                Button(
                    onClick = {viewModel.onSubmitClick()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                ) {
                    Text(
                        text = if (uiState.isEditing) "Update Rice Variety" else "Add New Rice Variety",
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
private val previewViewModel = PreviewRiceVarietyFormViewModel()

@Preview(showBackground = true)
@Composable
private fun PreviewRiceVarietyFormScreen() {
    RCRCSeedManagerTheme {
        RiceVarietyFormScreen(previewViewModel)
    }
}