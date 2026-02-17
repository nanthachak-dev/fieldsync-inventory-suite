package com.example.fieldsync_inventory_app.ui.main.report.stock_report.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@Composable
fun VarietyHeader(varietyName: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        Text(
            text = varietyName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            color = Color.Yellow
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = DividerDefaults.Thickness, color = Color.White.copy(alpha = 0.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVarietyHeader(){
    RCRCSeedManagerTheme {
        VarietyHeader("RX-78-2")
    }
}