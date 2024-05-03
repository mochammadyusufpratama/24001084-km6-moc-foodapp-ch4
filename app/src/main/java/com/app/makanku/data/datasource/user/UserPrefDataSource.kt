package com.app.makanku.data.datasource.user

import com.app.makanku.data.source.local.pref.UserPreference

interface UserPrefDataSource {
    fun isUsingGridMode(): Boolean

    fun setUsingGridMode(isUsingGridMode: Boolean)
}

class UserPrefDataSourceImpl(private val userPreference: UserPreference) : UserPrefDataSource {
    override fun isUsingGridMode(): Boolean {
        return userPreference.isUsingGridMode()
    }

    override fun setUsingGridMode(isUsingGridMode: Boolean) {
        return userPreference.setUsingGridMode(isUsingGridMode)
    }
}
