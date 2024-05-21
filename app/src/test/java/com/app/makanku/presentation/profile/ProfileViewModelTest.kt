package com.app.makanku.presentation.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.makanku.data.model.User
import com.app.makanku.data.repository.UserRepository
import com.app.makanku.tools.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ProfileViewModelTest {
    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @MockK
    lateinit var userRepository: UserRepository

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel =
            spyk(
                ProfileViewModel(userRepository),
            )
    }

    @Test
    fun isUserLoggedOut() {
        every { userRepository.doLogout() } returns true
        val result = viewModel.isUserLoggedOut()
        assertEquals(true, result)
        verify { userRepository.doLogout() }
    }

    @Test
    fun getCurrentUser() {
        val currentUser = mockk<User>()
        every { userRepository.getCurrentUser() } returns currentUser
        val result = viewModel.getCurrentUser()
        assertEquals(currentUser, result)
        verify { userRepository.getCurrentUser() }
    }

    @Test
    fun requestChangePasswordByEmail() {
        every { userRepository.requestChangePasswordByEmail() } returns true
        val result = viewModel.requestChangePasswordByEmail()
        assertEquals(true, result)
        verify { userRepository.requestChangePasswordByEmail() }
    }

}