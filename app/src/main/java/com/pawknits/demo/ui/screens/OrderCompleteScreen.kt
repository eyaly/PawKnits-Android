package com.pawknits.demo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pawknits.demo.ui.theme.Moss

@Composable
fun OrderCompleteScreen(orderNumber: String, amount: Int, onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp)
            .testTag("order_complete_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Filled.CheckCircle, contentDescription = "Payment successful", tint = Moss, modifier = Modifier.size(96.dp))
        Spacer(Modifier.height(16.dp))
        Text("Payment successful!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.testTag("order_complete_title"))
        Spacer(Modifier.height(8.dp))
        Text("Order $orderNumber", style = MaterialTheme.typography.titleMedium, modifier = Modifier.testTag("order_number"))
        Text("Charged $$amount.00 (demo)", modifier = Modifier.testTag("order_amount"))
        Spacer(Modifier.height(16.dp))
        Text(
            "Thanks for your purchase! Your pup will get cozy in our comfy sweaters soon.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onContinue,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("continue_shopping_button"),
        ) {
            Text("Continue shopping")
        }
    }
}
