package flashlight.phichung.com.torch.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import flashlight.phichung.com.torch.MyApplication
import flashlight.phichung.com.torch.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3.0 seconds

    private val mRunnable: Runnable = Runnable {
        gotoMain()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the splash screen transition.
        installSplashScreen()
        setContentView(R.layout.activity_splash)


    }

    private fun gotoMain() {

        if (MyApplication.appOpenAdManager.isShowingAd) {
            return
        }

        MyApplication.appOpenAdManager.showAdIfAvailable(
            this@SplashActivity,
            object : MyApplication.OnShowAdCompleteListener {

                override fun onShowAdComplete() {
                    val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }
            })


//        val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
//        startActivity(mainIntent)
//        finish()

    }

    override fun onResume() {
        super.onResume()

        try {
            mDelayHandler = Handler(Looper.getMainLooper())
            mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

        } catch (_: Exception) {
        }
    }


    public override fun onDestroy() {
        super.onDestroy()
        if (mDelayHandler != null) {
            mDelayHandler?.removeCallbacks(mRunnable)

        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}

