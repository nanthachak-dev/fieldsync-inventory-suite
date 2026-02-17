package com.example.fieldsync_inventory_app.ui.auth.login

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fieldsync_inventory_app.R
import com.example.fieldsync_inventory_app.ui.auth.login.components.PasswordTextField
import com.example.fieldsync_inventory_app.ui.auth.login.components.UserTextField
import com.example.fieldsync_inventory_app.ui.auth.passcode.components.PasscodeTextField
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: ILoginViewModel = hiltViewModel<LoginViewModel>(),
    onLoginSuccess: () -> Unit
) {
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val uiState by viewModel.loginUiState.collectAsState()
    val hasPasscode by viewModel.hasPasscode.collectAsState()
    val isTokenUsable by viewModel.isTokenUsable.collectAsState()

    // Side effect to trigger navigation when login is successful
    LaunchedEffect(key1 = resourceUiState.isSuccess) {
        if (resourceUiState.isSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF3D3C31)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "RCRC Seed Manager Logo",
            modifier = Modifier
                .size(240.dp)
                .padding(bottom = 32.dp)
        )

        // App name
        Text(
            text = "RCRC Seed Manager",
            color = Color(0xFFB7B03A),
            fontSize = 28.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (hasPasscode && isTokenUsable) { // Passcode login
            PasscodeTextField(
                value = uiState.passcode ?: "",
                onValueChange = viewModel::onPasscodeChange,
                label = "Passcode"
            )
        }else{ // Credential login
            UserTextField(
                value = uiState.username ?: "",
                onValueChange = viewModel::onUsernameChange,
                label = "Username"
            )

            PasswordTextField(
                value = uiState.password ?: "",
                onValueChange = viewModel::onPasswordChange,
                label = "Password"
            )
        }

        Button(
            onClick = viewModel::onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C5B40))
        ) {
            Text(
                text = "Login",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Show error message
        resourceUiState.error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )

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
val previewViewModel = PreviewLoginViewModel()

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    RCRCSeedManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF4A443A)
        ) {
            LoginScreen(viewModel = previewViewModel, onLoginSuccess = {})
        }
    }
}