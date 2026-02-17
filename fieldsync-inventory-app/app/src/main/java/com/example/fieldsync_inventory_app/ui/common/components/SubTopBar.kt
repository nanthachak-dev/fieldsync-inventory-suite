package com.example.fieldsync_inventory_app.ui.common.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubTopBar(title: String="Untitled", navController: NavController? = null){
    TopAppBar(
        // The title of the app bar.
        title = {
            // Use a Box to center the title text.
            // This is a common pattern to override the default title alignment.
                Text(
                    text = title,
                    //textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White
                )
        },
        // The navigation icon on the left side of the app bar.
        navigationIcon = {
            IconButton(
                // This lambda will be called when the back button is clicked.
                // You can add your navigation logic here, e.g.,
                // navController.navigateUp() or findNavController().navigateUp().
                onClick = {
                    navController?.popBackStack()
                }
            ) {
                // The icon for the back button.
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF3D3C31)
        ),
    )
}