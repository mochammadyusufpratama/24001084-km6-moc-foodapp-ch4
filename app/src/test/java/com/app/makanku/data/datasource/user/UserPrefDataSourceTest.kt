package com.app.makanku.data.datasource.user

import com.app.makanku.data.source.local.pref.UserPreference
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserPrefDataSourceTest {
    @MockK
    lateinit var userPreference: UserPreference
    private lateinit var userPrefDataSource: UserPrefDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userPrefDataSource = UserPrefDataSourceImpl(userPreference)
    }

    @Test
    fun isUsingGridMode() {
        every { userPrefDataSource.isUsingGridMode() } returns true
        val actualResult = userPreference.isUsingGridMode()
        verify { userPrefDataSource.isUsingGridMode() }
        assertEquals(true, actualResult)
    }

    @Test
    fun setUsingGridMode() {
        every { userPrefDataSource.setUsingGridMode(any()) } returns Unit
        userPreference.setUsingGridMode(true)
        verify { userPrefDataSource.setUsingGridMode(any()) }
    }

}