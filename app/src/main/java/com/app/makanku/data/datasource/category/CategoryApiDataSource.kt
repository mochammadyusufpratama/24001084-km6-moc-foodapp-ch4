package com.app.makanku.data.datasource.category

import com.app.makanku.data.source.network.model.category.CategoryResponse
import com.app.makanku.data.source.network.services.FoodAppApiService

class CategoryApiDataSource(
    private val service: FoodAppApiService,
) : CategoryDataSource {
    override suspend fun getCategoryData(): CategoryResponse {
        return service.getCategories()
    }
}
