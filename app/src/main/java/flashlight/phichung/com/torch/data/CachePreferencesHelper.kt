package flashlight.phichung.com.torch.data

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

open class CachePreferencesHelper @Inject constructor(context: Context) {

    companion object {
        private const val PREF_PACKAGE_NAME = "flashlight.phichung.com.torch.preferences"
        private const val PREF_KEY_LANGUAGE_APP = "language_app"
        private const val PREF_KEY_SOUND = "sound_on_of_app"


    }


    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)


    var languageApp: String
        get() = preferences.getString(PREF_KEY_LANGUAGE_APP, "").toString()
        set(token) = preferences.edit().putString(PREF_KEY_LANGUAGE_APP, token).apply()


    var stateSound: Boolean
        get() = preferences.getBoolean(PREF_KEY_SOUND, true)
        set(token) = preferences.edit().putBoolean(PREF_KEY_SOUND, token).apply()


    fun clearCache() {
        preferences.edit().clear().apply()
    }


}
