package com.app.makanku.data.datasource.authentication

import com.app.makanku.data.datasource.user.AuthDataSource
import com.app.makanku.data.datasource.user.FirebaseAuthDataSource
import com.app.makanku.data.source.firebase.FirebaseService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthDataSourceTest {
    @MockK
    lateinit var service: FirebaseService
    private lateinit var dataSource: AuthDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = FirebaseAuthDataSource(service)
    }

    @Test
    fun doLogin() {
        runTest {
            coEvery { dataSource.doLogin(any(), any()) } returns true
            val actualResult = service.doLogin("iyusjr05@gmail.com", "12345678")
            coVerify { dataSource.doLogin(any(), any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun doRegister() {
        runTest {
            coEvery { dataSource.doRegister(any(), any(), any()) } returns true
            val actualResult = service.doRegister("Cup", "mochammadyusufpratama6@gmail.com", "12345678")
            coVerify { dataSource.doRegister(any(), any(), any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun updateProfile() {
        runTest {
            coEvery { dataSource.updateProfile(any()) } returns true
            val actualResult = service.updateProfile("Cuppp")
            coVerify { dataSource.updateProfile(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun updatePassword() {
        runTest {
            coEvery { dataSource.updatePassword(any()) } returns true
            val actualResult = service.updatePassword("cuppp123")
            coVerify { dataSource.updatePassword(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun updateEmail() {
        runTest {
            coEvery { dataSource.updateEmail(any()) } returns true
            val actualResult = service.updateEmail("myp@gmail.com")
            coVerify { dataSource.updateEmail(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun reqChangePasswordByEmail() {
        runTest {
            every { dataSource.requestChangePasswordByEmail() } returns true
            val actualResult = service.requestChangePasswordByEmail()
            verify { dataSource.requestChangePasswordByEmail() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun doLogout() {
        runTest {
            every { dataSource.doLogout() } returns true
            val actualResult = service.doLogout()
            verify { dataSource.doLogout() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun isLoggedIn() {
        runTest {
            every { dataSource.isLoggedIn() } returns true
            val actualResult = service.isLoggedIn()
            verify { dataSource.isLoggedIn() }
            assertEquals(true, actualResult)
        }
    }

}