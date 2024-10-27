package com.example.password_manager.application

import java.io.Serializable

@kotlinx.serialization.Serializable
data class User (
    val email: String,
    val password: String,
) : Serializable