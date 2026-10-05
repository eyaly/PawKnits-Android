package com.pawknits.demo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pawknits.demo.data.CartItem

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
) {
    val id = item.product.id
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("cart_item_$id"),
    ) {
        Image(
            painter = painterResource(item.product.image),
            contentDescription = item.product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(10.dp)),
        )
        Column(Modifier.weight(1f)) {
            Text(item.product.name, fontWeight = FontWeight.Bold)
            Text("$${item.lineTotal}", modifier = Modifier.testTag("cart_item_total_$id"))
        }
        IconButton(onClick = onDecrement, modifier = Modifier.testTag("cart_decrement_$id")) {
            Icon(Icons.Filled.Remove, contentDescription = "Decrease quantity")
        }
        Text(
            "${item.quantity}",
            textAlign = TextAlign.Center,
            modifier = Modifier.width(20.dp).testTag("cart_quantity_$id"),
        )
        IconButton(onClick = onIncrement, modifier = Modifier.testTag("cart_increment_$id")) {
            Icon(Icons.Filled.Add, contentDescription = "Increase quantity")
        }
        IconButton(onClick = onRemove, modifier = Modifier.testTag("cart_remove_$id")) {
            Icon(Icons.Filled.Delete, contentDescription = "Remove ${item.product.name}", tint = MaterialTheme.colorScheme.error)
        }
    }
}
