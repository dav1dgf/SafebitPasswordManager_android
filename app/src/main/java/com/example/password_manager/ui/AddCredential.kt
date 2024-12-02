package com.example.password_manager.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
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

    private val inputWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            updateAddButtonState()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

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

        binding.etWebpage.addTextChangedListener(inputWatcher)
        binding.etEmail.addTextChangedListener(inputWatcher)
        binding.etPassword.addTextChangedListener(inputWatcher)
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
            val intent = Intent(this@AddCredential, CredentialList::class.java)
            intent.putExtra("CREDENTIAL_MANAGER", app.getCurrentCredentials())
            startActivity(intent)

        }

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnGeneratePassword.setOnClickListener {
            val password = generateRandomPassword(passwordLength) // specify the length you want
            binding.etPassword.setText(password) // assuming there's an EditText to display the password
        }
        binding.seekBarPasswordLength.apply {

            progress = passwordLength // Default length to 16

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    // Update the password length based on SeekBar progress
                    passwordLength = progress.coerceAtLeast(4) // Ensure a minimum length of 4
                    binding.tvPasswordLength.text = "Password Length: $passwordLength"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }



    }
    private fun updateAddButtonState() {
        // Check if all fields have text
        val isAllFieldsFilled = binding.etWebpage.text.isNotEmpty() &&
                binding.etEmail.text.isNotEmpty() &&
                binding.etPassword.text.isNotEmpty()

        // Enable button if all fields are filled
        binding.btnAddCredential.isEnabled = isAllFieldsFilled
    }
    private fun generateRandomPassword(length: Int): String {
        val lower = "abcdefghijklmnopqrstuvwxyz"
        val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val digits = "0123456789"
        val specials = "!@#$%^&*()-_=+<>?"

        // Ensure password contains at least one character from each required type
        val combinedChars = listOf(
            lower.random(),
            upper.random(),
            digits.random(),
            specials.random()
        )

        // Fill the remaining length with a random selection of all character sets
        val allChars = lower + upper + digits + specials
        val randomChars = (1..(length - combinedChars.size))
            .map { allChars.random() }

        // Shuffle combined characters to randomize their positions
        return (combinedChars + randomChars)
            .shuffled()
            .joinToString("")
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
