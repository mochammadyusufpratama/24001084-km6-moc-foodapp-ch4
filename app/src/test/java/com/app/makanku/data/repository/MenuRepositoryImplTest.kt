package com.app.makanku.data.repository

import app.cash.turbine.test
import com.app.makanku.data.datasource.menu.MenuDataSource
import com.app.makanku.data.model.Cart
import com.app.makanku.data.model.User
import com.app.makanku.data.source.network.model.checkout.CheckoutResponse
import com.app.makanku.data.source.network.model.menu.MenuItemResponse
import com.app.makanku.data.source.network.model.menu.MenuResponse
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

class MenuRepositoryImplTest {
    @MockK
    lateinit var datasource: MenuDataSource

    @MockK
    lateinit var userRepository: UserRepository
    private lateinit var menuRepository: MenuRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        menuRepository = MenuRepositoryImpl(datasource, userRepository)
    }

    @Test
    fun getMenu() {
        val menu1 =
            MenuItemResponse(
                imgUrl = "exampleImgUrl",
                name = "Seblak",
                formattedPrice = "Rp10.000",
                price = 10000.0,
                menuDesc = "Cemilan Kerupuk Seblak dikasih Kuah",
                restoAddress = "kono adoh numpak ngeng...",
            )
        val menu2 =
            MenuItemResponse(
                imgUrl = "exampleImgUrl1",
                name = "Sego Koceng",
                formattedPrice = "Rp100.000",
                price = 100000.0,
                menuDesc = "Sego + BSH",
                restoAddress = "tsarkeum",
            )

        val mockListMenu = listOf(menu1, menu2)
        val mockResponse = mockk<MenuResponse>()
        every { mockResponse.data } returns mockListMenu
        runTest {
            coEvery { datasource.getMenuData(any()) } returns mockResponse
            menuRepository.getMenu("makanan").map {
                delay(100)
                it
            }.test {
                delay(2210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { datasource.getMenuData(any()) }
            }
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val cart1 = Cart(1, "idgaf", "Seblak", "imgUrl", 10000.0, 1, "Gipikik Kiripik Siblik")
            val cart2 = Cart(2, "idgaf", "Sego Koceng", "imgUrl", 100000.0, 2, "Warna Biru Kocengnya")
            val items = listOf(cart1, cart2)

            val currentUser = User(id = "1", fullName = "Cak Doe", email = "myp@example.com", photoUrl = "info link")
            coEvery { userRepository.getCurrentUser() } returns currentUser

            coEvery { datasource.createOrder(any()) } returns
                CheckoutResponse(
                    status = true,
                    message = "success",
                    code = 200,
                )

            menuRepository.createOrder(items).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualResult = expectMostRecentItem()
                assertTrue(actualResult is ResultWrapper.Success)
            }
            coVerify { userRepository.getCurrentUser() }
            coVerify { datasource.createOrder(any()) }
        }
    }

}