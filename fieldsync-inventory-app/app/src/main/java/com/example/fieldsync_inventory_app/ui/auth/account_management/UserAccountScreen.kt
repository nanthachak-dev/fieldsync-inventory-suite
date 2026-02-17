package com.example.fieldsync_inventory_app.ui.auth.account_management

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.auth.account_management.components.ChangePasscodeDialog
import com.example.fieldsync_inventory_app.ui.auth.account_management.components.ChangePasswordDialog
import com.example.fieldsync_inventory_app.ui.auth.passcode.IPasscodeViewModel
import com.example.fieldsync_inventory_app.ui.auth.passcode.PasscodeViewModel
import com.example.fieldsync_inventory_app.ui.auth.passcode.PreviewPasscodeViewModel
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.view_model.user_session.IUserSessionViewModel
import com.example.fieldsync_inventory_app.ui.common.view_model.user_session.PreviewUserSessionViewModel
import com.example.fieldsync_inventory_app.ui.common.view_model.user_session.UserSessionViewModel
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme
import kotlinx.coroutines.launch


@Composable
fun UserAccountScreen(
    viewModel: IUserSessionViewModel = hiltViewModel<UserSessionViewModel>(),
    passcodeViewModel: IPasscodeViewModel = hiltViewModel<PasscodeViewModel>(),
    userAccountViewModel: IUserAccountViewModel = hiltViewModel<UserAccountViewModel>(),
    navController: NavController? = null,
){
    val username by viewModel.username.collectAsState()
    var showChangePasscodeDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    
    val passcodeResourceUiState by passcodeViewModel.resourceUiState.collectAsState()
    val passwordResourceUiState by userAccountViewModel.changePasswordResourceUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(passcodeResourceUiState.isSuccess) {
        if (passcodeResourceUiState.isSuccess && showChangePasscodeDialog) {
            showChangePasscodeDialog = false
            passcodeViewModel.resetPasscodeState()
            scope.launch {
                snackbarHostState.showSnackbar("Passcode changed successfully",
                    duration = SnackbarDuration.Short)
            }
        }
    }

    LaunchedEffect(passwordResourceUiState.isSuccess) {
        if (passwordResourceUiState.isSuccess && showChangePasswordDialog) {
            showChangePasswordDialog = false
            userAccountViewModel.resetChangePasswordState()
            scope.launch {
                snackbarHostState.showSnackbar("Password changed successfully",
                    duration = SnackbarDuration.Short)
            }
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = "User Account Management",
                navController = navController
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Username
            Text(
                text = username,
                color = Color(0xFFB7B03A),
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(64.dp))

            // Change password button
            Button(
                onClick = { showChangePasswordDialog = true },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
            ) {
                Text(
                    text = "Change Password",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(64.dp))
            // Change passcode button
            Button(
                onClick = { showChangePasscodeDialog = true }, 
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
            ) {
                Text(
                    text = "Change Passcode",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    
    if (showChangePasscodeDialog) {
        ChangePasscodeDialog(
            viewModel = passcodeViewModel,
            onDismissRequest = { showChangePasscodeDialog = false }
        )
    }

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            viewModel = userAccountViewModel,
            onDismissRequest = { showChangePasswordDialog = false }
        )
    }
}

// -- Preview --
private val previewViewModel = PreviewUserSessionViewModel()
private val previewPasscodeViewModel = PreviewPasscodeViewModel()
private val previewUserAccountViewModel = PreviewUserAccountViewModel()

@Preview(showBackground = true)
@Composable
private fun PreviewUserAccountScreen() {
    RCRCSeedManagerTheme {
        UserAccountScreen(previewViewModel, previewPasscodeViewModel, previewUserAccountViewModel)
    }
}