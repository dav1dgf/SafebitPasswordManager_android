package com.example.password_manager.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.password_manager.application.AppFacade
import com.example.password_manager.application.CredentialManager
import com.example.password_manager.databinding.ActivityCredentialListBinding

class CredentialList : AppCompatActivity() {

    private lateinit var adapter: CredentialAdapter
    private lateinit var binding: ActivityCredentialListBinding
    private lateinit var credentialManager: CredentialManager
    private lateinit var app: AppFacade

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        app = AppFacade()

        binding = ActivityCredentialListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        credentialManager = intent.getSerializableExtra("CREDENTIAL_MANAGER") as? CredentialManager
            ?: run {
                Toast.makeText(this, "Failed to load Credential Manager", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val credentialItems = credentialManager.getList()
        adapter = CredentialAdapter(credentialItems) { credential ->
            val intent = Intent(this, CredentialDetail::class.java)
            intent.putExtra("CREDENTIAL", credential)  // Pass the credential
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddCredential::class.java)
            startActivity(intent)
        }

        binding.buttonLogOut.setOnClickListener {
            app.logout()  // Call logout method in AppFacade
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
