package com.app.makanku.data.datasource.product

import com.app.makanku.data.model.Product

interface ProductDataSource {
    fun getProducts(): List<Product>
}