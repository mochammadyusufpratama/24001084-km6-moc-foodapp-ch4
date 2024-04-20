package com.app.makanku.presentation.home

import androidx.lifecycle.ViewModel
import com.app.makanku.data.repository.CategoryRepository
import com.app.makanku.data.repository.ProductRepository
import com.app.makanku.data.repository.UserRepository

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
): ViewModel() {

    fun getProducts() = productRepository.getProducts()
    fun getCategories() = categoryRepository.getCategories()

    fun getCurrentUser() = userRepository.getCurrentUser()

    fun userIsLoggedIn() = userRepository.isLoggedIn()

}