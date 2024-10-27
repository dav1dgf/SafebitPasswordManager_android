package com.example.safebit.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.safebit.application.AppFacade
import com.example.safebit.application.CredentialItem
import com.example.safebit.application.CredentialManager
import com.example.safebit.exceptions.DataBaseException
import com.example.safebit.exceptions.LogicalException
import com.example.safebit.databinding.ActivityCredentialDetailBinding
import kotlinx.coroutines.launch

class CredentialDetail : AppCompatActivity() {

    private lateinit var credentialItem: CredentialItem
    private var isPasswordVisible = false

    private lateinit var binding: ActivityCredentialDetailBinding
    private lateinit var app: AppFacade

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        app = AppFacade()
        binding = ActivityCredentialDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI
        initUI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        credentialItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("CREDENTIAL", CredentialItem::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("CREDENTIAL") as CredentialItem
        }

        // Display credential details
        displayCredentialDetails()

        // Button listeners
        binding.btnShowPassword.setOnClickListener { togglePasswordVisibility() }
        binding.btnCopyPassword.setOnClickListener { copyPasswordToClipboard() }
        binding.btnDeleteCredential.setOnClickListener { deleteCredential() }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun displayCredentialDetails() {
        binding.tvDetailWebpage.text = credentialItem.page
        binding.tvDetailEmail.text = credentialItem.email
        binding.tvDetailPassword.text = "••••••••" // Initially hidden
    }

    private fun togglePasswordVisibility() {
        val passwordTextView = binding.tvDetailPassword
        if (isPasswordVisible) {
            passwordTextView.text = "••••••••"
            binding.btnShowPassword.text = "Show Password"
        } else {
            passwordTextView.text = credentialItem.password
            binding.btnShowPassword.text = "Hide Password"
        }
        isPasswordVisible = !isPasswordVisible
    }

    private fun copyPasswordToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("password", credentialItem.password)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Password copied", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteCredential() {
        lifecycleScope.launch {
            try {
                app.deleteCurrentCredential(binding.tvDetailWebpage.text.toString(), binding.tvDetailEmail.text.toString())

                // Schedule database update and start CredentialList activity
                app.getCurrentCredentials()?.let { app.scheduleLazyDatabaseUpdate(it) }

                val credManager: CredentialManager? = app.getCurrentCredentials()
                val intent = Intent(this@CredentialDetail, CredentialList::class.java)
                intent.putExtra("CREDENTIAL_MANAGER", credManager)
                startActivity(intent)

                Toast.makeText(this@CredentialDetail, "Credential deleted", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after deletion

            } catch (e: DataBaseException) {
                Toast.makeText(this@CredentialDetail, "Error accessing database: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: LogicalException) {
                Toast.makeText(this@CredentialDetail, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
