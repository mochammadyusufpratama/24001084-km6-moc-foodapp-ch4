package com.app.makanku.presentation.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.makanku.data.repository.UserRepository
import com.app.makanku.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: UserRepository) : ViewModel() {

    private val _changePhotoResult = MutableLiveData<ResultWrapper<Boolean>>()
    val changePhotoResult: LiveData<ResultWrapper<Boolean>>
        get() = _changePhotoResult

    private val _changeProfileResult = MutableLiveData<ResultWrapper<Boolean>>()
    val changeProfileResult: LiveData<ResultWrapper<Boolean>>
        get() = _changeProfileResult

    fun getCurrentUser() = repo.getCurrentUser()

    fun requestChangePasswordByEmail() = repo.requestChangePasswordByEmail()

    fun updateProfilePicture(photoUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            photoUri?.let {
                repo.updateProfile(photoUri = photoUri).collect {
                    _changePhotoResult.postValue(it)
                }
            }
        }
    }

    fun updateFullName(fullName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateProfile(fullName = fullName).collect {
                _changeProfileResult.postValue(it)
            }
        }
    }
    fun updateEmail(newEmail: String) =
        repo
            .updateEmail(newEmail)
            .asLiveData(Dispatchers.IO)

    fun doLogout() = repo.doLogout()

    fun isUserLoggedOut() = repo.doLogout()

}