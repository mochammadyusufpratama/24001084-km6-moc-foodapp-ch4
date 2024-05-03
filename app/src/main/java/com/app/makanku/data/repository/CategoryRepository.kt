package com.app.makanku.data.repository

import com.app.makanku.data.datasource.category.CategoryDataSource
import com.app.makanku.data.mapper.toCategories
import com.app.makanku.data.model.Category
import com.app.makanku.utils.ResultWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CategoryRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
}

class CategoryRepositoryImpl(private val dataSource: CategoryDataSource) : CategoryRepository {
    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return flow {
            emit(ResultWrapper.Loading())
            delay(1000)
            val categoryData = dataSource.getCategoryData().data.toCategories()
            emit(ResultWrapper.Success(categoryData))
        }
    }
}
