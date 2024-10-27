package com.example.password_manager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.password_manager.application.AppFacade
import com.example.password_manager.exceptions.DataBaseException
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    // LiveData for loading state and registration result
    private val _isLoading = MutableLiveData<IsLoading>()
    val isLoading: LiveData<IsLoading> get() = _isLoading

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> get() = _registerResult

    private val appFacade = AppFacade()

    fun register(username: String, password: String) {
        _isLoading.value = IsLoading(isSuccess = true)

        viewModelScope.launch {
            try {
                // Attempt to register the user using the AppFacade
                if (appFacade.doesUserExist(username)) {
                    _registerResult.value =
                        RegisterResult(isSuccess = false, errorMessage = "Username already exists")
                    return@launch // Exit early
                }

                // Proceed to register the new user
                val registrationSuccessful = appFacade.register(username, password)
                if (registrationSuccessful) {
                    _registerResult.value = RegisterResult(isSuccess = true)
                } else {
                    _registerResult.value =
                        RegisterResult(isSuccess = false, errorMessage = "Registration failed")
                }
            }catch (e: DataBaseException){
                    _registerResult.value =
                        RegisterResult(isSuccess = false, errorMessage = e.message)
                }
        }
    }
}

data class RegisterResult(val isSuccess: Boolean, val errorMessage: String? = null)
