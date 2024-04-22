package com.app.makanku.data.datasource.category

import com.app.makanku.data.model.Category
import com.app.makanku.data.source.network.model.category.CategoryResponse

interface CategoryDataSource {
    suspend  fun getCategoryData() : CategoryResponse
}