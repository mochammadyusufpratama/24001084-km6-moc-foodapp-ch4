package com.app.makanku.presentation.login

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

class LoginViewModelTest {
    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @MockK
    lateinit var userRepository: UserRepository

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel =
            spyk(
                LoginViewModel(
                    userRepository,
                ),
            )
    }

    @Test
    fun doLogin() {
        every { userRepository.doLogin(any(), any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.doLogin("iyusjr05@gmail.com", "12345678")
        verify { userRepository.doLogin(any(), any()) }
    }

}