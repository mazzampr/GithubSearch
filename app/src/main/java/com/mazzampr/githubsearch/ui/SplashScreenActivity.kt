package com.mazzampr.githubsearch.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.mazzampr.githubsearch.databinding.ActivitySplashScreenBinding
import com.mazzampr.githubsearch.viewmodel.SplashViewModel
import com.mazzampr.githubsearch.viewmodel.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel by viewModels<SplashViewModel> { ViewModelFactory.getInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getThemeSetting().observe(this) {isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        setUpLogoAnimate()
    }

    private fun setUpLogoAnimate() {
        binding.ivLogoGithub.alpha = 0f
        binding.tvAppTitle.alpha = 0f

        binding.tvAppTitle.animate().apply {
            duration = 1500
            alpha(1f)
        }
        binding.ivLogoGithub.animate().apply {
            duration = 1500
            alpha(1f)
        }.withEndAction {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}