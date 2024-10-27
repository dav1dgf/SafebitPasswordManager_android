package com.example.safebit.application

import java.io.Serializable

@kotlinx.serialization.Serializable
data class CredentialItem(
    val page: String,
    val email: String,
    val password: String
) : Serializable {


    // Here I don't compare password bc 2 credentials will be the same if they both have same
    // email and page, password is not needed and so it excludes the need of working with that
    // sensitive information
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CredentialItem) return false


        return this.page == other.page && this.email == other.email
    }

    override fun hashCode(): Int {
        var result = page.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        return result
    }

}