package com.example.safebit.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebit.application.AppFacade
import com.example.safebit.exceptions.DataBaseException
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<IsLoading>()
    val isLoading: LiveData<IsLoading> get() = _isLoading
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult
    private val appFacade = AppFacade()
    fun login(username: String, password: String) {
        _isLoading.value = IsLoading(isSuccess = true)

        viewModelScope.launch {
            try {
                if (!appFacade.doesUserExist(username)) {
                    _loginResult.value =
                        LoginResult(isSuccess = false, errorMessage = "Username does not exist")
                    _isLoading.value = IsLoading(isSuccess = false) // Reset loading state
                    return@launch // Exit early
                }

                if (appFacade.login(username, password)) {
                    _loginResult.value = LoginResult(isSuccess = true)
                } else {
                    _loginResult.value =
                        LoginResult(isSuccess = false, errorMessage = "Invalid credentials")
                }
            }catch (e:DataBaseException){
                _loginResult.value =
                    LoginResult(isSuccess = false, errorMessage = e.message)
            }


        }
    }
}

data class LoginResult(val isSuccess: Boolean, val errorMessage: String? = null)
data class IsLoading(val isSuccess: Boolean, val errorMessage: String? = null)
