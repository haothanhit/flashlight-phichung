package flashlight.phichung.com.torch.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import flashlight.phichung.com.torch.data.model.Skin
import javax.inject.Inject

open class CachePreferencesHelper @Inject constructor(context: Context) {

    companion object {
        private const val PREF_PACKAGE_NAME = "flashlight.phichung.com.torch.preferences"
        private const val PREF_KEY_LANGUAGE_APP = "language_app"
        private const val PREF_KEY_SOUND = "sound_on_of_app"
        private const val PREF_KEY_SKIN = "skin_app"


    }

    private val gson = Gson()

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)


    var languageApp: String
        get() = preferences.getString(PREF_KEY_LANGUAGE_APP, "").toString()
        set(language) = preferences.edit().putString(PREF_KEY_LANGUAGE_APP, language).apply()



    var skin: Skin?
        get() {
            val json = preferences.getString(PREF_KEY_SKIN, null)
            return gson.fromJson(json, Skin::class.java)
        }
        set(value) {
            val json = gson.toJson(value)
            preferences.edit().putString(PREF_KEY_SKIN, json).apply()
        }


    var stateSound: Boolean
        get() = preferences.getBoolean(PREF_KEY_SOUND, true)
        set(sound) = preferences.edit().putBoolean(PREF_KEY_SOUND, sound).apply()


    fun clearCache() {
        preferences.edit().clear().apply()
    }


}
