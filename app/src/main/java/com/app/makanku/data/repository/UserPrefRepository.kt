package com.app.makanku.data.repository

import com.app.makanku.data.datasource.user.UserPrefDataSource

interface UserPrefRepository {
    fun isUsingGridMode(): Boolean
    fun setUsingGridMode(isUsingGridMode: Boolean)
}

class UserPrefRepositoryImpl(private val dataSource: UserPrefDataSource) : UserPrefRepository {
    override fun isUsingGridMode(): Boolean {
        return dataSource.isUsingGridMode()
    }

    override fun setUsingGridMode(isUsingGridMode: Boolean) {
        return dataSource.setUsingGridMode(isUsingGridMode)
    }

}