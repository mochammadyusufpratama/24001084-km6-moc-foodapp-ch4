package com.app.makanku.presentation.home

import androidx.lifecycle.ViewModel
import com.app.makanku.data.repository.CategoryRepository
import com.app.makanku.data.repository.ProductRepository

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
): ViewModel() {

    fun getProducts() = productRepository.getProducts()
    fun getCategories() = categoryRepository.getCategories()

}