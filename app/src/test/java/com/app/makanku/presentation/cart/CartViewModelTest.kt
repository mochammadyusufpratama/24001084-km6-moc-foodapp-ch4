package com.app.makanku.presentation.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.makanku.data.repository.CartRepository
import com.app.makanku.data.repository.UserRepository
import com.app.makanku.tools.MainCoroutineRule
import com.app.makanku.tools.getOrAwaitValue
import com.app.makanku.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CartViewModelTest {
    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @MockK
    lateinit var cartRepository: CartRepository

    @MockK
    private lateinit var userRepository: UserRepository

    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = spyk(CartViewModel(cartRepository))
    }

    @Test
    fun getAllCarts_success() {
        every { cartRepository.getUserCartData() } returns
            flow {
                emit(
                    ResultWrapper.Success(
                        Pair(
                            listOf(
                                mockk(relaxed = true),
                                mockk(relaxed = true),
                            ),
                            8000.0,
                        ),
                    ),
                )
            }
        val result = viewModel.getAllCarts().getOrAwaitValue()
        assertEquals(2, result.payload?.first?.size)
        assertEquals(8000.0, result.payload?.second)
    }

    @Test
    fun decreaseCart() {
        every { cartRepository.decreaseCart(any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.decreaseCart(mockk())
        verify { cartRepository.decreaseCart(any()) }
    }

    @Test
    fun increaseCart() {
        every { cartRepository.increaseCart(any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.increaseCart(mockk())
        verify { cartRepository.increaseCart(any()) }
    }

    @Test
    fun removeCart() {
        every { cartRepository.deleteCart(any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.removeCart(mockk())
        verify { cartRepository.deleteCart(any()) }
    }

    @Test
    fun setCartNotes() {
        every { cartRepository.setCartNotes(any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.setCartNotes(mockk())
        verify { cartRepository.setCartNotes(any()) }
    }

}