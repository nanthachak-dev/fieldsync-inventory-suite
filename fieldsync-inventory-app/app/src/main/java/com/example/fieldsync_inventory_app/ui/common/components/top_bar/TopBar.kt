package com.example.fieldsync_inventory_app.ui.common.components.top_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen
import com.example.fieldsync_inventory_app.ui.common.view_model.user_session.IUserSessionViewModel
import com.example.fieldsync_inventory_app.ui.common.view_model.user_session.PreviewUserSessionViewModel
import com.example.fieldsync_inventory_app.ui.common.view_model.user_session.UserSessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    screenTitle: String?,
    navController: NavController? = null,
    viewModel: IUserSessionViewModel = hiltViewModel<UserSessionViewModel>()
) {
    var showMenu by remember { mutableStateOf(false) }
    val username by viewModel.username.collectAsState()

    TopAppBar(
        title = {
            if (screenTitle != null) {
                Text(
                    screenTitle,
                    color = Color(0xFFB7B03A),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF3D3C31)
        ),
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = Color(0xFFB7B03A)
                )
            }

            TopBarDropDownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                navController = navController,
                username = username,
                onLogout = {
                    viewModel.logout {
                        navController?.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
                }
            )
        }
    )
}

// -- Preview --
val previewUserSessionViewModel = PreviewUserSessionViewModel()

@Preview(showBackground = true)
@Composable
fun PreviewTopBar(){
    TopBar(screenTitle = "Example Screen Title", viewModel = previewUserSessionViewModel)
}