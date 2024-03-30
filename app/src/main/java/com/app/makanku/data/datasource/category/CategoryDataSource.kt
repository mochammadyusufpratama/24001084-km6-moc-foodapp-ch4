package com.app.makanku.data.datasource.category

import com.app.makanku.data.model.Category

interface CategoryDataSource {
    fun getCategories(): List<Category>
}