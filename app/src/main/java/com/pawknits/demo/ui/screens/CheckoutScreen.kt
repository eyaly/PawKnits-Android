package com.pawknits.demo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pawknits.demo.data.CartItem
import com.pawknits.demo.data.DemoCards
import com.pawknits.demo.data.PaymentState

/**
 * Demo-only checkout. Nothing entered here leaves the device and no payment provider is called;
 * [onPay] just simulates a gateway round-trip in the ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    items: List<CartItem>,
    total: Int,
    paymentState: PaymentState,
    onPay: (cardNumber: String) -> Unit,
    onBack: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var card by rememberSaveable { mutableStateOf(DemoCards.APPROVED) }
    var expiry by rememberSaveable { mutableStateOf("12/30") }
    var cvv by rememberSaveable { mutableStateOf("123") }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val cardDigits = card.filter { it.isDigit() }
    val nameError = submitted && name.isBlank()
    val addressError = submitted && address.isBlank()
    val cardError = submitted && cardDigits.length != 16
    val expiryError = submitted && !Regex("""^(0[1-9]|1[0-2])/\d{2}$""").matches(expiry)
    val cvvError = submitted && !Regex("""^\d{3,4}$""").matches(cvv)
    val processing = paymentState == PaymentState.Processing

    Scaffold(
        modifier = Modifier.testTag("checkout_screen"),
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !processing, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DemoBanner()

            Text("Order summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            items.forEach {
                Row(Modifier.fillMaxWidth()) {
                    Text("${it.quantity} × ${it.product.name}", modifier = Modifier.weight(1f))
                    Text("$${it.lineTotal}.00")
                }
            }
            Row(Modifier.fillMaxWidth()) {
                Text("Delivery (5–10 days)", modifier = Modifier.weight(1f))
                Text("Free")
            }
            Row(Modifier.fillMaxWidth()) {
                Text("Total", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("$$total.00", fontWeight = FontWeight.Bold, modifier = Modifier.testTag("checkout_total"))
            }

            Spacer(Modifier.height(4.dp))
            Text("Shipping", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Field("Full name", name, { name = it }, "checkout_name", nameError, "Name is required")
            Field("Address", address, { address = it }, "checkout_address", addressError, "Address is required")

            Spacer(Modifier.height(4.dp))
            Text("Payment (demo card)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Field("Card number", card, { card = it }, "checkout_card_number", cardError, "Enter a 16-digit card number", KeyboardType.Number)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Field("MM/YY", expiry, { expiry = it }, "checkout_expiry", expiryError, "Invalid date", KeyboardType.Number, Modifier.weight(1f))
                Field("CVV", cvv, { cvv = it }, "checkout_cvv", cvvError, "Invalid CVV", KeyboardType.NumberPassword, Modifier.weight(1f))
            }

            if (paymentState is PaymentState.Declined) {
                Text(
                    paymentState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("payment_error"),
                )
            }

            Button(
                onClick = {
                    submitted = true
                    val valid = name.isNotBlank() && address.isNotBlank() && cardDigits.length == 16 &&
                        Regex("""^(0[1-9]|1[0-2])/\d{2}$""").matches(expiry) && Regex("""^\d{3,4}$""").matches(cvv)
                    if (valid) onPay(card)
                },
                enabled = !processing,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp).testTag("pay_button"),
            ) {
                if (processing) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp).testTag("payment_progress"))
                    Spacer(Modifier.width(12.dp))
                    Text("Processing…")
                } else {
                    Text("Pay $$total.00 (Demo)", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun DemoBanner() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth().testTag("demo_payment_banner"),
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Info, contentDescription = null)
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Demo payment — no real charge", fontWeight = FontWeight.SemiBold)
                Text(
                    "Approved: ${DemoCards.APPROVED}\nDeclined: ${DemoCards.DECLINED}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun Field(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    tag: String,
    isError: Boolean,
    errorText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        supportingText = if (isError) ({ Text(errorText, modifier = Modifier.testTag("${tag}_error")) }) else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.testTag(tag),
    )
}
