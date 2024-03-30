package com.app.makanku.data.repository

import com.app.makanku.data.datasource.product.ProductDataSource
import com.app.makanku.data.model.Product

interface ProductRepository {
    fun getProducts(): List<Product>
}

class ProductRepositoryImpl(private val dataSource: ProductDataSource) : ProductRepository {
    override fun getProducts(): List<Product> = dataSource.getProducts()
}