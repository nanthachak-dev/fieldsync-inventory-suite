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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.components.UserCard
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@Composable
fun UserScreen(
    viewModel: IUserViewModel = hiltViewModel<UserViewModel>(),
    navController: NavController? = null
) {
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val userUiState by viewModel.userUiState.collectAsState()
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()
    val users = userUiState.users

    var searchQuery by remember { mutableStateOf("") }

    val filteredList = remember(users, searchQuery, userUiState.hideInactive) {
        users.filter { user ->
            val matchesSearch = user.username.contains(searchQuery, ignoreCase = true)
            val matchesActiveStatus = if (userUiState.hideInactive) user.isActive else true
            matchesSearch && matchesActiveStatus
        }
    }

    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.firstLaunch()
        }
    }

    Scaffold(
        topBar = {
            SubTopBar("User Management", navController)
        },
        containerColor = Color(0xFF3D3C31),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onAddNewUserClick()
                    navController?.navigate(Screen.UserForm.route)
                },
                containerColor = Color(0xFF7C5B40),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add User")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val textInputModifier = Modifier
                .width(200.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextInputSimple(
                    label = "Search User",
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    textInputModifier,
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Hide Inactive:",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Switch(
                    checked = userUiState.hideInactive,
                    onCheckedChange = { viewModel.onHideInactiveChange(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF7C5B40),
                        uncheckedThumbColor = Color.LightGray,
                        uncheckedTrackColor = Color.DarkGray,
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredList, key = { it.id }) { user ->
                    UserCard(
                        user = user,
                        onCardClicked = {
                            viewModel.onEditUserItemClick(user)
                            navController?.navigate(Screen.UserForm.route)
                        }
                    )
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
private val previewViewModel = PreviewUserViewModel()

@Preview(showBackground = true)
@Composable
private fun PreviewUserScreen() {
    RCRCSeedManagerTheme {
        UserScreen(previewViewModel)
    }
}