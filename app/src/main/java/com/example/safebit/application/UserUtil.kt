package com.example.safebit.application

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.safebit.database.DBFacade
import java.util.Base64


class UserUtil {
    private val dB = DBFacade
    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bytesToBase64Str(dataBytes: ByteArray): String {
            return Base64.getEncoder().encodeToString(dataBytes)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun base64StrToBytes(base64Str: String): ByteArray {
            return Base64.getDecoder().decode(base64Str)
        }
    }
    suspend fun register(email: String, password: String) :Boolean {
        val hashedPassword = hashPassword(email + password)

        val user  = User(email, hashedPassword)
        return dB.addUser(user)
    }

    private fun hashPassword(password: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(password, salt)
    }

    suspend fun doesUserExist(email: String) : Boolean {
        return dB.doesUserExist(email)
    }
    suspend fun login(user: String, password: String): Boolean {
        val storedPw = dB.getPassword(user)

        val combinedInput = user + password

        return BCrypt.checkpw(combinedInput, storedPw)
    }

    fun logout(){
        CurrentUser.clear()
        CurrentCredentials.clear()
    }

    fun setCurrentUser(email: String, password: String){
        val user = User(email, hashPassword(password))
        CurrentUser.setUser(user)
    }

    fun getCurrentUserEmail() : String{
        return CurrentUser.getUser()?.email ?: ""
    }




}