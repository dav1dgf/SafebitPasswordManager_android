package com.example.safebit.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.safebit.application.AppFacade
import com.example.safebit.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var app: AppFacade

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Inflate layout
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = AppFacade()
        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Observe register state
        observeViewModel()

        // Set up the UI
        initUI()
    }

    private fun observeViewModel() {
        // Observe loading state
        viewModel.isLoading.observe(this) { result ->
            if (result.isSuccess) {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnRegister.isEnabled = false
            } else {
                binding.progressBar.visibility = View.GONE
                binding.btnSignIn.isEnabled = true
            }
        }

        // Observe register result
        viewModel.registerResult.observe(this) { result ->
            if (result.isSuccess) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                finish() // Close the RegisterActivity and return to the previous screen
            } else {
                // Show error
                Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initUI() {
        binding.progressBar.visibility = View.GONE

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etPassword2.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (!isValidPassword(password)) {
                Toast.makeText(this, "Password must contain at least one uppercase letter, one lowercase letter, one number, and be at least 8 characters long", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(email, password)
            }


        }

        binding.btnBack.setOnClickListener{
            finish()
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }
}