package com.app.makanku.data.repository

import app.cash.turbine.test
import com.app.makanku.data.datasource.cart.CartDataSource
import com.app.makanku.data.model.Cart
import com.app.makanku.data.model.Menu
import com.app.makanku.data.model.PriceItem
import com.app.makanku.data.source.local.database.entity.CartEntity
import com.app.makanku.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CartRepositoryImplTest {
    @MockK
    lateinit var datasource: CartDataSource
    private lateinit var repository: CartRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CartRepositoryImpl(datasource)
    }

    @Test
    fun insertCart() {
        val mockProduct = mockk<Menu>(relaxed = true)
        every { mockProduct.id } returns "666"
        coEvery { datasource.insertCart(any()) } returns 1
        runTest {
            repository.createCart(mockProduct, 3, "Siblik Tipi Gipikik Kiripik Siblik")
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2201)
                    val actualData = expectMostRecentItem()
                    assertTrue(actualData is ResultWrapper.Success)
                    assertEquals(true, actualData.payload)
                    coVerify { datasource.insertCart(any()) }
                }
        }
    }

    @Test
    fun decreaseCart() {
        val mockCart =
            Cart(
                id = 1,
                menuId = "idgaf",
                menuName = "Sego Komando",
                menuImgUrl = "exampleImgUrl1",
                menuPrice = 0.0,
                itemQuantity = 2,
                itemNotes = "Sayur Tok Ojok Karo Ndog Mentah...",
            )

        coEvery { datasource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 0) { datasource.deleteCart(any()) }
                coVerify(atLeast = 1) { datasource.updateCart(any()) }
            }
        }
    }

    @Test
    fun increaseCart() {
        val mockCart =
            Cart(
                id = 1,
                menuId = "idgaf",
                menuName = "Sego Komando",
                menuImgUrl = "exampleImgUrl1",
                menuPrice = 0.0,
                itemQuantity = 1,
                itemNotes = "Sayur Tok Ojok Karo Ndog Mentah...",
            )
        coEvery { datasource.updateCart(any()) } returns 1
        runTest {
            repository.increaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 1) { datasource.updateCart(any()) }
            }
        }
    }

    @Test
    fun setCartNotes() {
        val mockCart =
            Cart(
                id = 1,
                menuId = "idgaf",
                menuName = "Sego Komando",
                menuImgUrl = "exampleImgUrl1",
                menuPrice = 0.0,
                itemQuantity = 1,
                itemNotes = "Sayur Tok Ojok Karo Ndog Mentah...",
            )
        coEvery { datasource.updateCart(any()) } returns 1
        runTest {
            repository.setCartNotes(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify { datasource.updateCart(any()) }
            }
        }
    }

    @Test
    fun deleteCart() {
        val mockCart =
            Cart(
                id = 1,
                menuId = "idgaf",
                menuName = "Sego Komando",
                menuImgUrl = "exampleImgUrl1",
                menuPrice = 0.0,
                itemQuantity = 1,
                itemNotes = "Wes, Ndak Mampu Aku...",
            )
        coEvery { datasource.deleteCart(any()) } returns 1
        runTest {
            val result =
                repository.deleteCart(mockCart).map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val actualResult = expectMostRecentItem()
                    assertTrue(actualResult is ResultWrapper.Success)
                }
            coVerify { datasource.deleteCart(any()) }
        }
    }

    @Test
    fun deleteAllCarts() {
        coEvery { datasource.deleteAll() } returns Unit
        runTest {
            val result =
                repository.deleteAllCart().map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val actualResult = expectMostRecentItem()
                    assertTrue(actualResult is ResultWrapper.Success)
                }
            coVerify { datasource.deleteAll() }
        }
    }

    @Test
    fun getUserCartData() {
        val entity1 =
            CartEntity(
                id = 1,
                menuImgUrl = "ExampleImgUrl1",
                menuName = "Sego Komando",
                menuPrice = 0.0,
                itemQuantity = 1,
                itemNotes = "Wes, Ndak Mampu Aku...",
            )
        val entity2 =
            CartEntity(
                id = 2,
                menuImgUrl = "ExampleImgUrl2",
                menuName = "Pecel",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = "Pecel itu Pakek Sambel Kacang",
            )
        val mockList = listOf(entity1, entity2)
        val mockFlow =
            flow {
                emit(mockList)
            }
        every { datasource.getAllCarts() } returns mockFlow
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(mockList.size, actualData.payload?.first?.size)
                assertEquals(20000.0, actualData.payload?.second)
                verify { datasource.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData() {
        val entity1 =
            CartEntity(
                id = 1,
                menuName = "Sego Komando",
                menuImgUrl = "exampleImgUrl1",
                menuPrice = 0.0,
                itemQuantity = 1,
                itemNotes = "Sayur Tok Ojok Karo Ndog Mentah...",
            )
        val entity2 =
            CartEntity(
                id = 2,
                menuImgUrl = "ExampleImgUrl2",
                menuName = "Pecel",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = "Pecel itu ya pakek Sambel Kacang",
            )
        val mockPriceList =
            listOf(PriceItem("Sego Komando", 0.0), PriceItem("Pecel", 20000.0))
        val mockList = listOf(entity1, entity2)
        val mockFlow = flow { emit(mockList) }
        every { datasource.getAllCarts() } returns mockFlow
        runTest {
            repository.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val actualResult = expectMostRecentItem()
                assertTrue(actualResult is ResultWrapper.Success)
                assertEquals(mockList.size, actualResult.payload?.first?.size)
                assertEquals(mockPriceList, actualResult.payload?.second)
                assertEquals(20000.0, actualResult.payload?.third)
                verify { datasource.getAllCarts() }
            }
        }
    }

}