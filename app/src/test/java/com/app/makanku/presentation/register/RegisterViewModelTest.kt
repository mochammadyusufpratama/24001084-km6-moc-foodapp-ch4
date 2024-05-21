package com.app.makanku.presentation.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.makanku.data.repository.UserRepository
import com.app.makanku.tools.MainCoroutineRule
import com.app.makanku.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class RegisterViewModelTest {
    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @MockK
    lateinit var userRepository: UserRepository

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel =
            spyk(
                RegisterViewModel(userRepository),
            )
    }

    @Test
    fun doRegister() {
        every { userRepository.doRegister(any(), any(), any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.doRegister("Cup", "mochammadyusufpratama6@gmail.com", "12345678")
        verify { userRepository.doRegister(any(), any(), any()) }
    }

}