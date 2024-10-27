package com.example.password_manager.database

import com.example.password_manager.application.User
import com.example.password_manager.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object DBFacade {
    private val supabaseClient: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = BuildConfig.API_URL,
            supabaseKey = BuildConfig.API_PASSWORD
        ) {
            install(Postgrest)
        }
    }


    private val db = DB()

    fun getClient(): SupabaseClient {
        return supabaseClient
    }

    suspend fun updateCredentials(email: String, salt:String, data :String){
        db.updateCredentials(email, salt, data)
    }

    suspend fun addUser(user: User) :Boolean{
        return db.addUser(user)
    }


    suspend fun getSalt(email: String): String {
        return db.getSalt(email)
    }

    suspend fun getData(email: String): String {
        return db.getData(email)
    }

    suspend fun getPassword(email: String): String{
        return db.getPassword(email)
    }

    suspend fun doesUserExist(email: String) : Boolean {
        return db.doesUserExist(email)
    }


    }
