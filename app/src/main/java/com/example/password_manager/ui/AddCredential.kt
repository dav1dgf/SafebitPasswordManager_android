package com.example.password_manager.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.password_manager.application.AppFacade
import com.example.password_manager.databinding.ActivityAddCredentialBinding

class AddCredential : AppCompatActivity() {
    private lateinit var binding: ActivityAddCredentialBinding
    private lateinit var viewModel: AddCredentialViewModel
    private var passwordLength = 16 // Default password length
    private lateinit var app: AppFacade

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        app= AppFacade()
        binding = ActivityAddCredentialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AddCredentialViewModel::class.java)

        initUI()
        observeViewModel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        binding.etEmail.setText(app.getCurrentUserEmail())
        // Set up UI interactions (SeekBar, Generate Password button, etc.)
        binding.btnAddCredential.setOnClickListener {
            if (!isInputValid()) {
                Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val webpage = binding.etWebpage.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            // Trigger adding the credential via ViewModel
            viewModel.addCredential(webpage, email, password)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        // Observe the loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnAddCredential.isEnabled = !isLoading
        }

        // Observe the result of adding a credential
        viewModel.addCredentialResult.observe(this) { result ->
            if (result.success) {
                Toast.makeText(this, "Credential added successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CredentialList::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isInputValid(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val email = binding.etEmail.text.toString()
        val webpage = binding.etWebpage.text.toString()

        return email.matches(Regex(emailPattern)) && webpage.isNotEmpty()
    }
}
