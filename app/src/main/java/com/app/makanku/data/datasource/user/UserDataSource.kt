package com.app.makanku.data.datasource.user

interface UserDataSource {
    fun isUsingGridMode(): Boolean
    fun setUsingGridMode(isUsingGridMode: Boolean)
}