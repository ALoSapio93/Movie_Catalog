package it.step.moviecatalog

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import it.step.moviecatalog.util.Storage
import java.util.Locale

class MyApp : Application() {

    val storage: Storage by lazy {
        Storage(this)
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Disabling night mode if not needed

    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }

    fun updateBaseContextLocale(context: Context): Context {
        val sharedPreferences =
            context.getSharedPreferences("language_preferences", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString(
            "app_language",
            Locale.getDefault().language
        ) // Imposta "en" come valore predefinito

        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        return context.createConfigurationContext(config)
    }

}

