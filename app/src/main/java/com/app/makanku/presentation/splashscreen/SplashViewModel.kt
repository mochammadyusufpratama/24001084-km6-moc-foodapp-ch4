package com.app.makanku.presentation.splashscreen

import androidx.lifecycle.ViewModel
import com.app.makanku.data.repository.UserRepository

class SplashViewModel(private val repository: UserRepository) : ViewModel() {
    fun isUserLoggedIn() = repository.isLoggedIn()
}
