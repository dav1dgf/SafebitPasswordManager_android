package com.example.safebit.application


@kotlinx.serialization.Serializable
data class Credentials(
    val email: String,
    var salt: String,
    var data: String
) : java.io.Serializable