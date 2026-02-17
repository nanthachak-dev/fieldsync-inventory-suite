package com.example.fieldsync_inventory_app.ui.common.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.R
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavController?=null, selectedIndex: Int=0) {
    //var selectedItem by remember { mutableIntStateOf(0) }
    var selectedItem = selectedIndex

    NavigationBar(
        containerColor = Color(0xFF7C5B40),
    ) {
        // Home
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 0
                navController?.navigate(Screen.Dashboard.route)
            },
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Home",
                    Modifier.height(24.dp)
                )
            },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color(0xFFAAAAAA),
                unselectedTextColor = Color(0xFFAAAAAA),
                indicatorColor = Color.Transparent
            )
        )

        // Manage Stock
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = {
                selectedItem = 1
                navController?.navigate(Screen.Management.route)
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_package),
                    contentDescription = "Manage Stock",
                    Modifier.height(20.dp)
                )
            },
            label = { Text("Manage Stock") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color(0xFFAAAAAA),
                unselectedTextColor = Color(0xFFAAAAAA),
                indicatorColor = Color.Transparent
            )
        )
        // Inventory
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = {
                selectedItem = 2
                navController?.navigate(Screen.Inventory.route)
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_warehouse),
                    contentDescription = "Inventory",
                    Modifier.height(20.dp)
                )
            },
            label = { Text("Inventory") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color(0xFFAAAAAA),
                unselectedTextColor = Color(0xFFAAAAAA),
                indicatorColor = Color.Transparent
            )
        )
        // History
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = {
                selectedItem = 3
                navController?.navigate(Screen.History.route)
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_history),
                    contentDescription = "History",
                    Modifier.height(20.dp)
                )
            },
            label = { Text("History") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color(0xFFAAAAAA),
                unselectedTextColor = Color(0xFFAAAAAA),
                indicatorColor = Color.Transparent
            )
        )
    }
}

// -- Preview --
@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    BottomNavigationBar()
}