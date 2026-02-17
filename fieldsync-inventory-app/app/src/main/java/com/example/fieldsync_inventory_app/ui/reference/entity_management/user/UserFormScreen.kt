package com.example.fieldsync_inventory_app.ui.reference.entity_management.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@Composable
fun UserFormScreen(
    viewModel: IUserViewModel = hiltViewModel<UserViewModel>(),
    navController: NavController? = null
) {
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val uiState by viewModel.userFormUiState.collectAsState()

    LaunchedEffect(resourceUiState.isSuccess) {
        if (resourceUiState.isSuccess) {
            navController?.popBackStack()
        }
    }

    // Check screen first launch
    LaunchedEffect(viewModel.isFirstLaunch) {
        if (viewModel.isFirstLaunch.value) {
            viewModel.onUserFormFirstLaunch()
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = if (uiState.isEditing) "Update User [Id: #${viewModel.selectedUser?.id}]" else "Add New User",
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

            item {
                TextInputSimple(
                    label = "Username",
                    value = uiState.username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    textInputModifier,
                )
            }

            item {
                TextInputSimple(
                    label = if (uiState.isEditing) "Password (leave blank to keep current)" else "Password",
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    modifier = textInputModifier,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                Text(
                    text = "Account Status",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (uiState.isActive) "Active" else "Inactive",
                        color = if (uiState.isActive) Color.Green else Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Switch(
                        checked = uiState.isActive,
                        onCheckedChange = { viewModel.onIsActiveChange(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF7C5B40),
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.DarkGray,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }
            item {
                Text(
                    text = "Roles",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(uiState.availableRoles.size) { index ->
                val role = uiState.availableRoles[index]
                val isChecked = uiState.roles.contains(role.name)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { viewModel.onRoleToggle(role.name) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { viewModel.onRoleToggle(role.name) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF7C5B40),
                            uncheckedColor = Color.White,
                            checkmarkColor = Color.White
                        )
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = role.name,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        role.description?.let {
                            Text(
                                text = it,
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

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
                        text = if (uiState.isEditing) "Update User" else "Add New User",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

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
                resourceUiState.loadingMessage?.let {
                    Text(
                        text = it,
                        Modifier.padding(horizontal = 20.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}

// -- Preview --
private val previewViewModel = PreviewUserFormViewModel()

@Preview(showBackground = true)
@Composable
private fun PreviewUserFormScreen() {
    RCRCSeedManagerTheme {
        UserFormScreen(previewViewModel)
    }
}