package com.example.safebit.database

import com.example.safebit.application.Credentials
import com.example.safebit.application.User
import com.example.safebit.exceptions.DataBaseException
import io.github.jan.supabase.SupabaseClient

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import org.json.JSONArray
import org.json.JSONException

class DB {
    private val supabaseClient: SupabaseClient

    init {
        val facade = DBFacade
        supabaseClient = facade.getClient()
    }


    suspend fun getPassword(email: String): String {
        try {

            val response = supabaseClient.from("users").select(columns = Columns.list("hash_pw")) {
                filter {
                    eq("email", email)
                }
            }

            val jsonArray = JSONArray(response.data)
            val jsonObject = jsonArray.getJSONObject(0)
            return jsonObject.getString("hash_pw")

        } catch (e: JSONException) {
            throw DataBaseException("Failed to get password for $email: ${e.message}")
        } catch (e: Exception) {
            throw DataBaseException("Unexpected error when getting password: ${e.message}")
        }
    }

    suspend fun getData(email: String): String {
        return try {
            val response =
                supabaseClient.from("credentials").select(columns = Columns.list("salt", "data")) {
                    filter {
                        eq("email", email)
                    }
                }

            val jsonArray = JSONArray(response.data)
            val jsonObject = jsonArray.getJSONObject(0)
            jsonObject.getString("data")
        } catch (e: JSONException) {
            if (e.message?.contains("Index 0 out of range [0..0)") == true) {
                ""
            } else {
                throw DataBaseException("Failed to get data for $email: ${e.message}")
            }
        } catch (e: Exception) {
            throw DataBaseException("Unexpected error when getting data: ${e.message}")
        }
    }

    suspend fun doesUserExist(email: String): Boolean {
        try {

            val response = supabaseClient.from("users").select {
                filter {
                    eq("email", email)
                }
            }

            val jsonArray = JSONArray(response.data)
            return jsonArray.length() != 0

        } catch (e: JSONException) {
            throw DataBaseException("Failed to check if user exists for $email: ${e.message}")
        } catch (e: Exception) {
            throw DataBaseException("Unexpected error when checking user existence: ${e.message}")
        }
    }

    suspend fun getSalt(email: String): String {
        return try {
            val response =
                supabaseClient.from("credentials").select(columns = Columns.list("salt", "data")) {
                    filter {
                        eq("email", email)
                    }
                }

            val jsonArray = JSONArray(response.data)
            val jsonObject = jsonArray.getJSONObject(0)
            jsonObject.getString("salt")
        } catch (e: JSONException) {
            if (e.message?.contains("Index 0 out of range [0..0)") == true) {
                ""
            } else {
                throw DataBaseException("Failed to get salt for $email: ${e.message}")
            }
        } catch (e: Exception) {
            throw DataBaseException("Unexpected error when getting salt: ${e.message}")
        }
    }

    suspend fun updateSalt(email: String, salt: String) {
        try {
            supabaseClient.from("credentials").update(
                {
                    set("salt", salt)
                }
            ) {
                filter {

                    eq("email", email)
                }
            }
        } catch (e: Exception) {
            throw DataBaseException("Failed to update salt for $email: ${e.message}")
        }
    }

    suspend fun updateCredentials(email: String, salt: String, dataB64: String) {
        val credentials = Credentials(email, salt, dataB64)
        try {
            supabaseClient.from("credentials").upsert(credentials)
        } catch (e: Exception) {
            throw DataBaseException("Failed to update credentials for $email: ${e.message}")
        }
    }

        suspend fun addUser(user: User): Boolean {
            try {
                supabaseClient.from("users").insert(user)
                return true
            } catch (e: Exception) {
                throw DataBaseException("Failed to add user: ${e.message}")
            }
        }

}
