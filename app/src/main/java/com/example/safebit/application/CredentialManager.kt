package com.example.safebit.application

import com.example.safebit.exceptions.LogicalException
import org.json.JSONArray
import java.io.Serializable

@kotlinx.serialization.Serializable
class CredentialManager(var credentials: MutableList<CredentialItem> = mutableListOf()) : Serializable{

    // Adds a new credential to the list
    fun add(credential: CredentialItem) {
        if (!this.credentials.contains(credential))
            credentials.add(credential)
        else
            throw LogicalException("Adding a credential that already exists")
    }

    fun getList(): List<CredentialItem>{
        return credentials.toList()
    }

    fun toJSON(): String {
        val result = StringBuilder()
        result.append("[")

        for ((index, credentialItem) in credentials.withIndex()) {
            result.append("{")
            result.append("\"page\": \"${credentialItem.page}\",")
            result.append("\"email\": \"${credentialItem.email}\",")
            result.append("\"password\": \"${credentialItem.password}\"")
            result.append("}")

            if (index != credentials.size - 1) {
                result.append(",")
            }
        }

        result.append("]")
        return result.toString()
    }

    private fun fromJson(jsonString: String) {
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val webpage = jsonObject.getString("page")
                val email = jsonObject.getString("email")
                val password = jsonObject.getString("password")
                val credentialItem = CredentialItem(webpage, email, password)
                credentials.add(credentialItem)
            }
        } catch (e: Exception) {
            throw LogicalException("Failed to parse credentials from JSON.")
        }
    }


    constructor(json: String) : this() {
        if (json.isNotEmpty()) {
            fromJson(json)
        }
    }

    // Empty constructor (already provided by default, but can be explicitly defined for clarity)
    constructor() : this(mutableListOf())



    fun delete(webpage: String, email: String) {
        val target = credentials.find { it.page == webpage && it.email == email }
            ?: throw LogicalException("The credential item you want to delete does not exist.")
        credentials.remove(target)
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("CredentialManager with ${credentials.size} credentials:\n")

        for (credentialItem in credentials) {
            stringBuilder.append("Page: ${credentialItem.page}, Email: ${credentialItem.email}, Password: ${credentialItem.password}\n")
        }

        return stringBuilder.toString()
    }

}
