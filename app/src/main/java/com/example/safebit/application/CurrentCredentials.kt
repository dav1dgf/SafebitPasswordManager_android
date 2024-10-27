package com.example.safebit.application

@kotlinx.serialization.Serializable
object CurrentCredentials {
    private var credentials: CredentialManager? = null
    private  var salt: String? = null

    @Synchronized
    fun setSalt(salt: String){
        this.salt= salt
    }

    @Synchronized
    fun getSalt() : String? {
        return salt
    }

    @Synchronized
    fun setCredentials(cred: CredentialManager) {
        this.credentials= cred
    }

    @Synchronized
    fun clear() {
        credentials = null
        salt= ""
    }

    @Synchronized
    fun getCredentials(): CredentialManager? {
        return credentials
    }

    @Synchronized
    fun addCredential(credential: CredentialItem) {
        credentials?.add(credential) ?: throw IllegalStateException("Credentials have not been initialized.")
    }
}
