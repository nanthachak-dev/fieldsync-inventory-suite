package com.example.fieldsync_inventory_app.ui.reference.entity_management.user.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fieldsync_inventory_app.domain.model.AppUser

@Composable
fun UserCard(
    user: AppUser? = null,
    onCardClicked: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF10F)),
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = Color.White),
                onClick = onCardClicked
            )
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = (user?.username ?: "User") + (" [id #${user?.id ?: "0"}]"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "Roles: ${user?.roles?.joinToString(", ") ?: "None"}",
                        fontSize = 12.sp,
                        color = Color(0xFF434231),
                        style = TextStyle(lineHeight = 14.sp)
                    )

                    Text(
                        text = "Status: ${if (user?.isActive == true) "Active" else "Inactive"}",
                        fontSize = 12.sp,
                        color = if (user?.isActive == true) Color(0xFF1B5E20) else Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserCard() {
    UserCard(
        user = AppUser(
            id = 1,
            username = "admin",
            roles = listOf("ADMIN", "USER"),
            isActive = true
        )
    )
}
