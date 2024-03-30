package flashlight.phichung.com.torch.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.android.billingclient.api.Purchase
import com.haohao.billing.BillingClientListener
import com.haohao.billing.BillingEventListener
import com.haohao.billing.BillingHelper
import com.haohao.billing.model.ErrorType
import dagger.hilt.android.AndroidEntryPoint
import flashlight.phichung.com.torch.MyApplication
import flashlight.phichung.com.torch.R
import flashlight.phichung.com.torch.data.CachePreferencesHelper
import flashlight.phichung.com.torch.utils.Constant.KEY_PAYMENT_IN_APP_ADMOB
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3.0 seconds

    private val mRunnable: Runnable = Runnable {
        gotoMain()
    }

    @Inject
    lateinit var cachePreferencesHelper: CachePreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the splash screen transition.
        installSplashScreen()
        setContentView(R.layout.activity_splash)

        initBilling()
    }

    private fun initBilling() {


        BillingHelper(this@SplashActivity)
            .setInAppKeys(mutableListOf(KEY_PAYMENT_IN_APP_ADMOB))
            .enableLogging(isEnableLog = true)
        checkPaidRemoveAds()
            }

    private fun checkPaidRemoveAds() {

        // paid
        cachePreferencesHelper.stateRemoveAds = BillingHelper(this).isInAppPremiumUserByInAppKey(
            KEY_PAYMENT_IN_APP_ADMOB
        )
    }

    private fun gotoMain() {

        if (MyApplication.appOpenAdManager.isShowingAd) {
            return
        }


        MyApplication.appOpenAdManager.showAdIfAvailable(
            this@SplashActivity,
            object : MyApplication.OnShowAdCompleteListener {

                override fun onShowAdComplete() {

                    Handler(Looper.getMainLooper()).postDelayed({
                        val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }, 50)
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

