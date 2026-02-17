package com.example.fieldsync_inventory_app.ui.auth.account_management.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.fieldsync_inventory_app.ui.auth.passcode.IPasscodeViewModel
import com.example.fieldsync_inventory_app.ui.auth.passcode.components.PasscodeTextField

@Composable
fun ChangePasscodeDialog(
    viewModel: IPasscodeViewModel,
    onDismissRequest: () -> Unit
) {
    val uiState by viewModel.passcodeUiState.collectAsState()
    val resourceUiState by viewModel.resourceUiState.collectAsState()

    Dialog(
        onDismissRequest = {
            onDismissRequest()
            viewModel.resetPasscodeState()
        }
    ) {
        Surface(
            modifier = Modifier
                .width(350.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF3D3C31)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Change Passcode",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                PasscodeTextField(
                    value = uiState.currentPasscode ?: "",
                    onValueChange = viewModel::onCurrentPasscodeChange,
                    label = "Current Passcode"
                )

                PasscodeTextField(
                    value = uiState.passcode ?: "",
                    onValueChange = viewModel::onPasscodeChange,
                    label = "New Passcode"
                )

                PasscodeTextField(
                    value = uiState.confirmPasscode ?: "",
                    onValueChange = viewModel::onConfirmPasscodeChange,
                    label = "Confirm Passcode"
                )

                resourceUiState.error?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Button(
                    onClick = { viewModel.onChangePasscodeClicked() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
                ) {
                    Text(
                        text = "Change Passcode",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        onDismissRequest()
                        viewModel.resetPasscodeState()
                    }
                ) {
                    Text(text = "Cancel", color = Color.White)
                }
            }
        }
    }
}
