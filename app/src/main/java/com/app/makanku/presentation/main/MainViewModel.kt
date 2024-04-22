package com.app.makanku.presentation.main

import androidx.lifecycle.ViewModel
import com.app.makanku.data.repository.UserRepository

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun isUserLoggedIn() = userRepository.isLoggedIn()
}