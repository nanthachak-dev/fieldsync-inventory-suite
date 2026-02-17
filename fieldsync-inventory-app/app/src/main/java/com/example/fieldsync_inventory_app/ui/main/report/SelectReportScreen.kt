package com.example.fieldsync_inventory_app.ui.main.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen
import com.example.fieldsync_inventory_app.ui.main.report.components.ReportButton
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@Composable
fun SelectReportScreen(
    navController: NavController? = null
) {
    Scaffold(
        topBar = {
            SubTopBar("Select Report", navController)
        },
        containerColor = Color(0xFF3D3C31)
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            val buttonModifier = Modifier.padding(16.dp)

            // Stock Report button
            ReportButton(
                buttonModifier,
                mainText = "Stock Report",
                onClick = {navController?.navigate(Screen.StockReport.route)}
            )

            // Sale Report button
            ReportButton(
                buttonModifier,
                mainText = "Sale Report",
                onClick = {navController?.navigate(Screen.SaleReport.route)}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectReportScreen() {
    RCRCSeedManagerTheme {
        SelectReportScreen()
    }
}