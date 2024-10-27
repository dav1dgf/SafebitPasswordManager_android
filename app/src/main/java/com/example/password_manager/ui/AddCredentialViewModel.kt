package com.example.password_manager.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.password_manager.application.AppFacade
import com.example.password_manager.exceptions.DataBaseException
import com.example.password_manager.exceptions.LogicalException
import kotlinx.coroutines.launch

class AddCredentialViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _addCredentialResult = MutableLiveData<AddCredentialResult>()
    val addCredentialResult: LiveData<AddCredentialResult> get() = _addCredentialResult

    private val appFacade = AppFacade()

    // Method to add a new credential
    @RequiresApi(Build.VERSION_CODES.O)
    fun addCredential(webpage: String, email: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Call the AppFacade to add the credential
                appFacade.addCredential(webpage, email, password)

                // Optionally, schedule the database update
                appFacade.getCurrentCredentials()?.let {
                    appFacade.scheduleLazyDatabaseUpdate(it)
                }

                _addCredentialResult.value = AddCredentialResult(success = true)
            } catch (e: DataBaseException) {
                _addCredentialResult.value = AddCredentialResult(success = false, errorMessage = "Database error: ${e.message}")
            } catch (e: LogicalException) {
                _addCredentialResult.value = AddCredentialResult(success = false, errorMessage = "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Data class to represent the result of adding a credential
data class AddCredentialResult(val success: Boolean, val errorMessage: String? = null)
