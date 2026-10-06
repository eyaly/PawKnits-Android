package com.pawknits.demo.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class CartItem(val product: Product, val quantity: Int) {
    val lineTotal: Int get() = product.price * quantity
}

sealed interface PaymentState {
    data object Idle : PaymentState
    data object Processing : PaymentState
    data class Success(val orderNumber: String, val amount: Int) : PaymentState
    data class Declined(val message: String) : PaymentState
}

/** Demo card numbers. Anything else that passes basic validation is approved. */
object DemoCards {
    const val APPROVED = "4242 4242 4242 4242"
    const val DECLINED = "4000 0000 0000 0002"
}

class ShopViewModel : ViewModel() {

    var loggedInUser by mutableStateOf<String?>(null)
        private set

    private val _cart = mutableStateListOf<CartItem>()
    val cart: List<CartItem> get() = _cart

    val itemCount: Int get() = _cart.sumOf { it.quantity }
    val total: Int get() = _cart.sumOf { it.lineTotal }

    var paymentState by mutableStateOf<PaymentState>(PaymentState.Idle)
        private set

    fun login(username: String, password: String): LoginResult {
        val result = authenticate(username, password)
        if (result is LoginResult.Success) loggedInUser = username.trim().lowercase()
        // Demo network traffic only; login never waits for or depends on it.
        viewModelScope.launch { DemoApi.reportLogin(username.trim().lowercase(), result.httpStatus) }
        return result
    }

    fun logout() {
        loggedInUser = null
        _cart.clear()
        paymentState = PaymentState.Idle
    }

    fun addToCart(product: Product) {
        val index = _cart.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            _cart[index] = _cart[index].copy(quantity = _cart[index].quantity + 1)
        } else {
            _cart.add(CartItem(product, 1))
        }
    }

    fun decrement(product: Product) {
        val index = _cart.indexOfFirst { it.product.id == product.id }
        if (index < 0) return
        val item = _cart[index]
        if (item.quantity > 1) _cart[index] = item.copy(quantity = item.quantity - 1) else _cart.removeAt(index)
    }

    fun removeFromCart(product: Product) {
        _cart.removeAll { it.product.id == product.id }
    }

    /** Simulates a payment gateway round-trip. No real payment is processed. */
    fun pay(cardNumber: String) {
        if (paymentState == PaymentState.Processing) return
        paymentState = PaymentState.Processing
        val amount = total
        viewModelScope.launch {
            delay(1500)
            paymentState = if (cardNumber.filter { it.isDigit() } == DemoCards.DECLINED.filter { it.isDigit() }) {
                PaymentState.Declined("Your card was declined. Try the demo card ${DemoCards.APPROVED}.")
            } else {
                _cart.clear()
                PaymentState.Success(
                    orderNumber = "PK-" + (100000..999999).random(),
                    amount = amount,
                )
            }
        }
    }

    fun resetPayment() {
        paymentState = PaymentState.Idle
    }
}
