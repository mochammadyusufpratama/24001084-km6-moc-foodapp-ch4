package com.app.makanku.data.repository

import app.cash.turbine.test
import com.app.makanku.data.datasource.user.AuthDataSource
import com.app.makanku.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {
    @MockK
    lateinit var dataSource: AuthDataSource
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(dataSource)
    }

    @Test
    fun doLogin() {
        val email = "iyusjr05@gmail.com"
        val password = "12345678"
        coEvery { dataSource.doLogin(any(), any()) } returns true
        runTest {
            repository.doLogin(email, password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.doLogin(any(), any()) }
            }
        }
    }

    @Test
    fun doRegister() {
        val fullName = "Cup"
        val email = "mochammadyusufpratama6@gmail.com"
        val password = "12345678"

        coEvery { dataSource.doRegister(any(), any(), any()) } returns true
        runTest {
            repository.doRegister(fullName, email, password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.doRegister(any(), any(), any()) }
            }
        }
    }

    @Test
    fun updateProfile() {
        val fullName = "Cuppp"

        coEvery { dataSource.updateProfile(any()) } returns true
        runTest {
            repository.updateProfile(fullName).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.updateProfile(any()) }
            }
        }
    }

    @Test
    fun updatePassword() {
        val password = "cuppp123"

        coEvery { dataSource.updatePassword(any()) } returns true
        runTest {
            repository.updatePassword(password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.updatePassword(any()) }
            }
        }
    }

    @Test
    fun updateEmail() {
        val email = "myp@gmail.com"

        coEvery { dataSource.updateEmail(any()) } returns true
        runTest {
            repository.updateEmail(email).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.updateEmail(any()) }
            }
        }
    }

    @Test
    fun requestChangePasswordByEmail() {
        runTest {
            every { dataSource.requestChangePasswordByEmail() } returns true
            val actualResult = repository.requestChangePasswordByEmail()
            verify { dataSource.requestChangePasswordByEmail() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun doLogout() {
        runTest {
            every { dataSource.doLogout() } returns true
            val actualResult = repository.doLogout()
            verify { dataSource.doLogout() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun isLoggedIn() {
        runTest {
            every { dataSource.isLoggedIn() } returns true
            val actualResult = repository.isLoggedIn()
            verify { dataSource.isLoggedIn() }
            assertEquals(true, actualResult)
        }
    }

}