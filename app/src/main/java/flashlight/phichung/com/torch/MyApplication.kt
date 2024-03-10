package flashlight.phichung.com.torch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import flashlight.phichung.com.torch.utils.ReleaseTree
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {

    companion object {

        lateinit var instance: MyApplication


    }

    override fun onCreate() {
        instance = this

        super.onCreate()


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }


    }


}