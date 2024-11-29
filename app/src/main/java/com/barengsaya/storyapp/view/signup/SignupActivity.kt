package com.barengsaya.storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.barengsaya.storyapp.databinding.ActivitySignupBinding
import com.barengsaya.storyapp.view.ViewModelFactory
import com.barengsaya.storyapp.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
        observeViewModel()
        setupListeners()
    }

    private fun setupListeners() {
        binding.punyaAkun.setOnClickListener {
            Intent(this@SignupActivity, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = "Nama tidak boleh kosong"
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Email tidak boleh kosong"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Password tidak boleh kosong"
                }
                else -> {
                    signupViewModel.register(name, email, password)
                }
            }
        }
    }

    private fun observeViewModel() {
        signupViewModel.registerResult.observe(this) { result ->
            result?.let {
                Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        signupViewModel.errorMessage.observe(this) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, "Registrasi gagal: $message", Toast.LENGTH_SHORT).show()
            }
        }

        signupViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val punyaAkun =
            ObjectAnimator.ofFloat(binding.punyaAkun, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                punyaAkun,
                signup
            )
            startDelay = 100
        }.start()
    }

}
