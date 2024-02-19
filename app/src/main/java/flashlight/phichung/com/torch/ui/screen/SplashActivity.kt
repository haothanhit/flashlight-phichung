package flashlight.phichung.com.torch.ui.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the splash screen transition.
        installSplashScreen()
        gotoMain()
    }

    private fun gotoMain() {
        val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(mainIntent)
        finish()

    }

}

