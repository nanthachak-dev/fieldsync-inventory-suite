package com.example.fieldsync_inventory_app.ui.auth.passcode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.auth.passcode.components.PasscodeTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasscodeScreen(
    viewModel: IPasscodeViewModel = hiltViewModel<PasscodeViewModel>(),
    navController: NavController? = null
) {
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val uiState by viewModel.passcodeUiState.collectAsState()

    LaunchedEffect(resourceUiState.isSuccess) {
        if (resourceUiState.isSuccess) {
            navController?.navigate("dashboard_screen")
        }
    }

    Scaffold(
        containerColor = Color(0xFF3D3C31)
    ) { paddingValues ->
        Column(
            // Main container
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Create Passcode",
                    color = Color(0xFFFFFFFF),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // New passcode
                PasscodeTextField(
                    value = uiState.passcode ?: "",
                    onValueChange = viewModel::onPasscodeChange,
                    label = "New Passcode"
                )

                // Confirm passcode
                PasscodeTextField(
                    value = uiState.confirmPasscode ?: "",
                    onValueChange = viewModel::onConfirmPasscodeChange,
                    label = "Confirm Passcode"
                )

                // Create passcode button
                Button(
                    onClick = {
                        viewModel.onCreatePasscodeClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                ) {
                    Text(
                        text = "Create Passcode",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Error message
                resourceUiState.error?.let { errorMessage ->
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

@Preview(showBackground = true)
@Composable
fun PreviewPassCodeScreen() {
    PasscodeScreen()
}