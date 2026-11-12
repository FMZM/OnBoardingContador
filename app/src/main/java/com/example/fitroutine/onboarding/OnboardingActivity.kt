package com.example.fitroutine.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.fitroutine.HomeActivity
import com.example.fitroutine.R
import com.example.fitroutine.data.UserPreferencesRepository
import com.example.fitroutine.databinding.ActivityOnboardingBinding
import com.example.fitroutine.ui.applySystemBarsPadding
import com.example.fitroutine.ui.enableLightEdgeToEdge
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var repository: UserPreferencesRepository
    private var isSaving = false

    override fun onCreate(savedInstanceState: Bundle?) {
        enableLightEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        repository = UserPreferencesRepository(applicationContext)

        val adapter = OnboardingAdapter(onboardingPages)
        binding.viewPager.adapter = adapter

        // Puntos indicadores de página
        TabLayoutMediator(binding.tabIndicator, binding.viewPager) { _, _ -> }.attach()

        // El botón "Empezar ahora" solo aparece en la tercera pantalla.
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.btnStart.isVisible = position == adapter.itemCount - 1
            }
        })

        binding.btnStart.setOnClickListener { completeOnboarding() }
    }

    /** Guarda isOnboardingCompleted = true en el DataStore y navega a Home. */
    private fun completeOnboarding() {
        if (isSaving) return
        isSaving = true

        lifecycleScope.launch {
            try {
                // NonCancellable: la escritura termina aunque la Activity se destruya (p. ej. al rotar).
                withContext(NonCancellable) {
                    repository.setOnboardingCompleted(true)
                }
            } catch (e: IOException) {
                isSaving = false
                Toast.makeText(
                    this@OnboardingActivity,
                    R.string.error_saving,
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }

            // Se limpia la pila para que "Atrás" desde Home no regrese al onboarding.
            val intent = Intent(this@OnboardingActivity, HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
            finish()
        }
    }
}
