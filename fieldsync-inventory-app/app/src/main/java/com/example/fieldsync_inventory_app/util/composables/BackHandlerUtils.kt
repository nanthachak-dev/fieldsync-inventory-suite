package com.example.fieldsync_inventory_app.util.composables

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun PressBackToExit() {
    val context = LocalContext.current
    var backPressedTime: Long by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = true) {
        if (System.currentTimeMillis() - backPressedTime <= 2000) {
            // Exit the app
            (context as? Activity)?.finish()
        } else {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
    }
}