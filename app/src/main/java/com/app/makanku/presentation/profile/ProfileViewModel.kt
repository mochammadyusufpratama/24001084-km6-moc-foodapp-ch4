package com.app.makanku.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.makanku.data.model.Profile

class ProfileViewModel : ViewModel() {

    val profileData = MutableLiveData(
        Profile(
            name = "Mochammad Yusuf Pratama",
            username = "mochammadyusuf",
            email = "mochammadyusufpratama6@gmail.com",
            profileImg = "https://github.com/mochammadyusufpratama/makanku-asset/blob/main/profile.jpg?raw=true"
        )
    )

    val isEditMode = MutableLiveData(false)

    fun changeEditMode() {
        val currentValue = isEditMode.value ?: false
        isEditMode.postValue(!currentValue)
    }

}