package com.app.makanku.data.datasource.menu

import com.app.makanku.data.source.network.model.checkout.CheckoutRequestPayload
import com.app.makanku.data.source.network.model.checkout.CheckoutResponse
import com.app.makanku.data.source.network.model.checkout.CheckoutResponsePayload
import com.app.makanku.data.source.network.model.menu.MenuResponse
import com.app.makanku.data.source.network.services.FoodAppApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MenuApiDataSourceTest {
    @MockK
    lateinit var service: FoodAppApiService
    private lateinit var dataSource: MenuDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = MenuApiDataSource(service)
    }

    @Test
    fun getMenuData() {
        runTest {
            val mockResponse = mockk<MenuResponse>(relaxed = true)
            coEvery { service.getMenu() } returns mockResponse
            val actualResult = dataSource.getMenuData()
            coVerify { service.getMenu() }
            assertEquals(mockResponse, actualResult)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val order1 =
                CheckoutResponsePayload(
                    name = "Seblak",
                    notes = "Gapakek Seblak",
                    price = 2000,
                    qty = 5,
                )
            val order2 =
                CheckoutResponsePayload(
                    name = "Nasi Bubur",
                    notes = "Nasi Gapakek Bubur",
                    price = 1000,
                    qty = 100,
                )
            val ordersList = listOf(order1, order2)
            val total = 110000
            val username = "CUPPP"
            val requestPayload =
                CheckoutRequestPayload(
                    orders = ordersList,
                    total = total,
                    username = username,
                )
            val mockResponse = mockk<CheckoutResponse>(relaxed = true)
            coEvery { service.createOrder(requestPayload) } returns mockResponse
            val actualResult = dataSource.createOrder(requestPayload)
            coVerify { service.createOrder(requestPayload) }
            assertEquals(mockResponse, actualResult)
        }
    }

}