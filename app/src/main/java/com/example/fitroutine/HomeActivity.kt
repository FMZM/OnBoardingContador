package com.example.fitroutine

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitroutine.data.UserPreferencesRepository
import com.example.fitroutine.databinding.ActivityHomeBinding
import com.example.fitroutine.onboarding.OnboardingActivity
import com.example.fitroutine.ui.applySystemBarsPadding
import com.example.fitroutine.ui.enableLightEdgeToEdge
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var repository: UserPreferencesRepository

    private var series = 0
    private var reps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        enableLightEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        repository = UserPreferencesRepository(applicationContext)

        // Se conservan los contadores al rotar la pantalla.
        series = savedInstanceState?.getInt(KEY_SERIES) ?: 0
        reps = savedInstanceState?.getInt(KEY_REPS) ?: 0

        binding.btnSeriesUp.setOnClickListener {
            series++
            renderCounters()
        }
        binding.btnSeriesDown.setOnClickListener {
            if (series > 0) series--
            renderCounters()
        }
        binding.btnRepsUp.setOnClickListener {
            reps++
            renderCounters()
        }
        binding.btnRepsDown.setOnClickListener {
            if (reps > 0) reps--
            renderCounters()
        }
        binding.btnRestore.setOnClickListener {
            series = 0
            reps = 0
            renderCounters()
        }
        binding.btnBackToOnboarding.setOnClickListener { backToOnboarding() }

        renderCounters()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SERIES, series)
        outState.putInt(KEY_REPS, reps)
    }

    private fun renderCounters() {
        binding.tvSeriesCount.text = series.toString()
        binding.tvRepsCount.text = reps.toString()
    }

    /** Pone isOnboardingCompleted = false y vuelve a mostrar el onboarding. */
    private fun backToOnboarding() {
        lifecycleScope.launch {
            try {
                withContext(NonCancellable) {
                    repository.setOnboardingCompleted(false)
                }
            } catch (e: IOException) {
                Toast.makeText(this@HomeActivity, R.string.error_saving, Toast.LENGTH_SHORT).show()
                return@launch
            }

            val intent = Intent(this@HomeActivity, OnboardingActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
            finish()
        }
    }

    private companion object {
        const val KEY_SERIES = "key_series"
        const val KEY_REPS = "key_reps"
    }
}
