package com.example.safebit.application

import android.os.Build
import androidx.annotation.RequiresApi

class AppFacade {
    private val uU : UserUtil = UserUtil()
    private val credentialsUtil: CredentialsUtil = CredentialsUtil()

    suspend fun login(email: String, password: String) : Boolean {
        return uU.login(email, password)
    }

    fun deleteCurrentCredential(webpage: String, email: String){
        credentialsUtil.deleteCurrentCredential(webpage, email)
    }
    fun logout(){
        uU.logout()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun obtainData(email: String) : CredentialManager {
        return credentialsUtil.decryptData(email)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCredential(page: String, email: String, password: String){
        credentialsUtil.add_credential(page, email, password)
    }

    suspend fun doesUserExist(email: String) : Boolean {
         return uU.doesUserExist(email)
    }

    fun getCurrentCredentials() : CredentialManager?
    {
        return CurrentCredentials.getCredentials()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleLazyDatabaseUpdate(credentialManager: CredentialManager) {
        credentialsUtil.scheduleLazyDatabaseUpdate(credentialManager)
    }

    fun setCurrentUser(email: String, password: String){
        uU.setCurrentUser(email, password)
    }

    fun getCurrentUserEmail() : String{
        return uU.getCurrentUserEmail()
    }


    suspend fun register(email : String, password :String): Boolean{
        return uU.register(email, password)
    }

}