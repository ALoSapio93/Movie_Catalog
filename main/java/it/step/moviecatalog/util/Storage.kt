package it.step.moviecatalog.util

import android.content.Context
import android.content.SharedPreferences
import it.step.moviecatalog.util.LocaleUtil

class Storage(context: Context) {
    private var preferences: SharedPreferences = context.getSharedPreferences("language_preferences", Context.MODE_PRIVATE)

    fun getPreferredLocale(): String {
        return preferences.getString("app_language", LocaleUtil.OPTION_PHONE_LANGUAGE)!!
    }

    fun setPreferredLocale(localeCode: String) {
        preferences.edit().putString("app_language", localeCode).apply()
    }
}