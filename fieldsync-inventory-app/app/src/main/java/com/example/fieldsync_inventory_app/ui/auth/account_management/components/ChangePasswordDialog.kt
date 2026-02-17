package com.example.fieldsync_inventory_app.ui.auth.account_management.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.fieldsync_inventory_app.ui.auth.account_management.IUserAccountViewModel
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple

@Composable
fun ChangePasswordDialog(
    viewModel: IUserAccountViewModel,
    onDismissRequest: () -> Unit
) {
    val uiState by viewModel.changePasswordUiState.collectAsState()
    val resourceUiState by viewModel.changePasswordResourceUiState.collectAsState()

    Dialog(
        onDismissRequest = {
            onDismissRequest()
            viewModel.resetChangePasswordState()
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
                    text = "Change Password",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                TextInputSimple(
                    label = "Current Password",
                    value = uiState.currentPassword,
                    onValueChange = viewModel::onCurrentPasswordChange,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                TextInputSimple(
                    label = "New Password",
                    value = uiState.newPassword,
                    onValueChange = viewModel::onNewPasswordChange,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                TextInputSimple(
                    label = "Confirm New Password",
                    value = uiState.confirmationPassword,
                    onValueChange = viewModel::onConfirmationPasswordChange,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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
                    onClick = { viewModel.onChangePasswordClicked() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40)),
                    enabled = !resourceUiState.isLoading
                ) {
                    if (resourceUiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(24.dp).height(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Change Password",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        onDismissRequest()
                        viewModel.resetChangePasswordState()
                    }
                ) {
                    Text(text = "Cancel", color = Color.White)
                }
            }
        }
    }
}
