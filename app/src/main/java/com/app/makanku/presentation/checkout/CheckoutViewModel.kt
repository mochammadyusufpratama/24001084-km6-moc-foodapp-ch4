package com.app.makanku.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.makanku.data.repository.CartRepository
import com.app.makanku.data.repository.MenuRepository
import com.app.makanku.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val menuRepository: MenuRepository,
) : ViewModel() {
    val checkoutData = cartRepository.getCheckoutData().asLiveData(Dispatchers.IO)

    fun deleteAllCart() {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteAllCart().collect()
        }
    }

    fun checkoutCart() =
        menuRepository.createOrder(
            checkoutData.value?.payload?.first.orEmpty(),
        ).asLiveData(Dispatchers.IO)
}
