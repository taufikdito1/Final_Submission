package com.example.storyapp.ui.activity.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.model.login.UserSession
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.helper.DataStoreViewModel
import com.example.storyapp.helper.UserPreference
import com.example.storyapp.ui.activity.main.MainActivity
import com.example.storyapp.ui.activity.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var pref: SharedPreferences
    private lateinit var userPref: UserPreference
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Login"

        setupPreference()
        setupViewModel()
        setAction()
        playAnimation()
    }

    private fun setupPreference() {
        pref = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        userPref = UserPreference(this)
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        loginViewModel.isLoading.observe(this) { showLoading(it) }
        loginViewModel.toastMessage.observe(this) { showToast(it) }
    }

    private fun setAction() {
        binding.btnLogin.setOnClickListener {
            login()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity as Activity)
                    .toBundle()
            )
        }
    }

    private fun login() {
        val email = binding.tvEmail.text.toString().trim()
        val password = binding.tvPassword.text.toString().trim()
        when {
            email.isEmpty() -> {
                binding.layoutEmail.error = "Please enter your email"
            }
            password.isEmpty() -> {
                binding.textInputLayout.error = "Please enter your password"
            }
            else -> {
                loginViewModel.loginUser(email, password)
                loginViewModel.userLogin.observe(this) {
                    binding.progressBar.visibility = View.VISIBLE
                    if (it != null) {
                        AlertDialog.Builder(this).apply {
                            setTitle("Login Success!")
                            setMessage("Welcome, ${it.name}!")
                            setPositiveButton("Ok") { _, _ ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(
                                    intent,
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity as Activity)
                                        .toBundle()
                                )
                                finish()
                            }
                            create()
                            show()
                        }
                        saveSession(UserSession(it.name, it.token, it.userId, true))
                    }

                }
            }
        }
    }

    private fun saveSession(user: UserSession) {
        dataStoreViewModel.setSession(user)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val email = ObjectAnimator.ofFloat(binding.tvHello, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(email)
            startDelay = 500
        }.start()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val SHARED_PREFERENCES = "shared_preferences"
    }
}