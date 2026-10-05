package com.pawknits.demo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pawknits.demo.data.Product

@Composable
fun ProductCard(product: Product, onAdd: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier.testTag("product_card_${product.id}"),
    ) {
        Box(Modifier.fillMaxWidth().height(250.dp)) {
            Image(
                painter = painterResource(product.image),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize().clip(RoundedCornerShape(20.dp)),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.82f))
                    .padding(12.dp),
            ) {
                Text(product.name, fontWeight = FontWeight.Bold, modifier = Modifier.testTag("product_name_${product.id}"))
                Text("$${product.price}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.testTag("product_price_${product.id}"))
            }
            FilledIconButton(
                onClick = onAdd,
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.Black, contentColor = Color.White),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .testTag("add_to_cart_${product.id}"),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add ${product.name} to cart")
            }
        }
    }
}
