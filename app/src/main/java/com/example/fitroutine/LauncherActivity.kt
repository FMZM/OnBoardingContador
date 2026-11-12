package com.example.fitroutine

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.fitroutine.data.UserPreferencesRepository
import com.example.fitroutine.onboarding.OnboardingActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Activity de arranque (sin interfaz propia). Lee el DataStore mediante un Flow
 * y decide si abre el onboarding o directamente la HomeActivity.
 *
 * Mientras se lee el valor se mantiene el splash en pantalla, así nunca se
 * "parpadea" el onboarding antes de saltar a Home.
 *
 * Como el valor vive en disco, funciona igual aunque la app se haya cerrado
 * por completo desde las pestañas recientes.
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Debe ir antes de super.onCreate().
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var keepSplashOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

        lifecycleScope.launch {
            val completed = UserPreferencesRepository(applicationContext)
                .isOnboardingCompleted
                .first()

            val destination = if (completed) {
                HomeActivity::class.java
            } else {
                OnboardingActivity::class.java
            }

            startActivity(Intent(this@LauncherActivity, destination))
            finish()
            keepSplashOnScreen = false
        }
    }
}
