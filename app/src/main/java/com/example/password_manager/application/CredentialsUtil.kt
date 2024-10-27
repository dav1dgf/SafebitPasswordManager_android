package com.example.password_manager.application

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.password_manager.database.DBFacade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class CredentialsUtil() {
    val uU = UserUtil()
    val dB = DBFacade
    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(Exception::class)
    suspend fun getKey(email: String): ByteArray {
        val storedPassword: String = dB.getPassword(email)
        var saltB64: String = dB.getSalt(email)
        val salt : ByteArray
        if ( !saltB64.isNullOrEmpty()){

            salt = UserUtil.base64StrToBytes(saltB64)
            CurrentCredentials.setSalt(saltB64)
        } else {
            salt = generateSalt()
            saltB64 = UserUtil.bytesToBase64Str(salt)
            CurrentCredentials.setSalt(saltB64)

        }
        return deriveKey(email + storedPassword, salt, 256)
    }

    private fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16) // 16 bytes salt
        random.nextBytes(salt)
        return salt
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun deriveKey(password: String, salt: ByteArray, keyLength: Int): ByteArray {
        val iterations = 100000
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return factory.generateSecret(spec).encoded
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun decryptData(email: String): CredentialManager {
        val key: ByteArray = getKey(email)
        val dataB64: String = dB.getData(email)
        val cred :CredentialManager
        if (dataB64.isNullOrEmpty()){
            cred = CredentialManager()
        }
        else {
            val dataEnc: ByteArray = UserUtil.base64StrToBytes(dataB64)
            val decryptedData: ByteArray = decryptAES(key, dataEnc)


            val jsonString = String(decryptedData, Charsets.UTF_8)

            cred = CredentialManager(jsonString)



        }
        CurrentCredentials.setCredentials(cred)
        return cred
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun encryptData(email : String, currentCredentials: CredentialManager) {
        val key: ByteArray = getKey(email)

        val serializedData: String = currentCredentials.toJSON()
        val encryptedData : ByteArray = encryptAES(key, serializedData.toByteArray(Charsets.UTF_8))
        val b64str = UserUtil.bytesToBase64Str(encryptedData)

        CurrentCredentials.getSalt()?.let { dB.updateCredentials(email, it,  b64str) }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun add_credential(page: String, email: String, password: String){
        val credItem = CredentialItem(page, email, password)
        CurrentCredentials.addCredential(credItem)

    }
    private var lazyUpdateJob: Job? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleLazyDatabaseUpdate(credentials: CredentialManager?) {
        lazyUpdateJob?.cancel()
        if (credentials == null) {
            return
        }

        lazyUpdateJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                CurrentUser.getUser()?.let { encryptData(it.email, credentials) }
            } catch (e: Exception) {
                throw Exception("Error updating Databse" + e.message)
            }
        }
    }

    private fun encryptAES(key: ByteArray, plaintext: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val secretKey = SecretKeySpec(key, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedData = cipher.doFinal(plaintext)
        return encryptedData.sliceArray(0 until encryptedData.size)
    }

    private fun decryptAES(key: ByteArray, ciphertext: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val secretKey = SecretKeySpec(key, "AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)

        val decryptedData = cipher.doFinal(ciphertext)
        return decryptedData.sliceArray(0 until decryptedData.size)
    }


    fun deleteCurrentCredential(webpage: String, email: String){
        val credentialManager : CredentialManager? = CurrentCredentials.getCredentials()
        credentialManager?.delete(webpage, email)

    }

}