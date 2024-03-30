package com.app.makanku.data.repository

import com.app.makanku.data.datasource.category.CategoryDataSource
import com.app.makanku.data.model.Category

interface CategoryRepository {
    fun getCategories(): List<Category>
}

class CategoryRepositoryImpl(private val dataSource: CategoryDataSource) : CategoryRepository {
    override fun getCategories(): List<Category> = dataSource.getCategories()
}