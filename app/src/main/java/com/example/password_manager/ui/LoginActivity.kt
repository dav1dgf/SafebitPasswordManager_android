package com.example.password_manager.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.password_manager.databinding.ActivityLoginBinding
import androidx.lifecycle.lifecycleScope
import com.example.password_manager.application.AppFacade
import com.example.password_manager.application.CredentialManager
import kotlinx.coroutines.launch
import android.content.Context
import android.content.SharedPreferences


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private val app : AppFacade = AppFacade()
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        observeViewModel()
        initUI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        // Observe loading state

        viewModel.isLoading.observe(this)  { result ->
            if (result.isSuccess) {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnSignIn.isEnabled = false
            } else {
                binding.progressBar.visibility = View.GONE
                binding.btnSignIn.isEnabled = true
            }
        }

        // Observe login result
        viewModel.loginResult.observe(this) { result ->
            if (result.isSuccess) {
                saveCredentialsIfNeeded()
                //Inseguro, y current password no se usa para nada, por lo menos hashear
                app.setCurrentUser(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                lifecycleScope.launch {

                    val credManager : CredentialManager = app.obtainData(binding.etEmail.text.toString())
                    binding.progressBar.visibility = View.GONE
                    val intent = Intent(this@LoginActivity, CredentialList::class.java)
                    intent.putExtra("CREDENTIAL_MANAGER", credManager)
                    startActivity(intent)

                }

            } else {
                Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()

                binding.etPassword.setText("")
                binding.progressBar.visibility = View.GONE
                binding.btnSignIn.setEnabled(true)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return emailRegex.matches(email)
    }


    private fun initUI() {
        binding.progressBar.visibility = View.GONE
        loadSavedCredentials()
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty() ) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
            else if(!isValidEmail(email)){
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.login(email, password)
            }
        }



        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }
    }

    private fun saveCredentialsIfNeeded() {
        val email = binding.etEmail.text.toString()
        val isRememberMeChecked = binding.checkBoxRemember.isChecked

        val editor = sharedPreferences.edit()
        if (isRememberMeChecked) {
            editor.putString("email", email)
            editor.putBoolean("remember_email", true)
        } else {
            editor.clear() // Clear saved credentials if "Remember Me" is unchecked
        }
        editor.apply()
    }

    private fun loadSavedCredentials() {
        val isRememberMeChecked = sharedPreferences.getBoolean("remember_email", false)
        if (isRememberMeChecked) {
            val savedEmail = sharedPreferences.getString("email", "")
            binding.etEmail.setText(savedEmail)
            binding.checkBoxRemember.isChecked = true
        }
    }


}
