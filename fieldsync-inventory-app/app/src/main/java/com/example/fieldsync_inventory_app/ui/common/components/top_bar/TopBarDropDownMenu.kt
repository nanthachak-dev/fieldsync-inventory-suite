package com.example.fieldsync_inventory_app.ui.common.components.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.app.Activity
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen

@Composable
fun TopBarDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    navController: NavController?,
    username: String,
    onLogout: () -> Unit
) {
    var showExitDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.background(Color(0xFF3D3C31))
    ) {
        val itemColors = MenuDefaults.itemColors(
            textColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White
        )
        val dividerColor = Color(0xFFB7B03A).copy(alpha = 0.5f)
        // Username
        DropdownMenuItem(
            text = { 
                Text(
                    text = username,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ) 
            },
            onClick = {
                onDismissRequest()
            },
            colors = itemColors
        )

        HorizontalDivider(color = dividerColor)

        // Manage Account
        DropdownMenuItem(
            text = { Text("Manage Account") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Manage Account"
                )
            },
            onClick = {
                navController?.navigate(Screen.UserAccount.route)
                onDismissRequest()
            },
            colors = itemColors
        )

        // Sync Data
        DropdownMenuItem(
            text = { Text("Sync Data") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Sync Data"
                )
            },
            onClick = {
                navController?.navigate("sync_database_screen")
                onDismissRequest()
            },
            colors = itemColors
        )

        // Manage Entity
        DropdownMenuItem(
            text = { Text("Manage Entity") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Manage Entity"
                )
            },
            onClick = {
                navController?.navigate(Screen.ManageEntity.route)
                onDismissRequest()
            },
            colors = itemColors
        )

        HorizontalDivider(color = dividerColor)

        // Logout
        DropdownMenuItem(
            text = { Text("Logout") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout"
                )
            },
            onClick = {
                showLogoutDialog = true
                onDismissRequest()
            },
            colors = itemColors
        )

        // Exit App
        DropdownMenuItem(
            text = { Text("Exit App") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Exit App"
                )
            },
            onClick = {
                showExitDialog = true
                onDismissRequest()
            },
            colors = itemColors
        )
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit App") },
            text = { Text("Are you sure you want to exit?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        (context as? Activity)?.finish()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }
}
