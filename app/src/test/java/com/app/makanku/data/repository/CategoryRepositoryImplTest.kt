package com.app.makanku.data.repository

import app.cash.turbine.test
import com.app.makanku.data.datasource.category.CategoryDataSource
import com.app.makanku.data.source.network.model.category.CategoryItemResponse
import com.app.makanku.data.source.network.model.category.CategoryResponse
import com.app.makanku.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CategoryRepositoryImplTest {
    @MockK
    lateinit var dataSource: CategoryDataSource
    private lateinit var repository: CategoryRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CategoryRepositoryImpl(dataSource)
    }

    @Test
    fun getCategories() {
        val category1 =
            CategoryItemResponse(
                imgUrl = "exampleImageUrl",
                name = "Makanan",
            )
        val category2 =
            CategoryItemResponse(
                imgUrl = "exampleImageUrl2",
                name = "Minuman",
            )
        val mockListCategory = listOf(category1, category2)
        val mockResponse = mockk<CategoryResponse>()
        every { mockResponse.data } returns mockListCategory
        runTest {
            coEvery { dataSource.getCategoryData() } returns mockResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { dataSource.getCategoryData() }
            }
        }
    }

}