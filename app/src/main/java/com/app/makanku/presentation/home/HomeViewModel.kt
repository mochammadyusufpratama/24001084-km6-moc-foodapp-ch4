package com.app.makanku.presentation.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.makanku.data.model.Menu
import com.app.makanku.data.repository.CartRepository
import com.app.makanku.data.repository.CategoryRepository
import com.app.makanku.data.repository.MenuRepository
import com.app.makanku.data.repository.UserPrefRepository
import com.app.makanku.data.repository.UserRepository
import com.app.makanku.utils.proceedWhen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val menuRepository: MenuRepository,
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val userPrefRepository: UserPrefRepository,
) : ViewModel() {
    val menuCountLiveData =
        MutableLiveData(0).apply {
            postValue(0)
        }

    fun getMenu(categoryName: String? = null) = menuRepository.getMenu(categoryName).asLiveData(Dispatchers.IO)

    fun addItemToCart(menu: Menu) {
        menuCountLiveData.value = 1

        viewModelScope.launch {
            cartRepository.createCart(menu, 1).collect {
                it.proceedWhen(
                    doOnSuccess = {
                        Log.d(TAG, "addItemToCart: Success!")
                    },
                    doOnError = {
                        Log.d(TAG, "addItemToCart: Failed!")
                    },
                    doOnEmpty = {
                        Log.d(TAG, "addItemToCart: Empty!")
                    },
                )
            }
        }
    }

    fun getCategories() = categoryRepository.getCategories().asLiveData(Dispatchers.IO)

    fun getCurrentUser() = userRepository.getCurrentUser()

    fun userIsLoggedIn() = userRepository.isLoggedIn()

    fun isUsingGridMode() = userPrefRepository.isUsingGridMode()

    fun setUsingGridMode(isUsingGridMode: Boolean) = userPrefRepository.setUsingGridMode(isUsingGridMode)
}
