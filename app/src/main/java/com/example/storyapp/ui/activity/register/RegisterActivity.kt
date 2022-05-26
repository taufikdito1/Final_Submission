package com.example.storyapp.ui.activity.register

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.activity.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Register"

        setupViewModel()
        setAction()
    }

    private fun setAction() {
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        viewModel.isLoading.observe(this) { showLoading(it) }
    }

    private fun register() {
        val name = binding.textName.text.toString().trim()
        val email = binding.tvEmail.text.toString().trim()
        val password = binding.tvPassword.text.toString().trim()

        when {
            name.isEmpty() -> {
                binding.layoutName.error = "Please enter your username"
            }
            email.isEmpty() -> {
                binding.layoutEmail.error = "Please enter your email"
            }
            password.isEmpty() -> {
                binding.layoutPassword.error = "Please enter your password"
            }
            else -> {
                viewModel.registerUser(name, email, password)
                binding.progressBar.visibility = View.VISIBLE
                AlertDialog.Builder(this).apply {
                    setTitle("Registration Success!!!")
                    setMessage("Please login to continue")
                    setPositiveButton("Ok") { _, _ ->
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(
                            intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity as Activity)
                                .toBundle()
                        )
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}