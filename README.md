# FitRoutine — Onboarding + DataStore (Kotlin, Android)

App de rutinas de ejercicio con onboarding de 3 pantallas (`ViewPager2`) y persistencia
con **Preferences DataStore**. Replica la dinámica del video de referencia (onboarding con
puntos indicadores, botón en la última pantalla, Home con contadores, botón "Volver al onboarding").

## Cómo abrirlo

1. Android Studio → **File ▸ Open** → selecciona la carpeta `FitRoutine`.
2. Espera el **Gradle Sync** (la primera vez descarga Gradle 8.9 y las dependencias; necesita internet).
3. Elige un emulador o dispositivo (Android 8.0 / API 26 o superior) y presiona **Run ▶**.

Las imágenes son vectores XML incluidos en el proyecto (`res/drawable/ic_onboarding_*.xml`).
Si quieres usar tus propias imágenes, cámbialas en `OnboardingAdapter.kt` (campo `image`).

## Flujo de la app

- `LauncherActivity` (sin UI, con splash) lee `isOnboardingCompleted` del DataStore con un `Flow`
  (`.first()`) y abre `OnboardingActivity` o directamente `HomeActivity`.
- `OnboardingActivity`: 3 pantallas con `ViewPager2` + puntos indicadores. En la tercera aparece
  el botón **Empezar ahora**.
- Al presionar **Empezar ahora** se guarda `isOnboardingCompleted = true` y se navega a `HomeActivity`.
- `HomeActivity`: "¡Listo para sudar!", dos contadores (Series y Repeticiones) con ▲/▼, botón
  **Restaurar** y botón **Volver al onboarding** (guarda `isOnboardingCompleted = false`).

## Cómo probar la persistencia

1. Completa el onboarding → llegas a Home.
2. Cierra la app por completo desde las pestañas recientes y ábrela de nuevo → entra directo a Home.
3. Para ver otra vez el onboarding: botón **Volver al onboarding** en Home.

## Estructura

```
app/src/main/java/com/example/fitroutine/
├── LauncherActivity.kt
├── HomeActivity.kt
├── data/UserPreferencesRepository.kt      # DataStore + Flow
├── onboarding/
│   ├── OnboardingActivity.kt
│   └── OnboardingAdapter.kt               # RecyclerView.Adapter del ViewPager2
└── ui/EdgeToEdge.kt
```
