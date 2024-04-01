package flashlight.phichung.com.torch.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import flashlight.phichung.com.torch.FlashlightApp
import flashlight.phichung.com.torch.ui.theme.FlashlightTheme


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashlightTheme {
                FlashlightApp()
            }
        }
    }


}



