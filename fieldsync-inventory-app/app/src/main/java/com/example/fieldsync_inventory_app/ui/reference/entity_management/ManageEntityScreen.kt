package com.example.fieldsync_inventory_app.ui.reference.entity_management

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.components.EntityButton
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@Composable
fun ManageEntityScreen(
    navController: NavController? = null
) {
    Scaffold(
        topBar = {
            SubTopBar("Manage Entity", navController)
        },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            val buttonModifier = Modifier.padding(16.dp)

            // Variety button
            item {
                EntityButton(
                    buttonModifier,
                    mainText = "Variety",
                    onClick = {
                        navController?.navigate(Screen.RiceVariety.route)
                    }
                )
            }

            // Customer Type button
            item {
                EntityButton(
                    buttonModifier,
                    mainText = "Customer Type",
                    onClick = {
                        navController?.navigate(Screen.CustomerType.route)
                    }
                )
            }

            // Customer button
            item {
                EntityButton(
                    buttonModifier,
                    mainText = "Customer",
                    onClick = {
                        navController?.navigate(Screen.Customer.route)
                    }
                )
            }

            // Supplier Type button
            item {
                EntityButton(
                    buttonModifier,
                    mainText = "Supplier Type",
                    onClick = {
                        navController?.navigate(Screen.SupplierType.route)
                    }
                )
            }

            // Supplier button
            item {
                EntityButton(
                    buttonModifier,
                    mainText = "Supplier",
                    onClick = {
                        navController?.navigate(Screen.Supplier.route)
                    }
                )
            }

            // User button
            item {
                EntityButton(
                    buttonModifier,
                    mainText = "App User",
                    onClick = {
                        navController?.navigate(Screen.User.route)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewManageEntity() {
    RCRCSeedManagerTheme {
        ManageEntityScreen()
    }
}