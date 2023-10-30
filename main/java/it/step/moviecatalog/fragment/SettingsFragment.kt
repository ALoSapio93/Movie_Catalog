package it.step.moviecatalog.fragment

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import it.step.moviecatalog.MainActivity
import it.step.moviecatalog.R
import java.util.Locale


class SettingsFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val sharedPreferences =
            requireContext().getSharedPreferences("language_preferences", Context.MODE_PRIVATE)
        val sp_editor = sharedPreferences.edit()

        val italianCheckBox = findPreference<CheckBoxPreference>("pref_language_italian")
        val englishCheckBox = findPreference<CheckBoxPreference>("pref_language_english")

        val app_language =
            sharedPreferences.getString("app_language", getString(R.string.empty_string))

        if (app_language == "it") {
            italianCheckBox?.let { it.isChecked = true }
            englishCheckBox?.let { it.isChecked = false }
        } else {
            italianCheckBox?.let { it.isChecked = false }
            englishCheckBox?.let { it.isChecked = true }
        }

        italianCheckBox?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue as Boolean) {
                // La checkbox italiana è stata selezionata, imposta la lingua italiana
                setAppLanguage(requireContext(), "it")
                if (englishCheckBox != null && englishCheckBox.isChecked) {
                    englishCheckBox.isChecked = false
                }
                sp_editor.putString("app_language", "it")
                sp_editor.putBoolean("isLanguageSet", true)
                sp_editor.apply()

                // Avvia la funzione restartApplication dopo un breve ritardo
                Handler().postDelayed({
                    restartApplication()
                }, 100) // Delay di 100 millisecondi (puoi regolarlo a tuo piacimento)
            }

            true // Restituisci true per indicare che il cambiamento deve essere accettato
        }


        englishCheckBox?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue as Boolean) {
                // La checkbox inglese è stata selezionata, imposta la lingua inglese
                setAppLanguage(requireContext(), "en")
                if (italianCheckBox != null && italianCheckBox.isChecked) {
                    italianCheckBox.isChecked = false
                }
                sp_editor.putString("app_language", "en")
                sp_editor.putBoolean("isLanguageSet", true)
                sp_editor.apply()
                // Avvia la funzione restartApplication dopo un breve ritardo
                Handler().postDelayed({
                    restartApplication()
                },100) // Delay di 100 millisecondi (puoi regolarlo a tuo piacimento)
            }

            true // Restituisci true per indicare che il cambiamento deve essere accettato
        }
    }

    fun setAppLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }


    fun exitAndRelaunchApp() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }

    fun restartApplication() {
        // Crea un intent per riavviare l'applicazione
        val intent = requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)

        // Imposta i flag per riavviare completamente l'app
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        // Avvia l'attività principale
        if (intent != null) {
            startActivity(intent)
        }

        // Termina l'attuale processo dell'app
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}