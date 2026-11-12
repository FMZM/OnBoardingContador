package com.example.fitroutine.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Una sola instancia de DataStore por proceso (delegado a nivel de archivo).
private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

/**
 * Acceso a las preferencias del usuario guardadas con Preferences DataStore.
 */
class UserPreferencesRepository(context: Context) {

    private val dataStore = context.applicationContext.userPreferencesDataStore

    /** Flow que emite si el usuario ya completó el onboarding (false por defecto). */
    val isOnboardingCompleted: Flow<Boolean> = dataStore.data
        .catch { exception ->
            // Si el archivo no se puede leer, se asume el estado por defecto.
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences -> preferences[IS_ONBOARDING_COMPLETED] ?: false }

    /** Guarda el valor de isOnboardingCompleted en disco. */
    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETED] = completed
        }
    }

    private companion object {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("isOnboardingCompleted")
    }
}
