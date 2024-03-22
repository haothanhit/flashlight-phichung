package flashlight.phichung.com.torch

import android.app.Application
import android.util.Log
import com.haohao.languagepicker.getCurrentLanguageCode
import com.haohao.languagepicker.initLanguage
import com.haohao.languagepicker.listLangAppSupport
import com.haohao.languagepicker.localesLanguageApp
import com.haohao.languagepicker.setApplicationLocales
import dagger.hilt.android.HiltAndroidApp
import flashlight.phichung.com.torch.data.CachePreferencesHelper
import flashlight.phichung.com.torch.utils.ReleaseTree
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    companion object {

        lateinit var instance: MyApplication


    }

    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper

    override fun onCreate() {
        instance = this

        super.onCreate()


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }


        initLanguage()
        checkLanguageApp()

    }

    private fun checkLanguageApp() {

        if (cachePreferencesHelper.languageApp.isEmpty()) {

            var currentLang = getCurrentLanguageCode()
            if (!listLangAppSupport.contains(currentLang)) {
                currentLang = "en"
            }
            cachePreferencesHelper.languageApp = currentLang   //save language app

            val index = localesLanguageApp.indexOfFirst {
                it.toLanguageTag().lowercase() == currentLang.lowercase()
            }
            if (index != -1) {
                Log.i("HAOHAO", "index :  $index")
                setApplicationLocales(localesLanguageApp[index].language.toString().lowercase())
            }
        }
    }


}