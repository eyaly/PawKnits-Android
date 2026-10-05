package com.pawknits.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pawknits.demo.data.LoginResult
import com.pawknits.demo.data.PaymentState
import com.pawknits.demo.data.ShopViewModel
import com.pawknits.demo.ui.screens.CartScreen
import com.pawknits.demo.ui.screens.CheckoutScreen
import com.pawknits.demo.ui.screens.LoginScreen
import com.pawknits.demo.ui.screens.OrderCompleteScreen
import com.pawknits.demo.ui.screens.ShopScreen

private object Routes {
    const val LOGIN = "login"
    const val SHOP = "shop"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
    const val COMPLETE = "complete"
}

@Composable
fun PawKnitsApp(vm: ShopViewModel = viewModel()) {
    val nav = rememberNavController()

    fun backToShop() {
        vm.resetPayment()
        nav.popBackStack(Routes.SHOP, inclusive = false)
    }

    NavHost(navController = nav, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(onLogin = { user, pass ->
                vm.login(user, pass).also {
                    if (it is LoginResult.Success) {
                        nav.navigate(Routes.SHOP) { popUpTo(Routes.LOGIN) { inclusive = true } }
                    }
                }
            })
        }
        composable(Routes.SHOP) {
            ShopScreen(
                cartCount = vm.itemCount,
                onAddToCart = vm::addToCart,
                onOpenCart = { nav.navigate(Routes.CART) },
                onLogout = {
                    vm.logout()
                    nav.navigate(Routes.LOGIN) { popUpTo(Routes.SHOP) { inclusive = true } }
                },
            )
        }
        composable(Routes.CART) {
            CartScreen(
                items = vm.cart,
                total = vm.total,
                onIncrement = vm::addToCart,
                onDecrement = vm::decrement,
                onRemove = vm::removeFromCart,
                onCheckout = {
                    vm.resetPayment()
                    nav.navigate(Routes.CHECKOUT)
                },
                onBack = { nav.popBackStack() },
            )
        }
        composable(Routes.CHECKOUT) {
            val state = vm.paymentState
            LaunchedEffect(state) {
                if (state is PaymentState.Success) {
                    nav.navigate(Routes.COMPLETE) { popUpTo(Routes.SHOP) }
                }
            }
            CheckoutScreen(
                items = vm.cart,
                total = vm.total,
                paymentState = state,
                onPay = vm::pay,
                onBack = { nav.popBackStack() },
            )
        }
        composable(Routes.COMPLETE) {
            val success = vm.paymentState as? PaymentState.Success
            OrderCompleteScreen(
                orderNumber = success?.orderNumber ?: "",
                amount = success?.amount ?: 0,
                onContinue = ::backToShop,
            )
        }
    }
}
