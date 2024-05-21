package com.app.makanku.data.repository

import com.app.makanku.data.datasource.user.UserPrefDataSource
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserPrefRepositoryImplTest {
    @MockK
    lateinit var datasource: UserPrefDataSource
    private lateinit var userPrefRepository: UserPrefRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userPrefRepository = UserPrefRepositoryImpl(datasource)
    }

    @Test
    fun isUsingGridMode() {
        every { datasource.isUsingGridMode() } returns true
        val actualResult = userPrefRepository.isUsingGridMode()
        verify { datasource.isUsingGridMode() }
        assertEquals(true, actualResult)
    }

    @Test
    fun setUsingGridMode() {
        every { datasource.setUsingGridMode(any()) } returns Unit
        userPrefRepository.setUsingGridMode(true)
        verify { datasource.setUsingGridMode(any()) }
    }

}