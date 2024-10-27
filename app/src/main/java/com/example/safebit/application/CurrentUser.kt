package com.example.safebit.application

@kotlinx.serialization.Serializable
object CurrentUser {
    private var user: User? = null

    @Synchronized
    fun setUser(newUser: User) {
        user = newUser
    }

    @Synchronized
    fun getUser(): User? {
        return user
    }

    @Synchronized
    fun clear() {
        user = null
    }

}
