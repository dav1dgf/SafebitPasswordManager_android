package com.example.password_manager.application

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
